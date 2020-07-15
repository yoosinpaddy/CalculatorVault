package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.TranslatorAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.Customlangmodel;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TranslatorActivity extends AppCompatActivity implements View.OnClickListener {
    private TranslatorActivity activity;
    private ImageView iv_back, iv_language_translator_reversebutton,
            language_translator_iv_copy, language_translator_iv_mic, language_translator_iv_copy1, language_translator_iv_volum, spellhelper_iv_share;
    private EditText language_translator_srctext, language_translator_dsttext;
    private ImageView language_translator_tbutton, language_translator_iv_paste;
    private TextView tv_from_lang, tv_to_lang, tv_show_name1, tv_show_name2;
    int fromlangimg;
    int tolangimg;


    private List<Customlangmodel> languages;
    private TranslatorAdapter mDbHelper;
    private String toLang, fromLang;
    private TextToSpeech tts;
    protected Boolean chackURLForLanguages = false;
    String thetext;
    List<Customlangmodel> list;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView iv_more_app, iv_blast;
    int laungugeresult = -2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_translator);
        activity = TranslatorActivity.this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        initView();
        initViewAction();
        initViewListener();
        initViewAction1();

        if (Share.isNeedToAdShow(this)) {
            setActionBar();
        }

    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        language_translator_srctext = findViewById(R.id.language_translator_srctext);
        language_translator_dsttext = findViewById(R.id.language_translator_dsttext);
        language_translator_iv_paste = findViewById(R.id.language_translator_iv_paste);
        language_translator_tbutton = findViewById(R.id.language_translator_tbutton);
        iv_language_translator_reversebutton = findViewById(R.id.iv_language_translator_reversebutton);
        language_translator_iv_copy = findViewById(R.id.language_translator_iv_copy);
        language_translator_iv_copy1 = findViewById(R.id.language_translator_iv_copy1);
        language_translator_iv_mic = findViewById(R.id.language_translator_iv_mic);
        language_translator_iv_volum = findViewById(R.id.language_translator_iv_volum);
        tv_from_lang = findViewById(R.id.tv_from_lang);
        tv_to_lang = findViewById(R.id.tv_to_lang);
        tv_show_name1 = findViewById(R.id.tv_show_name1);
        tv_show_name2 = findViewById(R.id.tv_show_name2);
        spellhelper_iv_share = findViewById(R.id.spellhelper_iv_share);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);

    }

    private void initViewAction() {
        mDbHelper = new TranslatorAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        addItemsOnSpinner();
    }


    private void addItemsOnSpinner() {
        list = new ArrayList();

        Share.FromPosLang = SharedPrefs.getInt(activity, SharedPrefs.FromPosLang, 17);
        Share.ToPosLang = SharedPrefs.getInt(activity, SharedPrefs.ToPosLang, 29);

//        Share.ToPosLang = SharedPrefs.ToPosLang;
//        Share.FromPosLang = SharedPrefs.FromPosLang;

        int frompos = Share.FromPosLang;
        int topos = Share.ToPosLang;
        int[] pos = mDbHelper.getPositions();
        languages = mDbHelper.getAllLanguages();

        Collections.sort(languages, new Comparator<Customlangmodel>() {
            public int compare(Customlangmodel one, Customlangmodel other) {
                Collator collator = Collator.getInstance(Locale.getDefault());
                collator.setStrength(1);
                Log.e("compare: ", "one.getName() => " + one.getName() + " <= other.getName() => " + other.getName());
                return collator.compare(one.getName(), other.getName());
            }
        });

        int i = 0;
        for (Customlangmodel c : languages) {
            list.add(c);
            i++;
        }

        fromLang = ((Customlangmodel) languages.get(frompos)).getLang();
        toLang = ((Customlangmodel) languages.get(topos)).getLang();
        Customlangmodel toposlanguages = (Customlangmodel) getItem(topos);
        Customlangmodel fromposlanguages = (Customlangmodel) getItem(frompos);

        fromlangimg = languages.get(frompos).getImageId();
        tolangimg = languages.get(topos).getImageId();

        tv_from_lang.setText("" + fromposlanguages.getName());
        tv_to_lang.setText("" + toposlanguages.getName());
        tv_show_name1.setText("" + fromposlanguages.getName());
        tv_show_name2.setText("" + toposlanguages.getName());
//        tv_from_lang.setCompoundDrawablesWithIntrinsicBounds(fromlangimg, 0, 0, 0);
//        tv_to_lang.setCompoundDrawablesWithIntrinsicBounds(tolangimg, 0, 0, 0);

        tv_from_lang.setCompoundDrawablesWithIntrinsicBounds(fromlangimg, 0, 0, 0);
        tv_to_lang.setCompoundDrawablesWithIntrinsicBounds(tolangimg, 0, 0, 0);

        language_translator_srctext.setHint(fromposlanguages.getName());
        language_translator_dsttext.setHint(toposlanguages.getName());
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    private void initViewListener() {
        iv_back.setOnClickListener(this);

        language_translator_srctext.setOnClickListener(this);
        language_translator_dsttext.setOnClickListener(this);
        language_translator_tbutton.setOnClickListener(this);
        iv_language_translator_reversebutton.setOnClickListener(this);
        language_translator_iv_copy.setOnClickListener(this);
        language_translator_iv_copy1.setOnClickListener(this);
        language_translator_iv_mic.setOnClickListener(this);
        language_translator_iv_volum.setOnClickListener(this);
        tv_from_lang.setOnClickListener(this);
        tv_to_lang.setOnClickListener(this);
        tv_show_name1.setOnClickListener(this);
        tv_show_name2.setOnClickListener(this);
        language_translator_iv_paste.setOnClickListener(this);

        spellhelper_iv_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.language_translator_iv_paste:
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                ClipData abc = myClipboard.getPrimaryClip();

                if (abc != null) {
                    ClipData.Item item = abc.getItemAt(0);

                    String text = item.getText().toString();

                    language_translator_srctext.setText(language_translator_srctext.getText().toString() + text);

                    Toast.makeText(activity, "Text Pasted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "No data availbale for paste", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_from_lang:
                Intent intent2 = new Intent(activity, TranslatorSpinerLanguageActivity.class);
                intent2.putExtra("TextView", "Tv1");
                startActivityForResult(intent2, 101);
                hideKeyboard();
                break;

            case R.id.tv_to_lang:
                Intent intent3 = new Intent(activity, TranslatorSpinerLanguageActivity.class);
                intent3.putExtra("TextView", "Tv2");
                startActivityForResult(intent3, 102);
                hideKeyboard();
                break;

            case R.id.language_translator_srctext:
                break;

            case R.id.language_translator_dsttext:
                break;

            case R.id.language_translator_tbutton:
                if (language_translator_srctext.getText().toString().length() != 0) {
                    try {
                        doTranslate();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    hideKeyboard();
                    return;
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.emptytext), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_language_translator_reversebutton:

                int position = Share.FromPosLang;
                Share.FromPosLang = Share.ToPosLang;
                Share.ToPosLang = position;

                SharedPrefs.save(activity, SharedPrefs.FromPosLang, Share.FromPosLang);
                SharedPrefs.save(activity, SharedPrefs.ToPosLang, Share.ToPosLang);

                fromLang = ((Customlangmodel) languages.get(Share.FromPosLang)).getLang();
                Customlangmodel fromposlanguages = (Customlangmodel) getItem(Share.FromPosLang);
                fromlangimg = languages.get(Share.FromPosLang).getImageId();
                tv_from_lang.setCompoundDrawablesWithIntrinsicBounds(fromlangimg, 0, 0, 0);
                tv_from_lang.setText("" + fromposlanguages.getName());
                language_translator_srctext.setHint(fromposlanguages.getName());
                tv_show_name1.setText("" + fromposlanguages.getName());

                toLang = ((Customlangmodel) languages.get(Share.ToPosLang)).getLang();
                tolangimg = languages.get(Share.ToPosLang).getImageId();
                Customlangmodel toposlanguages = (Customlangmodel) getItem(Share.ToPosLang);
                tv_to_lang.setText("" + toposlanguages.getName());
                tv_to_lang.setCompoundDrawablesWithIntrinsicBounds(tolangimg, 0, 0, 0);
                language_translator_dsttext.setHint(toposlanguages.getName());
                tv_show_name2.setText("" + toposlanguages.getName());
                language_translator_srctext.setText("");
                language_translator_dsttext.setText("");
                language_translator_srctext.setHint("");
                language_translator_dsttext.setHint("");

                hideKeyboard();
                break;

            case R.id.language_translator_iv_copy:
                setClipboard1();
                break;

            case R.id.language_translator_iv_copy1:
                setClipboard();
                break;

            case R.id.language_translator_iv_mic:
                Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                intent.putExtra("android.speech.extra.LANGUAGE_MODEL", fromLang);
                intent.putExtra("android.speech.extra.LANGUAGE", fromLang);
                intent.putExtra("android.speech.extra.LANGUAGE_PREFERENCE", fromLang);
                try {
                    startActivityForResult(intent, 1);
                    language_translator_srctext.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(activity, getResources().getString(R.string.sttfail), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.language_translator_iv_volum:

                Log.e("valclcick", "onClick: show click-->");
                if (!language_translator_dsttext.getText().toString().isEmpty()) {
                    language_translator_dsttext.setVisibility(View.VISIBLE);
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        onMicClicked();
                    } catch (Exception e) {

                    }
                } else {
                    Toast.makeText(activity, "Please Enter Some Word", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.spellhelper_iv_share:
                if (language_translator_dsttext.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Nothing to share", Toast.LENGTH_SHORT).show();
                } else {
                    ShareText();
                }
                break;
        }
    }

    private void initViewAction1() {

        tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.UK);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                        Toast.makeText(activity, "This language is not supported..", Toast.LENGTH_SHORT).show();
                    } else {
                        if (status == 0) {
                            Log.e("TTS", "This Language is supported");
                            tts.setLanguage(new Locale(toLang, ""));
                            tts.speak(language_translator_dsttext.getText().toString(), 0, null);
                        }
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
    }


    public void onMicClicked() {
      /*  if (language_translator_dsttext.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Please Enter Some Word", Toast.LENGTH_SHORT).show();
//            txttospeech.speak("Please Enter Some Word", 0, null);
        } else {

        }*/

       tts = null;
        initViewAction1();
       // laungugeresult =  tts.setLanguage(Locale.getDefault());
        Log.e("result", "onMicClicked: suuuuuuuuuuuuu--->");

       /* if (tts != null) {

            if (laungugeresult == TextToSpeech.LANG_MISSING_DATA || laungugeresult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "TTS language is not supported", Toast.LENGTH_SHORT).show();
            } else {
                // Do something here
                tts.speak(language_translator_dsttext.getText().toString(), 0, null);
            }
        }*/


    }


    private void ShareText() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = language_translator_dsttext.getText().toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                /*if (resultCode == 1) {
                    if (!language_translator_dsttext.getText().toString().isEmpty()) {
                        language_translator_dsttext.setVisibility(View.VISIBLE);
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            onMicClicked();
                        } catch (Exception e) {

                        }
                    } else {
                        Toast.makeText(activity, "Please Enter Some Word", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }*/

                return;
            case 1:
                if (resultCode == -1 && data != null) {
                    language_translator_srctext.setText("" + (CharSequence) data.getStringArrayListExtra("android.speech.extra.RESULTS").get(0));
                    language_translator_srctext.setSelection(language_translator_srctext.getText().length());
                    try {
                        doTranslate();
                        return;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                return;

            case 101:
                fromLang = ((Customlangmodel) languages.get(Share.FromPosLang)).getLang();
                Customlangmodel fromposlanguages = (Customlangmodel) getItem(Share.FromPosLang);
                fromlangimg = languages.get(Share.FromPosLang).getImageId();
                tv_from_lang.setCompoundDrawablesWithIntrinsicBounds(fromlangimg, 0, 0, 0);

                tv_from_lang.setText("" + fromposlanguages.getName());
                tv_show_name1.setText("" + fromposlanguages.getName());
                language_translator_srctext.setHint(fromposlanguages.getName());
                Log.e("onActivityResult: ", "Share.FromPosLang => " + Share.FromPosLang);
                return;

            case 102:
                toLang = ((Customlangmodel) languages.get(Share.ToPosLang)).getLang();
                tolangimg = languages.get(Share.ToPosLang).getImageId();
                Customlangmodel toposlanguages = (Customlangmodel) getItem(Share.ToPosLang);
                tv_to_lang.setText("" + toposlanguages.getName());
                tv_show_name2.setText("" + toposlanguages.getName());
                tv_to_lang.setCompoundDrawablesWithIntrinsicBounds(tolangimg, 0, 0, 0);
                language_translator_dsttext.setHint(toposlanguages.getName());
                Log.e("onActivityResult: ", "Share.FromPosLang==> " + Share.ToPosLang);
                return;
            default:
                return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void doTranslate() throws UnsupportedEncodingException {
        thetext = URLEncoder.encode(language_translator_srctext.getText().toString(), "UTF-8");
        new ReadJSONFeedTask().execute(new String[]{"https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + fromLang + "&tl=" + toLang + "&dt=t&ie=UTF-8&oe=UTF-8&q=" + thetext});
    }

   /* @Override
    public void onInit(int status) {


        Log.e("TTS", "onInit: dfssfgsg" );
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.UK);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");

                Toast.makeText(activity, "This language is not supported..", Toast.LENGTH_SHORT).show();
            } else {
                if (status == 0) {
                    Log.e("TTS", "This Language is supported");
                    tts.setLanguage(new Locale(toLang, ""));
                    tts.speak(language_translator_dsttext.getText().toString(), 0, null);
                }
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }*/

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        private ReadJSONFeedTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog = new ProgressDialog(activity);
                dialog.setMessage(getResources().getString(R.string.loading));
                dialog.setCancelable(false);

                if (!activity.isFinishing())
                    dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
//            ShowProgressDialog(activity, getResources().getString(R.string.loading));
        }

        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            if (result.equals("[\"ERROR\"]")) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle(getResources().getString(R.string.no_internet));
                alertDialogBuilder.setMessage(getResources().getString(R.string.check_ur_internet));
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                arg0.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                return;
            }

            if (chackURLForLanguages) {
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    String str = "";

                    for (int i = 0; i < jsonarray.getJSONArray(0).length(); i++) {
                        str = str + jsonarray.getJSONArray(0).getJSONArray(i).getString(0);
                    }

                    language_translator_dsttext.setText("" + str);
                } catch (Exception e) {
                    Log.e("JSONFeedTask", e.getLocalizedMessage());
                }
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    int status1 = jsonObject.getInt("status");
                    String result1 = jsonObject.getString("result");

                    if (status1 == 1) {
                        language_translator_dsttext.setText("" + result1);
                    } else {
                        Toast.makeText(activity, "can't translate your words", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpResponse response = new DefaultHttpClient().execute(new HttpGet(URL));

            if (response.getStatusLine().getStatusCode() == 200) {
                chackURLForLanguages = true;
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                while (true) {
                    String line = reader.readLine();

                    if (line == null) {
                        break;
                    }

                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                //todo call another api
                chackURLForLanguages = false;
                String URL1 = "http://learntodraw.in/translateAPI/public/translate?from=" + fromLang + "&to=" + toLang + "&q=" + thetext;
                Log.e("val", "doTranslate: " + URL1);
                //HttpResponse response1 = new DefaultHttpClient().execute(new HttpGet(URL1));
                HttpResponse response1 = new DefaultHttpClient().execute(new HttpGet(URL1));
                InputStream inputStream1 = response1.getEntity().getContent();
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1));

                while (true) {
                    String line1 = reader1.readLine();

                    if (line1 == null) {
                        break;
                    }
                    stringBuilder.append(line1);
                }
                inputStream1.close();
                Log.e("JSON", "Failed to download file");
            }
        } catch (Exception e) {
            Log.e("readJSONFeed", e.getLocalizedMessage());
            stringBuilder.append("[\"ERROR\"]");
        }
        return stringBuilder.toString();
    }

    @SuppressLint("WrongConstant")
    private void setClipboard() {
        if (language_translator_dsttext.getText().toString().length() != 0) {
            if (Build.VERSION.SDK_INT < 11) {
                ((ClipboardManager) getSystemService("clipboard")).setText("" + language_translator_dsttext.getText().toString());
            } else {
                ((android.content.ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copy", language_translator_dsttext.getText().toString()));
            }

            Toast.makeText(activity, getResources().getString(R.string.copy), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(activity, getResources().getString(R.string.copyfail), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("WrongConstant")
    private void setClipboard1() {
        if (language_translator_srctext.getText().toString().length() != 0) {
            if (Build.VERSION.SDK_INT < 11) {
                ((ClipboardManager) getSystemService("clipboard")).setText("" + language_translator_srctext.getText().toString());
            } else {
                ((android.content.ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copy", language_translator_srctext.getText().toString()));
            }

            Toast.makeText(activity, getResources().getString(R.string.copy), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(activity, getResources().getString(R.string.copyfail), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("WrongConstant")
    private void hideKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(language_translator_srctext.getWindowToken(), 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ttsStop();
    }

    @Override
    protected void onDestroy() {
        ttsStop();
        super.onDestroy();
    }

    public void ttsStop() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    private void setActionBar() {
        try {
            iv_more_app.setVisibility(View.GONE);
            iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
            ((AnimationDrawable) iv_more_app.getBackground()).start();
            loadInterstialAd();

            iv_more_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_more_app.setVisibility(View.GONE);
                    iv_blast.setVisibility(View.VISIBLE);
                    ((AnimationDrawable) iv_blast.getBackground()).start();

                    if (MainApplication.getInstance()!= null && MainApplication.getInstance().requestNewInterstitial()) {
                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                iv_blast.setVisibility(View.GONE);
                                iv_more_app.setVisibility(View.GONE);
                                loadInterstialAd();
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                                iv_blast.setVisibility(View.GONE);
                                iv_more_app.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                iv_blast.setVisibility(View.GONE);
                                iv_more_app.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(TranslatorActivity.this, "Ad not loaded", Toast.LENGTH_SHORT).show();
                        iv_blast.setVisibility(View.GONE);
                        iv_more_app.setVisibility(View.GONE);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInterstialAd() {
        try {
            if (Share.isNeedToAdShow(this)) {
                if (MainApplication.getInstance().mInterstitialAd.isLoaded()) {
                    Log.e("if", "if");
//                    iv_more_app.setVisibility(View.VISIBLE);
                } else {
                    MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                    MainApplication.getInstance().mInterstitialAd = null;
                    MainApplication.getInstance().ins_adRequest = null;
                    MainApplication.getInstance().LoadAds();
                    MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            Log.e("load", "load");
//                            iv_more_app.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            Log.e("fail", "fail");
                            iv_more_app.setVisibility(View.GONE);
                            //ApplicationClass.getInstance().LoadAds();
                            loadInterstialAd();
                        }
                    });
                }
            }

        } catch (Exception e) {
//            iv_more_app.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }
}
