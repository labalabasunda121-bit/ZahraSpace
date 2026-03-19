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
        HiddenMessage::class, MonthlyLetter::class,
        Restaurant::class   // ← TAMBAH INI!
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
    abstract fun restaurantDao(): RestaurantDao

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
