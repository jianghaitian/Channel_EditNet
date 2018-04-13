package com.bonc.channel_edit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonc.channel_edit.bean.MenuBean;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by jht on 2017/12/5.
 */

public class MenuLayoutAdapter extends RecyclerView.Adapter<MenuLayoutAdapter.MenuViewHolder> {
    private List<MenuBean.MODULESBean> myBeanList;
    private Context context;
    // private int horizotalCount;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MenuLayoutAdapter(List<MenuBean.MODULESBean> myBeanList, Context context) {
        this.myBeanList = myBeanList;
        this.context = context;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chanel_item_interface, parent, false);
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        int perWidth = width/horizotalCount;
//        view.getLayoutParams().width = perWidth;
        MenuViewHolder menuViewHolder = new MenuViewHolder(view);
        return menuViewHolder;
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, final int position) {
        String menuTitle = myBeanList.get(position).getModuleTitle();
        holder.menuTv.setText(menuTitle);
        String menuImg = myBeanList.get(position).getModuleIconUrlAndroid();
        if (position == myBeanList.size() - 1) {
            Glide.with(context.getApplicationContext())
                    .load(menuImg)
                    .error(R.drawable.loadimagedefault_more)
                    .fitCenter()
                    .into(holder.menuIv);
        } else {
            Glide.with(context.getApplicationContext())
                    .load(menuImg)
                    .error(R.drawable.loadimagedefault1)
                    .fitCenter()
                    .into(holder.menuIv);
        }

        holder.menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        String bubbleNum = myBeanList.get(position).getBubbleNum();
        if (!TextUtils.isEmpty(bubbleNum) && !"0".equals(bubbleNum)) {
            holder.countTv.setVisibility(View.VISIBLE);
            holder.countTv.setText(bubbleNum);
        } else {
            holder.countTv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return myBeanList.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView menuTv;
        ImageView menuIv;
        TextView countTv;

        public MenuViewHolder(View itemView) {
            super(itemView);
            menuTv = (TextView) itemView.findViewById(R.id.src);
            menuIv = (ImageView) itemView.findViewById(R.id.menu_image);
            countTv = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
