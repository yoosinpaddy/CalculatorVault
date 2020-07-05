package com.calculator.vault.gallery.locker.hide.data.subscription.sliderviewlibrary;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.calculator.vault.gallery.locker.hide.data.R;

import java.util.ArrayList;
import java.util.TimerTask;

public class SliderView extends LinearLayout {
    TimerTask updatePage;
    SliderAdapter sliderAdapter;
    int currentPage = 0;
    int mode;
    ViewPager viewPager;
    LinearLayout linear_dots;
    private ArrayList<Integer> IMAGES;
    private ArrayList<String> URLs;
    private Context mContext;

    private ImageView ivDot1;
    private ImageView ivDot2;
    private ImageView ivDot3;

    public SliderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate(getContext(), R.layout.is_slide_view, this);
        viewPager = findViewById(R.id.viewPager);
        linear_dots = findViewById(R.id.linear_dots);
        sliderAdapter = new SliderAdapter(context, viewPager);
        ivDot1 = findViewById(R.id.iv_dot_1);
        ivDot2 = findViewById(R.id.iv_dot_2);
        ivDot3 = findViewById(R.id.iv_dot_3);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                setCurrentDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void setImages(ArrayList<Integer> IMAGES) {
        this.IMAGES = IMAGES;
        sliderAdapter.setImages(IMAGES);
        viewPager.setAdapter(sliderAdapter);
        setCurrentDot(0);

    }

    public void setUrls(ArrayList<String> URLs) {
        this.URLs = URLs;
        sliderAdapter.setUrls(URLs);
        viewPager.setAdapter(sliderAdapter);
        mode = 1;
    }

    public TimerTask getTimerTask() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 3) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        updatePage = new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        };
        return updatePage;
    }

    private void setCurrentDot(int selected) {

        ivDot1.setSelected(false);
        ivDot2.setSelected(false);
        ivDot3.setSelected(false);

        switch (selected) {
            case 0:
                ivDot1.setSelected(true);
                break;
            case 1:
                ivDot2.setSelected(true);
                break;
            case 2:
                ivDot3.setSelected(true);
                break;
        }
    }
}
