# Generic rules
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-optimizationpasses 1
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

# Kotlin Coroutines
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

# AndroidX Databinding
# Source: https://github.com/QuickPermissions/QuickPermissions/issues/1
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

# Keep R file
-keepclassmembers class **.R$* {
    public static <fields>;
}
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Enable Crashlytics deobfuscated stack traces
# Source: https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android
-keepattributes Annotation                         ## Keep Crashlytics annotations
-keepattributes SourceFile,LineNumberTable         ## Keep file names/line numbers
-keep public class * extends java.lang.Exception   ## Keep custom exceptions (opt)

# To keep DataSource "data class" models (needed to work with Firebase Firestore)
-keepclassmembers class com.napptilians.domain.models.category.** { *; }
-keepclassmembers class com.napptilians.domain.models.chat.** { *; }
-keepclassmembers class com.napptilians.domain.models.device.** { *; }
-keepclassmembers class com.napptilians.domain.models.movie.** { *; }
-keepclassmembers class com.napptilians.domain.models.service.** { *; }
-keepclassmembers class com.napptilians.domain.models.user.** { *; }
-keepclassmembers class com.napptilians.networkdatasource.api.models.** { *; }
-keepclassmembers class com.napptilians.diskdatasource.models.** { *; }

### Glide, Glide Okttp Module, Glide Transformations
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

### Retrofit 2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn retrofit2.adapter.rxjava.CompletableHelper$** # https://github.com/square/retrofit/issues/2034
#To use Single instead of Observable in Retrofit interface
-keepnames class rx.Single
#Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**
# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit
# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

### OkHttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

### Android Architecture Components
# Source: https://issuetracker.google.com/issues/62113696
-keep class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}

# ViewModel's empty constructor is considered to be unused by proguard
-keepclassmembers class * extends android.arch.lifecycle.ViewModel {
    <init>(...);
}

# keep Lifecycle State and Event enums values
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
# keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
-keepclassmembers class * {
    @android.arch.lifecycle.OnLifecycleEvent *;
}

### Kotlin
#https://stackoverflow.com/questions/33547643/how-to-use-kotlin-with-proguard
#https://medium.com/@AthorNZ/kotlin-metadata-jackson-and-proguard-f64f51e5ed32
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

### Nothing for Butterknife 8, Realm, RxJava2, RxBinding, RxRelay, Dagger2, OneSignal, Google Play Services, Firebase, Facebook SDK, Room DB,

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-ignorewarnings
-keep class * {
    public private *;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer