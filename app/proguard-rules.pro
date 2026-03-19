# Keep Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.* class *

# Keep Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Keep Gson
-keep class com.google.gson.** { *; }
-keep class com.zahra.space.data.entity.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }
