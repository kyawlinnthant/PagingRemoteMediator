package com.klt.paging.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(remoteKeys: List<RemoteKeyEntity>)

    @Query("SELECT * FROM ${RemoteKeyEntity.TABLE_NAME} WHERE id =:id")
    suspend fun getRemoteKey(id: String): RemoteKeyEntity?

    @Query("DELETE FROM ${RemoteKeyEntity.TABLE_NAME}")
    suspend fun deleteKeys()

    @Query(
        "SELECT `created_at` FROM ${RemoteKeyEntity.TABLE_NAME} ORDER BY `created_at` DESC LIMIT 1"
    )
    suspend fun getCreationTime(): Long?

}