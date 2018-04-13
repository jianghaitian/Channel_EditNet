package com.bonc.channel_edit.utils;

import android.text.TextUtils;

/**
 * Created by jht on 2017/10/9.
 * AES设置秘钥和加密
 */

public class AESEncrytion {
    public static String AESKEY = "nCbf67awoApYVlSB";
    /**
     * 设置秘钥
     * @param key
     */
    public static void setAesKey(String key){
        if(!TextUtils.isEmpty(key)){
            AES.setKey(key);
        }
    }

    /**
     * 加密
     * @param content
     * @return
     */
    public static String AesDecode(String content){
        if(TextUtils.isEmpty(AES.getKey())){
            AES.setKey(AESKEY);
        }
        String result="";
        if(!TextUtils.isEmpty(content)){
            result = AES.decode(content);
        }
        return result;
    }
}
