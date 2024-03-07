package com.klt.paging.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = KeyEntity.TABLE_NAME
)
data class KeyEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nextPage: Int?,
    val prevPage: Int?,
    val currentPage: Int,
    @ColumnInfo("created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val TABLE_NAME = "key_table"
    }
}
