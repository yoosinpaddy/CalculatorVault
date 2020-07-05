package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.activity.ViewFullScreenImageActivity;
import com.calculator.vault.gallery.locker.hide.data.model.BreakInImageModel;

import java.io.File;
import java.util.ArrayList;

public class BreakInImagesAdapter extends RecyclerView.Adapter<BreakInImagesAdapter.MyViewHolder> {
    private Context moContext;
    private ArrayList<BreakInImageModel> moImageFILEList;
    private int miScreenWidth;
    private ArrayList<BreakInImageModel> itemFileList;
    private String TAG = this.getClass().getSimpleName();
    private String msPickFromGallery;
    BreakInImagesAdapter.ItemOnClick itemOnClick;

    public BreakInImagesAdapter(Context foContext, ArrayList<BreakInImageModel> imagePathList, BreakInImagesAdapter.ItemOnClick itemOnClick) {
        this.moContext = foContext;
        this.moImageFILEList = imagePathList;
        miScreenWidth = ((Activity) moContext).getWindowManager().getDefaultDisplay().getWidth();
        // screen_height = ((Activity) moContext).getWindowManager().getDefaultDisplay().getHeight();
        this.itemOnClick = itemOnClick;
    }

    @NonNull
    @Override
    public BreakInImagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.break_in_image_item, parent, false);

        return new BreakInImagesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BreakInImagesAdapter.MyViewHolder foHolder, final int foPosition) {

        foHolder.setIsRecyclable(false);


        foHolder.loTvtimestamp.setText(moImageFILEList.get(foPosition).getDataTime());
        foHolder.loTvwrongPass.setText("Attempt code: " + moImageFILEList.get(foPosition).getWrongPassword());

        File file = new File(moImageFILEList.get(foPosition).getFilePath());

        Glide.with(moContext)
                .load(file)
                //    .error(R.drawable.ic_action_broken)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .centerCrop()
                .into(foHolder.loIvbreakInImage);
        /*Picasso.with(moContext)
                .load(moImageFILEList.get(position))
                .fit()
                .into(holder.loIvHiddenImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        //holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });*/

      /*  if (moImageFILEList.get(foPosition).isSelected()) {
            Log.e("isSelected", "Selected");
            foHolder.loCheckbox.setVisibility(View.VISIBLE);
        } else {
            Log.e("isSelected", "Not Selected");

            foHolder.loCheckbox.setVisibility(View.INVISIBLE);
        }*/
        //holder.iv_cake_image.setScaleType(ImageView.ScaleType.FIT_XY);
       /* foHolder.loIvHiddenImage.getLayoutParams().width = miScreenWidth / 3;
        foHolder.loIvHiddenImage.getLayoutParams().height = (int) ((miScreenWidth / 3) * 1.2);*/
        foHolder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(moContext, ViewFullScreenImageActivity.class);
            intent.putExtra("breakIntent",foPosition);
            intent.putExtra("isFrom","BreakIn");
            moContext.startActivity(intent);

            if (itemOnClick != null) {
                //itemOnClick.onClick(foPosition, moImageFILEList.get(foPosition), foHolder.loCheckbox, moImageFILEList.get(foPosition).isSelected());
            }

        });
    }

    @Override
    public int getItemCount() {
        Log.e("TAG", "getItemCount: " + moImageFILEList.size());
        return moImageFILEList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView loTvtimestamp, loTvwrongPass;
        ImageView loIvbreakInImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            loTvtimestamp = itemView.findViewById(R.id.tv_date_time);
            loTvwrongPass = itemView.findViewById(R.id.tv_attempt);
            loIvbreakInImage = itemView.findViewById(R.id.iv_ss_thumbnail);
        }

    }
    public void onUpdate(ArrayList<BreakInImageModel> moImageFILEList){
        Log.e(TAG, "onUpdate: "+"Notify" );
        this.moImageFILEList.clear();
        this.moImageFILEList=moImageFILEList;
        this.notifyDataSetChanged();
    }
    public interface ItemOnClick {
        void onClick(int fiPosition, BreakInImageModel foBreakInImageModel, ImageView foIvcheckBox, boolean isselected);
    }
}
