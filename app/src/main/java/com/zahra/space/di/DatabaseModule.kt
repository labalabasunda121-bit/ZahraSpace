package com.zahra.space.di

import android.content.Context
import androidx.room.Room
import com.zahra.space.data.AppDatabase
import com.zahra.space.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "zahra_space_database"
        )
        .createFromAsset("database/quran.sql")
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideQuranDao(appDatabase: AppDatabase): QuranDao = appDatabase.quranDao()

    @Provides
    @Singleton
    fun provideHadistDao(appDatabase: AppDatabase): HadistDao = appDatabase.hadistDao()

    @Provides
    @Singleton
    fun provideDzikirDao(appDatabase: AppDatabase): DzikirDao = appDatabase.dzikirDao()

    @Provides
    @Singleton
    fun provideDailyChecklistDao(appDatabase: AppDatabase): DailyChecklistDao = appDatabase.dailyChecklistDao()

    @Provides
    @Singleton
    fun provideTodoDao(appDatabase: AppDatabase): TodoDao = appDatabase.todoDao()

    @Provides
    @Singleton
    fun providePetDao(appDatabase: AppDatabase): PetDao = appDatabase.petDao()

    @Provides
    @Singleton
    fun provideHiddenMessageDao(appDatabase: AppDatabase): HiddenMessageDao = appDatabase.hiddenMessageDao()

    @Provides
    @Singleton
    fun provideMonthlyLetterDao(appDatabase: AppDatabase): MonthlyLetterDao = appDatabase.monthlyLetterDao()
}
