package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.calculator.vault.gallery.locker.hide.data.activity.NoteEditActivity;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.model.NoteListModel;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NoteListModel> noteTextList;
    private String TAG = this.getClass().getSimpleName();
    ItemOnClick itemOnClick;

    public NotesAdapter(Context context, ArrayList<NoteListModel> noteTextList, ItemOnClick itemOnClick) {
        this.context = context;
        this.noteTextList = noteTextList;
        this.itemOnClick = itemOnClick;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_list, parent, false);

        return new NotesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        holder.iv_note_copy.setVisibility(View.GONE);

        if (noteTextList != null && 0 <= position && position < noteTextList.size()) {
            final String data;
            if (!TextUtils.isEmpty(noteTextList.get(position).getNoteTitle())) {
                data = noteTextList.get(position).getNoteTitle();
            }else {
                data = noteTextList.get(position).getNote_text();
            }
            final String timestamp = noteTextList.get(position).getTimestamp();
            holder.bind(data, timestamp, position);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = noteTextList.get(position).getID();
                Intent editIntent = new Intent(context, NoteEditActivity.class);
                editIntent.putExtra("editIntentID", ID);
                context.startActivity(editIntent);
            }
        });

        holder.iv_note_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("note", noteTextList.get(holder.getAdapterPosition()).getNote_text().trim());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Note copied to clipboard.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("TAG", "getItemCount: " + noteTextList.size());
        return noteTextList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_note_item;
        TextView tv_note_timestamp;
        ImageView iv_note_delete, iv_note_copy;
        private View frontLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_note_delete = itemView.findViewById(R.id.iv_note_delete);
            iv_note_copy = itemView.findViewById(R.id.iv_note_copy);
            tv_note_item = itemView.findViewById(R.id.tv_note_text);
            tv_note_timestamp = itemView.findViewById(R.id.tv_note_timestamp);
            frontLayout = itemView.findViewById(R.id.front_layout);
        }

        public void bind(final String data, final String timestamp, final int position) {

            iv_note_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClick != null) {
                        final Dialog dialog1 = new Dialog(context);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.setContentView(R.layout.alert_decoy_passcode);
                        TextView mess = (TextView) dialog1.findViewById(R.id.tv_message1);
                        mess.setText(context.getString(R.string.delete_image_message));
                        Button yesbtn = (Button) dialog1.findViewById(R.id.btn_dialogOK1);
                        Button nobtn = (Button) dialog1.findViewById(R.id.btn_dialogNo1);

                        yesbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    itemOnClick.onClick(position, noteTextList.get(position));
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
                }
            });

            tv_note_item.setText(data);
            tv_note_timestamp.setText(timestamp);


            frontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String displayText = "" + data + " clicked";
                    Log.d("RecyclerAdapter", displayText);
                    int ID = noteTextList.get(position).getID();
                    Intent editIntent = new Intent(context, NoteEditActivity.class);
                    editIntent.putExtra("editIntentID", ID);
                    context.startActivity(editIntent);
                }
            });
        }
    }

    public void onUpdate(ArrayList<NoteListModel> noteListModelsArrayList) {
        Log.e(TAG, "onUpdate: " + "Notify");
        noteTextList.clear();
        noteTextList = noteListModelsArrayList;
        this.notifyDataSetChanged();
    }

    public interface ItemOnClick {
        void onClick(int position, NoteListModel itemModel);
    }
}