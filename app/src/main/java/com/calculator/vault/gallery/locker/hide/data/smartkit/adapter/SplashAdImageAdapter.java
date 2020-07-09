package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.AdModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/18/2016.
 */
public class SplashAdImageAdapter extends RecyclerView.Adapter<SplashAdImageAdapter.ViewHolder> {
    Context context;
    int screen_width;
    int screen_height;
    List<AdModel> al_ad_item = new ArrayList<>();
    public static int width = 0;

    public SplashAdImageAdapter(final Context context, List<AdModel> list_data) {
        this.context = context;
        this.al_ad_item = list_data;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_ad_image, iv_corner_ad;
        TextView tv_app_name;
        ProgressBar progressBar;
        LinearLayout sfl_main;
        RelativeLayout rll_ad_image;
        RelativeLayout fl_ad_image;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_ad_image = (ImageView) itemView.findViewById(R.id.iv_ad_image);
            fl_ad_image = (RelativeLayout) itemView.findViewById(R.id.fl_ad_image);
            iv_corner_ad = (ImageView) itemView.findViewById(R.id.iv_corner_ad);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            sfl_main = (LinearLayout) itemView.findViewById(R.id.sfl_main);
            rll_ad_image = (RelativeLayout) itemView.findViewById(R.id.rll_ad_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_splash_row_ad_data, parent, false);
        return new ViewHolder(view);
    }

    private float convertDpToPx(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        holder.fl_ad_image.setVisibility(View.INVISIBLE);
        holder.tv_app_name.setText(al_ad_item.get(position).getName());

        if (SplashAdImageAdapter.width == 0) {
            holder.rll_ad_image.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressWarnings("deprecation")
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                holder.rll_ad_image.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            } else {
                                holder.rll_ad_image.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                            SplashAdImageAdapter.width = holder.rll_ad_image.getMeasuredWidth();
                            serDimen(holder);
                        }
                    });
        }
        else {
            serDimen(holder);
        }

        Glide.with(context)
                .asBitmap()
                .load(al_ad_item.get(position).getThumb_image())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.iv_ad_image.setImageBitmap(resource);

                        System.gc();

                        holder.fl_ad_image.setVisibility(View.VISIBLE);
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.tv_app_name.setVisibility(View.VISIBLE);
                    }
                });

        holder.iv_ad_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(al_ad_item.get(position).getApp_link())));
            }
        });
    }

    private void serDimen(ViewHolder holder) {
        holder.fl_ad_image.getLayoutParams().height = SplashAdImageAdapter.width;
        holder.fl_ad_image.getLayoutParams().width = SplashAdImageAdapter.width;

        holder.iv_corner_ad.getLayoutParams().height = (int) ((90 * (SplashAdImageAdapter.width /3.6)) / 87);
        holder.iv_corner_ad.getLayoutParams().width = (int) (SplashAdImageAdapter.width /3.6);
    }

    @Override
    public int getItemCount() {
        return al_ad_item.size();
    }
}
