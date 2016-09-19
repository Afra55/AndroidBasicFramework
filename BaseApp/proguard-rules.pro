# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\AndroidStudioSDK\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:


# 混淆后会生成映射文件，包含类名和混淆后类名的映射关系， 使用 printmapping 可以指定映射的名称
-verbose
-printmapping proguardMapping.txt

# * 不忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers

# * 抛出异常时保留代码行号
-keepattributes SourceFile, LineNumberTable

# keep 所有的 javabean ==========================需要替换包名========================
-keep class com.afra55.apimodule.bean.**
-keep class com.afra55.apimodule.bean.**{*;}

# 最好不要有内嵌类，非要有 用示例 MainActivity$*{*;} 来避免混淆这个类的内嵌类, $ 是用来分割内嵌类的标志

# WebView 相关, 还有要确保 js 调用的原生android 方法不被混淆 <methods>;
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembernames class * extends android.webkit.WebViewClient {
    public void * (android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean * (android.webkit.WebView, java.lang.String);
}
-keepclassmembernames class * extends android.webkit.WebViewClient {
    public void * (android.webkit.WebView, java.lang.String);
}

# 混淆泛型
-keepattributes Signature

-dontwarn javax.annotation.Nullable
-dontwarn com.google.**
-keep class javax.annotation.** { *; }
-dontwarn javax.annotation.**

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

### keep options
#system default, from android example
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 保留 Serializable 序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 不混淆 R 资源下的所有类及方法
-keep class **.R$* {
 *;
}

# 对带有回调函数 onXXEvent， 不混淆该方法
-keepclassmembers class * {
    void *(**On*Event);
}

# 保留自定义控件不被混淆
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# OKHTTP
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

# Fresco
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-dontwarn okio.**
-dontwarn com.android.volley.toolbox.**

# retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# rxjava
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