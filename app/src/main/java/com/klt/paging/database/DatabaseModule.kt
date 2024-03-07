package com.klt.paging.database

import android.content.Context
import androidx.room.Room
import com.klt.paging.database.db.MockDb
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
    fun provideDatabase(@ApplicationContext context: Context): MockDb = Room.databaseBuilder(
        context,
        MockDb::class.java,
        MockDb.DB_NAME
    ).build()

}