package com.bonc.channel_edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bonc.channel_edit.bean.MenuBean;
import com.bonc.channel_edit.utils.JsonStrUtil;
import com.bonc.channel_edit.utils.RequetKeys;
import com.bonc.channel_edit.utils.ToastUtil;
import com.bonc.channel_edit.utils.UrlPool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;
import static android.R.attr.focusableInTouchMode;

/**
 * Created by jht on 2017/12/6.
 */

public class TestActivity extends Activity{
    private RecyclerView my_menu_rv;
    private MenuLayoutView menuLayoutView;
    private String myMenuJson,allMenuJson;
    private String atok;
    private List<MenuBean> allMenus;
    private MenuBean circleMenuBean;
    private List<Map<String, Object>> iconInfoCollection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        my_menu_rv = (RecyclerView) findViewById(R.id.my_menu_rv);
      //  menuLayoutView = (MenuLayoutView) findViewById(R.id.my_menu_rv);
        MenuDataHelper.getInstace(this).getAtok("http://172.16.13.250:8010/qixin/", "016834", "123456", new CallbackListener() {
            @Override
            public void onError(String des) {
                ToastUtil.showLong(TestActivity.this,des);
            }

            @Override
            public void onSuccess(final String result) {
                atok=result+"";
                Map<String,String > post = new HashMap<String, String>();
                post.put("ATOK",atok);
                post.put("category_ids","[7]");
                MenuDataHelper.getInstace(TestActivity.this).httpPost("http://172.16.13.250:8010/qixin/logic/adaptor.do?json&IFCODE=getRoleCategoryResourceInfo&", post, new CallbackListener() {
                    @Override
                    public void onError(String des) {

                    }
                    @Override
                    public void onSuccess(String result) {
                        Map<String, Object> responData;
                        JsonStrUtil jsonStrUtil = null;
                        try {
                            jsonStrUtil = new JsonStrUtil(result);
                            responData = (Map<String, Object>) jsonStrUtil.getResultObject();
                            handleMoreDataOfMenu(responData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }
    public void getMyMenus(String atok){

        Map<String,String > map = new HashMap<String, String>();
        map.put("ATOK",atok);
        map.put("menu_group_id","2");
        MenuDataHelper.getInstace(TestActivity.this).httpPost("http://172.16.13.250:8010/qixin/logic/adaptor.do?json&IFCODE=getMyResourceGroupInfo&", map, new CallbackListener() {
            @Override
            public void onError(String des) {

            }
            @Override
            public void onSuccess(String result) {
                Map<String, Object> responData;
                try {
                    JsonStrUtil jsonStrUtil = new JsonStrUtil(result);
                    responData = (Map<String, Object>) jsonStrUtil.getResultObject();
                    handleCircleMenuData(responData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void upUserResourceGroupInfo(String menuGroupId, String menuIds) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("ATOK", atok);
        data.put("menu_group_id", menuGroupId);
        data.put("menu_ids", menuIds);
        MenuDataHelper.getInstace(TestActivity.this).httpPost("http://172.16.13.250:8010/qixin/logic/adaptor.do?json&IFCODE=upUserResourceGroupInfo&", data, new CallbackListener() {
            @Override
            public void onError(String des) {

            }
            @Override
            public void onSuccess(String result) {
                Map<String, Object> responData;
                try {
                    JsonStrUtil jsonStrUtil = new JsonStrUtil(result);
                    responData = (Map<String, Object>) jsonStrUtil.getResultObject();
                    handleUpdateUserResoureData(responData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleMoreDataOfMenu(Map<String, Object> responData) {
        if (responData != null && "0".equals(JsonStrUtil.getItemObject(responData, "CODE"))) {
            List<Object> datas = (List<Object>) JsonStrUtil.getItemObject(responData, "DATA");
            Map<String, Object> dataMap = (Map<String, Object>) datas.get(0);
            MenuBean menuBean = new MenuBean();
            String menuGroupName = dataMap.get("CATEGORY_NAME") + "";
            String menuGroupId = dataMap.get("CATEGORY_ID") + "";
            menuBean.setMenuId(menuGroupId);
            menuBean.setMenuTitle(menuGroupName);
            menuBean.setMenuTips("");
            List<Object> datalist = (List<Object>) dataMap.get("DATALIST");
            List<MenuBean.MODULESBean> modules = new ArrayList<>();
            for (int i = 0; i < datalist.size(); i++) {
                Map<String, Object> map = (Map<String, Object>) datalist.get(i);
                MenuBean.MODULESBean bean = new MenuBean.MODULESBean();
                String moduleId = (String) map.get("MENU_ID");
                String moduleTitle = (String) map.get("MENU_NAME");
                String moduleIconUrlIos = (String) map.get("ANDROID_IOCN1");
                String moduleIconUrlAndroid = (String) map.get("IOS_IOCN1");
                String bubbleNum = (String) map.get("BUBBLENUM");
                bean.setModuleId(moduleId);
                bean.setModuleTitle(moduleTitle);
                bean.setModuleIconUrlIos(moduleIconUrlIos);
                bean.setModuleIconUrlAndroid(moduleIconUrlAndroid);
                bean.setBelongMenuId(menuGroupId);
                bean.setBubbleNum(bubbleNum);
                modules.add(bean);
            }
            menuBean.setModules(modules);
            allMenus = new ArrayList<>();
            allMenus.add(menuBean);

            getMyMenus(atok);
            showEidtMenu();
        }
    }

    /**
     * 处理圆形icon菜单的数据
     *
     * @param responData
     */
    private void handleCircleMenuData(Map<String, Object> responData) {
        if (responData != null && "0".equals(JsonStrUtil.getItemObject(responData, "CODE"))) {
            Map<String, Object> dataMap = (Map<String, Object>) JsonStrUtil.getItemObject(responData, "DATA");
            iconInfoCollection = (List<Map<String, Object>>) dataMap.get("DATALIST");
            circleMenuBean = new MenuBean();
            circleMenuBean.setMenuTitle(dataMap.get("MENU_GROUP_NAME") + "");
            circleMenuBean.setMenuId(dataMap.get("MENU_GROUP_ID") + "");
            circleMenuBean.setMenuTips("");

            List<MenuBean.MODULESBean> modules = new ArrayList<>();
            if (iconInfoCollection != null && !iconInfoCollection.isEmpty()) {
                for (Map<String, Object> itemOfIconInfo : iconInfoCollection) {
                    MenuBean.MODULESBean bean = new MenuBean.MODULESBean();
                    bean.setModuleId(itemOfIconInfo.get("MENU_ID") + "");
                    bean.setModuleTitle(itemOfIconInfo.get("MENU_NAME") + "");
                    bean.setModuleIconUrlIos(itemOfIconInfo.get("IOS_IOCN1") + "");
                    bean.setModuleIconUrlAndroid(itemOfIconInfo.get("ANDROID_IOCN1") + "");
                    bean.setBubbleNum(itemOfIconInfo.get("BUBBLENUM")+"");
                    modules.add(bean);
                }
            }
            circleMenuBean.setModules(modules);

            showEidtMenu();
        }
    }


    private void showEidtMenu() {
        if (circleMenuBean != null && allMenus != null) {
            MenuLayoutHelper.getInstance(this).setMenuAdapter(circleMenuBean, allMenus, my_menu_rv, 4, "");
            //我的菜单
            MenuLayoutHelper.getInstance(this).setOnMenuItemClickListener(new MenuLayoutHelper.OnMenuItemClickListener() {
                @Override
                public void onMenuItemClick(MenuBean.MODULESBean modulesBean, int postion) {
                    Map<String, Object> map = iconInfoCollection.get(postion);

                }
            });
            //菜单编辑页面的点击事件
            MenuLayoutHelper.getInstance(this).setMenuEditListener(new OnMenuEditListener() {
                @Override
                public void OnItemClick(MenuBean.MODULESBean modulesBean) {//点击某个菜单的事件

                }

                @Override
                public void OnSaveClick(MenuBean menuBean) {//点击完成的回调
                    circleMenuBean = menuBean;
                    List<MenuBean.MODULESBean> checkedModules = menuBean.getModules();
                    String menuIds = "";
                    for (int i = 0; i < checkedModules.size(); i++) {
                        String menuId = checkedModules.get(i).getModuleId();
                        if (i == checkedModules.size() - 1) {
                            menuIds += menuId;
                        } else {
                            menuIds += menuId + ",";
                        }
                    }
                    upUserResourceGroupInfo("2",menuIds);

                }
            });
        }
    }

    private void handleUpdateUserResoureData(Map<String, Object> responData) {
        if (responData != null) {
            String code = (String) JsonStrUtil.getItemObject(responData, "CODE");
            if ("0".equals(code)) {
                MenuLayoutHelper.getInstance(this).notifyMenuAdapter(circleMenuBean);
            }
        }
    }
}
