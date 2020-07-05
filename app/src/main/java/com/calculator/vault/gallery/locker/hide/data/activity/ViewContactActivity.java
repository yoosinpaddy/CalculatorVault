package com.calculator.vault.gallery.locker.hide.data.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.ContactModel;

import java.io.File;

public class ViewContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_contactPhoto;
    private TextView tv_contact_name, tv_contactFirstname, tv_contactLastname, tv_contactPhonenumber, tv_contactemail, tv_contactBirthday;
    private ContactModel contactListModel;
    private ImageVideoDatabase imageVideoDatabase = new ImageVideoDatabase(this);
    private String msFullname;
    private String msFirstname;
    private String msLastname;
    private String msPhoneNumber;
    private String msEmail;
    private String msBirthday;
    private ImageView iv_back;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String isDecode;
    private String mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        initView();
        isDecode = SharedPrefs.getString(ViewContactActivity.this,SharedPrefs.DecoyPassword,"false");
        initViewListener();
        initViewAction();
    }

    private void initViewAction() {
        contactListModel = new ContactModel();
        int ID = getIntent().getIntExtra("editIntentID", 0);


        if(isDecode.equals("true")){
            contactListModel = decoyDatabase.getSingleContactData(ID);
        }else{
            contactListModel = imageVideoDatabase.getSingleContactData(ID);
        }

        setContactPhoto(contactListModel);

        msFullname = contactListModel.getContactFullName();
        msFirstname = contactListModel.getContactFirstName();
        msLastname = contactListModel.getContactLastName();
        msPhoneNumber = contactListModel.getContactPhoneNumber();
        msEmail = contactListModel.getContactEmail();
        msBirthday = contactListModel.getContactBirthDate();

        if (msFullname==null || msFullname.equals("")) {
            msFullname = getString(R.string.NotAvailable);
        }
        if (msFirstname==null || msFirstname.equals("")) {
            msFirstname = getString(R.string.NotAvailable);
            findViewById(R.id.iv_copy_first_name).setVisibility(View.GONE);
        }
        if (msLastname==null || msLastname.equals("")) {
            msLastname = getString(R.string.NotAvailable);
            findViewById(R.id.iv_copy_last_name).setVisibility(View.GONE);
        }
        if (msPhoneNumber==null || msPhoneNumber.equals("")) {
            msPhoneNumber = getString(R.string.NotAvailable);
            findViewById(R.id.iv_copy_phone_number).setVisibility(View.GONE);
            findViewById(R.id.iv_call).setVisibility(View.GONE);
        }
        if (msEmail==null || msEmail.equals("")) {
            msEmail = getString(R.string.NotAvailable);
            findViewById(R.id.iv_copy_email).setVisibility(View.GONE);
        }
        if (msBirthday==null || msBirthday.equals("")) {
            msBirthday = getString(R.string.NotAvailable);
            findViewById(R.id.iv_copy_birth_date).setVisibility(View.GONE);
        }

        tv_contact_name.setText(msFullname);
        tv_contactFirstname.setText(msFirstname);
        tv_contactLastname.setText(msLastname);
        tv_contactPhonenumber.setText(msPhoneNumber);
        tv_contactemail.setText(msEmail);
        tv_contactBirthday.setText(msBirthday);

        findViewById(R.id.iv_copy_first_name).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("credPass", tv_contactFirstname.getText().toString().trim());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ViewContactActivity.this, "FirstName copied to clipboard.", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.iv_copy_last_name).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("credPass", tv_contactLastname.getText().toString().trim());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ViewContactActivity.this, "LastName copied to clipboard.", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.iv_copy_phone_number).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("credPass", tv_contactPhonenumber.getText().toString().trim());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ViewContactActivity.this, "Contact Number copied to clipboard.", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.iv_copy_email).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("credPass", tv_contactemail.getText().toString().trim());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ViewContactActivity.this, "Email address copied to clipboard.", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.iv_copy_birth_date).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("credPass", tv_contactBirthday.getText().toString().trim());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ViewContactActivity.this, "Birth date copied to clipboard.", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.iv_call).setOnClickListener(v -> {
            if (Utils.isTelephonyEnabled(ViewContactActivity.this)) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tv_contactPhonenumber.getText().toString().trim()));
                startActivity(intent);
            }else {
                Toast.makeText(ViewContactActivity.this, "You device dose not support calling.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setContactPhoto(ContactModel contactListModel) {

        mImage = contactListModel.getContactFilePath();
        if (mImage == null || mImage.equals("")) {
            Log.e("Tag", "setContactPhoto: "+"No Photo detected show default pic" );
            iv_contactPhoto.setImageResource(R.drawable.person);

        } else {
            try {
                Log.e("Tag", "setContactPhoto: "+"Pic Present:: " );
                File file = new File(contactListModel.getContactFilePath());
                Glide.with(ViewContactActivity.this)
                        .load(file)
                        //    .error(R.drawable.ic_action_broken)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)

                        .skipMemoryCache(true)
                        .fitCenter()
                        .centerCrop()
                        .into(iv_contactPhoto);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initViewListener() {
        iv_contactPhoto.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        tv_contact_name = findViewById(R.id.tv_contact_name);
        tv_contactFirstname = findViewById(R.id.tv_contactFirstname);
        tv_contactLastname = findViewById(R.id.tv_contactLastname);
        tv_contactPhonenumber = findViewById(R.id.tv_contactPhonenumber);
        tv_contactemail = findViewById(R.id.tv_contactemail);
        tv_contactBirthday = findViewById(R.id.tv_contactBirthday);
        iv_back = findViewById(R.id.iv_back);
        iv_contactPhoto = findViewById(R.id.iv_contactPhoto);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

        }
    }
}
