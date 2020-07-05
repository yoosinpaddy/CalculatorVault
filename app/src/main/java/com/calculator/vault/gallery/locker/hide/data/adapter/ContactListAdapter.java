package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.activity.ViewContactActivity;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.ContactModel;
import com.calculator.vault.gallery.locker.hide.data.textdrawable.ColorGenerator;
import com.calculator.vault.gallery.locker.hide.data.textdrawable.TextDrawable;

import java.io.File;
import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private ArrayList<ContactModel> contacts = new ArrayList<>();
    private ArrayList<ContactModel> filterContacts = new ArrayList<>();
    private String TAG = this.getClass().getSimpleName();
    private ItemOnClick itemOnClick;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;
    private ImageVideoDatabase imageVideoDatabase;
    private String mImage;

    public ContactListAdapter(Context context, ArrayList<ContactModel> contacts, ItemOnClick itemOnClick) {
        this.context = context;
        this.contacts = contacts;
        this.filterContacts.addAll(contacts);
        this.itemOnClick = itemOnClick;
        this.imageVideoDatabase = new ImageVideoDatabase(context);
    }

    @NonNull
    @Override
    public ContactListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contactlistitem, parent, false);

        return new ContactListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactListAdapter.MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        String letter = "A";

        if (filterContacts.get(position).getContactFullName() != null && !filterContacts.get(position).getContactFullName().isEmpty()) {
            letter = filterContacts.get(position).getContactFullName().substring(0, 1);
        }
        int color = mColorGenerator.getRandomColor();

        try {
            color = filterContacts.get(position).getContactColor();
        } catch (NumberFormatException e) {
            color = mColorGenerator.getRandomColor();
            e.printStackTrace();
        }

        mDrawableBuilder = TextDrawable.builder().buildRound(letter, color);

        mImage = filterContacts.get(position).getContactFilePath();
        if (mImage == null || mImage.equals("")) {
            holder.mThumbnailText.setVisibility(View.VISIBLE);
            holder.mThumbnailImage.setVisibility(View.GONE);
            holder.mThumbnailText.setImageDrawable(mDrawableBuilder);
        } else {
            holder.mThumbnailText.setVisibility(View.GONE);
            holder.mThumbnailImage.setVisibility(View.VISIBLE);
            try {
                File file = new File(filterContacts.get(position).getContactFilePath());
                Glide.with(context)
                        .load(file)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .fitCenter()
                        .centerCrop()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.mThumbnailImage.setImageDrawable(resource);
                                return false;
                            }
                        })
                        .into(holder.mThumbnailImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.loTvContactName.setText(filterContacts.get(position).getContactFullName());
        holder.loTvContactNumber.setText(filterContacts.get(position).getContactPhoneNumber());
        holder.iv_contact_delete.setOnClickListener(v -> {
            if (itemOnClick != null) {
                final Dialog dialog1 = new Dialog(context);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.setContentView(R.layout.alert_decoy_passcode);
                TextView mess = dialog1.findViewById(R.id.tv_message1);
                mess.setText(context.getString(R.string.delete_image_message));
                Button yesbtn = dialog1.findViewById(R.id.btn_dialogOK1);
                Button nobtn = dialog1.findViewById(R.id.btn_dialogNo1);

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            itemOnClick.onClick(position, filterContacts.get(position));
                          //  filterContacts.remove(position);
                          //  removeContact(filterContacts.get(position));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog1.dismiss();
                    }
                });
                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                if (dialog1 != null && !dialog1.isShowing()) {
                    dialog1.show();
                }
            }
        });

        holder.frontLayout.setOnClickListener(v -> {

            int ID = filterContacts.get(position).getID();
            Intent editIntent = new Intent(context, ViewContactActivity.class);
            editIntent.putExtra("editIntentID", ID);
            context.startActivity(editIntent);
        });
    }

    private void removeContact(ContactModel contactModel) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contactModel.getID() == contacts.get(i).getID()) {
                this.contacts.remove(i);
                break;
            }
        }
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {


        if (filterContacts != null && filterContacts.isEmpty()) {
            Log.e("TAG", "getItemCount: " + filterContacts.size());
            itemOnClick.onEmpty(true);
        } else {
            itemOnClick.onEmpty(false);
        }

        return filterContacts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterContacts = contacts;
                } else {
                    ArrayList<ContactModel> filteredList = new ArrayList<>();
                    for (ContactModel row : contacts) {
                        String fName = row.getContactFullName();
                        if (fName.trim().length() > 0) {
                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (fName.toLowerCase().contains(charString.toLowerCase()) || fName.contains(charSequence)) {
                                filteredList.add(row);
                            }
                        }
                    }

                    filterContacts = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterContacts;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterContacts = (ArrayList<ContactModel>) filterResults.values;

                try {
                    if (filterContacts == null || filterContacts.isEmpty()) {
                        //   emptyListener.isEmpty(true);
                    } else {
                        // emptyListener.isEmpty(false);
                        //  clickListener.itemChanged(filterContacts.size());
                    }
                } catch (Exception e) {
                    //  emptyListener.isEmpty(true);
                }


                notifyDataSetChanged();

            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView loTvContactName;
        TextView loTvContactNumber;
        ImageView mThumbnailText, mThumbnailImage, iv_contact_delete;
        private View frontLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            loTvContactName = itemView.findViewById(R.id.tv_contact_name);
            loTvContactNumber = itemView.findViewById(R.id.tv_contact_number);
            mThumbnailText = itemView.findViewById(R.id.text_thumbnail_image);
            mThumbnailImage = itemView.findViewById(R.id.thumbnail_image);
            frontLayout = itemView.findViewById(R.id.front_layout);
            iv_contact_delete = itemView.findViewById(R.id.iv_contact_delete);
        }
    }

    public void onUpdate(ArrayList<ContactModel> contactList, String string) {
        Log.e(TAG, "onUpdate: " + "Notify");
        contacts = new ArrayList<>();
        filterContacts = new ArrayList<>();
        contacts = contactList;
        filterContacts.addAll(contactList);
      //  notifyDataSetChanged();
        if (filterContacts != null && !filterContacts.isEmpty())
            getFilter().filter(string);

    }

    public interface ItemOnClick {
        void onClick(int position, ContactModel itemModel);

        void onEmpty(boolean isEmpty);
    }
}