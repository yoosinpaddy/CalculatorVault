package com.calculator.vault.gallery.locker.hide.data;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseEventUtils {

    // Subscriptions cLick
    public static String clickSubMonthly = "Click_Subscription_Monthly";
    public static String clickSubMonthlyDiscount = "Click_Subscription_Monthly_Discount";
    public static String clickSubYearly = "Click_Subscription_Yearly";


    // Subscriptions cLick
    public static String subMonthlyDone = "Subscription_Monthly_Purchased";
    public static String subMonthlyDiscountDone = "Subscription_Monthly_Discount_Purchased";
    public static String subYearlyDone = "Subscription_Yearly_Purchased";

    // Emoji Maker activity cLick
    public static String clickEmojiCategory = "Click_Emoji_Category: ";
    public static String clickRandomEmoji = "Click_Random_Emoji";
    public static String clickEmojiSave = "Click_Emoji_Save";

    // Masking activity cLick
    public static String clickMaskId = "Click_Mask_ID: "; // Click_Mask_ID: 001
    public static String clickMaskSave = "Click_Mask_Save";

    // Home activity cLick
    public static String clickHomeGIF = "Click_Home_GIF";
    public static String clickHomeKB = "Click_Home_Keyboard";
    public static String clickHomePacks = "Click_Home_Packs";
    public static String clickHomeSubscription = "Click_Home_Subscription";

    // GIF Fragment click
    public static String clickGIFCategory = "Click_GIF_Category:  "; // Click_GIF_Category:  19/BlackEmojis

    // GIF click
    public static String clickGIF = "Click_GIF:  "; // Click_GIF:  4/113/Naughty


    // Emoji click
    public static String clickEmoji = "Click_Emoji:  "; // Click_GIF:  4/113/Naughty

    // Keyboard theme adapter click
    public static String clickKbThemeOnline = "Click_Keyboard_Theme_Online:  "; // Click_Keyboard_Theme_Offline:  0
    public static String clickKbThemeOffline = "Click_Keyboard_Theme_Offline:  "; // Click_Keyboard_Theme_Online:  19/Luxury Black

    // WhatsApp Sticker Activity click
    public static String clickWAStickerAdd = "Click_Pack_Add_WA";


    public static void AddEvent(Context mContext, String tag) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString(tag, tag);
        mFirebaseAnalytics.logEvent(tag, params);

    }
}
