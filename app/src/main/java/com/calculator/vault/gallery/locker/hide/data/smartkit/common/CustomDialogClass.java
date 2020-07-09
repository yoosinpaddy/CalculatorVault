package com.calculator.vault.gallery.locker.hide.data.smartkit.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.AdModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bansi on 27-10-2017.
 */
public class CustomDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Button btn_exit, btn_cancel;
    private RecyclerView rcv_ad_images;
    private CardView card_view;
    private ImageView iv_more_apps;

    public CustomDialogClass(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.s_custom_dialog);

        findViews();
        setListners();
        initViewAction();
    }

    private void findViews() {
        rcv_ad_images = (RecyclerView) findViewById(R.id.rv_load_ads);
        card_view = (CardView) findViewById(R.id.card_view);
        iv_more_apps = (ImageView) findViewById(R.id.iv_more_apps);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    private void setListners() {
        btn_exit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        iv_more_apps.setOnClickListener(this);
    }

    private void initViewAction() {

        card_view.getLayoutParams().height = (int) (Share.screenHeight * 0.75);
        card_view.getLayoutParams().width = (int) (Share.screenWidth * 0.9);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.c , 3);
        rcv_ad_images.setLayoutManager(gridLayoutManager);

        ArrayList<AdModel> temp_list = new ArrayList<>();
        for (int i = Share.al_ad_data.size()-1; i >= 0 ; i--) {
            temp_list.add(Share.al_ad_data.get(i));
        }
        AdImageAdapter adImageAdapter = new AdImageAdapter(this.c, temp_list);
        rcv_ad_images.setAdapter(adImageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                dismiss();
                //Intent finish = new Intent(this.c, CalendarViewMainActivity.class);
               // c.startActivity(finish);
                c.finish();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.iv_more_apps:
               // AccountRedirectActivity.get_url(getContext());
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, this.c.getResources().getString(R.string.app_name));
                    String sAux = "Download this amazing "+this.c.getString(R.string.app_name).toLowerCase()+" app from play store\n\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id="+this.c.getPackageName()+"\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    this.c.startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;
            default:
                break;
        }
        dismiss();
    }

    public class AdImageAdapter extends RecyclerView.Adapter<AdImageAdapter.ViewHolder> {
        Context context;
        List<AdModel> al_ad_item = new ArrayList<>();


        public AdImageAdapter(final Context context, List<AdModel> list_data) {
            this.context = context;
            this.al_ad_item = list_data;

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_ad_image, iv_ad;
            TextView tv_download;
            ProgressBar progressBar;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_ad_image = (ImageView) itemView.findViewById(R.id.iv_ad_image);
                tv_download = (TextView) itemView.findViewById(R.id.tv_app_name);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
                iv_ad = (ImageView) itemView.findViewById(R.id.iv_ad);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_dialog_row_ad_data, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);

            int itemHeight = SharedPrefs.getInt(context, SharedPrefs.ITEM_SIZE);

            int height = (int) (Share.screenWidth * 0.23);
            holder.tv_download.getLayoutParams().width = holder.iv_ad_image.getLayoutParams().height = holder.iv_ad_image.getLayoutParams().width = height;
            holder.iv_ad.getLayoutParams().width = height/2;
            holder.iv_ad.getLayoutParams().height = (int) (((height/2) * 105)/125);

//            Picasso.with(context)
//                    .load(al_ad_item.get(position).getThumb_image())
//                    .into(new Target() {
//                        @Override
//                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                            holder.iv_ad_image.setImageBitmap(bitmap);
//                        }
//
//                        @Override
//                        public void onBitmapFailed(Drawable errorDrawable) {
//
//                        }
//
//                        @Override
//                        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                        }
//                    });


            Glide.with(context)
                    .asBitmap()
                    .load(al_ad_item.get(position).getThumb_image())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            holder.iv_ad_image.setImageBitmap(resource);
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(al_ad_item.get(position).getApp_link())));
                }
            });
        }

        @Override
        public int getItemCount() {
            return al_ad_item.size();
        }
    }

}