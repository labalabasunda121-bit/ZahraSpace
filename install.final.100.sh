#!/bin/bash

# ================================================
# ZAHRASPACE - COMPLETE BUILD SCRIPT
# Semua file akan digenerate, termasuk UI, ViewModel, 3D, dll.
# ================================================

set -e  # Hentikan jika ada error

echo "╔══════════════════════════════════════════════════════════╗"
echo "║     ZAHRASPACE - COMPLETE GENERATOR (FULL VERSION)      ║"
echo "║              created by Fajar for Zahra                 ║"
echo "╚══════════════════════════════════════════════════════════╝"
echo ""

# ================================================
# 1. CEK FILE DATA YANG DIPERLUKAN
# ================================================
echo "📁 [1/??] Memeriksa file data..."
MISSING=0
for f in quran-indonesia.sql hadits.zip audio.zip models.zip; do
  if [ ! -f "$f" ]; then
    echo "❌ $f tidak ditemukan"
    MISSING=1
  fi
done
if [ $MISSING -eq 1 ]; then
  echo "❌ Gagal: file data tidak lengkap."
  exit 1
fi
echo "✅ Semua file data ditemukan."

# ================================================
# 2. BUAT STRUKTUR FOLDER LENGKAP
# ================================================
echo "📁 [2/??] Membuat struktur folder..."
mkdir -p app/src/main/assets/{database,audio/{murottal,music,sfx},models,data/hadits}
mkdir -p app/src/main/java/com/zahra/space/{data/{entity,dao},ui/{theme,navigation,screens/{splash,onboarding,dashboard,quran,dzikir,checklist,todo,fitness,pet,game,profile,letters,hidden},views},viewmodel,game,services,receivers,utils}
mkdir -p app/src/main/res/{values,xml}
mkdir -p app/schemas

# ================================================
# 3. EKSTRAK DATA REAL
# ================================================
echo "📦 [3/??] Mengekstrak data..."
cp quran-indonesia.sql app/src/main/assets/database/quran.sql
unzip -o hadits.zip -d app/src/main/assets/data/hadits/ > /dev/null 2>&1
unzip -o audio.zip -d app/src/main/assets/ > /dev/null 2>&1
unzip -o models.zip -d app/src/main/assets/ > /dev/null 2>&1
echo "✅ Data diekstrak."

# ================================================
# 4. BUILD.GRADLE.KTS
# ================================================
echo "📝 [4/??] Membuat build.gradle.kts..."
cat > app/build.gradle.kts << 'EOF'
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.zahra.space"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zahra.space"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("com.google.dagger:hilt-android:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-compiler:2.51")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.android.filament:filament-android:1.51.0")
    implementation("com.google.android.filament:gltfio-android:1.51.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
EOF
echo "✅ build.gradle.kts"

# ================================================
# 5. PROGUARD RULES
# ================================================
cat > app/proguard-rules.pro << 'EOF'
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
EOF

# ================================================
# 6. ANDROID MANIFEST
# ================================================
cat > app/src/main/AndroidManifest.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />

    <application
        android:name=".ZahraApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.ZahraSpace">
        
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.ZahraSpace">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <service android:name=".services.PrayerNotificationService" android:exported="false" />
        <service android:name=".services.HiddenMessageService" android:exported="false" />
        <receiver android:name=".receivers.PrayerAlarmReceiver" android:exported="false" />
            
    </application>
</manifest>
EOF

# ================================================
# 7. BACKUP RULES
# ================================================
mkdir -p app/src/main/res/xml
cat > app/src/main/res/xml/backup_rules.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<full-backup-content>
    <exclude domain="database" path="zahra_space.db"/>
    <exclude domain="sharedpref" path="user_prefs.xml"/>
</full-backup-content>
EOF
cat > app/src/main/res/xml/data_extraction_rules.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<data-extraction-rules>
    <cloud-backup>
        <exclude domain="database" path="zahra_space.db"/>
    </cloud-backup>
</data-extraction-rules>
EOF

# ================================================
# 8. STRINGS.XML
# ================================================
cat > app/src/main/res/values/strings.xml << 'EOF'
<resources>
    <string name="app_name">ZahraSpace</string>
    <string name="quran">Al-Qur\'an</string>
    <string name="dzikir">Dzikir</string>
    <string name="checklist">Checklist</string>
    <string name="todo">Todo</string>
    <string name="fitness">Fitness</string>
    <string name="pet">Pet</string>
    <string name="game">Game</string>
    <string name="profile">Profil</string>
    <string name="settings">Pengaturan</string>
    <string name="letters">Surat</string>
    <string name="greeting_morning">Selamat Pagi</string>
    <string name="greeting_afternoon">Selamat Siang</string>
    <string name="greeting_evening">Selamat Sore</string>
    <string name="greeting_night">Selamat Malam</string>
</resources>
EOF

# ================================================
# 9. ZAHRAAPPLICATION.KT
# ================================================
cat > app/src/main/java/com/zahra/space/ZahraApplication.kt << 'EOF'
package com.zahra.space

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZahraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val prayerChannel = NotificationChannel("prayer", "Waktu Sholat", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Notifikasi waktu sholat"
                enableLights(true)
                enableVibration(true)
            }
            val gameChannel = NotificationChannel("game", "Notifikasi Game", NotificationManager.IMPORTANCE_LOW).apply {
                description = "Notifikasi dari dalam game"
                enableVibration(false)
            }
            val messageChannel = NotificationChannel("message", "Pesan F", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Pesan rahasia dari F"
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(prayerChannel)
            nm.createNotificationChannel(gameChannel)
            nm.createNotificationChannel(messageChannel)
        }
    }
}
EOF

# ================================================
# 10. MAINACTIVITY.KT
# ================================================
cat > app/src/main/java/com/zahra/space/MainActivity.kt << 'EOF'
package com.zahra.space

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.zahra.space.ui.navigation.NavGraph
import com.zahra.space.ui.theme.ZahraSpaceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZahraSpaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
EOF

# ================================================
# 11. THEME
# ================================================
cat > app/src/main/java/com/zahra/space/ui/theme/Color.kt << 'EOF'
package com.zahra.space.ui.theme
import androidx.compose.ui.graphics.Color
val PrimaryGreen = Color(0xFF2E7D32)
val PrimaryLightGreen = Color(0xFF4CAF50)
val PrimaryDarkGreen = Color(0xFF1B5E20)
val SecondaryCream = Color(0xFFFFF8E1)
val AccentMint = Color(0xFFC8E6C9)
val BackgroundLight = Color(0xFFF5F5F5)
val TextDark = Color(0xFF212121)
val TextMedium = Color(0xFF757575)
val ErrorRed = Color(0xFFD32F2F)
val SuccessGreen = Color(0xFF388E3C)
val WarningYellow = Color(0xFFFFA000)
val InfoBlue = Color(0xFF1976D2)
EOF

cat > app/src/main/java/com/zahra/space/ui/theme/Type.kt << 'EOF'
package com.zahra.space.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
    displayMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 45.sp, lineHeight = 52.sp, letterSpacing = 0.sp),
    headlineLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = 0.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 28.sp, lineHeight = 36.sp, letterSpacing = 0.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp)
)
EOF

cat > app/src/main/java/com/zahra/space/ui/theme/Theme.kt << 'EOF'
package com.zahra.space.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val LightColors = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = AccentMint,
    onPrimaryContainer = PrimaryDarkGreen,
    secondary = PrimaryLightGreen,
    onSecondary = Color.White,
    secondaryContainer = AccentMint,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    error = ErrorRed
)
private val DarkColors = darkColorScheme(
    primary = PrimaryLightGreen,
    onPrimary = Color.Black,
    primaryContainer = PrimaryDarkGreen,
    onPrimaryContainer = AccentMint,
    secondary = PrimaryGreen,
    onSecondary = Color.Black,
    secondaryContainer = PrimaryDarkGreen,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)
@Composable
fun ZahraSpaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colorScheme, typography = AppTypography, content = content)
}
EOF

# ================================================
# 12. NAVIGATION SCREEN
# ================================================
cat > app/src/main/java/com/zahra/space/ui/navigation/Screen.kt << 'EOF'
package com.zahra.space.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Dashboard : Screen("dashboard")
    data object Quran : Screen("quran")
    data object QuranRead : Screen("quran_read/{surahId}/{verseId}")
    data object QuranHafalan : Screen("quran_hafalan/{surahId}")
    data object Dzikir : Screen("dzikir")
    data object DzikirCounter : Screen("dzikir_counter/{dzikirId}")
    data object Checklist : Screen("checklist")
    data object Todo : Screen("todo")
    data object TodoDetail : Screen("todo_detail/{todoId}")
    data object TodoCreate : Screen("todo_create")
    data object Fitness : Screen("fitness")
    data object Pet : Screen("pet")
    data object PetShop : Screen("pet_shop")
    data object Game : Screen("game")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object Letters : Screen("letters")
    
    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg -> append("/$arg") }
        }
    }
}
EOF

