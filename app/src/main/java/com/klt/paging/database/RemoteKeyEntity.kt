package com.klt.paging.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.klt.paging.database.RemoteKeyEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val nextPage: Int?,
    val prevPage: Int?,
) {
    companion object {
        const val TABLE_NAME = "remote_key"
    }
}
