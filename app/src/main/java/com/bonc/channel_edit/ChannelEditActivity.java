package com.bonc.channel_edit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bonc.channel_edit.bean.ChanelBean;
import com.bonc.channel_edit.bean.MenuBean;
import com.bonc.channel_edit.utils.ToastUtil;
import com.bonc.channel_edit.view.AllChannelAdapter;
import com.bonc.channel_edit.view.ChannelManage;
import com.bonc.channel_edit.view.GridItemDecoration;
import com.bonc.channel_edit.view.MyChannelAdapter;
import com.bonc.channel_edit.view.OnEditListener;
import com.bonc.channel_edit.view.OnFinshDragListener;
import com.bonc.channel_edit.view.OnItemClickLitener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bonc.channel_edit.MenuLayoutHelper.onMenuEditListener;

/**
 * Created by leeky on 2017/11/8.
 */

public class ChannelEditActivity extends Activity {

    private RecyclerView myChannelRecyler;
    private RecyclerView allChannelRecyler;
    private MyChannelAdapter mMyChannelAdapter;
    private AllChannelAdapter mAllChannelAdapter;
    private TextView edit_save_tv, menu_title_tv, backTv;
    public boolean isEdit;
    public boolean isEditFinsh;
    private List<ChanelBean> myBeanList = new ArrayList<>();
    private List<ChanelBean> allBeanList = new ArrayList<>();
    private OnEditListener onEditListener;
    private String editStr, finishStr, menuTitle;
    private int columNum = 4;
    private MenuBean myMenuBean;
    private List<MenuBean> allMenuBeanList;
    private final int EDITMENUCODE = 0X0004;//请求码
    // public static OnMenuEditListener onMenuEditListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chanel_activity);
        initWidget();

        getMyDatas();
        getAllDatas();

