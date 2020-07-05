package com.calculator.vault.gallery.locker.hide.data.commonCode.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.commonCode.models.CategoryModel;
import com.calculator.vault.gallery.locker.hide.data.commonCode.models.SubCatModel;
import com.calculator.vault.gallery.locker.hide.data.commonCode.utils.GlobalData;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;


/**
 * Created by soham on 11/10/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public Context con;
    String cat_id;
    private AdapterView.OnItemClickListener mCallback;

    public MyAdapter(Context context, String id) {
        this.con = context;
        cat_id = id;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_header, ll_body, ll_body1, ll_firstrow;
        public SliderLayout bannerSlider;
        public TextView tv_header_text;
        public View view1, view2, view3, viewHorizontal, view11, view22, view33;

        public MyViewHolder(View view) {
            super(view);

            ll_header = (LinearLayout) view.findViewById(R.id.ll_header);
            ll_body = (LinearLayout) view.findViewById(R.id.ll_body);
            ll_body1 = (LinearLayout) view.findViewById(R.id.ll_body1);
            bannerSlider = (SliderLayout) view.findViewById(R.id.banner_slider);
            tv_header_text = (TextView) view.findViewById(R.id.tv_header_text);
            view1 = (View) view.findViewById(R.id.view1);
            view2 = (View) view.findViewById(R.id.view2);
            view3 = (View) view.findViewById(R.id.view3);
            view11 = (View) view.findViewById(R.id.view11);
            view22 = (View) view.findViewById(R.id.view22);
            view33 = (View) view.findViewById(R.id.view33);
            ll_firstrow = (LinearLayout) view.findViewById(R.id.ll_first_row);
            viewHorizontal = (View) view.findViewById(R.id.hori_view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rcv_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (position == 0) {
            holder.ll_header.setVisibility(View.VISIBLE);
            holder.ll_body.setVisibility(View.GONE);
            holder.ll_body1.setVisibility(View.GONE);

//            holder.bannerSlider.getLayoutParams().height = (int) (GlobalData.screenHeight * 0.25);
            holder.bannerSlider.getLayoutParams().height = (int) (SharedPrefs.getInt(con, SharedPrefs.screen_hight)  * 0.25);

            holder.bannerSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            holder.bannerSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            // holder.bannerSlider.setCustomAnimation(new DescriptionAnimation());
            // holder.bannerSlider.setDuration(4000);

            holder.ll_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("link111", "link" + holder.bannerSlider.getCurrentSlider().getDescription());
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + holder.bannerSlider.getCurrentSlider().getDescription()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        con.startActivity(intent);
                    } catch (android.content.ActivityNotFoundException anfe) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + holder.bannerSlider.getCurrentSlider().getDescription()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        con.startActivity(intent);
                    }
                }
            });

            if (cat_id.equalsIgnoreCase("Home")) {

                holder.bannerSlider.removeAllSliders();
                holder.bannerSlider.getPagerIndicator().removeAllViews();

                for (int i = 0; i < GlobalData.al_app_center_home_data.size(); i++) {
                    CategoryModel model = GlobalData.al_app_center_home_data.get(i);

                    for (int j = 0; j < model.getSub_category().size(); j++) {
                        final SubCatModel submodel = model.getSub_category().get(j);

                        Log.e("banner link", "banner" + submodel.getBanner_image());
                        if (!submodel.getBanner_image().equalsIgnoreCase("")) {

                            TextSliderView textSliderView = new TextSliderView(con.getApplicationContext());
                            // initialize a SliderLayout
                            textSliderView
                                    .image(submodel.getBanner_image())
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .description(submodel.getApp_link())
                                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(BaseSliderView slider) {
                                            try {
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + submodel.getApp_link()));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                con.startActivity(intent);
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + submodel.getApp_link()));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                con.startActivity(intent);
                                            }
                                        }
                                    });
                            holder.bannerSlider.addSlider(textSliderView);
                        }
                    }
                }
            } else {
                Log.e("Slider ======>","slider"+holder.bannerSlider.getPagerIndicator());

                holder.bannerSlider.removeAllSliders();
                holder.bannerSlider.getPagerIndicator().removeAllViews();

                for (int i = 0; i < GlobalData.al_app_center_data.size(); i++) {
                    CategoryModel model = GlobalData.al_app_center_data.get(i);

                    if (model.getName().equalsIgnoreCase(cat_id)) {
                        for (int j = 0; j < model.getSub_category().size(); j++) {
                            final SubCatModel submodel = model.getSub_category().get(j);

                            Log.e("banner link", "banner" + submodel.getBanner_image());
                            if (!submodel.getBanner_image().equalsIgnoreCase("")) {

                                TextSliderView textSliderView = new TextSliderView(con.getApplicationContext());
                                // initialize a SliderLayout
                                textSliderView
                                        .image(submodel.getBanner_image())
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .description(submodel.getApp_link())
                                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(BaseSliderView slider) {
                                                try {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + submodel.getApp_link()));
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    con.startActivity(intent);
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + submodel.getApp_link()));
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    con.startActivity(intent);
                                                }
                                            }
                                        });
                                holder.bannerSlider.addSlider(textSliderView);
                            }
                        }
                    }
                }
            }
        } else {
            holder.ll_header.setVisibility(View.GONE);

            if (cat_id.equalsIgnoreCase("Home")) {
                holder.ll_body.setVisibility(View.VISIBLE);
                holder.ll_body1.setVisibility(View.GONE);

                holder.tv_header_text.setText(GlobalData.al_app_center_home_data.get(position - 1).getName());

                for (int i = 0; i < GlobalData.al_app_center_home_data.get(position - 1).getSub_category().size(); i++) {

                    SubCatModel model = GlobalData.al_app_center_home_data.get(position - 1).getSub_category().get(i);
                    if (i == 0) {
                        SetSingleItem(holder.view1, model);
                        holder.view2.setVisibility(View.GONE);
                        holder.view3.setVisibility(View.GONE);
                    } else if (i == 1) {
                        SetSingleItem(holder.view2, model);
                        holder.view2.setVisibility(View.VISIBLE);
                    } else if (i == 2) {
                        SetSingleItem(holder.view3, model);
                        holder.view3.setVisibility(View.VISIBLE);
                    }

                    if (GlobalData.al_app_center_home_data.get(position - 1).getSub_category().size() < 3) {
                        Log.e("sdsdsd", "sdfasda");
                        holder.view1.setLayoutParams(new LinearLayout.LayoutParams((int) (GlobalData.screenWidth * 0.32), ViewGroup.LayoutParams.WRAP_CONTENT));
                        holder.view2.setLayoutParams(new LinearLayout.LayoutParams((int) (GlobalData.screenWidth * 0.32), ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
            } else {
                holder.ll_body1.setVisibility(View.VISIBLE);
                holder.ll_body.setVisibility(View.GONE);

                if (position == 1) {
                    holder.viewHorizontal.setVisibility(View.GONE);
                    holder.ll_firstrow.setVisibility(View.VISIBLE);


                    for (int i = 0; i < GlobalData.al_app_center_data.size(); i++) {

                        CategoryModel model = GlobalData.al_app_center_data.get(i);
                        if (model.getName().equalsIgnoreCase(cat_id)) {

                            for (int j = 0; j < model.getSub_category().size(); j++) {

                                SubCatModel submodel = model.getSub_category().get(j);
                                if (j == 0) {
                                    SetSingleItem(holder.view11, submodel);
                                    holder.view22.setVisibility(View.GONE);
                                    holder.view33.setVisibility(View.GONE);
                                } else if (j == 1) {
                                    SetSingleItem(holder.view22, submodel);
                                    holder.view22.setVisibility(View.VISIBLE);
                                } else if (j == 2) {
                                    SetSingleItem(holder.view33, submodel);
                                    holder.view33.setVisibility(View.VISIBLE);
                                }

                                if (model.getSub_category().size() < 3) {
                                    holder.view11.setLayoutParams(new LinearLayout.LayoutParams((int) (GlobalData.screenWidth * 0.32), ViewGroup.LayoutParams.WRAP_CONTENT));
                                    holder.view22.setLayoutParams(new LinearLayout.LayoutParams((int) (GlobalData.screenWidth * 0.32), ViewGroup.LayoutParams.WRAP_CONTENT));
                                }
                            }
                        }
                    }
                } else {
                    holder.viewHorizontal.setVisibility(View.VISIBLE);
                    holder.ll_firstrow.setVisibility(View.GONE);

                    for (int i = 0; i < GlobalData.al_app_center_data.size(); i++) {

                        CategoryModel model = GlobalData.al_app_center_data.get(i);
                        if (model.getName().equalsIgnoreCase(cat_id)) {

                            if (model.getSub_category().size() > (position + 1)) {
                                for (int j = 3; j < model.getSub_category().size(); j++) {

                                    SubCatModel submodel = model.getSub_category().get(position + 1);
                                    SetSingleHoriItem(holder.viewHorizontal, submodel);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void SetSingleItem(View view, final SubCatModel model) {
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tv_download = (TextView) view.findViewById(R.id.tv_download);
        TextView tv_appname = (TextView) view.findViewById(R.id.tv_appname);

        iv_icon.getLayoutParams().height = iv_icon.getLayoutParams().width = tv_download.getLayoutParams().width =
                tv_appname.getLayoutParams().width = (int) (GlobalData.screenWidth * 0.23);

        Picasso.with(con)
                .load(model.getIcon())
                .into(iv_icon);
        tv_appname.setText(model.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("link", "link" + model.getApp_link());
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + model.getApp_link()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    con.startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + model.getApp_link()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    con.startActivity(intent);
                }
            }
        });
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void SetSingleHoriItem(View view, final SubCatModel model) {
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tv_download = (TextView) view.findViewById(R.id.tv_download);
        TextView tv_appname = (TextView) view.findViewById(R.id.tv_appname);
        TextView tv_installs = (TextView) view.findViewById(R.id.tv_installs);
        ImageView iv_star = (ImageView) view.findViewById(R.id.iv_star);

        iv_icon.getLayoutParams().height = iv_icon.getLayoutParams().width = tv_download.getLayoutParams().width = (int) (GlobalData.screenWidth * 0.23);
        Log.e("nameeee", "name" + model.getIcon() + "//" + model.getName() + "//" + model.getInstalled_range());
        Picasso.with(con)
                .load(model.getIcon())
                .into(iv_icon);
        tv_installs.setText(model.getInstalled_range());
        tv_appname.setText(model.getName());
        if (Float.valueOf(model.getStar()) <= 3) {
            iv_star.setImageDrawable(con.getResources().getDrawable(R.drawable.ic_tran));
        } else if (Float.valueOf(model.getStar()) <= 3.5) {
            iv_star.setImageDrawable(con.getResources().getDrawable(R.drawable.ic_sada_tran));
        } else if (Float.valueOf(model.getStar()) <= 4) {
            iv_star.setImageDrawable(con.getResources().getDrawable(R.drawable.ic_four));
        } else if (Float.valueOf(model.getStar()) <= 4.5) {
            iv_star.setImageDrawable(con.getResources().getDrawable(R.drawable.ic_four_n_half));
        } else if (Float.valueOf(model.getStar()) <= 5) {
            iv_star.setImageDrawable(con.getResources().getDrawable(R.drawable.ic_five));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("link", "link" + model.getApp_link());
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + model.getApp_link()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    con.startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + model.getApp_link()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    con.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cat_id.equalsIgnoreCase("Home")) {
            return GlobalData.al_app_center_home_data.size() + 1;
        } else {
            int m = 1;
            for (int i = 0; i < GlobalData.al_app_center_data.size(); i++) {

                CategoryModel model = GlobalData.al_app_center_data.get(i);
                if (model.getName().equalsIgnoreCase(cat_id)) {
                    m = GlobalData.al_app_center_data.get(i).getSub_category().size();
                }
            }
            if (m == 0) {
                return 1;
            } else if (m <= 3) {
                return 2;
            } else {
                return m - 3 + 2;
            }
        }
    }
}
