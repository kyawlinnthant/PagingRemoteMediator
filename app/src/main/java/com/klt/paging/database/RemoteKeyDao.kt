package com.klt.paging.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRemoteKey(remoteKey: RemoteKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRemoteKeys(remoteKeys: List<RemoteKeyEntity>)

    @Query("SELECT * FROM ${RemoteKeyEntity.TABLE_NAME} WHERE id =:id")
    suspend fun getRemoteKey(id: String): RemoteKeyEntity

    @Query("DELETE FROM ${RemoteKeyEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Delete(entity = RemoteKeyEntity::class)
    suspend fun deleteRemoteKey(remoteKey: RemoteKeyEntity)
}