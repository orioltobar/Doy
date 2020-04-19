### Generic rules ###
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-optimizationpasses 5
-flattenpackagehierarchy
-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/removal/writeonly,!field/marking/private,!class/merging/*,!code/allocation/variable,!class/unboxing/enum

### Remove all logs ###
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

### Maintain enum constants and fields ###
# Source: https://stackoverflow.com/a/33201546
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

### Kotlin Coroutines ###
# Source: https://github.com/Kotlin/kotlinx.coroutines/issues/983
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepclassmembernames class kotlinx.** { volatile <fields>; }
-keepclassmembernames class kotlin.coroutines.SafeContinuation { volatile <fields>; }
-dontwarn kotlinx.coroutines.flow.**inlined**
-dontwarn kotlinx.coroutines.reactive.**inlined**

### Crashlytics (deobfuscated stack traces) ###
# Source: https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android
-keepattributes Annotation                         ## Keep Crashlytics annotations
-keepattributes SourceFile,LineNumberTable         ## Keep file names/line numbers
-keep public class * extends java.lang.Exception   ## Keep custom exceptions (opt)

### Custom models ###

# Keep Data Source models (Application classes that will be serialized/deserialized over Gson)
# Source: https://stackoverflow.com/a/23826357
-keepclassmembers class com.napptilians.networkdatasource.api.models.** { *; }
-keepclassmembers class com.napptilians.diskdatasource.models.** { *; }

# Keep names of models that can be passed as arguments between fragments with Navigation Components
# Source: https://developer.android.com/guide/navigation/navigation-pass-data#use_keepnames_rules
-keepnames class com.napptilians.domain.models.**.** implements java.io.Serializable

### GSON ###
# Source: https://stackoverflow.com/a/23826357
# Source: https://github.com/google/gson/blob/master/examples/android-proguard-example/proguard.cfg

# Keep GSON Signature and Annotations: @Expose, @SerializedName ...
-keepattributes Signature
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
