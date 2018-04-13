package com.bonc.channel_edit.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


import com.bonc.channel_edit.bean.ChanelBean;

import java.util.List;

/**
 * 频道管理类
 * Created by leeky on 2017/11/6.
 */

public class ChannelManage {

    public static ChannelManage channelManage;

    public static ChannelManage getInstance() {
        if (channelManage == null) {
            channelManage = new ChannelManage();
        }
        return channelManage;
    }

    /**
     * 删除频道
     *
     * @param channelBean        频道bean
     * @param position           删除的位置
     * @param myBeanList         我的频道数据源
     * @param allBeanList        全部频道数据源
     * @param mMyChannelAdapter  我的频道适配器
     * @param mAllChannelAdapter 全部频道适配器
     */
    public void deleteChannel(ChanelBean channelBean, int position,
                              final List<ChanelBean> myBeanList, final List<ChanelBean> allBeanList,
                              MyChannelAdapter mMyChannelAdapter, AllChannelAdapter mAllChannelAdapter) {
        if (2 == channelBean.getTypeView()) {
            int pos_fixed = 0;
            if (position > 0 && position <= myBeanList.size()) {
                for (int i = 0; i < allBeanList.size(); i++) {            //遍历全部菜单，将删掉的菜单置为可以添加
                    if (channelBean.getChannelId().equals(allBeanList.get(i).getChannelId())) {
                        pos_fixed = i;
                        allBeanList.get(i).setIsFixed("0");
                    }
                    if (myBeanList.contains(channelBean)) {
                        myBeanList.remove(channelBean);
                    }
                    mMyChannelAdapter.notifyItemRemoved(position);
                    mAllChannelAdapter.notifyItemRangeChanged(0, allBeanList.size());
                }
            }
        }

    }

    /**
     * 添加频道
     *
     * @param channelBean        频道bean
     * @param position           删除的位置
     * @param myBeanList         我的频道数据源
     * @param allBeanList        全部频道数据源
     * @param mMyChannelAdapter  我的频道适配器
     * @param mAllChannelAdapter 全部频道适配器
     */
    public void addChannel(ChanelBean channelBean, int position,
                           final List<ChanelBean> myBeanList, final List<ChanelBean> allBeanList,
                           MyChannelAdapter mMyChannelAdapter, AllChannelAdapter mAllChannelAdapter) {
        if (2 == channelBean.getTypeView()) {
            int pos_move = 0;
            if (position > 0 && position <= allBeanList.size()) {
                if ("0".equals(channelBean.getIsFixed())) {         //只能添加未固定的
                    pos_move = position;
                    channelBean.setIsFixed("1");
                    if (!myBeanList.contains(channelBean)) {
                        myBeanList.add(channelBean);
                    }
                    allBeanList.get(position).setIsFixed("1");
                    mMyChannelAdapter.notifyItemInserted(myBeanList.size() - 1);
                    mAllChannelAdapter.notifyItemRangeChanged(0, allBeanList.size());

                }
            }
        }
    }

    /**
     * 拖拽频道
     *
     * @param myChannelRecyler  被拖拽的Recycler
     * @param mMyChannelAdapter 被拖拽的Recycler的适配器
     */
    public void dragRecyclerView(RecyclerView myChannelRecyler, MyChannelAdapter mMyChannelAdapter) {
        //创建ItemTouchHelper
        final ItemTouchHelper mTouchHelper = new ItemTouchHelper(new ToucheCallBcak(mMyChannelAdapter));
        //attach到RecyclerView中
        mTouchHelper.attachToRecyclerView(myChannelRecyler);
        mMyChannelAdapter.setOnStartDragListener(new OnStartDragListener() {
            @Override
            public void startDrag(RecyclerView.ViewHolder holder) {
                mTouchHelper.startDrag(holder);
            }
        });
    }
}
