package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.RecordingAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.DisplayMetricsHandler;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.Recording;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static com.calculator.vault.gallery.locker.hide.data.smartkit.activity.SplashHomeActivity.activity;

//import static com.kit.tools.box.disk.news.shopping.activity.SplashHomeActivity.activity;


public class RecordingListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewRecordings;
    private ArrayList<Recording> recordingArraylist;
    private RecordingAdapter recordingAdapter;
    private TextView textViewNoRecordings;
    ImageView iv_back, iv_delete_all_files;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_recording_list);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initViews();


        fetchRecordings();

        clearDate(recordingArraylist.size());
        if (Share.isNeedToAdShow(this)) {
            if (activity != null) {
                NativeAdvanceHelper.loadAdBannerSize(activity, (FrameLayout) findViewById(R.id.fl_adplaceholder));
            } else {
                Log.e("RecordingListActivity", "onCreate: Context is null");
            }
        }

      /*  if (recordingArraylist.size() != 0) {
            textViewNoRecordings.setVisibility(View.GONE);
            recyclerViewRecordings.setVisibility(View.VISIBLE);
        } else {
            textViewNoRecordings.setVisibility(View.VISIBLE);
            recyclerViewRecordings.setVisibility(View.GONE);
        }*/
    }

    public void clearDate(int size) {
        if (recordingArraylist.size() == 0) {
            textViewNoRecordings.setVisibility(View.VISIBLE);
            recyclerViewRecordings.setVisibility(View.GONE);
            iv_delete_all_files.setVisibility(View.GONE);
        } else {
            textViewNoRecordings.setVisibility(View.GONE);
            recyclerViewRecordings.setVisibility(View.VISIBLE);
            iv_delete_all_files.setVisibility(View.VISIBLE);
        }
        Log.e("search", "search: -->" + size);
    }

    private void initViews() {
        recordingArraylist = new ArrayList<Recording>();
        iv_back = findViewById(R.id.iv_back);
        iv_delete_all_files = findViewById(R.id.iv_delete_all_files);

        iv_delete_all_files.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        /** setting up recyclerView **/
        recyclerViewRecordings = (RecyclerView) findViewById(R.id.recyclerViewRecordings);
        recyclerViewRecordings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewRecordings.setHasFixedSize(true);

        textViewNoRecordings = (TextView) findViewById(R.id.textViewNoRecordings);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void fetchRecordings() {

        File root = android.os.Environment.getExternalStorageDirectory();
        String path = root.getAbsolutePath() + "/SmartKit360/Record";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {

            for (int i = 0; i < files.length; i++) {

                Log.d("Files", "FileName:" + files[i].getName());
                String fileName = files[i].getName();
                String recordingUri = root.getAbsolutePath() + "/SmartKit360/Record/" + fileName;

                Recording recording = new Recording(recordingUri, fileName, false);
                recordingArraylist.add(recording);
            }

            textViewNoRecordings.setVisibility(View.GONE);
            recyclerViewRecordings.setVisibility(View.VISIBLE);
            setAdaptertoRecyclerView();

        } else {
            textViewNoRecordings.setVisibility(View.VISIBLE);
            recyclerViewRecordings.setVisibility(View.GONE);
        }
    }

    private void setAdaptertoRecyclerView() {

        //Collections.reverse(recordingArraylist);
        recordingAdapter = new RecordingAdapter(this, recordingArraylist);
        recyclerViewRecordings.setAdapter(recordingAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("list", "onResume: play list");
        if (recordingArraylist.size() == 0) {
            textViewNoRecordings.setVisibility(View.VISIBLE);
            recyclerViewRecordings.setVisibility(View.GONE);
        } else {
            textViewNoRecordings.setVisibility(View.GONE);
            recyclerViewRecordings.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_delete_all_files:

                Log.e("del", "onClick: iv_delete_all_files");

                final Dialog dialog3 = new Dialog(this);
                dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog3.setContentView(R.layout.s_clear_all_records_dialog);
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

                        ArrayList<File> filesList = new ArrayList<File>();
                        File contentFolder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/SmartKit360/Record");
                        if (!contentFolder.exists()) {
                            contentFolder.mkdir();
                        }
                        filesList.clear();
                        Collections.addAll(filesList, new File(contentFolder.getAbsolutePath()).listFiles());

                        for (int i = 0; i < filesList.size(); i++) {
                            File file = new File(filesList.get(i).getPath());
                            file.delete();
                            if (file.exists()) {
                                try {
                                    file.getCanonicalFile().delete();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (file.exists()) {
                                    getApplicationContext().deleteFile(file.getName());
                                }
                            }
                        }
                        recordingArraylist.clear();
                        recordingAdapter.notifyDataSetChanged();
                        clearDate(recordingArraylist.size());
                        dialog3.dismiss();
                    }
                });
                Window window3 = dialog3.getWindow();
                window3.setGravity(Gravity.CENTER);
                window3.setLayout((int) (0.9 * DisplayMetricsHandler.getScreenWidth()), Toolbar.LayoutParams.WRAP_CONTENT);

                if (dialog3 != null && !dialog3.isShowing())
                    dialog3.show();

                break;
        }
    }
}