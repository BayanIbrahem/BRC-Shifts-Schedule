package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.di

import android.content.Context
import androidx.room.Room
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.LocalDataSource
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.db.BrcDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context,
    ): BrcDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = BrcDatabase::class.java,
            name = "brc_db"
        )
            .fallbackToDestructiveMigrationFrom(*(1..10).toList().toIntArray())
            .build()

    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        db: BrcDatabase,
    ) = LocalDataSource(db)
}