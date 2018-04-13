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

public class MyChannelAdapter extends RecyclerView.Adapter<ViewHolder> implements IItemHelper {
    //标题
    public static final int CHANNEL_TITLE=1;
    //我的频道
    public static final int MY_CHANNEL=2;
    private final Context mContext;
    private List<ChanelBean> mDatas=new ArrayList<>();

    private final LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private OnEditListener mOnEditListener;

    public MyChannelAdapter(Context context, List<ChanelBean> datas, OnEditListener mOnEditListener){
        this.mContext = context;
        this.mOnEditListener = mOnEditListener;
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < datas.size(); i++) {
            if (i == 0) {
                datas.get(i).setIsFixed("0");
            } else {
                datas.get(i).setIsFixed("1");
            }
        }
        this.mDatas = datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view=null;
        if(viewType==CHANNEL_TITLE){
            view = mInflater.inflate(R.layout.chanel_title, parent, false);
        }else if(viewType==MY_CHANNEL){
            view = mInflater.inflate(R.layout.chanel_item_delete, parent, false);
        }
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //是否显示删除图标
        if(mOnEditListener.onEdit() && position<=mDatas.size()){
            ImageView imgDelete=holder.getView(R.id.img_delete);
            if(imgDelete!=null){
                imgDelete.setVisibility(View.VISIBLE);
            }
            RelativeLayout rl = holder.getView(R.id.channl_item_delet_rl);
            if(rl!=null){
                rl.setBackground(mContext.getResources().getDrawable(R.drawable.bg_chanel_item));
            }
        }else {
            ImageView imgDelete=holder.getView(R.id.img_delete);
            if(imgDelete!=null){
                imgDelete.setVisibility(View.GONE);
            }
            RelativeLayout rl = holder.getView(R.id.channl_item_delet_rl);
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
        TextView tvTip = holder.getView(R.id.tip_text);
        if(tvTip!=null){
            String pressDragStr = mContext.getResources().getString(R.string.press_drag_tip_string);
            if(!TextUtils.isEmpty(pressDragStr)){
                tvTip.setText(pressDragStr);
            }
        }
        TextView line = holder.getView(R.id.line_text);
        if(line!=null){
            line.setVisibility(View.GONE);
        }

        ImageView imgMenu = holder.getView(R.id.menu_image);
        if (imgMenu != null) {
            Glide.with(imgMenu.getContext())
                    .load(mDatas.get(position).getAndroidImgUrl())
                    .placeholder(R.drawable.loadimagedefault1)
                    .fitCenter()
                    .into(imgMenu);
        }


        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.getAdapterPosition()!=-1){
                        mOnItemClickLitener.onItemClick(mDatas.get(holder.getAdapterPosition()), holder.itemView, holder.getAdapterPosition());
                    }
                }
            });
        }


        if(mDatas.get(holder.getAdapterPosition()).getTypeView()==MY_CHANNEL){

            //长按事件
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if(mOnEditListener.onEdit()){

                        if(onStartDragListener!=null){
                            onStartDragListener.startDrag(holder);
                        }

                    }
                    return true;
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
        }else {
            return MY_CHANNEL;
        }
    }



    @Override
    public void itemMoved(int oldPosition, int newPosition) {
        ChanelBean channelBean = mDatas.get(oldPosition);
        mDatas.remove(oldPosition);
        mDatas.add(newPosition, channelBean);
        notifyItemMoved(oldPosition, newPosition);
        if(onFinshDragListener!=null){
            onFinshDragListener.finshDrag(oldPosition,newPosition);
        }
    }

    @Override
    public void itemDismiss(int position) {
    }

    private OnStartDragListener onStartDragListener;

    public void setOnStartDragListener(OnStartDragListener onStartDragListener){

        this.onStartDragListener = onStartDragListener;
    }
    private OnFinshDragListener onFinshDragListener;
    public void setOnFinshDragListener(OnFinshDragListener onFinshDragListener){
        this.onFinshDragListener = onFinshDragListener;
    }


}
