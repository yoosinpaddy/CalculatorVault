package com.calculator.vault.gallery.locker.hide.data.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.UserModel;

public class SecurityActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity mActivity;
    private ImageView mIvBack;
    private Spinner mSpinnerQuestions;
    private EditText mEtAnswer;
    private Button mBtnShow;
    private String mFromWhere = "";
    private ImageVideoDatabase mDBImageVideo;
    private UserModel mUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        mActivity = SecurityActivity.this;

        Intent intent = getIntent();
        mFromWhere = intent.getStringExtra("fromWhere");

        initViews();
        initViewActions();
    }

    private void initViewActions() {

        mBtnShow.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        if (mFromWhere.equalsIgnoreCase("init")) {
            mSpinnerQuestions.setSelection(0, true);
            mBtnShow.setText(R.string.set_answer);
        } else {
            mUserModel = mDBImageVideo.getSingleUserData(1);
            if (mUserModel != null) {
                mSpinnerQuestions.setSelection(mUserModel.getQuestion(), true);
            }
            mBtnShow.setText(R.string.show_passcode);
        }
    }

    private void initViews() {
        mDBImageVideo = new ImageVideoDatabase(mActivity);
        mIvBack = findViewById(R.id.iv_security_back);
        mSpinnerQuestions = findViewById(R.id.spinner_security_questions);
        mEtAnswer = findViewById(R.id.et_security_answer);
        mBtnShow = findViewById(R.id.btn_security_show);

        String[] listQuestions = getResources().getStringArray(R.array.forgot_passcode_questions);
        ArrayAdapter<String> spinnerItems = new ArrayAdapter<>(this
                , R.layout.j_spinner_item, R.id.tvSpinnerItem, listQuestions);
        spinnerItems.setDropDownViewResource(R.layout.j_spinner_item);
        mSpinnerQuestions.setAdapter(spinnerItems);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_security_back:
                onBackPressed();
                break;

            case R.id.btn_security_show:

                if (mFromWhere.equalsIgnoreCase("init")) {
                    if (mEtAnswer.getText().toString().trim().length() != 0) {
                        setQuestionsAnswer(mSpinnerQuestions.getSelectedItemPosition(), mEtAnswer.getText().toString().trim());
                    } else {
                        Toast.makeText(this, "Answer is not valid.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    showPasscode(mEtAnswer.getText().toString().trim());
                }
                break;
        }
    }

    private void setQuestionsAnswer(int question, String answer) {

        UserModel lUserModel = new UserModel();
        lUserModel.setID(1);
        lUserModel.setQuestion(question);
        lUserModel.setAnswer(answer);

        int dbUpdate = mDBImageVideo.updateUserQuestionAnswer(lUserModel);

        if (dbUpdate <= 0) {
            Log.e("SecurityActivity", "setQuestionsAnswer: Table not updated!!! " + dbUpdate);
            Toast.makeText(mActivity, "Something went wrong setting answer please try again.", Toast.LENGTH_LONG).show();
        } else {
            Log.e("SecurityActivity", "setQuestionsAnswer: QUESTION_ANSWER_ADDED " + dbUpdate);
            Intent iSelection = new Intent(mActivity, SelectionActivity.class);
            startActivity(iSelection);
            mActivity.finish();
        }
    }

    private void showPasscode(String answer) {
        if (TextUtils.isEmpty(answer)) {
            Toast.makeText(this, "Answer can not be empty.", Toast.LENGTH_LONG).show();
        } else if (!isAnswerValid(answer)) {
            Toast.makeText(this, "Answer did not match to your question.", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Answer matched.", Toast.LENGTH_LONG).show();
            displayPasscodeDialog();
        }
    }

    private void displayPasscodeDialog() {
        //Toast.makeText(mActivity, "Your passcode is: " + mUserModel.getPassword(), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setTitle("Recover Passcode");
        alertDialog.setMessage("Your passcode is : " + mUserModel.getPassword() + "%");
        alertDialog.create();
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mEtAnswer.getText().clear();
            }
        });
        alertDialog.show();
    }

    private boolean isAnswerValid(String answer) {
        try {
            if (mUserModel.getQuestion() == mSpinnerQuestions.getSelectedItemPosition())
                return mUserModel.getAnswer().equalsIgnoreCase(answer);
            else
                return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mActivity.finish();
    }
}
