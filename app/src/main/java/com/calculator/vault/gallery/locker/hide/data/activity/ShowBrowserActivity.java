package com.calculator.vault.gallery.locker.hide.data.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.DisplayHelper;
import com.calculator.vault.gallery.locker.hide.data.KeyboardHelper;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.adapter.BookmarkPopupAdapter;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.widgets.LollipopFixedWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShowBrowserActivity extends AppCompatActivity implements View.OnClickListener, BookmarkPopupAdapter.OnBookmarkClickListener {
    private String msBrowserLink;
    private ImageView ivLeftVisited, ivRightVisited, ivRefresh, ivBookMark;
    private TextView tvDone;
    private ProgressBar pb_browserLoad;
    private EditText etBrowserText;
    private ArrayList<String> mBrowserUrlList = new ArrayList<>();
    private int position = -1;
    private LollipopFixedWebView myWebView;

    private BookmarkPopupAdapter.OnBookmarkClickListener bookmarkClickListener;
    private PopupWindow popupWindow;
    private TextView tvEmptyBookmark;
    private Button btnAddBookmark;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_browser);
        bookmarkClickListener = this;
        initView();
        hideKeyBoard(etBrowserText, ShowBrowserActivity.this);
        initViewListener();
        initViewAction();
        setBrowserControllers(position, mBrowserUrlList.size());

        String loBrowserSelection = SharedPrefs.getString(this, SharedPrefs.BROWSER_SELECTED, null);
        if (loBrowserSelection == null) {
            msBrowserLink = getString(R.string.googlelink);
        } else {
            checkSelection(loBrowserSelection);
        }

        myWebView.loadUrl(msBrowserLink);
        Log.e("TAG::: ", "onCreate: " + myWebView.getUrl());

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                etBrowserText.setText(url);
                etBrowserText.setSelection(etBrowserText.getText().length());
                hideKeyBoard(etBrowserText, ShowBrowserActivity.this);
                Log.e("WebView", "your current url when webpage loading..:: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("WebView", "your current url when webpage loading.. finish::: " + url);
                super.onPageFinished(view, url);
                if (!mBrowserUrlList.contains(url)) {
                    mBrowserUrlList.add(url);
                    position++;
                    setBrowserControllers(position, mBrowserUrlList.size());
                }
                hideKeyBoard(etBrowserText, ShowBrowserActivity.this);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("when you click on any interlink on webview that time you got url :-" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        // To get web loaded progress and to display progressBar...
        myWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);

                pb_browserLoad.setProgress(progress);
                if (progress == 100) {
                    pb_browserLoad.setVisibility(View.GONE);

                } else {
                    pb_browserLoad.setVisibility(View.VISIBLE);

                }

            }
        });

        myWebView.getSettings().setJavaScriptEnabled(true);

    }

    private void initViewAction() {

        etBrowserText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    Log.e("TAG", "onKey: " + "Enter");

                    String query = etBrowserText.getText().toString();

                    Log.e("ShowBrowserActivity", "onKey: QUERY= " + query);
                    if (!Patterns.WEB_URL.matcher(query).matches()) {
                        myWebView.loadUrl(msBrowserLink + "search?q=" + query);
                    } else if (!query.startsWith("https://") && !query.startsWith("http://")) {
                        myWebView.loadUrl("https://" + query);
                    } else {
                        myWebView.loadUrl(query);
                    }

                    hideKeyBoard(myWebView, ShowBrowserActivity.this);

                    return true;
                }
                return false;
            }

        });

    }

    private void initViewListener() {

        tvDone.setOnClickListener(this);
        ivLeftVisited.setOnClickListener(this);
        ivRightVisited.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        ivBookMark.setOnClickListener(this);
    }

    private void initView() {

        myWebView = findViewById(R.id.webView);
        ivRightVisited = findViewById(R.id.iv_rightVisited);
        ivLeftVisited = findViewById(R.id.iv_leftVisited);
        ivRefresh = findViewById(R.id.iv_browser_refresh);
        etBrowserText = findViewById(R.id.et_browser_text);
        pb_browserLoad = findViewById(R.id.pb_browser_load);
        tvDone = findViewById(R.id.tv_browser_done);
        ivBookMark = findViewById(R.id.iv_browser_bookmark);

    }


    private void checkSelection(String loBrowserSelection) {
        switch (loBrowserSelection) {
            case "Google":
                msBrowserLink = getString(R.string.googlelink);
                break;
            case "Yahoo":
                msBrowserLink = getString(R.string.yahoolink);
                break;
            case "Bing":
                msBrowserLink = getString(R.string.binglink);
                break;
        }
    }

    public void hideKeyBoard(View view, Activity mActivity) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_leftVisited:

                if (position > 0) {
                    position--;
                }
                Log.e("TAG", "onClick: ll_leftVisted: " + mBrowserUrlList.size());
                myWebView.loadUrl(mBrowserUrlList.get(position));
                setBrowserControllers(position, mBrowserUrlList.size());

                break;

            case R.id.iv_rightVisited:

                position++;
                Log.e("TAG", "onClick: ll_rightVisted: " + mBrowserUrlList.size());
                myWebView.loadUrl(mBrowserUrlList.get(position));
                setBrowserControllers(position, mBrowserUrlList.size());

                break;

            case R.id.iv_browser_refresh:
                myWebView.reload();
                break;

            case R.id.tv_browser_done:
                finish();
                break;

            case R.id.iv_browser_bookmark:
                showBookMarkPopup();
                break;
        }
    }

    // To set browser controllers (Forward/Backward arrows)...
    private void setBrowserControllers(int position, int browserListSize) {

        if (position <= 0 && browserListSize <= 1) {

            ivLeftVisited.setColorFilter(ContextCompat.getColor(ShowBrowserActivity.this, R.color.black));
            ivLeftVisited.setEnabled(false);

            ivRightVisited.setColorFilter(ContextCompat.getColor(ShowBrowserActivity.this, R.color.black));
            ivRightVisited.setEnabled(false);

        } else if (position == (browserListSize - 1)) {

            ivLeftVisited.setColorFilter(ContextCompat.getColor(ShowBrowserActivity.this, R.color.white));
            ivLeftVisited.setEnabled(true);

            ivRightVisited.setColorFilter(ContextCompat.getColor(ShowBrowserActivity.this, R.color.black));
            ivRightVisited.setEnabled(false);

        } else if (position < (browserListSize - 1) && position > 0) {

            ivLeftVisited.setColorFilter(ContextCompat.getColor(ShowBrowserActivity.this, R.color.white));
            ivLeftVisited.setEnabled(true);

            ivRightVisited.setColorFilter(ContextCompat.getColor(ShowBrowserActivity.this, R.color.white));
            ivRightVisited.setEnabled(true);

        } else {

            ivLeftVisited.setColorFilter(ContextCompat.getColor(ShowBrowserActivity.this, R.color.black));
            ivLeftVisited.setEnabled(false);

            ivRightVisited.setColorFilter(ContextCompat.getColor(ShowBrowserActivity.this, R.color.white));
            ivRightVisited.setEnabled(true);

        }
    }

    private void showBookMarkPopup() {


        KeyboardHelper.hideKeyboard(ShowBrowserActivity.this);




        final ArrayList<String> list;

        String savedList = SharedPrefs.getString(ShowBrowserActivity.this, SharedPrefs.BOOKMARK_LIST);
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        if (savedList != null && !savedList.trim().isEmpty()) {
            list = new Gson().fromJson(savedList, listType);
        } else {
            list = new ArrayList<>();
        }

        popupWindow = new PopupWindow(ShowBrowserActivity.this);
        View view = LayoutInflater.from(ShowBrowserActivity.this).inflate(R.layout.layout_bookmark_popup2, null, false);
        popupWindow.setContentView(view);
        popupWindow.setWidth((int) getResources().getDimension(R.dimen._224sdp));
        popupWindow.setHeight((int) (DisplayHelper.getDisplayHeight(ShowBrowserActivity.this) * .85));
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(ActivityCompat.getDrawable(ShowBrowserActivity.this, R.drawable.ic_popup_box));

        tvEmptyBookmark = view.findViewById(R.id.popup_tv_empty);
        if (list != null && !list.isEmpty()) {
            tvEmptyBookmark.setVisibility(View.GONE);
        } else {
            tvEmptyBookmark.setVisibility(View.VISIBLE);
        }

        final RecyclerView rvBookmarks = view.findViewById(R.id.popup_rv_bookmarks);
//        rvBookmarks.addItemDecoration(new DividerItemDecoration(ShowBrowserActivity.this, DividerItemDecoration.VERTICAL));
        rvBookmarks.setLayoutManager(new LinearLayoutManager(ShowBrowserActivity.this));

        final BookmarkPopupAdapter popupAdapter = new BookmarkPopupAdapter(ShowBrowserActivity.this, list, bookmarkClickListener);
        rvBookmarks.setAdapter(popupAdapter);

        btnAddBookmark = view.findViewById(R.id.popup_btn_add);

        if (list.contains(etBrowserText.getText().toString().trim())) {
            btnAddBookmark.setEnabled(false);
        } else {
            btnAddBookmark.setEnabled(true);
        }

        btnAddBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(etBrowserText.getText().toString().trim());
//                BookmarkPopupAdapter popupAdapter = new BookmarkPopupAdapter(ShowBrowserActivity.this, list, bookmarkClickListener);
//                rvBookmarks.setAdapter(popupAdapter);
                tvEmptyBookmark.setVisibility(View.GONE);
                popupAdapter.notifyDataSetChanged();
                String newList = new Gson().toJson(list);
                SharedPrefs.save(ShowBrowserActivity.this, SharedPrefs.BOOKMARK_LIST, newList);
                btnAddBookmark.setEnabled(false);
            }
        });

        Button btnCancel = view.findViewById(R.id.popup_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(ivBookMark);

    }

    public void showEmptyBookmark() {
        tvEmptyBookmark.setVisibility(View.VISIBLE);
        btnAddBookmark.setEnabled(true);
    }

    @Override
    public void onBookmarkClicked(String url) {
        popupWindow.dismiss();
        myWebView.loadUrl(url);
    }

}
