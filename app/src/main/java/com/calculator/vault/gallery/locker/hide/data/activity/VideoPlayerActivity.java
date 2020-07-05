package com.calculator.vault.gallery.locker.hide.data.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    private VideoView movvVideo;

    private static final String TAG = "VideoPlayerActivity";

    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private UniversalVideoView mVideoView;
    private UniversalMediaController mMediaController;
    private TextView mTvToolbarTitle;
    private boolean isFullscreen;
    private int mSeekPosition = 0;

    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player2);

        movvVideo = findViewById(R.id.vv_main);

        Intent intent = getIntent();

        String VIDEO_PATH = intent.getStringExtra("videoURL");

        movvVideo.setVideoURI(Uri.parse(VIDEO_PATH));
        movvVideo.setOnPreparedListener(mp -> movvVideo.start());

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        mTvToolbarTitle = findViewById(R.id.toolbar_tv_title);
        mTvToolbarTitle.setText("Video");
        findViewById(R.id.iv_empty).setVisibility(View.GONE);

        mVideoView = findViewById(R.id.videoView);
        mMediaController = findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);

        mVideoView.setVideoPath(VIDEO_PATH);

        mVideoView.setOnPreparedListener(mp -> mVideoView.start());

        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
                if (isFullscreen) {
                    ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoView.setLayoutParams(layoutParams);
                } else {
                    ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoView.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
        mVideoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        mVideoView.seekTo(mSeekPosition);
        mVideoView.resume();
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.seekTo(mSeekPosition);
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.resume();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

}
