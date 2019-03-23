package com.example.wetalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wetalk.Friends;
import com.example.wetalk.R;
import com.example.wetalk.adapter.FriendsAdapter;
import com.example.wetalk.view.Login;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements
        android.view.View.OnClickListener {

    private ViewPager mViewPager;// 用来放置界面切换
    private PagerAdapter mPagerAdapter;// 初始化View适配器
    private List<View> mViews = new ArrayList<View>();// 用来存放Tab01-04
    // 四个Tab，每个Tab包含一个按钮
    private LinearLayout mTabWeiXin;
    private LinearLayout mTabAddress;
    private LinearLayout mTabFrd;
    private LinearLayout mTabSetting;
    // 四个按钮
    private ImageButton mWeiXinImg;
    private ImageButton mAddressImg;
    private ImageButton mFrdImg;
    private ImageButton mSettingImg;

    private List<Friends> friendsList=new ArrayList<>();        //用来填充朋友列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initViewPage();
        initEvent();
        mWeiXinImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.wx_));
    }

    private void initEvent() {
        mTabWeiXin.setOnClickListener(this);
        mTabAddress.setOnClickListener(this);
        mTabFrd.setOnClickListener(this);
        mTabSetting.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             *ViewPage左右滑动时
             */
            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        resetImg();
                        mWeiXinImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.wx_));
                        break;
                    case 1:
                        resetImg();
                        mAddressImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.address_));
                        break;
                    case 2:
                        resetImg();
                        mFrdImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.find_));
                        break;
                    case 3:
                        resetImg();
                        mSettingImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.me_));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 初始化设置
     */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpage);
        // 初始化四个LinearLayout
        mTabWeiXin = (LinearLayout) findViewById(R.id.id_tab_weixin);
        mTabAddress = (LinearLayout) findViewById(R.id.id_tab_address);
        mTabFrd = (LinearLayout) findViewById(R.id.id_tab_frd);
        mTabSetting = (LinearLayout) findViewById(R.id.id_tab_settings);
        // 初始化四个按钮
        mWeiXinImg = (ImageButton) findViewById(R.id.id_tab_weixin_img);
        mAddressImg = (ImageButton) findViewById(R.id.id_tab_address_img);
        mFrdImg = (ImageButton) findViewById(R.id.id_tab_frd_img);
        mSettingImg = (ImageButton) findViewById(R.id.id_tab_settings_img);
    }

    /**
     * 初始化ViewPage
     */
    private void initViewPage() {

        // 初妈化四个布局
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View tab01 = mLayoutInflater.inflate(R.layout.tab01, null);
        View tab02 = mLayoutInflater.inflate(R.layout.tab02, null);
        View tab03 = mLayoutInflater.inflate(R.layout.tab03, null);
        View tab04 = mLayoutInflater.inflate(R.layout.tab04, null);

        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);
        mViews.add(tab04);

        // 适配器初始化并设置
        mPagerAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return mViews.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
    }

    /**
     * 判断哪个要显示，及设置按钮图片
     */
    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.id_tab_weixin:
                mViewPager.setCurrentItem(0);
                resetImg();
                mWeiXinImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.wx_));
                break;
            case R.id.id_tab_address:
                mViewPager.setCurrentItem(1);
                resetImg();
                mAddressImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.address_));
                /*****
                 * 给朋友页填充信息
                 * ***/
                initFriends();
                FriendsAdapter adapter=new FriendsAdapter(MainActivity.this,R.layout.friends_item,friendsList);
                final ListView listView=(ListView)findViewById(R.id.lsv_friends);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Friends friends=friendsList.get(position);
                        //聊天ID信息存储
                        SharedPreferences.Editor editor=getSharedPreferences("chatInfo",MODE_PRIVATE).edit();
                        editor.putString("friendName",friends.getName());
                        editor.putInt("friendID",friends.getFriendID());
                        editor.apply();
                        //打开聊天界面
                        Intent intent=new Intent(MainActivity.this,chatActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.id_tab_frd:
                mViewPager.setCurrentItem(2);
                resetImg();
                mFrdImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.find_));
                break;
            case R.id.id_tab_settings:
                mViewPager.setCurrentItem(3);
                resetImg();
                mSettingImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.me_));
                break;
            default:
                break;
        }
    }

    /**
     * 把所有图片变暗
     */
    private void resetImg() {
        mWeiXinImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.wx));
        mAddressImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.address));
        mFrdImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.find));
        mSettingImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.me));
    }

    //初始化朋友
    private void initFriends(){
        for(int i=0;i<4;i++){
            Friends fA=new Friends("A",6666,R.drawable.tx);
            friendsList.add(fA);
            Friends fB=new Friends("B",6666,R.drawable.tx);
            friendsList.add(fB);
            Friends fC=new Friends("C",6666,R.drawable.tx);
            friendsList.add(fC);
            Friends fD=new Friends("D",6666,R.drawable.tx);
            friendsList.add(fD);
        }
    }

}