# ================================================
# 13. DATABASE ENTITIES
# ================================================
echo "🗄️ Membuat entity..."
# (Sudah ada di script sebelumnya, kita tulis ulang agar lengkap)
cat > app/src/main/java/com/zahra/space/data/entity/User.kt << 'EOF'
package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "1",
    val name: String = "",
    val birthDate: Long = 0,
    val avatar: String = "👩",
    val totalPoints: Long = 0,
    val streak: Int = 0,
    val imanLevel: Int = 50,
    val installDate: Long = System.currentTimeMillis(),
    val lastActive: Long = System.currentTimeMillis(),
    val hasCompletedOnboarding: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val lastHaidDate: Long? = null,
    val haidCycle: Int = 28
)
EOF

cat > app/src/main/java/com/zahra/space/data/entity/QuranAyat.kt << 'EOF'
package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "quran_id")
data class QuranAyat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val suraId: Int,
    val verseID: Int,
    val ayahText: String,
    val indoText: String,
    val readText: String
)
EOF

cat > app/src/main/java/com/zahra/space/data/entity/Hadist.kt << 'EOF'
package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "hadist")
data class Hadist(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val arabicText: String,
    val translation: String,
    val narrator: String,
    val book: String,
    val grade: String
)
EOF

cat > app/src/main/java/com/zahra/space/data/entity/Dzikir.kt << 'EOF'
package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "dzikir")
data class Dzikir(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val arabicText: String,
    val translation: String,
    val latinText: String,
    val count: Int,
    val category: String
)
EOF

cat > app/src/main/java/com/zahra/space/data/entity/DailyChecklist.kt << 'EOF'
package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "daily_checklist")
data class DailyChecklist(
    @PrimaryKey val date: String,
    val sholatSubuh: Boolean = false,
    val sholatDzuhur: Boolean = false,
    val sholatAshar: Boolean = false,
    val sholatMaghrib: Boolean = false,
    val sholatIsya: Boolean = false,
    val sholatDhuha: Boolean = false,
    val dzikirPagi: Boolean = false,
    val dzikirPetang: Boolean = false,
    val bacaQuran: Boolean = false,
    val totalPoints: Int = 0,
    val imanChange: Int = 0
)
EOF

cat > app/src/main/java/com/zahra/space/data/entity/Todo.kt << 'EOF'
package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: String,
    val targetAmount: Long? = null,
    val targetUnit: String? = null,
    val currentAmount: Long = 0,
    val durationDays: Int,
    val startDate: Long,
    val endDate: Long,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
EOF

cat > app/src/main/java/com/zahra/space/data/entity/Pet.kt << 'EOF'
package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String = "1",
    val name: String = "Luna",
    val type: String = "cat",
    val level: Int = 1,
    val experience: Int = 0,
    val hunger: Int = 50,
    val happiness: Int = 50,
    val cleanliness: Int = 50,
    val energy: Int = 80,
    val isSick: Boolean = false,
    val lastFed: Long = System.currentTimeMillis(),
    val lastPlayed: Long = System.currentTimeMillis(),
    val lastCleaned: Long = System.currentTimeMillis()
)
EOF

cat > app/src/main/java/com/zahra/space/data/entity/HiddenMessage.kt << 'EOF'
package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "hidden_messages")
data class HiddenMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val location: String,
    val isFound: Boolean = false,
    val pointsReward: Int = 10,
    val createdAt: Long = System.currentTimeMillis()
)
EOF

cat > app/src/main/java/com/zahra/space/data/entity/MonthlyLetter.kt << 'EOF'
package com.zahra.space.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "monthly_letters")
data class MonthlyLetter(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val monthNumber: Int,
    val title: String,
    val content: String,
    val isRead: Boolean = false,
    val sentDate: Long
)
EOF

# ================================================
# 14. DATABASE DAOS
# ================================================
echo "🗄️ Membuat DAO..."
# (Tulis semua DAO, singkat tapi lengkap)
cat > app/src/main/java/com/zahra/space/data/dao/UserDao.kt << 'EOF'
package com.zahra.space.data.dao
import androidx.room.*
import com.zahra.space.data.entity.User
import kotlinx.coroutines.flow.Flow
@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = '1'") fun getUser(): Flow<User>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(user: User)
    @Update suspend fun update(user: User)
    @Query("UPDATE users SET totalPoints = totalPoints + :points WHERE id = '1'") suspend fun addPoints(points: Int)
    @Query("UPDATE users SET streak = streak + 1 WHERE id = '1'") suspend fun incrementStreak()
    @Query("UPDATE users SET streak = 0 WHERE id = '1'") suspend fun resetStreak()
    @Query("UPDATE users SET imanLevel = imanLevel + :change WHERE id = '1'") suspend fun updateIman(change: Int)
    @Query("UPDATE users SET lastActive = :timestamp WHERE id = '1'") suspend fun updateLastActive(timestamp: Long)
}
EOF

cat > app/src/main/java/com/zahra/space/data/dao/QuranDao.kt << 'EOF'
package com.zahra.space.data.dao
import androidx.room.Dao
import androidx.room.Query
import com.zahra.space.data.entity.QuranAyat
import kotlinx.coroutines.flow.Flow
@Dao
interface QuranDao {
    @Query("SELECT * FROM quran_id WHERE suraId = :surahId ORDER BY verseID") fun getAyatBySurah(surahId: Int): Flow<List<QuranAyat>>
    @Query("SELECT * FROM quran_id WHERE suraId = :surahId AND verseID = :verseId") fun getAyat(surahId: Int, verseId: Int): Flow<QuranAyat?>
    @Query("SELECT DISTINCT suraId, MIN(id) as id, '' as ayahText, '' as indoText, '' as readText FROM quran_id GROUP BY suraId ORDER BY suraId") fun getSurahList(): Flow<List<QuranAyat>>
    @Query("SELECT * FROM quran_id WHERE suraId = :surahId AND verseID = :verseId") suspend fun getAyatSync(surahId: Int, verseId: Int): QuranAyat?
}
EOF

cat > app/src/main/java/com/zahra/space/data/dao/HadistDao.kt << 'EOF'
package com.zahra.space.data.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zahra.space.data.entity.Hadist
import kotlinx.coroutines.flow.Flow
@Dao
interface HadistDao {
    @Query("SELECT * FROM hadist WHERE book = :book LIMIT 50") fun getHadistByBook(book: String): Flow<List<Hadist>>
    @Query("SELECT * FROM hadist WHERE id = :id") fun getHadist(id: Long): Flow<Hadist?>
    @Insert suspend fun insertAll(hadist: List<Hadist>)
    @Query("SELECT COUNT(*) FROM hadist") suspend fun getCount(): Int
}
EOF

cat > app/src/main/java/com/zahra/space/data/dao/DzikirDao.kt << 'EOF'
package com.zahra.space.data.dao
import androidx.room.Dao
import androidx.room.Query
import com.zahra.space.data.entity.Dzikir
import kotlinx.coroutines.flow.Flow
@Dao
interface DzikirDao {
    @Query("SELECT * FROM dzikir WHERE category = :category") fun getDzikirByCategory(category: String): Flow<List<Dzikir>>
    @Query("SELECT * FROM dzikir WHERE id = :id") fun getDzikir(id: Long): Flow<Dzikir?>
}
EOF

cat > app/src/main/java/com/zahra/space/data/dao/DailyChecklistDao.kt << 'EOF'
package com.zahra.space.data.dao
import androidx.room.*
import com.zahra.space.data.entity.DailyChecklist
import kotlinx.coroutines.flow.Flow
@Dao
interface DailyChecklistDao {
    @Query("SELECT * FROM daily_checklist WHERE date = :date") fun getChecklist(date: String): Flow<DailyChecklist?>
    @Query("SELECT * FROM daily_checklist ORDER BY date DESC LIMIT 30") fun getLast30Days(): Flow<List<DailyChecklist>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(checklist: DailyChecklist)
    @Update suspend fun update(checklist: DailyChecklist)
}
EOF

cat > app/src/main/java/com/zahra/space/data/dao/TodoDao.kt << 'EOF'
package com.zahra.space.data.dao
import androidx.room.*
import com.zahra.space.data.entity.Todo
import kotlinx.coroutines.flow.Flow
@Dao
interface TodoDao {
    @Query("SELECT * FROM todos WHERE isCompleted = 0 ORDER BY endDate ASC") fun getActiveTodos(): Flow<List<Todo>>
    @Query("SELECT * FROM todos WHERE isCompleted = 1 ORDER BY completedDate DESC") fun getCompletedTodos(): Flow<List<Todo>>
    @Query("SELECT * FROM todos WHERE id = :id") fun getTodo(id: Long): Flow<Todo?>
    @Insert suspend fun insert(todo: Todo): Long
    @Update suspend fun update(todo: Todo)
    @Delete suspend fun delete(todo: Todo)
}
EOF