        onEditListener = new OnEditListener() {
            @Override
            public boolean onEdit() {
                edit_save_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editStr.equals(edit_save_tv.getText().toString())) {
                            isEdit = true;
                            mMyChannelAdapter.notifyItemRangeChanged(0, myChannelRecyler.getChildCount());
                            mAllChannelAdapter.notifyItemRangeChanged(0, allChannelRecyler.getChildCount());
                            edit_save_tv.setText(finishStr);
                            ToastUtil.showShort(ChannelEditActivity.this, "可以编辑频道了");
                            return;
                        }
                        if (finishStr.equals(edit_save_tv.getText().toString())) {
                            isEdit = false;
                            mMyChannelAdapter.notifyItemRangeChanged(0, myChannelRecyler.getChildCount());
                            mAllChannelAdapter.notifyItemRangeChanged(0, allChannelRecyler.getChildCount());
                            edit_save_tv.setText(editStr);
                            //编辑菜单完成
                            isEditFinsh = true;
//                            ToastUtil.showShort(ChannelEditActivity.this, new Gson().toJson(myMenuBean));
                            if (onMenuEditListener != null) {
                                onMenuEditListener.OnSaveClick(myMenuBean);
                            }
                            return;
                        }

                    }
                });
                return isEdit;
            }
        };


        myChannelRecyler = (RecyclerView) findViewById(R.id.channl_recyler);
        allChannelRecyler = (RecyclerView) findViewById(R.id.all_channl_recyler);

        //我的频道适配器
        mMyChannelAdapter = new MyChannelAdapter(this, myBeanList, onEditListener);
        GridLayoutManager manager = new GridLayoutManager(this, columNum);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mMyChannelAdapter.getItemViewType(position);
                if (itemViewType == mMyChannelAdapter.CHANNEL_TITLE) {
                    return columNum;
                }
                return 1;
            }
        });
        myChannelRecyler.setLayoutManager(manager);
        myChannelRecyler.addItemDecoration(new GridItemDecoration(15));
        myChannelRecyler.setAdapter(mMyChannelAdapter);
        myChannelRecyler.setNestedScrollingEnabled(false);
        mMyChannelAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(ChanelBean channelBean, View view, int position) {
                if (isEdit) {
                    if (2 == channelBean.getTypeView() && "1".equals(channelBean.getIsFixed())) {
                        myMenuBean.getModules().remove(position - 1);
                    }
                    ChannelManage.getInstance().deleteChannel(channelBean, position, myBeanList, allBeanList,
                            mMyChannelAdapter, mAllChannelAdapter);
                } else {
                    ToastUtil.showShort(ChannelEditActivity.this, channelBean.getSrc());
                    if (onMenuEditListener != null) {
                        onMenuEditListener.OnItemClick(myMenuBean.getModules().get(position - 1));
                    }

                }
            }
        });
        ChannelManage.getInstance().dragRecyclerView(myChannelRecyler, mMyChannelAdapter);
        mMyChannelAdapter.setOnFinshDragListener(new OnFinshDragListener() {
            @Override
            public void finshDrag(int oldPosition, int newPosition) {
                if (oldPosition - 1 < newPosition - 1) {
                    for (int i = oldPosition - 1; i < newPosition - 1; i++) {
                        Collections.swap(myMenuBean.getModules(), i, i + 1);
                    }
                } else {
                    for (int i = oldPosition - 1; i > newPosition - 1; i--) {
                        Collections.swap(myMenuBean.getModules(), i, i - 1);
                    }
                }
            }
        });

        //全部频道适配器
        mAllChannelAdapter = new AllChannelAdapter(this, myBeanList, allBeanList, onEditListener);
        GridLayoutManager allManager = new GridLayoutManager(this, columNum);
        allManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAllChannelAdapter.getItemViewType(position);
                if (itemViewType == mAllChannelAdapter.CHANNEL_TITLE) {
                    return columNum;
                }
                return 1;
            }
        });
        allChannelRecyler.setLayoutManager(allManager);
        allChannelRecyler.addItemDecoration(new GridItemDecoration(15));
        allChannelRecyler.setAdapter(mAllChannelAdapter);
        mAllChannelAdapter.notifyItemRangeChanged(0, allChannelRecyler.getChildCount());
        allChannelRecyler.setNestedScrollingEnabled(false);
        mAllChannelAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(ChanelBean channelBean, View view, int position) {
                if (isEdit) {
                    int max_add_menu_num = getResources().getInteger(R.integer.max_add_menu_number);
                    if (myBeanList.size() < max_add_menu_num) {
                        if (2 == channelBean.getTypeView() && "0".equals(channelBean.getIsFixed())) {
                            MenuBean.MODULESBean module = new MenuBean.MODULESBean();
                            module.setModuleId(channelBean.getChannelId());
                            module.setModuleTitle(channelBean.getSrc());
                            module.setModuleIconUrlAndroid(channelBean.getAndroidImgUrl());
                            module.setModuleIconUrlIos(channelBean.getIosImgUrl());
                            module.setBelongMenuId(channelBean.getBelongId());
                            myMenuBean.getModules().add(myMenuBean.getModules().size(), module);
                        }
                        ChannelManage.getInstance().addChannel(channelBean, position, myBeanList, allBeanList,
                                mMyChannelAdapter, mAllChannelAdapter);
                    } else {
                        String over_beyond_max_tips = getResources().getString(R.string.over_beyond_max_tips);
                        new AlertDialog.Builder(ChannelEditActivity.this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setTitle("提示").setMessage(over_beyond_max_tips).create().show();
                    }
                } else {
                    ToastUtil.showShort(ChannelEditActivity.this, channelBean.getSrc());
                }
            }
        });

    }

    /**
     * 初始化控件
     */
    public void initWidget() {
        backTv = (TextView) findViewById(R.id.title);
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit_save_tv = (TextView) findViewById(R.id.edit_save_tv);
        menu_title_tv = (TextView) findViewById(R.id.subtitle);
        if (!TextUtils.isEmpty(menuTitle)) {
            menu_title_tv.setText(menuTitle);
        }
        editStr = getResources().getString(R.string.edit_btn_string);
        finishStr = getResources().getString(R.string.finish_btn_string);
        menuTitle = getResources().getString(R.string.menu_title_string);
        columNum = getResources().getInteger(R.integer.column_num_string);
        edit_save_tv.setText(editStr);

        int color = getResources().getColor(R.color.default_black_text_color);
        setStatusBarBg(color);

        //获取intent传递过来的数据
        getIntentData();
    }

    public void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String myMenuJson = intent.getStringExtra("MyMenuKey");
            if (myMenuJson != null) {
                myMenuBean = new Gson().fromJson(myMenuJson, MenuBean.class);
            }
            String allMenuJson = intent.getStringExtra("AllMenuKey");
            if (allMenuJson != null) {
                allMenuBeanList = new Gson().fromJson(allMenuJson, new TypeToken<List<MenuBean>>() {
                }.getType());
            }
        }
    }

    private List<ChanelBean> getMyDatas() {

        myBeanList.add(new ChanelBean(myMenuBean.getMenuTitle(), myMenuBean.getMenuId(), 1));
        for (int i = 0; i < myMenuBean.getModules().size(); i++) {
            String title = myMenuBean.getModules().get(i).getModuleTitle();
            String androidUrl = myMenuBean.getModules().get(i).getModuleIconUrlAndroid();
            String iosUrl = myMenuBean.getModules().get(i).getModuleIconUrlIos();
            String moduleId = myMenuBean.getModules().get(i).getModuleId();
            String belongId = myMenuBean.getModules().get(i).getBelongMenuId();
            myBeanList.add(new ChanelBean(title, androidUrl, iosUrl, moduleId, belongId, 2));
        }
        return myBeanList;

    }

    private List<ChanelBean> getAllDatas() {
        for (int i = 0; i < allMenuBeanList.size(); i++) {
            MenuBean menuBean = allMenuBeanList.get(i);
            String menuTitle = menuBean.getMenuTitle();
            String menuId = menuBean.getMenuId();
            allBeanList.add(new ChanelBean(menuTitle, menuId, 1));
            for (int j = 0; j < menuBean.getModules().size(); j++) {
                String moduleTitle = menuBean.getModules().get(j).getModuleTitle();
                String moduleId = menuBean.getModules().get(j).getModuleId();
                String androidUrl = menuBean.getModules().get(j).getModuleIconUrlAndroid();
                String iosUrl = menuBean.getModules().get(j).getModuleIconUrlIos();
                String belongId = menuBean.getMenuId();
                allBeanList.add(new ChanelBean(moduleTitle, androidUrl, iosUrl, moduleId, belongId, 2));
            }
        }
        return allBeanList;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onStart() {
        super.onStart();
        float alpha = findViewById(R.id.header).getAlpha();
        Log.i("alpaha",alpha+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(onMenuEditListener!=null){
//            onMenuEditListener=null;
//        }
    }
    /**
     * 设置状态栏颜色
     * @param statusColor
     */
    @TargetApi(21)
    protected void setStatusBarBg(int statusColor) {
        if(Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(statusColor);

            ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
            if(mContentView!=null){
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                    ViewCompat.setFitsSystemWindows(mChildView, true);
                }
            }
        }
    }

}
