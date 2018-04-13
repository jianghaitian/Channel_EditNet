package com.bonc.channel_edit.view;

import android.view.View;

import com.bonc.channel_edit.bean.ChanelBean;


/**
 * Created by leeky on 2017/11/3.
 */

public interface OnItemClickLitener {
    void onItemClick(ChanelBean channelBean, View view, int position);
}
