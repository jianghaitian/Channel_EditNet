package com.bonc.channel_edit;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;

import com.bonc.channel_edit.utils.AES;
import com.bonc.channel_edit.utils.AESEncrytion;
import com.bonc.channel_edit.utils.JsonStrUtil;
import com.bonc.channel_edit.utils.MD5;
import com.bonc.channel_edit.utils.MD5AESEncryption;
import com.bonc.channel_edit.utils.RequetKeys;
import com.bonc.channel_edit.utils.SHA1;
import com.bonc.channel_edit.utils.UrlPool;
import com.bonc.mobile.app.net.BoncHttpPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.provider.CallLog.Calls.NEW;


/**
 * Created by jht on 2017/12/8.
 */

public class MenuDataHelper {
    /**
     * 授权码
     */
    private String akCode;
    private String timeStamp;
    private String nonce;
    private String token = "";//令牌，创建应用时填写
    private String timeStrNonce;//时间戳和随机数
    private String signature;//签名

    private static MenuDataHelper menuDataHelper = null;
    private Context context;
    final Handler h = new Handler();

    private MenuDataHelper(Context context) {
        this.context = context;
    }

    public static synchronized MenuDataHelper getInstace(Context context) {
        if (menuDataHelper == null) {
            menuDataHelper = new MenuDataHelper(context);
        }
        return menuDataHelper;
    }

