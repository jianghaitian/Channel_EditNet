package com.bonc.channel_edit;


/**
 * Created by Administrator on 2017/12/11.
 */

public interface CallbackListener {
    /**
     * @param des
     */
    void onError(String des);

    /**
     * accessToken返回失败回调
     *
     * @param result
     */
    void onSuccess(String result);
}
