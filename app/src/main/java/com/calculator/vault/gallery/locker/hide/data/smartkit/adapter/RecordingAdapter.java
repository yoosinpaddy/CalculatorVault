package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.RecordingListActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.DisplayMetricsHandler;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.Recording;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder> {

    RecordingAdapter adapter;
    private ArrayList<Recording> recordingArrayList;
    private ArrayList<File> filesList = new ArrayList<File>();
    private Context context;
    private MediaPlayer mPlayer;
    private boolean isPlaying = false;
    private int last_index = -1;
    File contentFolder;
    private Handler handler;
    MediaPlayer mediaPlayer = null;
    ViewHolder.MediaObserver observer;

    public RecordingAdapter(Context context, ArrayList<Recording> recordingArrayList) {
        this.context = context;
        this.recordingArrayList = recordingArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.s_recording_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setUpData(holder, position);
    }

    @Override
    public int getItemCount() {
        return recordingArrayList.size();
    }


    private void setUpData(ViewHolder holder, int position) {

        Recording recording = recordingArrayList.get(position);
        for (int i = 0; i < recordingArrayList.size(); i++) {
            holder.tv_recording_name.setText("Recording " + (position + 1));
        }
        // holder.textViewName.setText(recording.getFileName());

        String str = recording.getFileName();

        String[] items = str.split(".m");

        String[] strSplit = items[0].split(" ");

        String strDate = strSplit[0];
        holder.tv_date.setText(strDate);
        String strTime = strSplit[1];
        String strampm = null;
        if (strSplit.length > 2) {
            strampm = strSplit[2];
            holder.tv_time.setText(strTime + strampm);
        } else {
            holder.tv_time.setText(strTime);
        }


        if (recording.isPlaying()) {
            holder.iv_play.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            }
            //  holder.seekBar.setVisibility(View.VISIBLE);
            holder.iv_play.setVisibility(View.GONE);

            // holder.seekUpdation(holder);
        } else {
            holder.iv_play.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            }
            // holder.seekBar.setVisibility(View.GONE);
            holder.iv_play.setVisibility(View.VISIBLE);
        }
        //  holder.manageSeekBar(holder);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_play, iv_delete;
        // SeekBar seekBar;
        TextView tv_date, tv_time, tv_recording_name;
        private String recordingUri;
        private int lastProgress = 0;
        private Handler mHandler = new Handler();
        ViewHolder holder;
        int position;
        Recording recording;
        int progr;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_play = itemView.findViewById(R.id.iv_play);

            iv_delete = itemView.findViewById(R.id.iv_delete);
            //seekBar = itemView.findViewById(R.id.seekBar);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_recording_name = itemView.findViewById(R.id.tv_recording_name);

            contentFolder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/SmartKit360/Record");
            if (!contentFolder.exists()) {
                contentFolder.mkdir();
            }
            filesList.clear();
            Collections.addAll(filesList, new File(contentFolder.getAbsolutePath()).listFiles());

            iv_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    recording = recordingArrayList.get(position);

                    recordingUri = recording.getUri();
                    Log.e("uri", "onClick: recordingUri-->" + recordingUri);
                    Log.e("uri", "onClick: recordingUri size -->" + recordingArrayList.size());
                    Log.e("uri", "onClick: recording-->" + recording);
                    last_index = position;

                    playAudioDialog(recordingUri, R.color.white);
                }

            });


            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog3 = new Dialog(context);
                    dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog3.setContentView(R.layout.s_clear_record_dialog);
                    dialog3.setCanceledOnTouchOutside(true);

                    Button btn_no = dialog3.findViewById(R.id.btn_no);
                    Button btn_yes = dialog3.findViewById(R.id.btn_yes);


                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog3.dismiss();
                        }
                    });
                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            File file = new File(filesList.get(position).getPath());
                            file.delete();
                            recordingArrayList.remove(position);
                            notifyItemRemoved(position);

                            contentFolder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/SmartKit360/Record");
                            if (!contentFolder.exists()) {
                                contentFolder.mkdir();
                            }
                            filesList.clear();
                            Collections.addAll(filesList, new File(contentFolder.getAbsolutePath()).listFiles());
                            notifyDataSetChanged();
                            ((RecordingListActivity)context).clearDate(recordingArrayList.size());
                            dialog3.dismiss();
                        }
                    });
                    Window window3 = dialog3.getWindow();
                    window3.setGravity(Gravity.CENTER);
                    window3.setLayout((int) (0.9 * DisplayMetricsHandler.getScreenWidth()), Toolbar.LayoutParams.WRAP_CONTENT);

                    if (dialog3 != null && !dialog3.isShowing())
                        dialog3.show();
                }
            });

        }


        private void initMediaPlayer(String uri, final AppCompatSeekBar sb_seekbar, final ImageView iv_play_pause) {

            mediaPlayer = new MediaPlayer();
            FileInputStream fis;
            try {
                fis = new FileInputStream(uri);
                mediaPlayer.setDataSource(fis.getFD());

                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        sb_seekbar.setMax(mp.getDuration());
                        try {
                            observer = new MediaObserver(sb_seekbar);
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Failed to initialize", Toast.LENGTH_SHORT).show();
                        }
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                        }
                        new Thread(observer).start();
                        iv_play_pause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));
                        iv_play_pause.setColorFilter(context.getResources().getColor(R.color.black));
                        iv_play_pause.setTag("pause");
                        sb_seekbar.setEnabled(true);
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        observer.stop();
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        iv_play_pause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                        iv_play_pause.setColorFilter(context.getResources().getColor(R.color.black));
                        iv_play_pause.setTag("play");
                        sb_seekbar.setEnabled(false);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private class MediaObserver implements Runnable {
            AppCompatSeekBar appCompatSeekBar;
            private AtomicBoolean stop = new AtomicBoolean(false);

            MediaObserver(AppCompatSeekBar sb_seekbar) {
                this.appCompatSeekBar = sb_seekbar;
            }

            void stop() {
                stop.set(true);
            }

            @Override
            public void run() {
                while (!stop.get()) {
                    if (mediaPlayer != null) {
                        appCompatSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public void playAudioDialog(final String uri, int color) {

            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.s_custom_dailog_play_audio);
            final DisplayMetrics displayMetrics = new DisplayMetrics();

            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            dialog.getWindow().setLayout(displayMetrics.widthPixels - 100, Toolbar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            TextView tv_file_name = dialog.findViewById(R.id.tv_file_name);
            final AppCompatSeekBar sb_seekbar = dialog.findViewById(R.id.sb_seekbar);
            final ImageView iv_play_pause = dialog.findViewById(R.id.iv_play_pause);
            iv_play_pause.setColorFilter(context.getResources().getColor(R.color.black));
            //iv_play_pause.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            // DrawableCompat.setTint(iv_play_pause.getDrawable(), ContextCompat.getColor(context, R.color.white));
            tv_file_name.setText("Recording " + (position + 1));


            iv_play_pause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));
            iv_play_pause.setTag("pause");
            iv_play_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iv_play_pause.getTag().toString().equals("pause")) {
                        if (mediaPlayer != null) {
                            mediaPlayer.pause();
                        }
                        iv_play_pause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                        iv_play_pause.setTag("play");
                        iv_play_pause.setColorFilter(context.getResources().getColor(R.color.black));
                    } else {

                        Log.e("play_pause", "onClick: play");
                        if (mediaPlayer == null) {
                            initMediaPlayer(uri, sb_seekbar, iv_play_pause);
                        } else {
                            mediaPlayer.start();
                        }
                        iv_play_pause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));
                        iv_play_pause.setTag("pause");
                        iv_play_pause.setColorFilter(context.getResources().getColor(R.color.black));
                    }
                }
            });

            sb_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    mediaPlayer.seekTo(100);
                    //seekBar.setProgress(200);
                }
            });
            initMediaPlayer(recordingUri, sb_seekbar, iv_play_pause);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (mediaPlayer != null) {
                        if (observer != null) {
                            observer.stop();
                        }
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

                    /*File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartKit360/Record/" + (recordingUri));
                    if (!file.exists()) {
                        file.delete();
                    }*/
                }
            });

            dialog.show();
        }
    }


}