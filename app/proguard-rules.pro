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

# Preserve Firebase classes and methods
-keep class com.google.firebase.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Preserve the entry points (your app's activities)
-keep class com.example.courseproject.** { *; }

# Preserve Parcelable implementations
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep View methods to prevent issues with XML-based UI
-keepclassmembers class * extends android.view.View {
    public void *(android.content.Context, android.util.AttributeSet);
    public void *(android.content.Context);
}

# Prevent obfuscation of Logcat messages (optional)
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
}
