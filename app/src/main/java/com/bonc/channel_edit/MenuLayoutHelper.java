package com.bonc.channel_edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.bonc.channel_edit.bean.MenuBean;
import com.bonc.channel_edit.view.WrapContentViewPager;
import com.bonc.mobile.ui.widget.pagerindicator.CirclePageIndicator;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jht on 2017/12/5.
 * 菜单编辑默认入口帮助类
 */

public class MenuLayoutHelper {
    //第一种默认入口
    private List<MenuBean.MODULESBean> myBeanList;
    private MenuLayoutAdapter menuLayoutAdapter;
    private Context context;
    private static MenuLayoutHelper menuLayoutHelper;
    private OnMenuItemClickListener onMenuItemClickListener;
    private final int EDITMENUCODE = 0X0004;//请求码

    //第二种默认入口
    public static int item_grid_num = 12;//每一页中GridView中item的数量
    public static int number_columns = 4;//gridview一行展示的数目
    private WrapContentViewPager view_pager;
    private ViewPagerAdapter mAdapter;
    private List<GridView> gridList = new ArrayList<>();
    private CirclePageIndicator indicator;
    private String allMenus;
    private String myMenus;
    private String moreIconUrl;
    private MenuBean.MODULESBean moreBean = new MenuBean.MODULESBean();

    public static OnMenuEditListener onMenuEditListener;

