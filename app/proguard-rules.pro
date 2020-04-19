### Generic rules
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-optimizationpasses 5
-flattenpackagehierarchy
-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/removal/writeonly,!field/marking/private,!class/merging/*,!code/allocation/variable,!class/unboxing/enum

### Remove all logs
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

### Maintain enum constants and fields
# Source: https://stackoverflow.com/a/33201546
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

### Kotlin Coroutines
# Source: https://github.com/Kotlin/kotlinx.coroutines/issues/983
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepclassmembernames class kotlinx.** { volatile <fields>; }
-keepclassmembernames class kotlin.coroutines.SafeContinuation { volatile <fields>; }
-dontwarn kotlinx.coroutines.flow.**inlined**
-dontwarn kotlinx.coroutines.reactive.**inlined**
-dontwarn kotlinx.coroutines.reactive.**
-keep class kotlinx.coroutines.flow.** { *; }

### AndroidX Databinding
# Source: https://github.com/QuickPermissions/QuickPermissions/issues/1
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

### AndroidX Lifecycle
# Source: https://issuetracker.google.com/issues/62113696

# Keep Lifecycle State and Event enums values
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }

# Keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
-keepclassmembers class * {
    @android.arch.lifecycle.OnLifecycleEvent *;
}
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

### Enable Crashlytics deobfuscated stack traces
# Source: https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android
-keepattributes Annotation                         ## Keep Crashlytics annotations
-keepattributes SourceFile,LineNumberTable         ## Keep file names/line numbers
-keep public class * extends java.lang.Exception   ## Keep custom exceptions (opt)

### Custom models

# To keep Data Source models (Application classes that will be serialized/deserialized over Gson)
# Source: https://stackoverflow.com/a/23826357
-keepclassmembers class com.napptilians.networkdatasource.api.models.** { *; }
-keepclassmembers class com.napptilians.diskdatasource.models.** { *; }

# To keep names of models that can be passed as arguments between fragments
# Source: https://developer.android.com/guide/navigation/navigation-pass-data#use_keepnames_rules
-keepnames class com.napptilians.domain.models.**.** implements java.io.Serializable

### GSON
# Source: https://stackoverflow.com/a/23826357

# For using GSON Annotations: @Expose, @SerializedName ...
-keepattributes Signature
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
