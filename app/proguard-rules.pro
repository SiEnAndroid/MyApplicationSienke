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

-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class nineoldandroids.**{*;}
-keep class org.opencv.**{*;}
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-dontwarn com.google.gson.**
-keep class com.wang.avi.** {*;}
-keep class com.example.administrator.thinker_soft.meter_code.sk.bean.**{*;}
-keep public class * implements java.io.Serializable {*;}

-dontwarn org.kobjects.**
-keep class org.kobjects.** { *;}
-dontwarn org.ksoap2.**
-keep class org.ksoap2.** { *;}
-dontwarn org.kxml2.**
-keep class org.kxml2.** { *;}
-dontwarn org.xmlpull.v1.**
-keep class org.xmlpull.v1.** { *;}

-dontwarn com.dataeye.**


-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class org.xz_sale.entity.**{*;}










