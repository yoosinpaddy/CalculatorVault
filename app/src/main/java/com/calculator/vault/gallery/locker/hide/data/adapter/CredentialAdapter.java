package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.activity.EditCredentialActivity;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.textdrawable.ColorGenerator;
import com.calculator.vault.gallery.locker.hide.data.textdrawable.TextDrawable;
import com.calculator.vault.gallery.locker.hide.data.model.CredentialsModel;

import java.util.ArrayList;

public class CredentialAdapter extends RecyclerView.Adapter<CredentialAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<CredentialsModel> CredentialsModelArrayList;
    private ArrayList<String> itemFileList;
    private String TAG = this.getClass().getSimpleName();
    CredentialAdapter.ItemOnClick itemOnClick;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;
    private ImageVideoDatabase imageVideoDatabase;
    private String mImage;

    public CredentialAdapter(Context context, ArrayList<CredentialsModel> CredentialsModelArrayList, ItemOnClick itemOnClick) {
        this.context = context;
        this.CredentialsModelArrayList = CredentialsModelArrayList;
        this.itemOnClick = itemOnClick;
        this.imageVideoDatabase = new ImageVideoDatabase(context);
    }

    @NonNull
    @Override
    public CredentialAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.credential_list_item, parent, false);

        return new CredentialAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CredentialAdapter.MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        String letter = "A";

        if (CredentialsModelArrayList.get(position).getTitleText() != null && !CredentialsModelArrayList.get(position).getTitleText().isEmpty()) {
            letter = CredentialsModelArrayList.get(position).getTitleText().substring(0, 1);
        }
        int color = mColorGenerator.getRandomColor();

        // Create a circular icon consisting of  a random background colour and first letter of title
        mDrawableBuilder = TextDrawable.builder().buildRound(letter, color);

        holder.mThumbnailText.setVisibility(View.VISIBLE);
        holder.mThumbnailImage.setVisibility(View.GONE);
        holder.mThumbnailText.setImageDrawable(mDrawableBuilder);


        holder.loTvCredentialName.setText(CredentialsModelArrayList.get(position).getTitleText());
        holder.iv_creds_delete.setOnClickListener(v -> {
            if (itemOnClick != null) {
                final Dialog dialog1 = new Dialog(context);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.setContentView(R.layout.alert_decoy_passcode);
                TextView mess = dialog1.findViewById(R.id.tv_message1);
                mess.setText(context.getString(R.string.delete_image_message));
                Button yesbtn = dialog1.findViewById(R.id.btn_dialogOK1);
                Button nobtn = dialog1.findViewById(R.id.btn_dialogNo1);

                yesbtn.setOnClickListener(v1 -> {
                    try {
                        itemOnClick.onClick(position, CredentialsModelArrayList.get(position));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog1.dismiss();
                });
                nobtn.setOnClickListener(v12 -> dialog1.dismiss());

                if (dialog1 != null && !dialog1.isShowing()) {
                    dialog1.show();
                }
            }
        });

        holder.frontLayout.setOnClickListener(v -> {
            int ID = CredentialsModelArrayList.get(position).getID();
            Intent editIntent = new Intent(context, EditCredentialActivity.class);
            editIntent.putExtra("editIntentID", ID);
            context.startActivity(editIntent);
        });
    }

    @Override
    public int getItemCount() {
        Log.e("TAG", "getItemCount: " + CredentialsModelArrayList.size());
        return CredentialsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView loTvCredentialName;
        ImageView mThumbnailText, mThumbnailImage, iv_creds_delete;
        private View frontLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_creds_delete = itemView.findViewById(R.id.iv_creds_delete);
            loTvCredentialName = itemView.findViewById(R.id.tv_credential_user_name);
            mThumbnailText = itemView.findViewById(R.id.text_thumbnail_image);
            mThumbnailImage = itemView.findViewById(R.id.thumbnail_image);
            frontLayout = itemView.findViewById(R.id.front_layout);
        }
    }

    public void onUpdate(ArrayList<CredentialsModel> credListModelsArrayList) {
        Log.e(TAG, "onUpdate: " + "Notify");
        CredentialsModelArrayList.clear();
        CredentialsModelArrayList = credListModelsArrayList;
        this.notifyDataSetChanged();
    }

    public interface ItemOnClick {
        void onClick(int position, CredentialsModel itemModel);
    }
}