    public void setMenuEditListener(OnMenuEditListener menuEditListener) {
        this.onMenuEditListener = menuEditListener;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    private MenuLayoutHelper(Context context) {
        this.context = context;
    }

    public static MenuLayoutHelper getInstance(Context context) {
        if (menuLayoutHelper == null) {
            menuLayoutHelper = new MenuLayoutHelper(context);
        }
        return menuLayoutHelper;
    }

    /**
     * 设置我的菜单列表数据
     *
     * @param allMenuBeans
     * @param mMenuBean
     * @param spanCount
     */
    public void setMenuAdapter(final MenuBean mMenuBean, final List<MenuBean> allMenuBeans, RecyclerView myMenusRv, int spanCount, String moreMenuIcon) {
        myMenus = new Gson().toJson(mMenuBean);
        allMenus = new Gson().toJson(allMenuBeans);
        moreIconUrl = moreMenuIcon;
        myBeanList = mMenuBean.getModules();
        if (!myBeanList.contains(moreBean)) {
            moreBean.setModuleTitle("更多");
            moreBean.setModuleIconUrlAndroid(moreMenuIcon);
            myBeanList.add(moreBean);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount, LinearLayoutManager.VERTICAL, false);
        myMenusRv.setLayoutManager(layoutManager);
        menuLayoutAdapter = new MenuLayoutAdapter(myBeanList, context);
        menuLayoutAdapter.setOnItemClickListener(new MenuLayoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (myMenus != null && allMenus != null && !"null".equals(allMenus)) {
                    if (position == myBeanList.size() - 1) {
                        Intent intent = new Intent(context, ChannelEditActivity.class);
                        intent.putExtra("MyMenuKey", myMenus);//myMenus我的菜单json字符串
                        intent.putExtra("AllMenuKey", allMenus);//allMenus所有的菜单json字符串
                        context.startActivity(intent);
                    } else {
                        if (onMenuItemClickListener != null) {
                            onMenuItemClickListener.onMenuItemClick(myBeanList.get(position), position);
                        }
                    }
                }
            }
        });
        myMenusRv.setAdapter(menuLayoutAdapter);
    }

    /**
     * 我的菜单点击事件接口定义
     */
    public interface OnMenuItemClickListener {
        void onMenuItemClick(MenuBean.MODULESBean modulesBean, int position);
    }

    /**
     * 刷新我的菜单列表
     *
     * @param menuBean
     */
    public void notifyMenuAdapter(MenuBean menuBean) {
        myMenus = new Gson().toJson(menuBean);
        List<MenuBean.MODULESBean> list = menuBean.getModules();
        myBeanList.clear();
        myBeanList.addAll(list);
        MenuBean.MODULESBean moreBean = new MenuBean.MODULESBean();
        moreBean.setModuleTitle("更多");
        moreBean.setModuleIconUrlAndroid(moreIconUrl);
        myBeanList.add(myBeanList.size(), moreBean);
        if (menuLayoutAdapter != null) {
            menuLayoutAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 默认入口为viewPager嵌套recyclerview
     *
     * @param myMenus
     * @param allMenus
     * @param myMenusView
     * @param numberColumns
     * @param itemGridNum
     */
    public void setMenuLayoutData(final String myMenus, final String allMenus, MenuLayoutView myMenusView, int numberColumns, int itemGridNum) {
        this.allMenus = allMenus;
        number_columns = numberColumns;
        item_grid_num = itemGridNum;
        MenuBean menuBean = new Gson().fromJson(myMenus, MenuBean.class);
        myBeanList = menuBean.getModules();
        MenuBean.MODULESBean moreBean = new MenuBean.MODULESBean();
        moreBean.setModuleTitle("更多");
        myBeanList.add(moreBean);
        initViews(myMenusView);
        //计算viewpager一共显示几页
        final int pageSize = myBeanList.size() % item_grid_num == 0
                ? myBeanList.size() / item_grid_num
                : myBeanList.size() / item_grid_num + 1;
        if (pageSize > 1) {
            indicator.setVisibility(View.VISIBLE);
            indicator.setCurrentItem(0);
        } else {
            indicator.setVisibility(View.GONE);
        }
        for (int i = 0; i < pageSize; i++) {
            GridView gridView = new GridView(context);
            GridViewAdapter adapter = new GridViewAdapter(context, myBeanList, i);
            final int finalI = i;
            adapter.setOnItemClickListener(new GridViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, List<MenuBean.MODULESBean> dataList) {
                    if (finalI == pageSize - 1 && position == dataList.size() - 1) {
                        Intent intent = new Intent(context, ChannelEditActivity.class);
                        intent.putExtra("MyMenuKey", myMenus);//myMenus我的菜单json字符串
                        intent.putExtra("AllMenuKey", allMenus);//allMenus所有的菜单json字符串
                        ((Activity) context).startActivityForResult(intent, EDITMENUCODE);
                    } else {
                        if (onMenuItemClickListener != null) {
                            onMenuItemClickListener.onMenuItemClick(dataList.get(position), position);
                        }
                    }
                }
            });
            gridView.setNumColumns(number_columns);
            gridView.setAdapter(adapter);
            gridList.add(gridView);
        }
        mAdapter.add(gridList);

    }

    /**
     * 刷新列表数据
     *
     * @param myMenus
     */
    public void notifyMenuData(final String myMenus, MenuLayoutView myMenusView) {
        MenuBean menuBean = new Gson().fromJson(myMenus, MenuBean.class);
        List<MenuBean.MODULESBean> list = menuBean.getModules();
        myBeanList.clear();
        myBeanList.addAll(list);
        MenuBean.MODULESBean moreBean = new MenuBean.MODULESBean();
        moreBean.setModuleTitle("更多");
        initViews(myMenusView);
        myBeanList.add(moreBean);
        gridList.clear();
        //计算viewpager一共显示几页
        final int pageSize = myBeanList.size() % item_grid_num == 0
                ? myBeanList.size() / item_grid_num
                : myBeanList.size() / item_grid_num + 1;
        if (pageSize > 1) {
            indicator.setVisibility(View.VISIBLE);
            indicator.setCurrentItem(0);
        } else {
            indicator.setVisibility(View.GONE);
        }
        for (int i = 0; i < pageSize; i++) {
            GridView gridView = new GridView(context);
            GridViewAdapter adapter = new GridViewAdapter(context, myBeanList, i);
            final int finalI = i;
            adapter.setOnItemClickListener(new GridViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, List<MenuBean.MODULESBean> dataList) {
                    if (finalI == pageSize - 1 && position == dataList.size() - 1) {
                        Intent intent = new Intent(context, ChannelEditActivity.class);
                        intent.putExtra("MyMenuKey", myMenus);//myMenus我的菜单json字符串
                        intent.putExtra("AllMenuKey", allMenus);//allMenus所有的菜单json字符串
                        ((Activity) context).startActivityForResult(intent, EDITMENUCODE);
                    } else {
                        if (onMenuItemClickListener != null) {
                            onMenuItemClickListener.onMenuItemClick(dataList.get(position), position);
                        }
                    }
                }
            });
            gridView.setNumColumns(number_columns);
            gridView.setAdapter(adapter);
            gridList.add(gridView);
        }
        mAdapter.add(gridList);
    }

    /**
     * 初始化默认菜单布局组件
     *
     * @param myMenusView
     */
    private void initViews(MenuLayoutView myMenusView) {
        //初始化ViewPager
        view_pager = myMenusView.getViewPager();
        indicator = myMenusView.getCirclePageIndicator();
        mAdapter = new ViewPagerAdapter();
        view_pager.setAdapter(mAdapter);
        //圆点指示器
        indicator.setViewPager(view_pager);
    }

}
