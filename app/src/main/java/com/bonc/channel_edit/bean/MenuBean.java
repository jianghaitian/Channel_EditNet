package com.bonc.channel_edit.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jht on 2017/11/24.
 * 菜单模块实体类
 */

public class MenuBean implements Serializable{
    /**
     * MENU_ID : 0
     * MENU_TITLE : 我的应用
     * MENU_TIPS : 按住拖动调整位置
     * MODULES : [{"MODULE_ID":"13","BELONG_MENU_ID":"1","MODULE_TITLE":"易受理","MODULE_ICON_URL_IOS":"https://timgsa.baidu.com/timg?image.jpg","MODULE_ICON_URL_ Android ":"https://timgsa.baidu.com/timg?image.jpg"},{"MODULE_ID":"14","BELONG_MENU_ID":"1","MODULE_TITLE":"掌上装维","MODULE_ICON_URL_IOS":"https://timgsa.baidu.com/timg?image.jpg","MODULE_ICON_URL_ Android ":"https://timgsa.baidu.com/timg?image.jpg"},{"MODULE_ID":"15","BELONG_MENU_ID":"1","MODULE_TITLE":"沃运营","MODULE_ICON_URL_IOS":"https://timgsa.baidu.com/timg?image.jpg","MODULE_ICON_URL_ Android ":"https://timgsa.baidu.com/timg?image.jpg"},{"MODULE_ID":"16","BELONG_MENU_ID":"1","MODULE_TITLE":"随沃行","MODULE_ICON_URL_IOS":"https://timgsa.baidu.com/timg?image.jpg","MODULE_ICON_URL_ Android ":"https://timgsa.baidu.com/timg?image.jpg"},{"MODULE_ID":"21","BELONG_MENU_ID":"2","MODULE_TITLE":"决策分析","MODULE_ICON_URL_IOS":"https://timgsa.baidu.com/timg?image.jpg","MODULE_ICON_URL_ Android ":"https://timgsa.baidu.com/timg?image.jpg"}]
     */
    @SerializedName("MENU_ID")
    private String menuId;
    @SerializedName("MENU_TITLE")
    private String menuTitle;
    @SerializedName("MENU_TIPS")
    private String menuTips;
    @SerializedName("MODULES")
    private List<MODULESBean> modules;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuTips() {
        return menuTips;
    }

    public void setMenuTips(String menuTips) {
        this.menuTips = menuTips;
    }

    public List<MODULESBean> getModules() {
        return modules;
    }

    public void setModules(List<MODULESBean> modules) {
        this.modules = modules;
    }

    public static class MODULESBean implements Serializable{
        /**
         * MODULE_ID : 13
         * BELONG_MENU_ID : 1
         * MODULE_TITLE : 易受理
         * MODULE_ICON_URL_IOS : https://timgsa.baidu.com/timg?image.jpg
         * MODULE_ICON_URL_ Android  : https://timgsa.baidu.com/timg?image.jpg
         */
        @SerializedName("MODULE_ID")
        private String moduleId;
        @SerializedName("BELONG_MENU_ID")
        private String belongMenuId;
        @SerializedName("MODULE_TITLE")
        private String moduleTitle;
        @SerializedName("MODULE_ICON_URL_IOS")
        private String moduleIconUrlIos;
        @SerializedName("MODULE_ICON_URL_Android")
        private String moduleIconUrlAndroid; // FIXME check this code
        @SerializedName("BUBBLENUM")
        private String bubbleNum;

        public String getBubbleNum() {
            return bubbleNum;
        }

        public void setBubbleNum(String bubbleNum) {
            this.bubbleNum = bubbleNum;
        }

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }

        public String getBelongMenuId() {
            return belongMenuId;
        }

        public void setBelongMenuId(String belongMenuId) {
            this.belongMenuId = belongMenuId;
        }

        public String getModuleTitle() {
            return moduleTitle;
        }

        public void setModuleTitle(String moduleTitle) {
            this.moduleTitle = moduleTitle;
        }

        public String getModuleIconUrlIos() {
            return moduleIconUrlIos;
        }

        public void setModuleIconUrlIos(String moduleIconUrlIos) {
            this.moduleIconUrlIos = moduleIconUrlIos;
        }

        public String getModuleIconUrlAndroid() {
            return moduleIconUrlAndroid;
        }

        public void setModuleIconUrlAndroid(String moduleIconUrlAndroid) {
            this.moduleIconUrlAndroid = moduleIconUrlAndroid;
        }

        @Override
        public String toString() {
            return "MODULESBean{" +
                    "moduleId='" + moduleId + '\'' +
                    ", belongMenuId='" + belongMenuId + '\'' +
                    ", moduleTitle='" + moduleTitle + '\'' +
                    ", moduleIconUrlIos='" + moduleIconUrlIos + '\'' +
                    ", moduleIconUrlAndroid='" + moduleIconUrlAndroid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MenuBean{" +
                "menuId='" + menuId + '\'' +
                ", menuTitle='" + menuTitle + '\'' +
                ", menuTips='" + menuTips + '\'' +
                ", modules=" + modules +
                '}';
    }
}