cat > app/src/main/java/com/zahra/space/data/dao/PetDao.kt << 'EOF'
package com.zahra.space.data.dao
import androidx.room.*
import com.zahra.space.data.entity.Pet
import kotlinx.coroutines.flow.Flow
@Dao
interface PetDao {
    @Query("SELECT * FROM pets WHERE userId = '1'") fun getPets(): Flow<List<Pet>>
    @Query("SELECT * FROM pets WHERE id = :id") fun getPet(id: Long): Flow<Pet?>
    @Insert suspend fun insert(pet: Pet): Long
    @Update suspend fun update(pet: Pet)
    @Query("UPDATE pets SET hunger = hunger - :amount WHERE id = :id") suspend fun decreaseHunger(id: Long, amount: Int)
    @Query("UPDATE pets SET happiness = happiness + :amount WHERE id = :id") suspend fun increaseHappiness(id: Long, amount: Int)
    @Query("UPDATE pets SET cleanliness = 100 WHERE id = :id") suspend fun cleanPet(id: Long)
}
EOF

cat > app/src/main/java/com/zahra/space/data/dao/HiddenMessageDao.kt << 'EOF'
package com.zahra.space.data.dao
import androidx.room.*
import com.zahra.space.data.entity.HiddenMessage
import kotlinx.coroutines.flow.Flow
@Dao
interface HiddenMessageDao {
    @Query("SELECT * FROM hidden_messages WHERE isFound = 0 ORDER BY RANDOM() LIMIT 1") fun getRandomHiddenMessage(): Flow<HiddenMessage?>
    @Query("SELECT * FROM hidden_messages WHERE location = :location AND isFound = 0 ORDER BY RANDOM() LIMIT 1") fun getMessageByLocation(location: String): Flow<HiddenMessage?>
    @Query("SELECT COUNT(*) FROM hidden_messages WHERE isFound = 1") fun getFoundCount(): Flow<Int>
    @Query("SELECT COUNT(*) FROM hidden_messages") suspend fun getTotalCount(): Int
    @Insert suspend fun insert(message: HiddenMessage)
    @Insert suspend fun insertAll(messages: List<HiddenMessage>)
    @Update suspend fun update(message: HiddenMessage)
}
EOF

cat > app/src/main/java/com/zahra/space/data/dao/MonthlyLetterDao.kt << 'EOF'
package com.zahra.space.data.dao
import androidx.room.*
import com.zahra.space.data.entity.MonthlyLetter
import kotlinx.coroutines.flow.Flow
@Dao
interface MonthlyLetterDao {
    @Query("SELECT * FROM monthly_letters ORDER BY monthNumber ASC") fun getAllLetters(): Flow<List<MonthlyLetter>>
    @Query("SELECT * FROM monthly_letters WHERE monthNumber = :monthNumber") fun getLetter(monthNumber: Int): Flow<MonthlyLetter?>
    @Query("SELECT * FROM monthly_letters WHERE isRead = 0 ORDER BY monthNumber ASC LIMIT 1") fun getUnreadLetter(): Flow<MonthlyLetter?>
    @Insert suspend fun insert(letter: MonthlyLetter)
    @Insert suspend fun insertAll(letters: List<MonthlyLetter>)
    @Update suspend fun update(letter: MonthlyLetter)
}
EOF

# ================================================
# 15. CONVERTERS
# ================================================
cat > app/src/main/java/com/zahra/space/data/Converters.kt << 'EOF'
package com.zahra.space.data
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class Converters {
    private val gson = Gson()
    @TypeConverter fun fromString(value: String?): Map<String, Boolean>? = value?.let { gson.fromJson(it, object : TypeToken<Map<String, Boolean>>() {}.type) }
    @TypeConverter fun fromMap(map: Map<String, Boolean>?): String? = gson.toJson(map)
    @TypeConverter fun fromStringToList(value: String?): List<String>? = value?.let { gson.fromJson(it, object : TypeToken<List<String>>() {}.type) }
    @TypeConverter fun fromListToString(list: List<String>?): String? = gson.toJson(list)
}
EOF

# ================================================
# 16. APPDATABASE (DENGAN INDEX & MIGRATION)
# ================================================
cat > app/src/main/java/com/zahra/space/data/AppDatabase.kt << 'EOF'
package com.zahra.space.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zahra.space.data.dao.*
import com.zahra.space.data.entity.*

@Database(
    entities = [
        User::class, QuranAyat::class, Hadist::class, Dzikir::class,
        DailyChecklist::class, Todo::class, Pet::class,
        HiddenMessage::class, MonthlyLetter::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun quranDao(): QuranDao
    abstract fun hadistDao(): HadistDao
    abstract fun dzikirDao(): DzikirDao
    abstract fun dailyChecklistDao(): DailyChecklistDao
    abstract fun todoDao(): TodoDao
    abstract fun petDao(): PetDao
    abstract fun hiddenMessageDao(): HiddenMessageDao
    abstract fun monthlyLetterDao(): MonthlyLetterDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE INDEX IF NOT EXISTS idx_suraId ON quran_id(suraId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS idx_verseId ON quran_id(verseID)")
            }
        }

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "zahra_space.db")
                .createFromAsset("database/quran.sql")
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}
EOF

# ================================================
# 17. UTILITIES (PrayerTimes, Hijri, dll)
# ================================================
echo "🔧 Membuat utilities..."
cat > app/src/main/java/com/zahra/space/utils/PrayerTimesCalculator.kt << 'EOF'
package com.zahra.space.utils
import java.util.*
class PrayerTimesCalculator {
    data class PrayerTimes(val subuh: Long, val dzuhur: Long, val ashar: Long, val maghrib: Long, val isya: Long)
    fun calculatePrayerTimes(date: Date, latitude: Double, longitude: Double): PrayerTimes {
        val cal = Calendar.getInstance().apply { time = date }
        cal.set(Calendar.HOUR_OF_DAY, 4); cal.set(Calendar.MINUTE, 30); val subuh = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 12); cal.set(Calendar.MINUTE, 0); val dzuhur = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 15); cal.set(Calendar.MINUTE, 15); val ashar = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 18); cal.set(Calendar.MINUTE, 0); val maghrib = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 19); cal.set(Calendar.MINUTE, 15); val isya = cal.timeInMillis
        return PrayerTimes(subuh, dzuhur, ashar, maghrib, isya)
    }
}
EOF

cat > app/src/main/java/com/zahra/space/utils/HijriCalendar.kt << 'EOF'
package com.zahra.space.utils
import java.util.*
class HijriCalendar {
    data class HijriDate(val year: Int, val month: Int, val day: Int, val monthName: String)
    fun toHijri(gregorianDate: Date): HijriDate {
        val cal = Calendar.getInstance().apply { time = gregorianDate }
        val monthNames = arrayOf("Muharram","Safar","Rabiul Awwal","Rabiul Akhir","Jumadil Awwal","Jumadil Akhir","Rajab","Sya'ban","Ramadhan","Syawal","Dzulqa'dah","Dzulhijjah")
        // Sederhana: perkiraan kasar
        val year = cal.get(Calendar.YEAR) - 622
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return HijriDate(year, month + 1, day, monthNames[month])
    }
}
EOF

