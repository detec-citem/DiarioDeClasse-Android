-dontwarn android.test.**
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-dontwarn org.junit.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class br.gov.sp.educacao.sed.mobile.fragment.** { *; }
-assumenosideeffects class android.util.Log {
    public static *** e(...);
}