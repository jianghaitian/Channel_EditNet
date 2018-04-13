package com.bonc.channel_edit;


import com.bonc.channel_edit.bean.MenuBean;

/**
 * Created by jht on 2017/12/15.
 */

public interface OnMenuEditListener {
    void OnItemClick(MenuBean.MODULESBean bean);
    void OnSaveClick(MenuBean menuBean);
}
