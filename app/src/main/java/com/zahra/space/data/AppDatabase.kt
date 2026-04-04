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
        User::class,
        QuranAyat::class,
        Hadist::class,
        Dzikir::class,
        DailyChecklist::class,
        Todo::class,
        Pet::class,
        HiddenMessage::class,
        MonthlyLetter::class,
        Restaurant::class
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
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Buat tabel restaurant
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS restaurant (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        level INTEGER NOT NULL DEFAULT 1,
                        experience INTEGER NOT NULL DEFAULT 0,
                        balance INTEGER NOT NULL DEFAULT 1000,
                        reputation INTEGER NOT NULL DEFAULT 50,
                        employeeCount INTEGER NOT NULL DEFAULT 0,
                        tables INTEGER NOT NULL DEFAULT 4,
                        completedOrders INTEGER NOT NULL DEFAULT 0,
                        totalCustomers INTEGER NOT NULL DEFAULT 0
                    )
                """)
                
                // Insert data awal
                database.execSQL("""
                    INSERT INTO restaurant (level, experience, balance, reputation, employeeCount, tables, completedOrders, totalCustomers)
                    SELECT 1, 0, 1000, 50, 0, 4, 0, 0
                    WHERE NOT EXISTS (SELECT 1 FROM restaurant)
                """)
                
                // Buat index untuk quran (sebelumnya sudah ada)
                database.execSQL("CREATE INDEX IF NOT EXISTS idx_suraId ON quran_id(suraId)")
                database.execSQL("CREATE INDEX IF NOT EXISTS idx_verseId ON quran_id(verseID)")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zahra_space_database"
                )
                .createFromAsset("database/quran.sql")
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}
