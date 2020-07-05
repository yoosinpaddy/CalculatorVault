package com.calculator.vault.gallery.locker.hide.data.commonCode.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.commonCode.models.AdModel;

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
        setContentView(R.layout.custom_dialog);

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

//        card_view.getLayoutParams().height = (int) (GlobalData.screenHeight * 0.75);
//        card_view.getLayoutParams().width = (int) (GlobalData.screenWidth * 0.9);

        card_view.getLayoutParams().height = (int) (SharedPrefs.getInt(getContext(), SharedPrefs.screen_hight) * 0.75);
        card_view.getLayoutParams().width = (int) (SharedPrefs.getInt(getContext(), SharedPrefs.screen_width)  * 0.9);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.c, 3);
        rcv_ad_images.setLayoutManager(gridLayoutManager);

        Log.e("TAG", "Size : " + GlobalData.al_ad_data.size());
        ArrayList<AdModel> exit_adds = new ArrayList<>();
        for (int i = GlobalData.al_ad_data.size()-1; i >= 0 ; i--) {
            exit_adds.add(GlobalData.al_ad_data.get(i));
        }
        AdImageAdapter adImageAdapter = new AdImageAdapter(this.c, exit_adds);
        rcv_ad_images.setAdapter(adImageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                dismiss();
                //ToDo: Intent Removed...
//                Intent finish = new Intent(this.c, Splash_MenuActivity.class);
//                c.startActivity(finish);
                c.finish();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.iv_more_apps:
                //AccountRedirectActivity.get_url(getContext());
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getContext().getResources().getString(R.string.app_name));
                    String sAux = "Download this amazing "+getContext().getString(R.string.app_name).toLowerCase()+" app from play store\n\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id="+getContext().getPackageName()+"\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    getContext().startActivity(Intent.createChooser(i, "choose one"));
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
            ImageView iv_ad_image;
            TextView tv_download;
            ProgressBar progressBar;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_ad_image = (ImageView) itemView.findViewById(R.id.iv_icon);
                tv_download = (TextView) itemView.findViewById(R.id.tv_download);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_row_ad_data, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);

            int itemHeight = SharedPrefs.getInt(context, SharedPrefs.ITEM_SIZE);

            int height = (int) (GlobalData.screenWidth * 0.23);
            holder.tv_download.getLayoutParams().width = holder.iv_ad_image.getLayoutParams().height = holder.iv_ad_image.getLayoutParams().width = height;


            Glide.with(context)
                    .load(al_ad_item.get(position).getThumb_image())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.iv_ad_image) ;

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