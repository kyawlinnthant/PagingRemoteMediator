package com.klt.paging.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CatEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CatDatabase : RoomDatabase() {
    abstract fun dao(): CatDao

    companion object {
        const val DB_NAME = "cat_db"
    }
}