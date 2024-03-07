package com.klt.paging.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.klt.paging.database.dao.KeyDao
import com.klt.paging.database.dao.MockDao
import com.klt.paging.database.entity.KeyEntity
import com.klt.paging.database.entity.MockDataEntity

@Database(
    entities = [MockDataEntity::class, KeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MockDb : RoomDatabase() {
    abstract fun mockDao(): MockDao
    abstract fun keyDao(): KeyDao

    companion object {
        const val DB_NAME = "mock_db"
    }
}