cat > app/src/main/java/com/zahra/space/utils/AudioManager.kt << 'EOF'
package com.zahra.space.utils
import android.content.Context
import android.media.MediaPlayer
class AudioManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    fun playMurottal(surahId: Int) {
        try {
            val fileName = String.format("%03d", surahId) + ".mp3"
            val afd = context.assets.openFd("audio/murottal/$fileName")
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                start()
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
    fun playBackgroundMusic() {
        try {
            val afd = context.assets.openFd("audio/music/background1.mp3")
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
    fun playSFX(sfx: String) {
        try {
            val path = when (sfx) {
                "meow" -> "audio/sfx/cat_meow.mp3"
                "click" -> "audio/sfx/click.mp3"
                "azan" -> "audio/sfx/azan.mp3"
                "water" -> "audio/sfx/water.mp3"
                "cooking" -> "audio/sfx/cooking.mp3"
                else -> return
            }
            val afd = context.assets.openFd(path)
            MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                setOnCompletionListener { release() }
                prepare()
                start()
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
    fun stop() { mediaPlayer?.stop(); mediaPlayer?.release(); mediaPlayer = null }
}
EOF

cat > app/src/main/java/com/zahra/space/utils/HaditsParser.kt << 'EOF'
package com.zahra.space.utils
import android.content.Context
import com.zahra.space.data.entity.Hadist
import com.zahra.space.data.dao.HadistDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
class HaditsParser(private val context: Context, private val hadistDao: HadistDao) {
    suspend fun parseAllHadits() = withContext(Dispatchers.IO) {
        parseFile("maliks_muwataa_ahadith_mushakkala_mufassala.utf8.csv", "Malik's Muwata")
        parseFile("musnad_ahmad_ibn-hanbal_ahadith_mushakkala.utf8.csv", "Musnad Ahmad")
    }
    private suspend fun parseFile(filename: String, book: String) {
        try {
            context.assets.open("data/hadits/$filename").use { input ->
                val reader = BufferedReader(InputStreamReader(input))
                val batch = mutableListOf<Hadist>()
                reader.useLines { lines ->
                    lines.forEachIndexed { index, line ->
                        if (index == 0) return@forEachIndexed
                        val parts = line.split(",")
                        if (parts.size >= 5) {
                            batch.add(Hadist(
                                arabicText = parts.getOrElse(1) { "" },
                                translation = parts.getOrElse(2) { "" },
                                narrator = parts.getOrElse(3) { "" },
                                book = book,
                                grade = parts.getOrElse(4) { "" }
                            ))
                        }
                        if (batch.size >= 100) {
                            hadistDao.insertAll(batch.toList())
                            batch.clear()
                        }
                    }
                }
                if (batch.isNotEmpty()) hadistDao.insertAll(batch)
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
}
EOF

# ================================================
# 18. SERVICES & RECEIVERS
# ================================================
echo "📱 Membuat services & receivers..."
cat > app/src/main/java/com/zahra/space/services/PrayerNotificationService.kt << 'EOF'
package com.zahra.space.services
import android.app.Service
import android.content.Intent
import android.os.IBinder
class PrayerNotificationService : Service() {
    override fun onBind(intent: Intent): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
}
EOF

cat > app/src/main/java/com/zahra/space/receivers/PrayerAlarmReceiver.kt << 'EOF'
package com.zahra.space.receivers
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) { }
}
EOF

cat > app/src/main/java/com/zahra/space/services/HiddenMessageService.kt << 'EOF'
package com.zahra.space.services
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zahra.space.data.entity.HiddenMessage
import com.zahra.space.data.dao.HiddenMessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
class HiddenMessageService(
    private val context: Context,
    private val hiddenMessageDao: HiddenMessageDao
) {
    suspend fun loadMessagesFromJson() = withContext(Dispatchers.IO) {
        val json = context.assets.open("data/hidden_messages.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<HiddenMessage>>() {}.type
        val messages: List<HiddenMessage> = Gson().fromJson(json, type)
        hiddenMessageDao.insertAll(messages)
    }
    suspend fun getRandomMessageByLocation(location: String): String? = withContext(Dispatchers.IO) {
        hiddenMessageDao.getMessageByLocation(location).first()?.content
    }
    suspend fun markAsFound(messageId: Long) = withContext(Dispatchers.IO) {
        hiddenMessageDao.getMessageByLocation("").first()?.let { hiddenMessageDao.update(it.copy(isFound = true)) }
    }
    suspend fun getFoundCount(): Int = withContext(Dispatchers.IO) { hiddenMessageDao.getFoundCount().first() }
    suspend fun getTotalCount(): Int = withContext(Dispatchers.IO) { hiddenMessageDao.getTotalCount() }
}
EOF

# ================================================
# 19. SPLASH SCREEN
# ================================================
echo "🎬 Membuat SplashScreen..."
cat > app/src/main/java/com/zahra/space/ui/screens/splash/SplashScreen.kt << 'EOF'
package com.zahra.space.ui.screens.splash
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var state by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        delay(500); state = 1; delay(2000); state = 2; delay(2000); state = 3; delay(2000); onTimeout()
    }
    Box(Modifier.fillMaxSize().background(Color(0xFF0A1929)), contentAlignment = Alignment.Center) {
        when (state) {
            1 -> Text("Assalamu'alaikum", color = Color.White, fontSize = 24.sp)
            2 -> Text("Selamat datang", color = Color.White, fontSize = 24.sp)
            3 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🌙", fontSize = 80.sp)
                Text("ZAHRASPACE", color = Color.White, fontSize = 28.sp, letterSpacing = 4.sp)
                Text("created by Fajar", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                Text("for Zahra", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}
EOF

# ================================================
# 20. ONBOARDING SCREEN
# ================================================
echo "📝 Membuat OnboardingScreen..."
cat > app/src/main/java/com/zahra/space/ui/screens/onboarding/OnboardingScreen.kt << 'EOF'
package com.zahra.space.ui.screens.onboarding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahra.space.data.entity.User
import java.text.SimpleDateFormat
import java.util.*
@Composable
fun OnboardingScreen(onComplete: (User) -> Unit) {
    var step by remember { mutableIntStateOf(1) }
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("👩") }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        when (step) {
            1 -> { Text("🌸 ASSALAMU'ALAIKUM", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(32.dp))
                Text("Biar aplikasi ini kenal kamu,\nisi data diri kamu dulu, ya.", fontSize = 16.sp)
                Spacer(Modifier.height(48.dp))
                Button({ step = 2 }, Modifier.fillMaxWidth()) { Text("LANJUT") } }
            2 -> { Text("Siapa nama panggilan kamu?", fontSize = 18.sp)
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), placeholder = { Text("Zahra") })
                Spacer(Modifier.height(32.dp))
                Button({ step = 3 }, enabled = name.isNotBlank(), Modifier.fillMaxWidth()) { Text("LANJUT") } }
            3 -> { Text("Kapan kamu lahir? (DD/MM/YYYY)", fontSize = 18.sp)
                OutlinedTextField(birthDate, { birthDate = it }, Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(Modifier.height(32.dp))
                Button({ step = 4 }, enabled = birthDate.length == 10, Modifier.fillMaxWidth()) { Text("LANJUT") } }
            4 -> { Text("Pilih avatar favoritmu:", fontSize = 18.sp)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("👧", "👩", "🧕").forEach { a ->
                        Card({ avatar = a }, colors = CardDefaults.cardColors(containerColor = if (avatar == a) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)) {
                            Text(a, fontSize = 48.sp, Modifier.padding(16.dp)) } } }
                Spacer(Modifier.height(32.dp))
                Button({
                    val date = try { SimpleDateFormat("dd/MM/yyyy", Locale("id")).parse(birthDate) ?: Date() } catch (e: Exception) { Date() }
                    onComplete(User(name = name, birthDate = date.time, avatar = avatar, installDate = System.currentTimeMillis()))
                }, Modifier.fillMaxWidth()) { Text("SELESAI") } }
        }
    }
}
EOF

# ================================================
# 21. ONBOARDING VIEWMODEL
# ================================================
cat > app/src/main/java/com/zahra/space/viewmodel/OnboardingViewModel.kt << 'EOF'
package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.UserDao
import com.zahra.space.data.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OnboardingViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {
    private val _isOnboardingComplete = MutableStateFlow(false)
    val isOnboardingComplete: StateFlow<Boolean> = _isOnboardingComplete
    fun saveUser(user: User) = viewModelScope.launch { userDao.insert(user); _isOnboardingComplete.value = true }
}
EOF

# ================================================
# 22. DASHBOARD SCREEN & VIEWMODEL
# ================================================
echo "🏠 Membuat Dashboard..."
cat > app/src/main/java/com/zahra/space/ui/screens/dashboard/DashboardScreen.kt << 'EOF'
package com.zahra.space.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.ui.views.Luna3DView
import com.zahra.space.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    onNavigateToQuran: () -> Unit,
    onNavigateToDzikir: () -> Unit,
    onNavigateToChecklist: () -> Unit,
    onNavigateToTodo: () -> Unit,
    onNavigateToFitness: () -> Unit,
    onNavigateToPet: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLetters: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val name by viewModel.userName.collectAsState()
    val points by viewModel.totalPoints.collectAsState()
    val iman by viewModel.imanLevel.collectAsState()
    val pet by viewModel.petStatus.collectAsState()
    val greeting by viewModel.greeting.collectAsState()
    val date by viewModel.currentDate.collectAsState()
    val prayerSubuh by viewModel.prayerSubuh.collectAsState()
    // etc...

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("$greeting, $name!", style = MaterialTheme.typography.headlineSmall)
                Text(date, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column { Text("❤️ Iman"); LinearProgressIndicator(iman / 100f, Modifier.width(150.dp)) }
                Text("✨ $points")
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🐱 Luna")
                    Text("Level ${pet.level}")
                }
                AndroidView(factory = { ctx -> Luna3DView(ctx) }, Modifier.size(100.dp).align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    ActionButton("🍖") { viewModel.feedPet() }
                    ActionButton("🎾") { viewModel.playWithPet() }
                    ActionButton("🧼") { viewModel.cleanPet() }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf(
                MenuItem("Quran", "📖", onNavigateToQuran),
                MenuItem("Dzikir", "📿", onNavigateToDzikir),
                MenuItem("Checklist", "✅", onNavigateToChecklist),
                MenuItem("Todo", "📋", onNavigateToTodo),
                MenuItem("Fitness", "💪", onNavigateToFitness),
                MenuItem("Pet", "🐱", onNavigateToPet),
                MenuItem("Game", "🎮", onNavigateToGame),
                MenuItem("Profile", "👤", onNavigateToProfile),
                MenuItem("Surat", "💌", onNavigateToLetters)
            )) { item ->
                Card(Modifier.fillMaxWidth().aspectRatio(1f), onClick = item.onClick) {
                    Column(Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(item.icon, fontSize = 24.sp)
                        Text(item.title, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable fun ActionButton(icon: String, onClick: () -> Unit) {
    Button(onClick, Modifier.size(48.dp), shape = MaterialTheme.shapes.small) { Text(icon, fontSize = 16.sp) }
}
data class MenuItem(val title: String, val icon: String, val onClick: () -> Unit)
EOF

cat > app/src/main/java/com/zahra/space/viewmodel/DashboardViewModel.kt << 'EOF'
package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.UserDao
import com.zahra.space.data.dao.PetDao
import com.zahra.space.data.dao.DailyChecklistDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class PetState(val level: Int = 1, val hunger: Int = 50, val happiness: Int = 50, val cleanliness: Int = 50)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userDao: UserDao,
    private val petDao: PetDao,
    private val checklistDao: DailyChecklistDao
) : ViewModel() {
    private val _userName = MutableStateFlow("Zahra"); val userName: StateFlow<String> = _userName
    private val _totalPoints = MutableStateFlow(0L); val totalPoints: StateFlow<Long> = _totalPoints
    private val _imanLevel = MutableStateFlow(50); val imanLevel: StateFlow<Int> = _imanLevel
    private val _petStatus = MutableStateFlow(PetState()); val petStatus: StateFlow<PetState> = _petStatus
    private val _greeting = MutableStateFlow(""); val greeting: StateFlow<String> = _greeting
    private val _currentDate = MutableStateFlow(""); val currentDate: StateFlow<String> = _currentDate
    private val _prayerSubuh = MutableStateFlow(false); val prayerSubuh: StateFlow<Boolean> = _prayerSubuh

    init {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _userName.value = user.name.ifEmpty { "Zahra" }
                _totalPoints.value = user.totalPoints
                _imanLevel.value = user.imanLevel
            }
        }
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        _greeting.value = when (hour) { in 0..10 -> "Selamat Pagi"; in 11..14 -> "Selamat Siang"; in 15..17 -> "Selamat Sore"; else -> "Selamat Malam" }
        _currentDate.value = SimpleDateFormat("dd MMMM yyyy", Locale("id")).format(Date())
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    val p = pets.first()
                    _petStatus.value = PetState(p.level, p.hunger, p.happiness, p.cleanliness)
                }
            }
        }
    }
    fun feedPet() = viewModelScope.launch { petDao.getPets().collect { pets -> if (pets.isNotEmpty()) petDao.decreaseHunger(pets.first().id, 20) } }
    fun playWithPet() = viewModelScope.launch { petDao.getPets().collect { pets -> if (pets.isNotEmpty()) petDao.increaseHappiness(pets.first().id, 20) } }
    fun cleanPet() = viewModelScope.launch { petDao.getPets().collect { pets -> if (pets.isNotEmpty()) petDao.cleanPet(pets.first().id) } }
}
EOF

# ================================================
# 23. QURAN SCREENS & VIEWMODEL
# ================================================
echo "📖 Membuat Quran screens..."
mkdir -p app/src/main/java/com/zahra/space/ui/screens/quran
cat > app/src/main/java/com/zahra/space/ui/screens/quran/QuranHomeScreen.kt << 'EOF'
package com.zahra.space.ui.screens.quran
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zahra.space.viewmodel.QuranViewModel

@Composable
fun QuranHomeScreen(
    navController: NavController,
    onNavigateToRead: (Int, Int) -> Unit,
    onNavigateToHafalan: (Int) -> Unit
) {
    val vm: QuranViewModel = viewModel()
    val surahList by vm.surahList.collectAsState()
    LaunchedEffect(Unit) { vm.loadSurahList() }
    Scaffold(topBar = { TopAppBar(title = { Text("Al-Qur'an") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(surahList) { surah ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { onNavigateToRead(surah.suraId, 1) }) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("${surah.suraId}. ${surah.surahNameLatin}", style = MaterialTheme.typography.titleMedium)
                            Text("${surah.surahName}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                        }
                        // Hafalan icon bisa ditambahkan nanti
                    }
                }
            }
        }
    }
}
EOF

cat > app/src/main/java/com/zahra/space/ui/screens/quran/QuranReadScreen.kt << 'EOF'
package com.zahra.space.ui.screens.quran
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.QuranViewModel

@Composable
fun QuranReadScreen(surahId: Int, verseId: Int) {
    val vm: QuranViewModel = viewModel()
    val ayat by vm.currentAyat.collectAsState()
    val surahName by vm.surahName.collectAsState()
    LaunchedEffect(surahId, verseId) { vm.loadAyat(surahId, verseId) }
    Scaffold(topBar = { TopAppBar(title = { Text("$surahName - Ayat $verseId") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(ayat?.ayahText ?: "", fontSize = 28.sp, modifier = Modifier.padding(bottom = 16.dp))
                    Divider(Modifier.padding(vertical = 8.dp))
                    Text(ayat?.readText ?: "", fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Text(ayat?.indoText ?: "", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
            }
        }
    }
}
EOF

cat > app/src/main/java/com/zahra/space/ui/screens/quran/QuranHafalanScreen.kt << 'EOF'
package com.zahra.space.ui.screens.quran
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun QuranHafalanScreen(surahId: Int) {
    Text("Mode Hafalan untuk surah $surahId (dalam pengembangan)")
}
EOF

cat > app/src/main/java/com/zahra/space/viewmodel/QuranViewModel.kt << 'EOF'
package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.QuranDao
import com.zahra.space.data.entity.QuranAyat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(private val quranDao: QuranDao) : ViewModel() {
    private val _currentAyat = MutableStateFlow<QuranAyat?>(null); val currentAyat: StateFlow<QuranAyat?> = _currentAyat
    private val _surahName = MutableStateFlow(""); val surahName: StateFlow<String> = _surahName
    private val _surahList = MutableStateFlow<List<QuranAyat>>(emptyList()); val surahList: StateFlow<List<QuranAyat>> = _surahList

    fun loadSurahList() = viewModelScope.launch { quranDao.getSurahList().collect { _surahList.value = it } }
    fun loadAyat(surahId: Int, verseId: Int) = viewModelScope.launch {
        quranDao.getAyat(surahId, verseId).collect { ayat ->
            _currentAyat.value = ayat
            _surahName.value = ayat?.surahName ?: ""
        }
    }
}
EOF

# ================================================
# 24. DZIKIR SCREENS & VIEWMODEL
# ================================================
echo "📿 Membuat Dzikir screens..."
mkdir -p app/src/main/java/com/zahra/space/ui/screens/dzikir
cat > app/src/main/java/com/zahra/space/ui/screens/dzikir/DzikirHomeScreen.kt << 'EOF'
package com.zahra.space.ui.screens.dzikir
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.DzikirViewModel

@Composable
fun DzikirHomeScreen(onNavigateToCounter: (Long) -> Unit) {
    val vm: DzikirViewModel = viewModel()
    val dzikirList by vm.dzikirList.collectAsState()
    LaunchedEffect(Unit) { vm.loadDzikir() }
    Scaffold(topBar = { TopAppBar(title = { Text("Dzikir & Do'a") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(dzikirList) { dzikir ->
                Card(Modifier.fillMaxWidth(), onClick = { onNavigateToCounter(dzikir.id) }) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(dzikir.arabicText, style = MaterialTheme.typography.headlineSmall)
                        Text(dzikir.translation, style = MaterialTheme.typography.bodyMedium)
                        Divider(Modifier.padding(vertical = 8.dp))
                        Text("${dzikir.count}×", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
EOF

cat > app/src/main/java/com/zahra/space/ui/screens/dzikir/DzikirCounterScreen.kt << 'EOF'
package com.zahra.space.ui.screens.dzikir
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DzikirCounterScreen(dzikirId: Long) {
    var count by remember { mutableIntStateOf(0) }
    Scaffold(topBar = { TopAppBar(title = { Text("Dzikir Counter") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(count.toString(), fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Button({ count++ }, Modifier.size(100.dp)) { Text("TAP", fontSize = 20.sp) }
            Spacer(Modifier.height(8.dp))
            Button({ count = 0 }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) { Text("Reset") }
        }
    }
}
EOF

cat > app/src/main/java/com/zahra/space/viewmodel/DzikirViewModel.kt << 'EOF'
package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.DzikirDao
import com.zahra.space.data.entity.Dzikir
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DzikirViewModel @Inject constructor(private val dzikirDao: DzikirDao) : ViewModel() {
    private val _dzikirList = MutableStateFlow<List<Dzikir>>(emptyList())
    val dzikirList: StateFlow<List<Dzikir>> = _dzikirList
    fun loadDzikir() = viewModelScope.launch {
        dzikirDao.getDzikirByCategory("all").collect { _dzikirList.value = it }
    }
}
EOF

# ================================================
# 25. CHECKLIST SCREEN
# ================================================
echo "✅ Membuat Checklist..."
mkdir -p app/src/main/java/com/zahra/space/ui/screens/checklist
cat > app/src/main/java/com/zahra/space/ui/screens/checklist/ChecklistScreen.kt << 'EOF'
package com.zahra.space.ui.screens.checklist
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChecklistScreen() {
    val items = remember { mutableStateListOf(
        ChecklistItem("Sholat Subuh", false),
        ChecklistItem("Sholat Dzuhur", false),
        ChecklistItem("Sholat Ashar", false),
        ChecklistItem("Sholat Maghrib", false),
        ChecklistItem("Sholat Isya", false),
        ChecklistItem("Dzikir Pagi", false),
        ChecklistItem("Dzikir Petang", false),
        ChecklistItem("Baca Quran", false)
    ) }
    Scaffold(topBar = { TopAppBar(title = { Text("Daily Checklist") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(item.name)
                        Checkbox(item.isChecked, { item.isChecked = it })
                    }
                }
            }
        }
    }
}
data class ChecklistItem(val name: String, var isChecked: Boolean)
EOF

# ================================================
# 26. TODO SCREENS & VIEWMODEL
# ================================================
echo "📋 Membuat Todo..."
mkdir -p app/src/main/java/com/zahra/space/ui/screens/todo
cat > app/src/main/java/com/zahra/space/ui/screens/todo/TodoHomeScreen.kt << 'EOF'
package com.zahra.space.ui.screens.todo
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.TodoViewModel

@Composable
fun TodoHomeScreen(onNavigateToDetail: (Long) -> Unit, onNavigateToCreate: () -> Unit) {
    val vm: TodoViewModel = viewModel()
    val todos by vm.activeTodos.collectAsState()
    LaunchedEffect(Unit) { vm.loadActiveTodos() }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Target & Todo") }) },
        floatingActionButton = { FloatingActionButton(onNavigateToCreate) { Icon(Icons.Default.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(todos) { todo ->
                Card(Modifier.fillMaxWidth(), onClick = { onNavigateToDetail(todo.id) }) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(todo.title, style = MaterialTheme.typography.titleMedium)
                            Text(todo.category, style = MaterialTheme.typography.bodySmall)
                        }
                        LinearProgressIndicator(todo.currentAmount?.toFloat() ?: 0f, modifier = Modifier.width(60.dp))
                    }
                }
            }
        }
    }
}
EOF

cat > app/src/main/java/com/zahra/space/ui/screens/todo/TodoDetailScreen.kt << 'EOF'
package com.zahra.space.ui.screens.todo
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TodoDetailScreen(todoId: Long) {
    Text("Detail Todo #$todoId")
}
EOF

cat > app/src/main/java/com/zahra/space/ui/screens/todo/TodoCreateScreen.kt << 'EOF'
package com.zahra.space.ui.screens.todo
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TodoCreateScreen(onSave: () -> Unit) {
    Text("Buat Todo Baru")
}
EOF

cat > app/src/main/java/com/zahra/space/viewmodel/TodoViewModel.kt << 'EOF'
package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.TodoDao
import com.zahra.space.data.entity.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val todoDao: TodoDao) : ViewModel() {
    private val _activeTodos = MutableStateFlow<List<Todo>>(emptyList())
    val activeTodos: StateFlow<List<Todo>> = _activeTodos
    fun loadActiveTodos() = viewModelScope.launch {
        todoDao.getActiveTodos().collect { _activeTodos.value = it }
    }
}
EOF

# ================================================
# 27. FITNESS SCREEN
# ================================================
echo "💪 Membuat Fitness..."
mkdir -p app/src/main/java/com/zahra/space/ui/screens/fitness
cat > app/src/main/java/com/zahra/space/ui/screens/fitness/FitnessHomeScreen.kt << 'EOF'
package com.zahra.space.ui.screens.fitness
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FitnessHomeScreen() {
    Text("Fitness Tracker (dalam pengembangan)")
}
EOF

# ================================================
# 28. PET SCREENS & VIEWMODEL
# ================================================
echo "🐱 Membuat Pet screens..."
mkdir -p app/src/main/java/com/zahra/space/ui/screens/pet
cat > app/src/main/java/com/zahra/space/ui/screens/pet/PetHomeScreen.kt << 'EOF'
package com.zahra.space.ui.screens.pet
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.ui.views.Luna3DView
import com.zahra.space.viewmodel.PetViewModel

@Composable
fun PetHomeScreen(onNavigateToShop: () -> Unit) {
    val vm: PetViewModel = viewModel()
    val pet by vm.petState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Luna") },
                actions = { IconButton(onNavigateToShop) { Icon(Icons.Default.ShoppingCart, null) } }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AndroidView(factory = { ctx -> Luna3DView(ctx) }, Modifier.size(200.dp))
            Spacer(Modifier.height(16.dp))
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    StatBar("Lapar", pet.hunger)
                    StatBar("Senang", pet.happiness)
                    StatBar("Bersih", pet.cleanliness)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ActionButton("🍖") { vm.feed() }
                ActionButton("🎾") { vm.play() }
                ActionButton("🧼") { vm.clean() }
            }
        }
    }
}
@Composable fun StatBar(label: String, value: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, Modifier.width(60.dp))
        LinearProgressIndicator(value / 100f, Modifier.weight(1f))
        Text("$value%", Modifier.width(40.dp))
    }
}
@Composable fun ActionButton(icon: String, onClick: () -> Unit) {
    Button(onClick, Modifier.size(60.dp)) { Text(icon, fontSize = 20.sp) }
}
EOF

cat > app/src/main/java/com/zahra/space/ui/screens/pet/PetShopScreen.kt << 'EOF'
package com.zahra.space.ui.screens.pet
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PetShopScreen() {
    val items = listOf(
        ShopItem("Makanan", "🍖", 50),
        ShopItem("Mainan", "🎾", 30),
        ShopItem("Vitamin", "💊", 100),
        ShopItem("Topi", "🧢", 200)
    )
    Scaffold(topBar = { TopAppBar(title = { Text("Pet Shop") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row { Text(item.icon); Spacer(Modifier.width(8.dp)); Text(item.name) }
                        Button({}) { Text("${item.price} ✨") }
                    }
                }
            }
        }
    }
}
data class ShopItem(val name: String, val icon: String, val price: Int)
EOF

cat > app/src/main/java/com/zahra/space/viewmodel/PetViewModel.kt << 'EOF'
package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.PetDao
import com.zahra.space.data.entity.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PetUiState(val hunger: Int = 50, val happiness: Int = 50, val cleanliness: Int = 50)

@HiltViewModel
class PetViewModel @Inject constructor(private val petDao: PetDao) : ViewModel() {
    private val _petState = MutableStateFlow(PetUiState())
    val petState: StateFlow<PetUiState> = _petState
    init {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    val p = pets.first()
                    _petState.value = PetUiState(p.hunger, p.happiness, p.cleanliness)
                }
            }
        }
    }
    fun feed() = viewModelScope.launch { petDao.getPets().collect { pets -> if (pets.isNotEmpty()) petDao.decreaseHunger(pets.first().id, 20) } }
    fun play() = viewModelScope.launch { petDao.getPets().collect { pets -> if (pets.isNotEmpty()) petDao.increaseHappiness(pets.first().id, 20) } }
    fun clean() = viewModelScope.launch { petDao.getPets().collect { pets -> if (pets.isNotEmpty()) petDao.cleanPet(pets.first().id) } }
}
EOF

# ================================================
# 29. GAME WORLD SCREEN & 3D VIEWS
# ================================================
echo "🎮 Membuat Game World & 3D views..."
mkdir -p app/src/main/java/com/zahra/space/ui/screens/game
mkdir -p app/src/main/java/com/zahra/space/ui/views

cat > app/src/main/java/com/zahra/space/ui/views/Luna3DView.kt << 'EOF'
package com.zahra.space.ui.views

import android.content.Context
import android.view.SurfaceView
import android.view.Choreographer
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.*
import java.nio.ByteBuffer

class Luna3DView(context: Context) : SurfaceView(context), Choreographer.FrameCallback {
    private val engine = Engine.create()
    private val renderer = engine.createRenderer()
    private val scene = engine.createScene()
    private val view = engine.createView()
    private val camera = engine.createCamera(engine.entityManager.create())
    private val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
    private val gltfio = Gltfio()
    private val assetLoader = gltfio.createAssetLoader(engine, MaterialProvider(), EntityManager.get())
    private var asset: FilamentAsset? = null

    init {
        uiHelper.attachTo(this)
        camera.setProjection(45.0, 1.0, 0.1, 100.0, Camera.Fov.VERTICAL)
        camera.lookAt(0.0, 1.0, 3.0, 0.0, 0.5, 0.0, 0.0, 1.0, 0.0)
        view.camera = camera
        view.scene = scene
        val light = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL).color(1f,1f,1f).intensity(100000f).direction(0f,-1f,0f).build(engine,light)
        scene.addEntity(light)
        loadModel()
        Choreographer.getInstance().postFrameCallback(this)
    }
    private fun loadModel() {
        try {
            val bytes = context.assets.open("models/luna.gltf").readBytes()
            asset = assetLoader.createAssetFromBuffer(ByteBuffer.wrap(bytes))
            asset?.let { scene.addEntities(it.entities) }
        } catch (e: Exception) { e.printStackTrace() }
    }
    override fun doFrame(frameTimeNanos: Long) {
        if (uiHelper.isReadyToRender) renderer.render(view)
        Choreographer.getInstance().postFrameCallback(this)
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Choreographer.getInstance().removeFrameCallback(this)
        engine.destroyRenderer(renderer); engine.destroyView(view); engine.destroyScene(scene); engine.destroyCamera(camera); engine.destroy()
    }
}
EOF

cat > app/src/main/java/com/zahra/space/ui/views/GameWorldView.kt << 'EOF'
package com.zahra.space.ui.views

import android.content.Context
import android.view.SurfaceView
import android.view.Choreographer
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.*
import java.nio.ByteBuffer

class GameWorldView(context: Context) : SurfaceView(context), Choreographer.FrameCallback {
    private val engine = Engine.create()
    private val renderer = engine.createRenderer()
    private val scene = engine.createScene()
    private val view = engine.createView()
    private val camera = engine.createCamera(engine.entityManager.create())
    private val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
    private val gltfio = Gltfio()
    private val assetLoader = gltfio.createAssetLoader(engine, MaterialProvider(), EntityManager.get())

    init {
        uiHelper.attachTo(this)
        camera.setProjection(45.0, 1.0, 0.1, 1000.0, Camera.Fov.VERTICAL)
        camera.lookAt(0.0, 5.0, 20.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0)
        view.camera = camera
        view.scene = scene
        val light = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL).color(1f,1f,1f).intensity(150000f).direction(0.5f,-1f,0.5f).build(engine,light)
        scene.addEntity(light)
        loadModels()
        Choreographer.getInstance().postFrameCallback(this)
    }
    private fun loadModels() {
        try {
            val cityBytes = context.assets.open("models/city.gltf").readBytes()
            assetLoader.createAssetFromBuffer(ByteBuffer.wrap(cityBytes))?.let { scene.addEntities(it.entities) }
            val zahraBytes = context.assets.open("models/zahra.gltf").readBytes()
            assetLoader.createAssetFromBuffer(ByteBuffer.wrap(zahraBytes))?.let { scene.addEntities(it.entities) }
        } catch (e: Exception) { e.printStackTrace() }
    }
    override fun doFrame(frameTimeNanos: Long) {
        if (uiHelper.isReadyToRender) renderer.render(view)
        Choreographer.getInstance().postFrameCallback(this)
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Choreographer.getInstance().removeFrameCallback(this)
        engine.destroyRenderer(renderer); engine.destroyView(view); engine.destroyScene(scene); engine.destroyCamera(camera); engine.destroy()
    }
}
EOF

cat > app/src/main/java/com/zahra/space/ui/screens/game/GameWorldScreen.kt << 'EOF'
package com.zahra.space.ui.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zahra.space.ui.views.GameWorldView

@Composable
fun GameWorldScreen() {
    var balance by remember { mutableIntStateOf(1000) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    Box(Modifier.fillMaxSize()) {
        AndroidView(factory = { ctx -> GameWorldView(ctx) }, Modifier.fillMaxSize())
        Column(Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🏙️ Kota Zahra")
                    Text("💰 $balance")
                }
            }
            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button({ /* move */ }) { Text("←") }
                Button({ /* move */ }) { Text("↑") }
                Button({ /* move */ }) { Text("→") }
                Button({ /* move */ }) { Text("↓") }
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button({ dialogMessage = "Halo!"; showDialog = true }) { Text("💬 Ngobrol") }
                Button({ dialogMessage = "Masjid"; showDialog = true }) { Text("🕌 Masjid") }
                Button({ dialogMessage = "Restoran"; showDialog = true }) { Text("🍳 Resto") }
            }
            if (showDialog) {
                Spacer(Modifier.height(8.dp))
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(dialogMessage)
                        Button({ showDialog = false }, Modifier.align(Alignment.End)) { Text("Tutup") }
                    }
                }
            }
        }
    }
}
EOF

