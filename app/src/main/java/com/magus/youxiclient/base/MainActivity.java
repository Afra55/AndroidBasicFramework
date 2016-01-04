package com.magus.youxiclient.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.magus.youxiclient.R;
import com.magus.youxiclient.module.community.CommunityFragment;
import com.magus.youxiclient.module.home.HomeFragment;
import com.magus.youxiclient.module.me.MeFragment;
import com.magus.youxiclient.module.shop.ShopFragment;
import com.magus.youxiclient.util.SystemBarTintManager;


public class MainActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener {

    private static final int INIT_TAB_ID = -1;
    private static final String KEY_BUNDLE_ID = "key_bundle_id";
    protected int currentFragmentId = INIT_TAB_ID;
    private ImageView homeImg, communityImg, shopImg, meImg;
    private TextView homeText, communityText, shopText, meText;

    /* framgent */
    private BaseFragment selectedFragment;
    private HomeFragment homeFragment;
    private CommunityFragment communityFragment;
    private ShopFragment shopFragment;
    private MeFragment meFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersiveStatusBar();
        View mainLayout = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentLayout(mainLayout);
        initBottomMenu(mainLayout);

        if (savedInstanceState != null)
            currentFragmentId = savedInstanceState.getInt(KEY_BUNDLE_ID, currentFragmentId);
        setCurrentBottomMenuView(currentFragmentId);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            if (currentFragmentId != INIT_TAB_ID) {
                outState.putInt(KEY_BUNDLE_ID, currentFragmentId);
            }
        }
    }

    /* 初始化底部菜单 */
    private void initBottomMenu(View view) {
        view.findViewById(R.id.main_bottom_menu_home).setOnClickListener(this);
        view.findViewById(R.id.main_bottom_menu_community).setOnClickListener(this);
        view.findViewById(R.id.main_bottom_menu_shop).setOnClickListener(this);
        view.findViewById(R.id.main_bottom_menu_me).setOnClickListener(this);

        homeImg = (ImageView) view.findViewById(R.id.main_bottom_menu_home_img);
        homeText = (TextView) view.findViewById(R.id.main_bottom_menu_home_text);
        communityImg = (ImageView) view.findViewById(R.id.main_bottom_menu_community_img);
        communityText = (TextView) view.findViewById(R.id.main_bottom_menu_community_text);
        shopImg = (ImageView) view.findViewById(R.id.main_bottom_menu_shop_img);
        shopText = (TextView) view.findViewById(R.id.main_bottom_menu_shop_text);
        meImg = (ImageView) view.findViewById(R.id.main_bottom_menu_me_img);
        meText = (TextView) view.findViewById(R.id.main_bottom_menu_me_text);
    }

    /* 设置当前选中的fragment */
    private void setCurrentBottomMenuView(int id) {
        resetBottomMenuState();
        switch (id) {
            case R.id.main_bottom_menu_home:
                showHomeFragment();
                break;
            case R.id.main_bottom_menu_community:
                showCommunityFragment();
                break;
            case R.id.main_bottom_menu_shop:
                showShopFragment();
                break;
            case R.id.main_bottom_menu_me:
                showMeFragment();
                break;
            default:
                showHomeFragment();
                break;
        }
    }

    /* 首页 */
    private void showHomeFragment() {
        homeImg.setBackgroundResource(R.drawable.btn_shouye2);
        homeText.setTextColor(getResources().getColor(R.color.red));
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance("main", "home");
        }
        switchFragment(
                R.id.main_fragment_content,
                selectedFragment,
                homeFragment,
                homeFragment.getClass().getSimpleName());
    }

    /* 社区 */
    private void showCommunityFragment() {
        communityImg.setBackgroundResource(R.drawable.btn_shouye2);
        communityText.setTextColor(getResources().getColor(R.color.red));
        if (communityFragment == null) {
            communityFragment = CommunityFragment.newInstance("main", "community");
        }
        switchFragment(
                R.id.main_fragment_content,
                selectedFragment,
                communityFragment,
                communityFragment.getClass().getSimpleName());
    }

    /* 购物 */
    private void showShopFragment() {
        shopImg.setBackgroundResource(R.drawable.btn_shouye2);
        shopText.setTextColor(getResources().getColor(R.color.red));
        if (shopFragment == null) {
            shopFragment = ShopFragment.newInstance("main", "shop");
        }
        switchFragment(
                R.id.main_fragment_content,
                selectedFragment,
                shopFragment,
                shopFragment.getClass().getSimpleName());
    }

    /* 我 */
    private void showMeFragment() {
        meImg.setBackgroundResource(R.drawable.btn_shouye2);
        meText.setTextColor(getResources().getColor(R.color.red));
        if (meFragment == null) {
            meFragment = MeFragment.newInstance("main", "shop");
        }
        switchFragment(
                R.id.main_fragment_content,
                selectedFragment,
                meFragment,
                meFragment.getClass().getSimpleName());
    }

    /* 重置底部菜单状态 */
    private void resetBottomMenuState() {
        homeImg.setBackgroundResource(R.drawable.btn_shouye);
        communityImg.setBackgroundResource(R.drawable.btn_shouye);
        shopImg.setBackgroundResource(R.drawable.btn_shouye);
        meImg.setBackgroundResource(R.drawable.btn_shouye);

        homeText.setTextColor(getResources().getColor(R.color.main_bottom_menu_text));
        communityText.setTextColor(getResources().getColor(R.color.main_bottom_menu_text));
        shopText.setTextColor(getResources().getColor(R.color.main_bottom_menu_text));
        meText.setTextColor(getResources().getColor(R.color.main_bottom_menu_text));
    }

    private void switchFragment(int id, BaseFragment from,
                                BaseFragment to, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (from == null || !from.isAdded()) {
            if (!to.isAdded()) {
                transaction.add(id, to, tag).commit();
            } else {
                transaction.show(to).commit();
            }
        } else {
            if (!to.isAdded()) {
                from.setFragmentSeleted(false);
                transaction.hide(from).add(id, to, tag).commit();
            } else {
                from.setFragmentSeleted(false);
                transaction.hide(from).show(to).commit();
                to.setFragmentSeleted(true);
            }
        }
        selectedFragment = to;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == currentFragmentId) {
            return;
        }
        switch (v.getId()) {
            case R.id.main_bottom_menu_home:
            case R.id.main_bottom_menu_community:
            case R.id.main_bottom_menu_shop:
            case R.id.main_bottom_menu_me:
                currentFragmentId = v.getId();
                setCurrentBottomMenuView(currentFragmentId);
                break;
        }
    }

    /* 与 fragment 交互 start */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String message) {

    }
    /* 与 fragment 交互 end */
}
