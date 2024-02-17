package com.klt.paging.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CatEntity::class,RemoteKeyEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CatDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao
    abstract fun remoteKeyDao() : RemoteKeyDao

    companion object {
        const val DB_NAME = "cat_db"
    }
}