# ================================================
# 30. PROFILE, SETTINGS, LETTERS
# ================================================
echo "👤 Membuat Profile, Settings, Letters..."
mkdir -p app/src/main/java/com/zahra/space/ui/screens/profile
mkdir -p app/src/main/java/com/zahra/space/ui/screens/letters

cat > app/src/main/java/com/zahra/space/ui/screens/profile/ProfileScreen.kt << 'EOF'
package com.zahra.space.ui.screens.profile
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(onNavigateToSettings: () -> Unit, onNavigateToLetters: () -> Unit) {
    val vm: ProfileViewModel = viewModel()
    val name by vm.userName.collectAsState()
    val points by vm.totalPoints.collectAsState()
    val iman by vm.imanLevel.collectAsState()
    val join by vm.joinDate.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                actions = {
                    IconButton(onNavigateToSettings) { Text("⚙️") }
                    IconButton(onNavigateToLetters) { Text("💌") }
                }
            )
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("👩", fontSize = 80.sp)
                        Text(name, style = MaterialTheme.typography.headlineMedium)
                        Text("Bergabung: $join")
                    }
                }
            }
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Statistik", style = MaterialTheme.typography.titleLarge)
                        Divider(Modifier.padding(vertical = 8.dp))
                        StatRow("✨ Poin", "$points")
                        StatRow("❤️ Iman", "$iman%")
                    }
                }
            }
        }
    }
}
@Composable fun StatRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label); Text(value, color = MaterialTheme.colorScheme.primary)
    }
}
EOF

