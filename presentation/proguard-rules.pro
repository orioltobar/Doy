# Generic rules
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-optimizationpasses 5
-flattenpackagehierarchy
-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/removal/writeonly,!field/marking/private,!class/merging/*,!code/allocation/variable,!class/unboxing/enum

# Remove all logs
# Source: http://stackoverflow.com/a/13327603/2969811
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
    public static int wtf(...);
}

# Maintain enum constants and fields
# Source: https://stackoverflow.com/a/33201546
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#ViewModels
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepclassmembernames class kotlinx.** { volatile <fields>; }
-keepclassmembernames class kotlin.coroutines.SafeContinuation { volatile <fields>; }

-dontwarn kotlinx.coroutines.flow.inlined
-dontwarn kotlinx.coroutines.reactive.inlined