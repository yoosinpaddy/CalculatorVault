package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.model.BreakInImageModel;

import java.io.File;
import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {
    ArrayList<BreakInImageModel> breakInImageModelArrayList;
    Context mContext;
    LayoutInflater mLayoutInflater;
    ViewGroup containerView;
    View view;

    public CustomPagerAdapter(Context context, ArrayList<BreakInImageModel> breakInImageModelArrayList) {
        mContext = context;
        this.breakInImageModelArrayList = breakInImageModelArrayList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return breakInImageModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        File file = new File(breakInImageModelArrayList.get(position).getFilePath());

        Glide.with(mContext)
                .load(file)
                //    .error(R.drawable.ic_action_broken)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .centerCrop()
                .into(imageView);
        //imageView.setImageResource(mResources[position]);

        container.addView(itemView);
        containerView=container;
        view=itemView;
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void removeView(int index) {
        breakInImageModelArrayList.remove(index);
        containerView.removeView(view);
        notifyDataSetChanged();
    }
}