    /**
     * 先获取akcode,再获取atok
     *
     * @param loginName
     * @param passWord
     * @param listener
     */
    public void getAtok(final String hostUrl, final String loginName, final String passWord, final CallbackListener listener) {
        final BoncHttpPost boncHttpPost = new BoncHttpPost(context);
        final Map<String, String> map = new HashMap<>();
        map.put("response_type", "code");
        map.put("appId", "client");
        map.put("secrityId", "client");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final byte[] bytes = boncHttpPost.postData(hostUrl + UrlPool.GET_AKCODE, map, false, false, null);
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> responData = null;
                        if (bytes != null && bytes.length > 0) {
                            try {
                                String str = new String(bytes);
                                JsonStrUtil jsonStrUtil = new JsonStrUtil(str);
                                responData = (Map<String, Object>) jsonStrUtil.getResultObject();
                                if (responData.get(RequetKeys.codeKey).equals("0")) {
                                    Map<String, Object> result = (Map<String, Object>) responData.get("DATA");
                                    akCode = (String) result.get("AKCODE");
                                    timeStrNonce = (String) result.get("timestamp".toUpperCase());
                                    //获取atok请求接口
                                    getAtokNormal(hostUrl,loginName, passWord, listener);
                                } else {
                                    String message = (String) responData.get(RequetKeys.messageKey);
                                    listener.onError(message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onError(e.toString());
                            }
                        } else {
                            listener.onError("获取atok失败");
                        }
                    }
                });

            }
        }).start();
    }

    /**
     * 获取atok
     *
     * @param loginName
     * @param listener
     */
    private void getAtokNormal(final String hostUrl, String loginName, String login_pass, final CallbackListener listener) {
        final BoncHttpPost boncHttpPost = new BoncHttpPost(context);
        final Map<String, String> postMap = new HashMap<String, String>();
        final Map<String, String> dataMap = new HashMap<String, String>();
        String pw_encode_str = context.getResources().getString(R.string.pw_encode);
        if (!TextUtils.isEmpty(pw_encode_str)) {
            int pw_encode_style = Integer.parseInt(pw_encode_str);
            switch (pw_encode_style) {
                case 1://不加密
                    break;
                case 2://md5
                    login_pass = new MD5().getMD5ofStr(login_pass);
                    break;
                case 3://sha1
                    login_pass = SHA1.getSHAResult(login_pass);
                    break;
                case 4:
                    login_pass = MD5AESEncryption.getMd5AESEncrption(login_pass);
                    break;
                default:
                    break;
            }
        } else {
            listener.onError("密码加密方式为空");
        }
        postMap.put("user_id", loginName);
        postMap.put("password", login_pass);
        postMap.put("atCode", 2 + "");
        postMap.put("grant_type", "authorization_code");
        postMap.put("AKCODE", akCode);
        String login_inter = context.getResources().getString(R.string.login_interface_type);
        if (!TextUtils.isEmpty(login_inter)) {
            final int login_interface_type = Integer.parseInt(login_inter);
            if (3 == login_interface_type) {
                String timeStr = AESEncrytion.AesDecode(timeStrNonce);
                nonce = timeStr.substring(timeStr.length() - 4, timeStr.length());
                String timeSt = timeStr.substring(0, timeStr.length() - 4);
                long startTime = System.currentTimeMillis();
                long endTime = System.currentTimeMillis();
                timeStamp = endTime - startTime + Long.parseLong(timeSt) + "";
                JSONObject object = new JSONObject();
                try {
                    object.put("user_id", loginName);
                    object.put("grant_type", "authorization_code");
                    object.put("AKCODE", akCode);
                    object.put("atCode", 2 + "");
                    object.put("password", login_pass);
                    String msg = object.toString();
                    String _data = AES.encode(msg);

                    String sortStr = timeStamp + nonce + token + _data;
                    char[] ch = sortStr.toCharArray();
                    Arrays.sort(ch);

                    String sign = String.valueOf(ch);
                    byte[] buf = Base64.encode(sign.getBytes(), Base64.NO_WRAP);
                    String base64ofStr = new String(buf, Charset.forName("8859-1"));
                    signature = new MD5().getMD5ofStr(base64ofStr);
                    dataMap.put("_data", _data);
                    dataMap.put("signature", signature);
                    dataMap.put("timestamp", timeStamp);
                    dataMap.put("nonce", nonce);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String HOST = context.getResources().getString(R.string.get_atok_ip);
                            final byte[] bytes = boncHttpPost.postData(hostUrl + UrlPool.GET_ATOK_SIGNATURE, dataMap, false, false, null);
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    Map<String, Object> responData = null;
                                    if (bytes != null && bytes.length > 0) {
                                        try {
                                            JsonStrUtil jsonStrUtil = new JsonStrUtil(new String(bytes));
                                            responData = (Map<String, Object>) jsonStrUtil.getResultObject();
                                            if (responData.get(RequetKeys.codeKey).equals("0")) {
                                                String dataEncode = (String) responData.get("DATA");
                                                String dataJson = AES.decode(dataEncode);
                                                try {
                                                    JSONObject map = new JSONObject(dataJson);
                                                    String atok = map.getString("ATOK");
                                                    //成功返回
                                                    listener.onSuccess(atok);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    listener.onError(e.toString());
                                                }
                                            } else {
                                                String message = (String) responData.get(RequetKeys.messageKey);
                                                listener.onError(message);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            listener.onError(e.toString());
                                        }
                                    } else {
                                        listener.onError("获取atok失败");
                                    }
                                }
                            });

                        }
                    }).start();
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError(e.toString());
                }
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final byte[] bytes;
                        if (2 == login_interface_type) {
                            bytes = boncHttpPost.postData(hostUrl + UrlPool.GET_ATOK_SPECIAL, postMap, false, false, null);
                        } else {
                            bytes = boncHttpPost.postData(hostUrl + UrlPool.GET_ATOK_NORMAL, postMap, false, false, null);
                        }
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, Object> responData = null;
                                try {
                                    JsonStrUtil jsonStrUtil = new JsonStrUtil(new String(bytes));
                                    responData = (Map<String, Object>) jsonStrUtil.getResultObject();
                                    if (responData.get(RequetKeys.codeKey).equals("0")) {
                                        Map<String, Object> result = (Map<String, Object>) responData.get("DATA");
                                        String atok = (String) result.get("ATOK");
                                        listener.onSuccess(atok);
                                    } else {
                                        String message = (String) responData.get(RequetKeys.messageKey);
                                        listener.onError(message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    listener.onError(e.toString());
                                }
                            }
                        });

                    }
                }).start();
            }
        } else {
            listener.onError("登录接口类型为空");
        }
    }

    /**
     * 网路请求工具方法
     * @param url
     * @param map
     */
    public void httpPost(final String url, final Map<String, String> map, final CallbackListener listener){

        final BoncHttpPost boncHttpPost = new BoncHttpPost(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final byte[] bytes = boncHttpPost.postData(url, map, false, false, null);
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> responData = null;
                        if (bytes != null && bytes.length > 0) {
                            try {
                                JsonStrUtil jsonStrUtil = new JsonStrUtil(new String(bytes));
                                responData = (Map<String, Object>) jsonStrUtil.getResultObject();
                                if (responData.get(RequetKeys.codeKey).equals("0")) {
                                    String result = new String(bytes);
                                    //我的菜单信息
                                    listener.onSuccess(result);
                                } else {
                                    String message = (String) responData.get(RequetKeys.messageKey);
                                    listener.onError(message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onError(e.toString());
                            }
                        } else {
                            listener.onError("网络请求失败");
                        }
                    }
                });

            }
        }).start();
    }

}
