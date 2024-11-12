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

-keep class com.facebook.drawee.** { *; }
-keep class com.facebook.imagepipeline.** { *; }
-keep class com.facebook.common.** { *; }
-keep class com.facebook.cache.** { *; }
-keep class com.facebook.imageutils.** { *; }
-keep class com.facebook.webpsupport.** { *; }
-keep class com.facebook.fresco.** { *; }
-keep class com.facebook.soloader.** { *; }
-keep class com.facebook.jni.** { *; }
-keep class com.facebook.common.logging.** { *; }
-keep class com.facebook.common.references.** { *; }
-keep class com.facebook.common.executors.** { *; }
-keep class com.facebook.common.time.** { *; }
-keep class com.facebook.common.internal.** { *; }
-keep class com.facebook.common.activitylistener.** { *; }
-keep class com.facebook.common.disk.** { *; }
-keep class com.facebook.common.file.** { *; }
-keep class com.facebook.common.statfs.** { *; }
-keep class com.facebook.common.streams.** { *; }
-keep class com.facebook.common.soloader.** { *; }
-keep class com.facebook.common.soloader.** { *; }
-keep class com.facebook.common.soloader.** { *; }
