package com.bonc.channel_edit;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonc.channel_edit.bean.MenuBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MQ on 2016/11/11.
 */

class GridViewAdapter extends BaseAdapter {
    private List<MenuBean.MODULESBean> dataList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    GridViewAdapter(Context context,List<MenuBean.MODULESBean> datas, int page) {
        dataList = new ArrayList<>();
        this.context=context;
        //start end分别代表要显示的数组在总数据List中的开始和结束位置
        int start = page * MenuLayoutHelper.item_grid_num;
        int end = start + MenuLayoutHelper.item_grid_num;
        while ((start < datas.size()) && (start < end)) {
            dataList.add(datas.get(start));
            start++;
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if (itemView == null) {
            mHolder = new ViewHolder();
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chanel_item_interface, viewGroup, false);
            mHolder.iv_img = (ImageView) itemView.findViewById(R.id.menu_image);
            mHolder.tv_text = (TextView) itemView.findViewById(R.id.src);
            mHolder.tv_count = (TextView)itemView.findViewById(R.id.tv_count);
            itemView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) itemView.getTag();
        }
        String menuTitle = dataList.get(position).getModuleTitle();
        mHolder.tv_text.setText(menuTitle);
        String menuImg = dataList.get(position).getModuleIconUrlAndroid();
        Glide.with(context)
                .load(menuImg)
                .placeholder(R.drawable.loadimagedefault1)
                .fitCenter()
                .into(mHolder.iv_img);
        mHolder.iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(position,dataList);
                }
            }
        });
        String bubbleNum = dataList.get(position).getBubbleNum();
        if(!TextUtils.isEmpty(bubbleNum)&&!"0".equals(bubbleNum)){
            mHolder.tv_count.setVisibility(View.VISIBLE);
            mHolder.tv_count.setText(bubbleNum);
        }else{
            mHolder.tv_count.setVisibility(View.INVISIBLE);
        }
        return itemView;
    }

    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_text;
        private TextView tv_count;
    }
    interface OnItemClickListener{
        void onItemClick(int position,List<MenuBean.MODULESBean> dataList);
    }
}
