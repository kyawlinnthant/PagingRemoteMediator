package com.klt.paging.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCat(cat: CatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCats(cats: List<CatEntity>)

    @Query("SELECT * FROM ${CatEntity.TABLE_NAME}")
    fun queryCats(): List<CatEntity>

    @Query("DELETE FROM ${CatEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Delete(entity = CatEntity::class)
    suspend fun deleteCat(cat: CatEntity)
}