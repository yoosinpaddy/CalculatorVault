package com.calculator.vault.gallery.locker.hide.data.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.calculator.vault.gallery.locker.hide.data.KeyboardHelper;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.image.ImagePicker;
import com.calculator.vault.gallery.locker.hide.data.model.ContactModel;
import com.calculator.vault.gallery.locker.hide.data.textdrawable.ColorGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class CreateNewContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText ed_contact_name, ed_contactFirstname, ed_contactLastname, ed_contactPhonenumber, ed_contactemail;
    private TextView ed_contactBirthday;
    private ContactModel contactListModel;
    private ImageVideoDatabase imageVideoDatabase = new ImageVideoDatabase(this);
    private String msFullname;
    private String msFirstname;
    private String msLastname;
    private String msPhoneNumber;
    private String msEmail;
    private String msBirthday;
    private LinearLayout ll_save_newContact;
    private String msFilePath = "";
    private ColorGenerator colorGenerator = ColorGenerator.DEFAULT;
    private int birth_year;
    private int birth_month;
    private int birth_day;
    private CircleImageView iv_contactPhoto;
    private List<String> listPermissionsNeeded1 = new ArrayList<>();
    private List<String> listPermissionsNeeded = new ArrayList<>();
    private List<String> listPermissionsNeededContact = new ArrayList<>();
    private String image_name;
    private static final int STORAGE_PERMISSION_CODE_CAMERA = 1234;
    private static final int STORAGE_PERMISSION_CODE = 1111;
    private static final int STORAGE_PERMISSION_CODE_Contact = 1111;
    private Uri imageUri;
    private String TAG = this.getClass().getSimpleName();
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private Bitmap contactBitmap;
    private ContactModel contactModel;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String isDecoy;
    private static final long MIN_CLICK_INTERVAL = 1000;
    private long mLastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);

        initView();

        isDecoy = SharedPrefs.getString(CreateNewContactActivity.this, SharedPrefs.DecoyPassword, "false");
        initViewListener();
        initViewAction();

    }

    private void initViewAction() {
        contactModel = new ContactModel();

        msFirstname = ed_contactFirstname.getText().toString().trim();
        msLastname = ed_contactLastname.getText().toString().trim();
        msPhoneNumber = ed_contactPhonenumber.getText().toString().trim();
        msEmail = ed_contactemail.getText().toString().trim();
        msBirthday = ed_contactBirthday.getText().toString().trim();

    }

    private void initViewListener() {
        iv_contactPhoto.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ll_save_newContact.setOnClickListener(this);
        ed_contactBirthday.setOnClickListener(this);
    }

    private void initView() {
        //ed_contact_name = findViewById(R.id.ed_contact_name);
        ed_contactFirstname = findViewById(R.id.ed_contactFirstname);
        ed_contactLastname = findViewById(R.id.ed_contactLastname);
        ed_contactPhonenumber = findViewById(R.id.ed_contactPhonenumber);
        ed_contactemail = findViewById(R.id.ed_contactemail);
        ed_contactBirthday = findViewById(R.id.ed_contactBirthday);
        iv_back = findViewById(R.id.iv_back);
        iv_contactPhoto = findViewById(R.id.iv_contactPhoto);
        ll_save_newContact = findViewById(R.id.ll_save_newContact);
        ed_contactBirthday.setCursorVisible(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_save_newContact:

                msFirstname = ed_contactFirstname.getText().toString().trim();
                msEmail = ed_contactemail.getText().toString().trim();
                msPhoneNumber = ed_contactPhonenumber.getText().toString().trim();

                if (TextUtils.isEmpty(msFirstname)) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Name field cannot be Empty");

                    alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (TextUtils.isEmpty(ed_contactPhonenumber.getText().toString().trim())) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Contact Number field cannot be Empty");

                    alertDialogBuilder.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());

                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (!TextUtils.isEmpty(msEmail)) {
                    if (!isValidEmail(msEmail)) {
                        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
                        alertDialogBuilder.setMessage("Email is not valid.");
                        alertDialogBuilder.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());

                        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        long currentClickTime = SystemClock.uptimeMillis();
                        long elapsedTime = currentClickTime - mLastClickTime;
                        mLastClickTime = currentClickTime;
                        if (elapsedTime <= MIN_CLICK_INTERVAL)
                            return;

                        saveNewContact();
                    }
                } else if (msPhoneNumber.length() < 10 && !TextUtils.isEmpty(msPhoneNumber)) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Mobile Number is not valid.");
                    alertDialogBuilder.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());

                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    long currentClickTime = SystemClock.uptimeMillis();
                    long elapsedTime = currentClickTime - mLastClickTime;
                    mLastClickTime = currentClickTime;
                    if (elapsedTime <= MIN_CLICK_INTERVAL)
                        return;

                    saveNewContact();
                }

                break;
            case R.id.ed_contactBirthday:
                birth_dayPicker();
                break;
            case R.id.iv_contactPhoto:
                selectImage();
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CreateNewContactActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {

            //  boolean result = Utility.checkPermission(CreateNewContactActivity.this);
            if (items[item].equals("Take Photo")) {
                //  if (result)
                cameraIntent();
            } else if (items[item].equals("Choose from Gallery")) {
                galleryIntent();
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean checkAndRequestPermissions() {

        listPermissionsNeeded.clear();
//     listPermissionsNeeded1.clear();

        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }

        return true;
    }


    private boolean checkAndRequestPermissionscamera() {

        listPermissionsNeeded1.clear();
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded1.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded1.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "resultCode :" + resultCode);

        if (resultCode == RESULT_OK) {
//Uri captureUri = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            Uri captureUri = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

            if (captureUri != null)
                Log.e(TAG, "captureUri " + captureUri);

            if (captureUri != null) {
                File file = new File(captureUri.getPath());
                Share.imageUrl = file.getAbsolutePath();
                if (file.exists()) {
                    Share.imageUrl = file.getAbsolutePath();
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra(Share.KEYNAME.SELECTED_PHONE_IMAGE, file.getAbsolutePath());
//intent.putExtra(GlobalData.KEYNAME.SELECTED_PHONE_IMAGE, captureUri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        Log.e("TAG", "onRequestPermissionsResult: deny");

                    } else {
                        Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("Please allow permission for storage")
                                .setPositiveButton("Cancel", (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .setNegativeButton("Ok", (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    }

                } else {
                    // Permission has already been granted
                    if (image_name == "camera") {
                        ImagePicker.pickImage(CreateNewContactActivity.this, "Select your image:");
                    } else if (image_name == "gallery") {
                        Intent i = new Intent(CreateNewContactActivity.this, AlbumActivity.class);
                        startActivity(i);
                        this.finish();
                    }
                }
                break;

            case STORAGE_PERMISSION_CODE_CAMERA:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                        Log.e("TAG", "onRequestPermissionsResult: deny");

                    } else {
                        Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("Please allow permission for camera")
                                .setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss())
                                .setNegativeButton("Ok", (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    }
                } else {
                    // Permission has already been granted
                  /*  if (image_name == "camera") {
                        ImagePicker.pickImage(Splash_MenuActivity.this, "Select your image:");


                    } else if (image_name == "gallery") {
                        Intent i = new Intent(Splash_MenuActivity.this, FaceActivity.class);
                        startActivity(i);
                        this.finish();
                        // overridePendingTransition(R.anim.app_right_in, R.anim.app_left_out); //forward

                    } else if (image_name == "my_photos") {

                        Intent intent = new Intent(Splash_MenuActivity.this, MyPhotosActivity.class);
                        startActivity(intent);
                        this.finish();
                        //overridePendingTransition(R.anim.app_right_in, R.anim.app_left_out); //forward


                    }*/

                    if (image_name == "camera") {
                        ImagePicker.pickImage(CreateNewContactActivity.this, "Select your image:");

                    } else if (image_name == "gallery") {
                        Intent i = new Intent(CreateNewContactActivity.this, AlbumActivity.class);
                        startActivity(i);
                        this.finish();

                    }
                }
                break;
        }
    }

    private void galleryIntent() {
      /*  Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,SelectPicture);*/
        if (checkAndRequestPermissions()) {

            image_name = "gallery";
            SharedPrefs.save(CreateNewContactActivity.this, SharedPrefs.GallerySelected, image_name);
            SharedPrefs.save(this, SharedPrefs.PickFromGallery, "pickimage");
            Intent i = new Intent(CreateNewContactActivity.this, AlbumActivity.class);
            startActivity(i);
        } else {
            image_name = "gallery";
            ActivityCompat.requestPermissions(CreateNewContactActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
        }
    }

    private void cameraIntent() {
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,"data");
        startActivityForResult(intent,5);*/
        if (checkAndRequestPermissionscamera()) {
            image_name = "camera";
            ImagePicker.pickImage(this, "Select your image:");
        } else {
            image_name = "camera";
            ActivityCompat.requestPermissions(this, listPermissionsNeeded1.toArray(new String[listPermissionsNeeded1.size()]), STORAGE_PERMISSION_CODE_CAMERA);
        }
        //ImagePicker.pickImage(activity);

    }

    private void saveNewContact() {
        msFirstname = ed_contactFirstname.getText().toString().trim();
        msLastname = ed_contactLastname.getText().toString().trim();
        msPhoneNumber = ed_contactPhonenumber.getText().toString().trim();
        msEmail = ed_contactemail.getText().toString().trim();
        msBirthday = ed_contactBirthday.getText().toString().trim();

        Log.e("TAG", "saveNewContact: " + msFirstname);
        Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
        long rowContactId = getRawContactId();

        if (!msFirstname.equals("")) {
            contactModel.setContactFirstName(msFirstname);
        }
        if (!msLastname.equals("")) {
            contactModel.setContactLastName(msLastname);
        }
        if (!msPhoneNumber.equals("")) {
            contactModel.setContactPhoneNumber(msPhoneNumber);
        } else {
            Log.e("TAG:: ", "saveNewContact: msPhoneNumber:: " + "Empty");
        }
        if (!msEmail.equals("")) {
            contactModel.setContactEmail(msEmail);
        } else {
            Log.e("TAG:: ", "saveNewContact: msEmail:: " + "Empty");
        }
        if (!msBirthday.equals("")) {
            contactModel.setContactBirthDate(msBirthday);
        } else {
            Log.e("TAG:: ", "saveNewContact: msBirthday:: " + "Empty");
        }
        contactModel.setContactFullName(msFirstname + " " + msLastname);

        contactModel.setContactFilePath(msFilePath);
        int color = colorGenerator.getRandomColor();
        contactModel.setContactColor(color);
        try {
            createImageFileInStorage();

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();

        int rawContactID = ops.size();

        // Adding insert operation to operations list
        // to insert a new raw contact in the table ContactsContract.RawContacts
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // Adding insert operation to operations list
        // to insert display name in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, msFirstname + " " + msLastname)
                .build());


      /*  ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.DATA2)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DATA5, msLastname)
                .build());*/

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, msPhoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());


        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, msBirthday)
                .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                .build());

        if (contactModel.getContactFilePath() != null && !contactModel.getContactFilePath().equals("")) {
            Bitmap bmImage = BitmapFactory.decodeFile(contactModel.getContactFilePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();


            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.DATA15, b)
                    .build());
        }

       /* ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, etHomePhone.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());*/

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, msEmail)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                .build());

        // Adding insert operation to operations list
        // to insert Work Email in the table ContactsContract.Data
       /* ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, etWorkEmail.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());*/

        try {
            // Executing all the insert operations as a single database transaction
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//            Toast.makeText(getBaseContext(), "Contact is successfully added", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }

        if (isDecoy.equals("true")) {
            decoyDatabase.addContactData(contactModel);
        } else {
            imageVideoDatabase.addContactData(contactModel);
        }


        Toast.makeText(this, "Contact Saved Successfully", Toast.LENGTH_SHORT).show();

        KeyboardHelper.hideKeyboard(CreateNewContactActivity.this);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }

    private long getRawContactId() {
        // Inser an empty contact.
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        // Get the newly created contact raw id.
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }

    private void birth_dayPicker() {
        final Calendar c = Calendar.getInstance();
        birth_year = 1990;
        birth_month = c.get(Calendar.MONTH);
        birth_day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String daytext, monthText;
                    int month = monthOfYear + 1;
                    int day = dayOfMonth;
                    if (day > 1 && day < 10) {
                        daytext = "0" + day;
                    } else {
                        daytext = String.valueOf(day);
                    }

                    if (month > 1 && month < 10) {
                        monthText = "0" + month;
                    } else {
                        monthText = String.valueOf(month);
                    }
                    ed_contactBirthday.setText(new StringBuilder().append(daytext).append("/")
                            .append(monthText).append("/").append(year));
                    // et_birthday.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
                    //  ed_contactBirthday.setSelection(ed_contactBirthday.getText().length());
                   /* birthdayDayEdit.setText(String.valueOf(dayOfMonth));
                    birthdayMonthEdit.setText(String.valueOf(monthOfYear + 1).trim());
                    birthdayYearEdit.setText(String.valueOf(year));*/
                   /* birthdayDayEdit.setText(String.valueOf(dayOfMonth));
                    birthdayMonthEdit.setText(String.valueOf(monthOfYear+1).trim());
                    birthdayYearEdit.setText(String.valueOf(year));*/
                }, birth_year, birth_month, birth_day);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.show();
    }

    // Insert newly created contact display name.
    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, ContactModel contactModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);

        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contactModel.getContactFirstName());
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, contactModel.getContactLastName());
        contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, contactModel.getContactEmail());
        contentValues.put(ContactsContract.CommonDataKinds.Event.START_DATE, contactModel.getContactBirthDate());
        contentValues.put(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY);
        getContentResolver().insert(addContactsUri, contentValues);

    }

    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, ContactModel contactModel) {
        // Create a ContentValues object.
        ContentValues contentValues = new ContentValues();

        // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

        // Put phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contactModel.getContactPhoneNumber());

        // Calculate phone type by user selection.
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;

      /*  if ("home".equalsIgnoreCase("HOME")) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        } else if ("mobile".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        } else if ("work".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }*/
        // Put phone type value.
        phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);

        // Insert new contact data into phone contact list.

        try {
            getContentResolver().insert(addContactsUri, contentValues);
        } catch (Exception e) {
            Log.e("TAG:: ", "insertContactPhoneNumber: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Share.croppedImage = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, " cropfile   onResume:  Share.croppedImage " + Share.croppedImage);
        if (Share.croppedImage != null) {
            File f = new File(Share.croppedImage, "profile.png");
            imageUri = Uri.fromFile(f);
            Log.e(TAG, "onResume: nonnull::::" + imageUri);

            Glide.with(this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .override(300, 300)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            contactBitmap = ((BitmapDrawable) resource).getBitmap();
                            return false;
                        }
                    }).into(iv_contactPhoto);
        } else {
            Log.e(TAG, "onResume: " + Share.croppedImage);
            Log.e(TAG, "onResume: null::::" + imageUri);
        }
    }

    private void createImageFileInStorage() throws IOException {
        File path = new File(Share.msPathToWriteContact);
        if (!path.exists())
            path.mkdirs();
        File f = new File(Share.msPathToWriteContact, "temp.jpeg");
        f.createNewFile();

//Convert bitmap to byte array
        String pathName;
        Log.e(TAG, "createImageFileInStorage: path::: " + Share.croppedImage);
        // Bitmap bitmap = BitmapFactory.decodeFile(Share.croppedImage);
        /*BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(Share.croppedImage,bmOptions);*/

        Bitmap bitmap = ((BitmapDrawable) iv_contactPhoto.getDrawable()).getBitmap();


        Log.e(TAG, "createImageFileInStorage: bitmap:: " + bitmap);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmssSSS").format(new Date());

        File from = new File(f.getParent(), f.getName());

        File to = new File(f.getParent(), "file" + timeStamp + "." + "zxcv");
        from.renameTo(to);
        contactModel.setContactFilePath(to.getAbsolutePath());
        Share.croppedImage = null;
        fos.flush();
        fos.close();
    }
}
