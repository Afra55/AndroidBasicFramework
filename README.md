# AndroidBasicFramework
## Android项目的基本架构搭建

MVP + Rxjava + Retrofit + Fresco.
----------

### 第三方库
1. Retrofit [http://square.github.io/retrofit/](http://square.github.io/retrofit/)
2. Fresco [Fresco 文档](http://www.fresco-cn.org/ "Fresco 文档")
3. Rxjava [Rxjava 文档](https://github.com/mcxiaoke/RxDocs "Rxjava 文档")

### 推荐使用

1. AppBarLayout，CollapsingToolbarLayout，Toobar [添加应用栏](https://developer.android.com/training/appbar/index.html)
2. NestedScrollView [NestedScrollView](https://developer.android.com/reference/android/support/v4/widget/NestedScrollView.html)
3. NavigationView [创建抽屉式导航栏](https://developer.android.com/training/implementing-navigation/nav-drawer.html)
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

4. AppCompatPreferenceActivity [设置](https://developer.android.com/guide/topics/ui/settings.html)

5. NotificationCompat.Builder [通知代码示例](https://github.com/Afra55/TrainingFirstApp/blob/9c2fe61c37c15fa1f91bf4c4972b0a976bcdbac2/app/src/main/java/com/afra55/trainingfirstapp/utils/NewMessageNotification.java)