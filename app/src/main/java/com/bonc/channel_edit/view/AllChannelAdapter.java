package com.bonc.channel_edit.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bonc.channel_edit.R;
import com.bonc.channel_edit.bean.ChanelBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leeky on 2017/11/2.
 */

public class AllChannelAdapter extends RecyclerView.Adapter<ViewHolder> implements IItemHelper {
    //标题
    public static final int CHANNEL_TITLE=1;
    //全部频道
    public static final int ALL_CHANNEL=2;
    private final Context mContext;
    private List<ChanelBean> mDatas=new ArrayList<>();

    private final LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private OnEditListener mOnEditListener;

    public AllChannelAdapter(Context context, List<ChanelBean> topDatas, List<ChanelBean> datas, OnEditListener mOnEditListener){
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mOnEditListener = mOnEditListener;
        for (int i = 0; i < datas.size(); i++) {
            String channelId = datas.get(i).getChannelId();

            for (int j = 0; j < topDatas.size(); j++) {
                if (TextUtils.equals(channelId,topDatas.get(j).getChannelId())) {
                    datas.get(i).setIsFixed("1");
                    break;
                }
            }
        }
        for (int i = 0; i < datas.size(); i++) {
            String isFixed = datas.get(i).getIsFixed();
            if (!"1".equals(isFixed)) {
                datas.get(i).setIsFixed("0");
            }
        }
        this.mDatas = datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view=null;
        if(viewType==CHANNEL_TITLE){
            view = mInflater.inflate(R.layout.chanel_title, parent, false);
        } else if(viewType==ALL_CHANNEL) {
            view = mInflater.inflate(R.layout.chanel_item_add, parent, false);
        }
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //是否显示 添加、固定 图标
        if(mOnEditListener.onEdit() && position<=mDatas.size()){
            if ("1".equals(mDatas.get(holder.getAdapterPosition()).getIsFixed())) {//我的频道已有此项菜单，不能添加
                ImageView imgFixed=holder.getView(R.id.img_fixed);
                if(imgFixed!=null){
                    imgFixed.setVisibility(View.VISIBLE);
                }
                ImageView imgAdd=holder.getView(R.id.img_add);
                if(imgAdd!=null){
                    imgAdd.setVisibility(View.GONE);
                }
            } else {        //可以添加菜单
                ImageView imgAdd=holder.getView(R.id.img_add);
                if(imgAdd!=null){
                    imgAdd.setVisibility(View.VISIBLE);
                }
                ImageView imgFixed=holder.getView(R.id.img_fixed);
                if(imgFixed!=null){
                    imgFixed.setVisibility(View.GONE);
                }
            }
            RelativeLayout rl = holder.getView(R.id.channl_item_add_rl);
            if(rl!=null){
                rl.setBackground(mContext.getResources().getDrawable(R.drawable.bg_chanel_item));
            }

        }else {
            ImageView imgFixed=holder.getView(R.id.img_fixed);
            if(imgFixed!=null){
                imgFixed.setVisibility(View.GONE);
            }
            ImageView imgAdd=holder.getView(R.id.img_add);
            if(imgAdd!=null){
                imgAdd.setVisibility(View.GONE);
            }
            RelativeLayout rl = holder.getView(R.id.channl_item_add_rl);
            if(rl!=null){
                rl.setBackground(null);
            }
        }

        TextView channel = holder.getView(R.id.src);
        if(channel!=null){
            channel.setText(mDatas.get(position).getSrc());
        }
        TextView title = holder.getView(R.id.title);
        if(title!=null){
            title.setText(mDatas.get(position).getSrc());
        }

        ImageView imgMenu = holder.getView(R.id.menu_image);
        if (imgMenu != null) {
            String imgUrl = mDatas.get(position).getAndroidImgUrl();
            Glide.with(imgMenu.getContext())
                    .load(imgUrl)
                    .placeholder(R.drawable.loadimagedefault1)
                    .fitCenter()
                    .into(imgMenu);
        }

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = position;
                    mOnItemClickLitener.onItemClick(mDatas.get(pos), holder.itemView, pos);
                }
            });
        }


    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas.get(position).getTypeView()==1){
            return CHANNEL_TITLE;
        }else{
            return ALL_CHANNEL;
        }
    }



    @Override
    public void itemMoved(int oldPosition, int newPosition) {

    }


    @Override
    public void itemDismiss(int position) {
    }

    private OnStartDragListener onStartDragListener;

    public interface OnStartDragListener{
        void startDrag(RecyclerView.ViewHolder holder);
    }

    public void setOnStartDragListener(OnStartDragListener onStartDragListener){

        this.onStartDragListener = onStartDragListener;
    }
}