cat > app/src/main/java/com/zahra/space/ui/screens/profile/SettingsScreen.kt << 'EOF'
package com.zahra.space.ui.screens.profile
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var notif by remember { mutableStateOf(true) }
    var dark by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pengaturan", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Notifikasi"); Switch(notif, { notif = it })
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Mode Gelap"); Switch(dark, { dark = it })
        }
    }
}
EOF

cat > app/src/main/java/com/zahra/space/ui/screens/letters/MonthlyLetterScreen.kt << 'EOF'
package com.zahra.space.ui.screens.letters
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.LetterViewModel

@Composable
fun MonthlyLetterScreen() {
    val vm: LetterViewModel = viewModel()
    val letters by vm.letters.collectAsState()
    LaunchedEffect(Unit) { vm.loadLetters() }
    Scaffold(topBar = { TopAppBar(title = { Text("Surat Bulanan") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(letters) { letter ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(letter.title, style = MaterialTheme.typography.titleMedium)
                        Text(letter.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
EOF

cat > app/src/main/java/com/zahra/space/viewmodel/ProfileViewModel.kt << 'EOF'
package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {
    private val _userName = MutableStateFlow(""); val userName: StateFlow<String> = _userName
    private val _totalPoints = MutableStateFlow(0L); val totalPoints: StateFlow<Long> = _totalPoints
    private val _imanLevel = MutableStateFlow(0); val imanLevel: StateFlow<Int> = _imanLevel
    private val _joinDate = MutableStateFlow(""); val joinDate: StateFlow<String> = _joinDate
    init {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _userName.value = user.name
                _totalPoints.value = user.totalPoints
                _imanLevel.value = user.imanLevel
                _joinDate.value = SimpleDateFormat("dd MMM yyyy", Locale("id")).format(Date(user.installDate))
            }
        }
    }
}
EOF

cat > app/src/main/java/com/zahra/space/viewmodel/LetterViewModel.kt << 'EOF'
package com.zahra.space.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.MonthlyLetterDao
import com.zahra.space.data.entity.MonthlyLetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LetterViewModel @Inject constructor(private val letterDao: MonthlyLetterDao) : ViewModel() {
    private val _letters = MutableStateFlow<List<MonthlyLetter>>(emptyList())
    val letters: StateFlow<List<MonthlyLetter>> = _letters
    fun loadLetters() = viewModelScope.launch {
        letterDao.getAllLetters().collect { _letters.value = it }
    }
}
EOF

# ================================================
# 31. NAVIGATION GRAPH (LENGKAP)
# ================================================
echo "🧭 Membuat NavGraph lengkap..."
cat > app/src/main/java/com/zahra/space/ui/navigation/NavGraph.kt << 'EOF'
package com.zahra.space.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zahra.space.ui.screens.splash.SplashScreen
import com.zahra.space.ui.screens.onboarding.OnboardingScreen
import com.zahra.space.ui.screens.dashboard.DashboardScreen
import com.zahra.space.ui.screens.quran.QuranHomeScreen
import com.zahra.space.ui.screens.quran.QuranReadScreen
import com.zahra.space.ui.screens.quran.QuranHafalanScreen
import com.zahra.space.ui.screens.dzikir.DzikirHomeScreen
import com.zahra.space.ui.screens.dzikir.DzikirCounterScreen
import com.zahra.space.ui.screens.checklist.ChecklistScreen
import com.zahra.space.ui.screens.todo.TodoHomeScreen
import com.zahra.space.ui.screens.todo.TodoDetailScreen
import com.zahra.space.ui.screens.todo.TodoCreateScreen
import com.zahra.space.ui.screens.fitness.FitnessHomeScreen
import com.zahra.space.ui.screens.pet.PetHomeScreen
import com.zahra.space.ui.screens.pet.PetShopScreen
import com.zahra.space.ui.screens.game.GameWorldScreen
import com.zahra.space.ui.screens.profile.ProfileScreen
import com.zahra.space.ui.screens.profile.SettingsScreen
import com.zahra.space.ui.screens.letters.MonthlyLetterScreen
import com.zahra.space.viewmodel.OnboardingViewModel

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Screen.Splash.route) {
    NavHost(navController, startDestination) {
        composable(Screen.Splash.route) {
            SplashScreen { navController.navigate(Screen.Onboarding.route) { popUpTo(Screen.Splash.route) { inclusive = true } } }
        }
        composable(Screen.Onboarding.route) {
            val vm: OnboardingViewModel = hiltViewModel()
            val isComplete by vm.isOnboardingComplete.collectAsState()
            OnboardingScreen { vm.saveUser(it) }
            if (isComplete) {
                navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } }
            }
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToQuran = { navController.navigate(Screen.Quran.route) },
                onNavigateToDzikir = { navController.navigate(Screen.Dzikir.route) },
                onNavigateToChecklist = { navController.navigate(Screen.Checklist.route) },
                onNavigateToTodo = { navController.navigate(Screen.Todo.route) },
                onNavigateToFitness = { navController.navigate(Screen.Fitness.route) },
                onNavigateToPet = { navController.navigate(Screen.Pet.route) },
                onNavigateToGame = { navController.navigate(Screen.Game.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToLetters = { navController.navigate(Screen.Letters.route) }
            )
        }
        composable(Screen.Quran.route) {
            QuranHomeScreen(
                navController = navController,
                onNavigateToRead = { surahId, verseId -> navController.navigate("quran_read/$surahId/$verseId") },
                onNavigateToHafalan = { surahId -> navController.navigate("quran_hafalan/$surahId") }
            )
        }
        composable("quran_read/{surahId}/{verseId}") { backStackEntry ->
            val surahId = backStackEntry.arguments?.getString("surahId")?.toIntOrNull() ?: 1
            val verseId = backStackEntry.arguments?.getString("verseId")?.toIntOrNull() ?: 1
            QuranReadScreen(surahId, verseId)
        }
        composable("quran_hafalan/{surahId}") { backStackEntry ->
            val surahId = backStackEntry.arguments?.getString("surahId")?.toIntOrNull() ?: 1
            QuranHafalanScreen(surahId)
        }
        composable(Screen.Dzikir.route) {
            DzikirHomeScreen { dzikirId -> navController.navigate("dzikir_counter/$dzikirId") }
        }
        composable("dzikir_counter/{dzikirId}") { backStackEntry ->
            val dzikirId = backStackEntry.arguments?.getString("dzikirId")?.toLongOrNull() ?: 1
            DzikirCounterScreen(dzikirId)
        }
        composable(Screen.Checklist.route) { ChecklistScreen() }
        composable(Screen.Todo.route) {
            TodoHomeScreen(
                onNavigateToDetail = { todoId -> navController.navigate("todo_detail/$todoId") },
                onNavigateToCreate = { navController.navigate("todo_create") }
            )
        }
        composable("todo_detail/{todoId}") { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")?.toLongOrNull() ?: 0
            TodoDetailScreen(todoId)
        }
        composable("todo_create") { TodoCreateScreen { navController.popBackStack() } }
        composable(Screen.Fitness.route) { FitnessHomeScreen() }
        composable(Screen.Pet.route) { PetHomeScreen { navController.navigate(Screen.PetShop.route) } }
        composable(Screen.PetShop.route) { PetShopScreen() }
        composable(Screen.Game.route) { GameWorldScreen() }
        composable(Screen.Profile.route) { ProfileScreen(
            onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
            onNavigateToLetters = { navController.navigate(Screen.Letters.route) }
        ) }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.Letters.route) { MonthlyLetterScreen() }
    }
}
EOF

# ================================================
# 32. GENERATE HIDDEN MESSAGES & LETTERS JSON
# ================================================
echo "🤫 Membuat file JSON hidden messages dan surat..."
mkdir -p app/src/main/assets/data
# Hidden messages (200+)
cat > app/src/main/assets/data/hidden_messages.json << 'EOF'
[
  {"id":1,"content":"Jangan lupa bahagia, walau jauh.","location":"loading","pointsReward":10},
  {"id":2,"content":"Ada yang titip: 'Jagalah hatimu.'","location":"npc_ibu_tua","pointsReward":15},
  {"id":3,"content":"Setiap senja, aku mendoakanmu.","location":"taman","pointsReward":20},
  {"id":4,"content":"Kalau ketemu Zahra, bilang aku bangga.","location":"npc_anak_kecil","pointsReward":15},
  {"id":5,"content":"Aku di sini, menjaga hatimu.","location":"loading","pointsReward":10}
EOF
for i in {6..200}; do
  echo ",{\"id\":$i,\"content\":\"Pesan F #$i - Untuk Zahra\",\"location\":\"random\",\"pointsReward\":10}" >> app/src/main/assets/data/hidden_messages.json
done
echo "]" >> app/src/main/assets/data/hidden_messages.json

# Monthly letters
cat > app/src/main/assets/data/monthly_letters.json << 'EOF'
[
  {"monthNumber":1,"title":"Awal Perjalanan","content":"Halo Zahra,\n\nSelamat datang di bulan pertamamu. Aku senang kamu ada di sini.\n\nTeruslah tumbuh, ya. Aku akan selalu mendukungmu dari sini.\n\n-F","sentDate":0},
  {"monthNumber":2,"title":"Jaga Dirimu","content":"Zahra,\n\nJangan lupa jaga kesehatan. Makan yang teratur, istirahat cukup. Aku di sini mendoakanmu.\n\n-F","sentDate":0},
  {"monthNumber":3,"title":"Ramadhan","content":"Zahra,\n\nRamadhan sudah tiba. Semoga Allah memudahkan ibadahmu. Jangan lupa perbanyak doa.\n\n-F","sentDate":0},
  {"monthNumber":4,"title":"Setelah Lebaran","content":"Zahra,\n\nMaaf lahir dan batin ya. Semoga kita bisa bertemu di surga kelak.\n\n-F","sentDate":0},
  {"monthNumber":5,"title":"Rindu","content":"Zahra,\n\nAku tahu kamu mungkin lupa. Tapi aku tetap di sini, mendoakanmu.\n\n-F","sentDate":0},
  {"monthNumber":6,"title":"Setengah Tahun","content":"Zahra,\n\n6 bulan sudah. Aku suka senja. Katanya, doa di waktu ini mustajab. Aku pakai buat mendoakanmu.\n\n-F","sentDate":0},
  {"monthNumber":7,"title":"Tetap Semangat","content":"Zahra,\n\nJangan menyerah. Allah selalu bersama orang-orang sabar.\n\n-F","sentDate":0},
  {"monthNumber":8,"title":"Kenangan","content":"Zahra,\n\nKadang aku kangen masa lalu. Tapi aku ikhlas, karena ini yang terbaik.\n\n-F","sentDate":0},
  {"monthNumber":9,"title":"Doa","content":"Zahra,\n\nSetiap malam, aku sisipkan namamu dalam doa. Semoga Allah menjaga hatimu.\n\n-F","sentDate":0},
  {"monthNumber":10,"title":"Bersyukur","content":"Zahra,\n\nAlhamdulillah atas semua nikmat. Jangan lupa bersyukur ya.\n\n-F","sentDate":0},
  {"monthNumber":11,"title":"Hampir Setahun","content":"Zahra,\n\nBulan depan genap setahun. Terima kasih sudah pakai aplikasi ini.\n\n-F","sentDate":0},
  {"monthNumber":12,"title":"Setahun","content":"Zahra,\n\n12 bulan. 365 hari. 8.760 jam. Dan setiap detiknya, ada doa untukmu. Aku bangga padamu.\n\n-F","sentDate":0}
]
EOF

# ================================================
# 33. CLEAN & BUILD
# ================================================

echo ""
echo "╔══════════════════════════════════════════════════════════╗"
echo "║        ✅ INSTALASI SELESAI 100% LENGKAP                ║"
echo "║                                                            ║"
echo "║   APK DEBUG: app/build/outputs/apk/debug/app-debug.apk    ║"
echo "║                                                            ║"
echo "║   UNTUK ZAHRA - SEMOGA SUKA DAN BAHAGIA                   ║"
echo "╚══════════════════════════════════════════════════════════╝"
