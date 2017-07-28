package com.example.a25564.weifusifeng.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.a25564.weifusifeng.R;
import com.example.a25564.weifusifeng.bean.AnnImageResult;
import com.example.a25564.weifusifeng.bean.AnnImgs;
import com.example.a25564.weifusifeng.net.OkHttpManger;
import com.example.a25564.weifusifeng.utils.Constant;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;

/**
 * Created by 25564 on 2017/7/16.
 */

public class HomeFragment extends BaseFragment {
    private View view;

    private LinearLayout mZhishiRe;
    private ViewPager mLunboVp;
    private List<View> views;//轮播图展示图片view
    private Mylunboadapter vp_adapter;//viewpager适配器
    private Timer timer;//计时器
    private int count = 0;//轮播图当前下标
    public final int GetImags = 1014;//获取广告图返回码
    private final int AnnFaild = 1011;//获取广告图失败返回码
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
//        view.findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DataSupport.deleteAll(user.class);
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//            }
//        });
        bindView();
        initdata();
        return view;
    }

    private void bindView(){
        mZhishiRe= (LinearLayout) view.findViewById(R.id.zhishiqiL);
        mLunboVp= (ViewPager) view.findViewById(R.id.lubboVP);

    }

    /**
     * 初始化数据展示
     */
    private void initdata() {
        if (views == null) {
            views = new ArrayList<View>();
        }
        vp_adapter = new Mylunboadapter();
        mLunboVp.setAdapter(vp_adapter);
        //添加界面滚动监听
        mLunboVp.addOnPageChangeListener(vp_adapter);
        //首页轮播图获取
        OkHttpManger.getInstance().getNet(Constant.Announcement, new OkHttpManger.ResultCallback() {
            @Override
            public void onFailed(Request request, IOException e) {
                getAnnFailure();
            }

            @Override
            public void onSuccess(String response) {
                getAnnSuccess(response);
            }
        });
    }

    /**
     * 从服务端获取公告信息失败
     * 此时展示数据库缓存数据
     */
    private void getAnnFailure() {
        //从数据库中获取数据
        List<AnnImgs> imgs_dblist = DataSupport.findAll(AnnImgs.class);
        if (imgs_dblist != null) {
            updateAnnShow(imgs_dblist);
        }
    }


    /**
     * 从服务端获取公告信息成功
     */
    private void getAnnSuccess(String resultImgs) {
        //服务端返回有效数据则展示，没有不做处理
        if (resultImgs != null && !"".equals(resultImgs)) {
            Gson gson = new Gson();
            AnnImageResult air = gson.fromJson(resultImgs, AnnImageResult.class);
            List<AnnImgs> imgs_list = air.getBody();
            if (imgs_list == null) {
                imgs_list = new ArrayList<AnnImgs>();
            }
            updateAnnShow(imgs_list);
            //更新缓存
            if (imgs_list.size() > 0) {
                //从数据库清除数据保存
                DataSupport.deleteAll(AnnImgs.class);
                //添加新数据到数据库
                DataSupport.saveAll(imgs_list);
            }
        }
    }

    /**
     * 根据公告图片地址动态更新界面
     *
     * @param imgs_dblist
     */
    private void updateAnnShow(List<AnnImgs> imgs_dblist) {
        views.clear();
        //动态创建轮播展示view
        for (int i = 0; i < imgs_dblist.size(); i++) {
            ImageView img = new ImageView(mActivity);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //通过网络地址显示图片
            Glide.with(mActivity)
                    .load(Constant.BaseUrl + imgs_dblist.get(i).getImgUrl())
                    .into(img);
            views.add(img);
        }
        //更新界面显示
        vp_adapter.notifyDataSetChanged();
        //添加指示器下标点
        initPoint();
        //开启任务计时器
        if (timer == null) {
            timer = new Timer();
            timer.schedule(task, 0, 3000);
        }
    }

    private class Mylunboadapter extends PagerAdapter implements ViewPager.OnPageChangeListener{

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container,int position,Object object){
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
        /**
         * viewpager滑动监听，动态更改指示下标的选中状态
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mZhishiRe.getChildCount(); i++) {
                ImageView image = (ImageView) mZhishiRe.getChildAt(i);
                if (i == position) {
                    image.setSelected(true);
                } else {
                    image.setSelected(false);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Constant.Scroll://接收滚动消息，并执行
                    mLunboVp.setCurrentItem(count);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 创建图片变化下标图标
     */
    public void initPoint() {
        //清除所有指示下标
        mZhishiRe.removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            ImageView img = new ImageView(mActivity);
            //添加下标圆点参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            img.setLayoutParams(params);
            img.setImageResource(R.drawable.sns_v2_page_point);
            if (i == 0) {
                img.setSelected(true);
            }
            mZhishiRe.addView(img);
        }
    }

    // 创建记时器发送图片轮播消息
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (count == views.size()) {
                count = 0;
            } else {
                count = count + 1;
            }
            mHandler.sendEmptyMessage(Constant.Scroll);
        }
    };

}
