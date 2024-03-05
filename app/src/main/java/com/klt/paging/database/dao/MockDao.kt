package com.klt.paging.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.klt.paging.database.entity.MockDataEntity

@Dao
interface MockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMocks(mocks: List<MockDataEntity>)

    @Query("SELECT * FROM ${MockDataEntity.TB_NAME} ORDER BY `id` ASC")
    fun pagingSource(): PagingSource<Int, MockDataEntity>

    @Query("SELECT * FROM ${MockDataEntity.TB_NAME}")
    suspend fun getMocks(): List<MockDataEntity>

    @Query("DELETE FROM ${MockDataEntity.TB_NAME}")
    suspend fun deleteAll()
}