# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizations !class/unboxing/enum
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.alipay.**
-dontwarn android.support.**
-dontwarn android.support.v4.**
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep public class * extends android.support.v7.**
-keep public class * extends android.app.Fragment
-keep class * extends java.lang.annotation.Annotation { *; }#注解包下的所有内容不要混淆

-keep class com.hlb.haolaoban.bean.**{*;}
-keep class com.hlb.haolaoban.http.**{*;}
-keep class com.hlb.haolaoban.module.**{*;}
-keep class com.hlb.haolaoban.otto.**{*;}
-keep class com.hlb.haolaoban.utils.**{*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
# 同上
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class com.hlb.haolaoban.R$*{
    public static final int *;
}

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}

-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}

 -keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
 public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
     **[] $VALUES;
     public *;
 }

# 不混淆的第三方
-keepattributes *Annotation*
-keepattributes Signature

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**

-keepnames class com.fasterxml.jackson.** { *; }
 -dontwarn com.fasterxml.jackson.databind.**
-keep class com.google.gson.**{*;}
-keep class io.agora.rtc.**{*;}
-keep class com.tencent.bugly.**{*;}
-keep class retrofit2.** { *; }
-keep class com.tencent.mm.opensdk.**{*;}
-keep class de.tavendo.autobahn.**{*;}
-keep class com.orhanobut.hawk.**{*;}

-dontwarn com.tencent.mm.opensdk.**
-dontwarn com.orhanobut.hawk.**
-dontwarn retrofit2.**
-dontwarn com.google.**
-dontwarn com.tencent.bugly.**
-dontwarn io.agora.rtc.**
-dontwarn okio.**
-dontwarn de.tavendo.autobahn.**
-dontwarn com.google.**
# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
 -keep class org.codehaus.** { *; }


