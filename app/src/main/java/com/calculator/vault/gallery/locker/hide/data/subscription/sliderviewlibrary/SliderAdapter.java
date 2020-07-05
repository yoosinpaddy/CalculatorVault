package com.calculator.vault.gallery.locker.hide.data.subscription.sliderviewlibrary;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.TimerTask;

public class SliderAdapter extends PagerAdapter {
    public Runnable Update;
    TimerTask updatePage;
    int currentPage = 0;
    int mode = 0;
    ViewPager viewPager;
    private ArrayList<Integer> IMAGES;
    private ArrayList<String> URLs;
    private LayoutInflater inflater;
    private Context context;

    public SliderAdapter(Context context, ViewPager viewPager) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.viewPager = viewPager;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (mode == 0) {
            return IMAGES.size();
        } else {
            return URLs.size();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.is_main_slide, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        if (mode == 0) {
            imageView.setImageResource(IMAGES.get(position));
        }
        if (mode == 1) {
            Glide.with(context).load(URLs.get(position)).into(imageView);
        }


        view.addView(imageLayout, 0);


        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void setImages(ArrayList<Integer> IMAGES) {
        this.IMAGES = IMAGES;
    }

    public void setUrls(ArrayList<String> URLs) {
        this.URLs = URLs;
        mode = 1;
    }


}
