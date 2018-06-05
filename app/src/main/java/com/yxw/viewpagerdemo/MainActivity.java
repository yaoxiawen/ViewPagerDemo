package com.yxw.viewpagerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ViewPager vp;
    private TextView tv;
    private List<ad> list;
    private LinearLayout ll;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            vp.setCurrentItem(vp.getCurrentItem() + 1);
            //发送延迟空消息，让自动播放
            handler.sendEmptyMessageDelayed(0, 4000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        update();
    }

    private void initView() {
        vp = findViewById(R.id.vp);
        tv = findViewById(R.id.tv);
        list = new ArrayList();
        ll = findViewById(R.id.ll);
    }

    private void initData() {
        list.add(new ad(R.drawable.a, "This is a"));
        list.add(new ad(R.drawable.b, "This is b"));
        list.add(new ad(R.drawable.c, "This is c"));
        list.add(new ad(R.drawable.d, "This is d"));
        list.add(new ad(R.drawable.e, "This is e"));
        vp.setAdapter(new MyPagerAdapter());
        //实现左右循环播放
        vp.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % list.size());
        for (int i = 0; i < list.size(); i++) {
            View view = new View(this);
            //布局参数，设置宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            //左右边界
            params.leftMargin = 7;
            params.rightMargin = 7;
            //给view设置布局参数
            view.setLayoutParams(params);
            //view设置背景
            view.setBackgroundResource(R.drawable.selector_dot);
            //开始时第一个点显示，enabled设置为true
            view.setEnabled(i == 0);
            //添加view
            ll.addView(view);
        }
        //发送延迟空消息，让自动播放
        handler.sendEmptyMessageDelayed(0, 4000);
    }

    /**
     * 数据的更新显示
     */
    private void update() {
        //设置监听，监听page改变了要更新显示数据
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //vp.getCurrentItem()该方法和参数position一样
//                System.out.println("111111111111111111111:"+position);
//                System.out.println("222222222222222222222:"+vp.getCurrentItem());
                //实现循环播放
                position = position % list.size();
                tv.setText(list.get(position).getDesc());
                for (int i = 0; i < ll.getChildCount(); i++) {
                    ll.getChildAt(i).setEnabled(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //实现循环播放
            return Integer.MAX_VALUE;
        }

        /**
         * 判断当前滑动的view和接下去要显示的view是不是同一个
         *
         * @param view
         * @param object
         * @return true，表示是同一个，不去创建，使用缓存
         * false，表示重新创建
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            //一般这么写
            return view == object;
        }

        /**
         * 销毁page
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //父类的该方法是抛异常，该方法未被覆盖
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        /**
         * 类似于BaseAdapter的getView方法
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //父类的该方法是抛异常，该方法未被覆盖
            //return super.instantiateItem(container, position);
            //实现循环播放
            position = position % list.size();
            View v = View.inflate(MainActivity.this, R.layout.item_image, null);
            ImageView iv = v.findViewById(R.id.iv);
            iv.setImageResource(list.get(position).getId());
            //最后要把view添加进去不能忘
            container.addView(v);
            return v;
        }
    }
}
