package com.calculator.vault.gallery.locker.hide.data.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.library_feedback.FeedbackUtils;
import com.hsalf.smilerating.SmileRating;

public class RatingDialog {

    public static Dialog SmileyExitDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.new_exit_dialog); //get layout from ExitDialog folder

        SmileRating smileRating = dialog.findViewById(R.id.smile_rating);
        Button btn_yes = dialog.findViewById(R.id.btn_yes_exit);
        Button btn_no = dialog.findViewById(R.id.btn_no_exit);

        btn_no.setOnClickListener(v -> {
            Share.isRateDisplayed = true;
            dialog.dismiss();
        });

        btn_yes.setOnClickListener(v -> {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        smileRating.setOnSmileySelectionListener((smiley, reselected) -> {
            if (smiley == SmileRating.BAD || smiley == SmileRating.OKAY || smiley == SmileRating.TERRIBLE) {
                Toast.makeText(activity, "Thanks for review", Toast.LENGTH_SHORT).show();
            } else if (smiley == SmileRating.GOOD || smiley == SmileRating.GREAT) {
                rate_app(activity);
            }
            SharedPrefs.save(activity, Share.IS_RATED, true);
            dialog.dismiss();
        });
        return dialog;
    }

    public static void SmileyRatingDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.new_rating_dialog); //get layout from ExitDialog folder

        SmileRating smileRating = dialog.findViewById(R.id.smile_rating);
        Button btn_yes = dialog.findViewById(R.id.btn_yes_exit);
        Button btn_no = dialog.findViewById(R.id.btn_no_exit);

        btn_no.setOnClickListener(v -> dialog.dismiss());

        btn_yes.setOnClickListener(v -> {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        smileRating.setOnSmileySelectionListener((smiley, reselected) -> {
            if (smiley == SmileRating.BAD || smiley == SmileRating.OKAY || smiley == SmileRating.TERRIBLE) {
                FeedbackUtils.FeedbackDialog(activity);
            } else if (smiley == SmileRating.GOOD || smiley == SmileRating.GREAT) {
                rate_app(activity);
            }
            SharedPrefs.save(activity, Share.IS_RATED, true);
            dialog.dismiss();
        });
        dialog.show();
    }

    private static void rate_app(Activity foActivity) {
        try {
            foActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + foActivity.getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            foActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + foActivity.getPackageName())));
        }
    }
}
