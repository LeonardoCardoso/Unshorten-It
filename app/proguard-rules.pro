-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.v4.app.** { *; } 
-keep interface android.support.v4.app.** { *; } 
-keep class com.actionbarsherlock.** { *; } 
-keep interface com.actionbarsherlock.** { *; } 
-keepattributes *Annotation*

-keep public class org.jsoup.** {
	public *;
}

-keepclasseswithmember class * {
    native <methods>;
}

-keepclasseswithmember class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmember class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}