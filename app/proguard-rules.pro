# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn javax.annotation.**
-dontwarn okhttp3.**
-dontwarn com.squareup.**
-dontwarn org.apache.**
-dontwarn com.google.ads.**

-keep class com.android.vending.billing.**

#------------ Start GOOGLE DRIVE ------------
-keep class com.google.** { *;}
-keep interface com.google.** { *;}
-dontwarn com.google.**

-dontwarn sun.misc.Unsafe
-keep class * extends com.google.api.client.json.GenericJson {
*;
}
-keep class com.google.api.services.drive.** {
*;
}
#------------ End GOOGLE DRIVE ------------

#------------ FABRIC ------------
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keepattributes SourceFile,LineNumberTable,*Annotation*
-keep class com.crashlytics.android.**
-dontwarn android.webkit.WebView

#------------ Start ORG APACHE ------------
-keep class org.apache.http.** { *; }
-keep class org.apache.commons.codec.** { *; }
-keep class org.apache.commons.logging.** { *; }
-keep class android.net.compatibility.** { *; }
-keep class android.net.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.webkit.**
-dontwarn android.webkit.JavascriptInterface
#------------ End ORG APACHE ------------

# For Error->>>> InnerClass annotations are missing corresponding EnclosingMember annotations
-keep @**annotation** class * {*;}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------End: proguard configuration for Gson  ----------

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

#---------------------Start Glide------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#---------------------End Glide------------------

#---------------------Start Glide------------------
-keep class com.facebook.ads.** { *; }
#---------------------End Glide------------------

# MT - START
-dontobfuscate
-keep class org.mtransit.**  { *; }
# MT - END

# SUPPORT V7 APPCOMPAT - START
-keep class android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
# SUPPORT V7 APPCOMPAT - END

# GOOGLE PLAY SERVICES - START
-keep class * extends java.util.ListResourceBundle {
    protected java.lang.Object[][] getContents();
}
# Keep SafeParcelable value, needed for reflection. This is required to support backwards
# compatibility of some classes.
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
# Keep the names of classes/members we need for client functionality.
-keep @interface com.google.android.gms.common.annotation.KeepName
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}
# Needed for Parcelable/SafeParcelable Creators to not get stripped
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
# Needed when building against pre-Marshmallow SDK.
-dontwarn android.security.NetworkSecurityPolicy
# Keep metadata about included modules.
-keep public class com.google.android.gms.dynamite.descriptors.** {
  public <fields>;
}
# Keep the implementation of the flags api for google-play-services-flags
-keep public class com.google.android.gms.flags.impl.FlagProviderImpl {
  public <fields>; public <methods>;
}
# Needed when building against the Marshmallow SDK (?)
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.internal.**
# Needed when building against pre-Marshmallow SDK.
-dontwarn android.security.NetworkSecurityPolicy
# GOOGLE PLAY SERVICES - END

#------------ Start GOOGLE MOBILE ADS (ADMOB) ------------
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep public class com.google.ads.** {
   public *;
}
-keep class com.google.ads.mediation.admob.AdMobAdapter {
    *;
}
-keep class com.google.ads.mediation.AdUrlAdapter {
    *;
}
#------------ End GOOGLE MOBILE ADS (ADMOB) ------------

#------------ Start FACEBOOK AUDIENCE NETWORK ------------
-keep public class com.facebook.ads.** {
   public *;
}
-keep class com.google.ads.mediation.facebook.FacebookAdapter {
    *;
}
-dontwarn com.facebook.ads.internal.**

# Facebook
-keep class com.facebook.ads.** { *; }
-keeppackagenames com.facebook.*
-dontwarn com.facebook.ads.**
#------------ End FACEBOOK AUDIENCE NETWORK ------------


#------------ Start GOOGLE ANALYTICS ------------
-keep public class com.google.android.gms.analytics.** {
    public *;
}
#------------ End GOOGLE ANALYTICS ------------

#------------ Start GOOGLE PLAY IN-APP BILLING ------------
-keep class com.android.vending.billing.**
#------------ End GOOGLE PLAY IN-APP BILLING ------------

#------------ SAMSUNG/WIKO MESS ------------
-keep class !android.support.v7.internal.view.menu.**,** {*;}
# ------------ End SAMSUNG/WIKO MESS ------------

#------------ Start Iron Source ------------
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface { public *; }
-keepclassmembers class * implements android.os.Parcelable { public static final android.os.Parcelable$Creator *; }
-dontwarn com.moat.**
-keep class com.moat.** { public protected private *; }
-keep class com.ironsource.adapters.** { *; }
-keepnames class com.ironsource.mediationsdk.IronSource { *; }
-dontwarn com.ironsource.**
#------------ End Iron Source ------------

#------------ Start Applovin ------------
-keep class com.applovin.** { *; }
-dontwarn com.applovin.**
#------------ End Applovin ------------

#------------ Start InMobi ------------
-keepattributes SourceFile,LineNumberTable
-keep class com.inmobi.** { *; }
-dontwarn com.inmobi.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}
#skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
#skip Moat classes
-keep class com.moat.** {*;}
-dontwarn com.moat.**
#skip AVID classes
-keep class com.integralads.avid.library.** {*;}
#------------ End  InMobi ------------

