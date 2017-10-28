package com.afra55.baseclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afra55.baseclient.R;
import com.afra55.baseclient.common.AppActivity;
import com.afra55.baseclient.common.AppFragment;
import com.afra55.baseclient.ui.fragment.CommunityFragment;
import com.afra55.baseclient.ui.fragment.HomeFragment;
import com.afra55.baseclient.ui.fragment.MeFragment;
import com.afra55.baseclient.ui.fragment.ShopFragment;
import com.afra55.commontutils.activity.ActivityUtils;
import com.afra55.commontutils.base.OnFragmentInteractionListener;


public class MainActivity extends AppActivity
        implements View.OnClickListener, OnFragmentInteractionListener {

    private static final int INIT_TAB_ID = -1;
    private static final String KEY_BUNDLE_ID = "key_bundle_id";
    protected int currentFragmentId = INIT_TAB_ID;
    private ImageView homeImg, communityImg, shopImg, meImg;
    private TextView homeText, communityText, shopText, meText;

    /* framgent */
    private AppFragment selectedFragment;
    private HomeFragment homeFragment;
    private CommunityFragment communityFragment;
    private ShopFragment shopFragment;
    private MeFragment meFragment;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initBottomMenu();

        if (savedInstanceState != null)
            currentFragmentId = savedInstanceState.getInt(KEY_BUNDLE_ID, currentFragmentId);
        setCurrentBottomMenuView(currentFragmentId);
    }

    @Override
    protected String getScreenTitle() {
        return "Home";
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            if (currentFragmentId != INIT_TAB_ID) {
                outState.putInt(KEY_BUNDLE_ID, currentFragmentId);
            }
        }
    }

    /* 初始化底部菜单 */
    private void initBottomMenu() {
        findViewById(R.id.main_bottom_menu_home).setOnClickListener(this);
        findViewById(R.id.main_bottom_menu_community).setOnClickListener(this);
        findViewById(R.id.main_bottom_menu_shop).setOnClickListener(this);
        findViewById(R.id.main_bottom_menu_me).setOnClickListener(this);

        homeImg = findView(R.id.main_bottom_menu_home_img);
        homeText = (TextView) findViewById(R.id.main_bottom_menu_home_text);
        communityImg = (ImageView) findViewById(R.id.main_bottom_menu_community_img);
        communityText = (TextView) findViewById(R.id.main_bottom_menu_community_text);
        shopImg = (ImageView) findViewById(R.id.main_bottom_menu_shop_img);
        shopText = (TextView) findViewById(R.id.main_bottom_menu_shop_text);
        meImg = (ImageView) findViewById(R.id.main_bottom_menu_me_img);
        meText = (TextView) findViewById(R.id.main_bottom_menu_me_text);
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
    public void showHomeFragment() {
        homeImg.setBackgroundResource(R.drawable.icon_home_selected);
        homeText.setTextColor(getResources().getColor(R.color.red));
        if (homeFragment == null && !isDestroyedCompatible()) {
            homeFragment = HomeFragment.newInstance("main", "home");
        }
        ActivityUtils.switchFragment(getSupportFragmentManager(),
                selectedFragment,
                homeFragment);

        selectedFragment = homeFragment;
    }

    /* 社区 */
    public void showCommunityFragment() {
        communityImg.setBackgroundResource(R.drawable.icon_community_selected);
        communityText.setTextColor(getResources().getColor(R.color.red));
        if (communityFragment == null && !isDestroyedCompatible()) {
            communityFragment = CommunityFragment.newInstance("main", "community");
        }
        ActivityUtils.switchFragment(getSupportFragmentManager()
                , selectedFragment,
                communityFragment);

        selectedFragment = communityFragment;
    }

    /* 购物 */
    public void showShopFragment() {
        shopImg.setBackgroundResource(R.drawable.icon_shop_selected);
        shopText.setTextColor(getResources().getColor(R.color.red));
        if (shopFragment == null && !isDestroyedCompatible()) {
            shopFragment = ShopFragment.newInstance("main", "shop");
        }
        ActivityUtils.switchFragment(getSupportFragmentManager(),
                selectedFragment,
                shopFragment);

        selectedFragment = shopFragment;
    }

    /* 我 */
    public void showMeFragment() {
        meImg.setBackgroundResource(R.drawable.icon_me_selected);
        meText.setTextColor(getResources().getColor(R.color.red));
        if (meFragment == null && !isDestroyedCompatible()) {
            meFragment = MeFragment.newInstance("main", "me");
        }
        ActivityUtils.switchFragment(getSupportFragmentManager(),
                selectedFragment,
                meFragment);

        selectedFragment = meFragment;
    }

    /* 重置底部菜单状态 */
    private void resetBottomMenuState() {
        homeImg.setBackgroundResource(R.drawable.icon_home);
        communityImg.setBackgroundResource(R.drawable.icon_community);
        shopImg.setBackgroundResource(R.drawable.icon_shop);
        meImg.setBackgroundResource(R.drawable.icon_me);

        homeText.setTextColor(getResources().getColor(R.color.main_bottom_menu_text));
        communityText.setTextColor(getResources().getColor(R.color.main_bottom_menu_text));
        shopText.setTextColor(getResources().getColor(R.color.main_bottom_menu_text));
        meText.setTextColor(getResources().getColor(R.color.main_bottom_menu_text));
    }

    @Override
    public void onClick(View v) {
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

    /* 与 fragment 交互 end */

    //双击退出
    private long exitTime = -1;

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.click_again_to_exit, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    //双击返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
