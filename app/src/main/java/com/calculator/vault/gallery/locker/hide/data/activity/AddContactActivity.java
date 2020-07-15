package com.calculator.vault.gallery.locker.hide.data.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.adapter.ContactListAdapter;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.ContactModel;
import com.calculator.vault.gallery.locker.hide.data.textdrawable.ColorGenerator;
import com.calculator.vault.gallery.locker.hide.data.textdrawable.TextDrawable;
import com.google.android.gms.ads.AdView;
import com.mopub.common.MoPub;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AddContactActivity extends AppCompatActivity implements ContactListAdapter.ItemOnClick, View.OnClickListener /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper
.onInterstitialAdListener*/ {

    private static final int REQUEST_NEW = 1002;
    private Context mContext;

    private RecyclerView moRvContactList;
    private GridLayoutManager moGridLayoutManager;
    private ContactListAdapter moContactListAdapter;
    private LinearLayout moLLaddContact;
    private ContactModel contactModel = new ContactModel();
    private ArrayList<ContactModel> moContactModelArrayList = new ArrayList<>();
    private static final String TAG = AddContactActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private EditText ed_search;
    private ImageView id_cross;
    private LinearLayout ll_searchTExt;
    private RelativeLayout ll_nocontactView;
    private Uri uriContact;
    private String contactID;     // contacts unique ID
    private RelativeLayout ll_no_data_found;
    private String msPathToWrite = Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".con" + File.separator + ".make" + File.separator;
    ImageVideoDatabase mdbImageVideoDatabase = new ImageVideoDatabase(this);
    ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private ImageView iv_back;
    public static boolean isaddNewContact = false;
    private String msPathToWriteContact = Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".con" + File.separator + ".make" + File.separator;
    private ImageView iv_add_contact;
    private String isDecoyPass;
    private boolean isDecoy;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private boolean isFirstTime = true;

    private AdView adView;
    private TextDrawable mDrawableBuilder;
    private boolean isAdLoad = false;

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
//    TODO Admob Ads
//    private InterstitialAd interstitial;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        isInterstitialAdLoaded = true;
//    }
//
//    @Override
//    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
//        Log.e(TAG, "onInterstitialFailed: "+ moPubErrorCode);
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(AddContactActivity.this)) {
//            mInterstitial.load();
//        }
//    }
//
//    @Override
//    public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {
//
//    }
//
//    @Override
//    public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {
//
//    }
//
//    @Override
//    public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(AddContactActivity.this)) {
//            mInterstitial.load();
//        }
//        getallContacts();
//    }

//    TODO Admob Ads
//    @Override
//    public void onLoad() {
//        isInterstitialAdLoaded = true;
//    }
//
//    @Override
//    public void onFailed() {
//        isInterstitialAdLoaded = false;
//        if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id1))){
//            interstitial = InterstitialAdHelper.getInstance().load2(AddContactActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(AddContactActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(AddContactActivity.this, this);
//        }
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(AddContactActivity.this, this);
//        getallContacts();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        mContext = AddContactActivity.this;

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//        if (Utils.isNetworkConnected(AddContactActivity.this)) {
//            mInterstitial.load();
        IntAdsHelper.loadInterstitialAds(this, new AdsListener() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onClosed() {
            }
        });
//        }

//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(AddContactActivity.this, this);

