# Filament
-keep class com.google.android.filament.** { *; }
-keep class com.google.android.filament.gltfio.** { *; }
# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep class **.data.entity.** { *; }
# Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.* class *
# Gson
-keep class com.google.gson.** { *; }
-keep class com.zahra.space.data.entity.** { *; }
# Compose
-keep class androidx.compose.** { *; }
# Kotlin
-keep class kotlin.** { *; }
-keep class kotlinx.coroutines.** { *; }
