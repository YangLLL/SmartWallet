package com.example.smartwallet.main;

//import android.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.myapplication.R;import com.example.smartwallet.bluetooth.TabFragment1;
import com.example.smartwallet.communication.TabFragment2;
import com.example.smartwallet.personalCenter.TabFragment3;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */

public class MainActivity extends FragmentActivity implements OnClickListener{

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments;

    private LinearLayout mtab1;
    private LinearLayout mtab2;
    private LinearLayout mtab3;


    private ImageButton mtab1Img;
    private ImageButton mtab2Img;
    private ImageButton mtab3Img;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();

        initEvents();
        setSelect(0);
    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        // tabs
        mtab1 = (LinearLayout) findViewById(R.id.tab1);
        mtab2 = (LinearLayout) findViewById(R.id.tab2);
        mtab3 = (LinearLayout) findViewById(R.id.tab3);

        // ImageButton
        mtab1Img = (ImageButton) findViewById(R.id.tab1_image);
        mtab2Img = (ImageButton) findViewById(R.id.tab2_image);
        mtab3Img = (ImageButton) findViewById(R.id.tab3_image);

//      mFragments = new ArrayList<Fragment>();
        mFragments = new ArrayList<Fragment>();
        Fragment mTab1 = new TabFragment1();
        Fragment mTab2 = new TabFragment2();
        Fragment mTab3 = new TabFragment3();
        mFragments.add(mTab1);
        mFragments.add(mTab2);
        mFragments.add(mTab3);


        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentItem = mViewPager.getCurrentItem();
                setTab(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvents()
    {
        mtab1.setOnClickListener(this);
        mtab2.setOnClickListener(this);
        mtab3.setOnClickListener(this);


        mViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int arg0)
            {
                int currentItem = mViewPager.getCurrentItem();
                resetImgs();
                switch (currentItem)
                {
                    case 0:
                        mtab1Img.setImageResource(R.drawable.wdsb1);
                        break;
                    case 1:
                        mtab2Img.setImageResource(R.drawable.xiaoxi1);
                        break;
                    case 2:
                        mtab3Img.setImageResource(R.drawable.grzx1);
                        break;

                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tab1:
               setSelect(0);

                break;
            case R.id.tab2:
                setSelect(1);

                break;
            case R.id.tab3:
                setSelect(2);
                break;

            default:
                break;
        }
    }
    private void setSelect(int i){
        setTab(i);
        mViewPager.setCurrentItem(i);
    }

    private void setTab(int i){
        resetImgs();
        switch (i)
        {
            case 0:
                mtab1Img.setImageResource(R.drawable.wdsb1);
                break;
            case 1:
                mtab2Img.setImageResource(R.drawable.xiaoxi1);
                break;
            case 2:
                mtab3Img.setImageResource(R.drawable.grzx1);
                break;

        }
    }
    /**
     * 将所有的图片切换为暗色的
     */
    private void resetImgs()
    {
        mtab1Img.setImageResource(R.drawable.wdsb2);
        mtab2Img.setImageResource(R.drawable.xiaoxi2);
        mtab3Img.setImageResource(R.drawable.grzx2);

    }
}
