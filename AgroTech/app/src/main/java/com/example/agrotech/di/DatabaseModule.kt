package com.example.agrotech.di

import android.content.Context
import androidx.room.Room
import com.example.agrotech.data.local.AppDatabase
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
    fun provideRoomDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java, "fitia-db"
            ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase) = db.getUserDao()
}