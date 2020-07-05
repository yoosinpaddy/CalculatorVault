package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.model.itemModel;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.util.ArrayList;

public class CustomHiddenImageAdapter extends PagerAdapter {
    ArrayList<itemModel> itemModelArrayList;
    Context mContext;
    LayoutInflater mLayoutInflater;
    ViewGroup containerView;
    View view;

    public CustomHiddenImageAdapter(Context context, ArrayList<itemModel> itemModelArrayList) {
        mContext = context;
        this.itemModelArrayList = itemModelArrayList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        final ZoomageView imageView = itemView.findViewById(R.id.imageView);

        File file = new File(itemModelArrayList.get(position).getNewFilepath() + itemModelArrayList.get(position).getNewFilename());

        Glide.with(mContext)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(imageView);

        container.addView(itemView);
        containerView=container;
        view=itemView;
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}