//        TODO IronSource Ads
//        IronSource.init(this, "8fb1d745", IronSource.AD_UNIT.INTERSTITIAL);
//        IronSource.shouldTrackNetworkState(this, true);
//        IronSource.setInterstitialListener(this);
//        IronSource.loadInterstitial();


        isDecoyPass = SharedPrefs.getString(AddContactActivity.this, SharedPrefs.DecoyPassword, "false");
        adView = (AdView) findViewById(R.id.adView);
        Share.loadAdsBanner(AddContactActivity.this, adView);


        initView();
        initData();
        initAction();
        initListener();

        hideKeyBoard(ed_search, this);
        // IronSource Banner Ads
        //ISAdsHelper.loadBannerAd(this,(FrameLayout) findViewById(R.id.flBanner));

        moRvContactList.setVisibility(View.VISIBLE);

        if (isDecoyPass.equals("true")) {
            isDecoy = true;
            moContactModelArrayList = decoyDatabase.getallContacts();
        } else {
            isDecoy = false;
            moContactModelArrayList = mdbImageVideoDatabase.getallContacts();
        }


    }

    private void initData() {

        if (isDecoyPass.equals("true")) {
            isDecoy = true;
            moContactModelArrayList = decoyDatabase.getallContacts();
        } else {
            isDecoy = false;
            moContactModelArrayList = mdbImageVideoDatabase.getallContacts();
        }
        Collections.reverse(moContactModelArrayList);

        moContactListAdapter = new ContactListAdapter(AddContactActivity.this, moContactModelArrayList, AddContactActivity.this);
        moRvContactList.setLayoutManager(new LinearLayoutManager(AddContactActivity.this, RecyclerView.VERTICAL, false));
        moRvContactList.addItemDecoration(new DividerItemDecoration(AddContactActivity.this, DividerItemDecoration.VERTICAL));
        moRvContactList.setAdapter(moContactListAdapter);
        //getallContacts();

        if (moContactListAdapter.getItemCount() < 1) {
            ll_nocontactView.setVisibility(View.VISIBLE);
            moRvContactList.setVisibility(View.GONE);
        } else {
            moRvContactList.setVisibility(View.VISIBLE);
            ll_nocontactView.setVisibility(View.GONE);
        }
    }


    private void initAction() {


    }

    private void initListener() {
        moLLaddContact.setOnClickListener(this);
        id_cross.setOnClickListener(this);
        ed_search.setCursorVisible(false);
        ed_search.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_add_contact.setOnClickListener(this);

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (ed_search.getText().toString().isEmpty()) {
                    id_cross.setVisibility(View.GONE);
                } else {
                    id_cross.setVisibility(View.VISIBLE);
                }

                moContactListAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initView() {
        moRvContactList = findViewById(R.id.rv_contactList);
        moLLaddContact = findViewById(R.id.ll_add_contacts);
        ll_searchTExt = findViewById(R.id.ll_searchTExt);
        ed_search = findViewById(R.id.ed_search);
        id_cross = findViewById(R.id.id_cross);
        ll_no_data_found = findViewById(R.id.tv_nodatafound);
        ll_nocontactView = findViewById(R.id.ll_nocontact);
        iv_add_contact = findViewById(R.id.iv_add_contact);
        iv_back = findViewById(R.id.iv_back);
        ed_search.setCursorVisible(false);


        moRvContactList.setLayoutManager(new LinearLayoutManager(AddContactActivity.this, RecyclerView.VERTICAL, false));

    }

    @Override
    protected void onResume() {
        super.onResume();
        MoPub.onResume(this);
    }

    @Override
    public void onClick(int position, ContactModel itemModel) {
        if (isDecoy) {
            decoyDatabase.removeSingleContactData(itemModel.getID());
            moContactModelArrayList = decoyDatabase.getallContacts();
        } else {
            mdbImageVideoDatabase.removeSingleContactData(itemModel.getID());
            moContactModelArrayList = mdbImageVideoDatabase.getallContacts();
        }

        Collections.reverse(moContactModelArrayList);

        moContactListAdapter.onUpdate(moContactModelArrayList, ed_search.getText().toString());
        if (moContactListAdapter.getItemCount() < 1) {
            ll_nocontactView.setVisibility(View.VISIBLE);
            moRvContactList.setVisibility(View.GONE);
            ed_search.setVisibility(View.GONE);
        } else {
            moRvContactList.setVisibility(View.VISIBLE);
            ll_nocontactView.setVisibility(View.GONE);
            ed_search.setVisibility(View.GONE);
        }

        if (Share.isNeedToAdShow(this) && IntAdsHelper.isInterstitialLoaded()) {
            IntAdsHelper.showInterstitialAd();
        }
    }

    @Override
    public void onEmpty(boolean isEmpty) {

        if (isEmpty) {
            ll_nocontactView.setVisibility(View.VISIBLE);
        } else {
            ll_nocontactView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_contacts:
            case R.id.iv_add_contact:
                ed_search.setText("");
                selectContactMode();
                break;
            case R.id.id_cross:
                ed_search.setText("");
                ed_search.setCursorVisible(false);
                moRvContactList.setVisibility(View.VISIBLE);
                ll_no_data_found.setVisibility(View.GONE);
                hideKeyBoard(ed_search, AddContactActivity.this);
                break;
            case R.id.ed_search:
                ed_search.setCursorVisible(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(ed_search, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //IronSource.onPause(this);
        MoPub.onPause(this);

        if (ed_search != null) {
            ed_search.setCursorVisible(false);
            hideKeyBoard(ed_search, AddContactActivity.this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MoPub.onStop(this);
    }

    @Override
    public void onBackPressed() {
        Share.hideKeyboard(this);
        super.onBackPressed();
    }

    public void hideKeyBoard(View view, Activity mActivity) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onClickSelectContact() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAdvanceHelper.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean isExist = false;
        String contactNumber = null;
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {

            contactModel = new ContactModel();
            contactModel.setContactFirstName("Unknown");
            contactModel.setContactLastName("Unknown");
            contactModel.setContactFullName("Unknown");

            Log.e(TAG, "Response: " + data.toString());
            uriContact = data.getData();
            Log.e(TAG, "onActivityResult: uricontact:: " + uriContact);
            Cursor cursorID = getContentResolver().query(uriContact, new String[]{ContactsContract.Contacts._ID}, null, null, null);

            if (cursorID.moveToFirst()) {
                contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
            }
            Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                    new String[]{contactID}, null);
            if (cursorPhone.moveToFirst()) {
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                for (int i = 0; i < moContactModelArrayList.size(); i++) {
                    if (contactNumber != null && moContactModelArrayList.get(i).getContactPhoneNumber() != null) {
                        if (moContactModelArrayList.get(i).getContactPhoneNumber().equalsIgnoreCase(contactNumber)) {
                            isExist = true;
                        }
                    }
                }
            }
            cursorPhone.close();

            if (!isExist) {
                contactModel.setContactPhoneNumber(contactNumber);
                retrieveContactName();
                try {
                    retrieveContactPhoto();
                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult: " + e.getMessage());
                }

//            try {
//                getNameEmailDetails();
//            } catch (Exception e) {
//                Log.e(TAG, "onActivityResult: " + e.getMessage());
//            }

                int color = mColorGenerator.getRandomColor();
                contactModel.setContactColor(color);

                moContactModelArrayList = new ArrayList<>();

                if (isDecoy) {
                    decoyDatabase.addContactData(contactModel);

                    moContactModelArrayList = decoyDatabase.getallContacts();

                } else {
                    mdbImageVideoDatabase.addContactData(contactModel);

                    moContactModelArrayList = mdbImageVideoDatabase.getallContacts();
                }


                Collections.reverse(moContactModelArrayList);
                moContactListAdapter.onUpdate(moContactModelArrayList, ed_search.getText().toString());
                if (moContactListAdapter.getItemCount() < 1) {
                    ll_nocontactView.setVisibility(View.VISIBLE);
                    moRvContactList.setVisibility(View.GONE);
                } else {
                    moRvContactList.setVisibility(View.VISIBLE);
                    ll_nocontactView.setVisibility(View.GONE);
                }

                if (Share.isNeedToAdShow(this) && IntAdsHelper.isInterstitialLoaded()) {
                    IntAdsHelper.showInterstitialAd();
                }


            } else {
                Toast.makeText(this, "Contact is already exist in vault.", Toast.LENGTH_SHORT).show();
            }


        } else if (requestCode == REQUEST_NEW) {


            if (resultCode == RESULT_OK) {
                if (isDecoy) {
                    moContactModelArrayList = decoyDatabase.getallContacts();
                } else {
                    moContactModelArrayList = mdbImageVideoDatabase.getallContacts();
                }
                Collections.reverse(moContactModelArrayList);
                moContactListAdapter.onUpdate(moContactModelArrayList, ed_search.getText().toString());
                if (moContactListAdapter.getItemCount() < 1) {
                    ll_nocontactView.setVisibility(View.VISIBLE);
                    moRvContactList.setVisibility(View.GONE);
                } else {
                    moRvContactList.setVisibility(View.VISIBLE);
                    ll_nocontactView.setVisibility(View.GONE);
                }
            }

            if (Share.isNeedToAdShow(this) && IntAdsHelper.isInterstitialLoaded()) {
                IntAdsHelper.showInterstitialAd();
            }
        }
    }

    private void selectContactMode() {
        final CharSequence[] items = {"Create new Contact", "Add from existing Contacts",
                "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddContactActivity.this);
        builder.setTitle("Add Contact!");
        builder.setItems(items, (dialog, item) -> {

            if (items[item].equals("Create new Contact")) {
                //  if (result)
                Intent intent = new Intent(AddContactActivity.this, CreateNewContactActivity.class);
                startActivityForResult(intent, REQUEST_NEW);
                isaddNewContact = true;
            } else if (items[item].equals("Add from existing Contacts")) {
                onClickSelectContact();
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.e(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " = " + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.e(TAG, "Contact Phone Number: " + contactNumber);
        contactModel.setContactPhoneNumber(contactNumber);

        Cursor cursor = getContactsBirthdays();
        int bDayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
        while (cursor.moveToNext()) {
            String bDay = cursor.getString(bDayColumn);
            Log.e(TAG, "Birthday: " + bDay);
            contactModel.setContactBirthDate(bDay);
        }
    }

    private Cursor getContactsBirthdays() {
        Uri uri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Event.CONTACT_ID, ContactsContract.CommonDataKinds.Event.START_DATE
        };

        String where = ContactsContract.Data.MIMETYPE + "= ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;

        String[] selectionArgs = new String[]{
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        };
        String sortOrder = null;
        return managedQuery(uri, projection, where, selectionArgs, sortOrder);
    }

    private void retrieveContactName() {
        // querying contact data store
        ContentResolver cursor = getContentResolver();
        Cursor cursorID = getContentResolver().query(uriContact, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
        String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, contactID};
        Cursor nameCur = cursor.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

        if (nameCur.moveToFirst()) {
            String firstname = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
            String lastname = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
            String fullname = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
            Log.e(TAG, "getNameEmailDetails: firstname" + firstname + " lastname::: " + lastname + " fullname::: " + fullname);

            if (firstname != null && !firstname.isEmpty()) {
                contactModel.setContactFirstName(firstname);
            } else {
                contactModel.setContactFirstName("Unknown");
            }
            if (lastname != null && !lastname.isEmpty()) {
                contactModel.setContactLastName(lastname);
            } else {
                contactModel.setContactLastName("Unknown");
            }

            if (fullname != null && !fullname.isEmpty()) {
                contactModel.setContactFullName(fullname);
            } else {
                contactModel.setContactFullName("Unknown");
            }
        }

        Cursor cursorBirthday = getContactsBirthdays();
        int bDayColumn = cursorBirthday.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
        if (cursorBirthday.moveToFirst()) {
            String bDay = cursorBirthday.getString(bDayColumn);
            Log.e(TAG, "Birthday: " + bDay);
            contactModel.setContactBirthDate(bDay);
        }
//        while (cursorBirthday.moveToNext()) {
//            String bDay = cursorBirthday.getString(bDayColumn);
//            Log.e(TAG, "Birthday: " + bDay);
//            contactModel.setContactBirthDate(bDay);
//        }
//
//        while (nameCur.moveToNext()) {
//
//        }
        nameCur.close();
    }

    private void retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                File path = new File(msPathToWriteContact);
                if (!path.exists()) {
                    path.mkdirs();
                }
                File f = new File(msPathToWrite, "temp.jpeg");
                f.createNewFile();

//Convert bitmap to byte array
                Bitmap bitmap = photo;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmssSSS").format(new Date());

                File from = new File(f.getParent(), f.getName());

                File to = new File(f.getParent(), "file" + timeStamp + "." + "zxcv");
                from.renameTo(to);
                contactModel.setContactFilePath(to.getAbsolutePath());
                fos.flush();
                fos.close();
                assert inputStream != null;
                inputStream.close();
            } else {
                contactModel.setContactFilePath("");
            }
            // Create a circular icon consisting of  a random background colour and first letter of title
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getNameEmailDetails() {
        ArrayList<String> names = new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
                    String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.e("Name :", name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", email);
                    contactModel.setContactEmail(email);
                    if (email != null) {
                        names.add(name);
                    }
                }
                cur1.close();
            }
        }
        return names;
    }

}