package com.klt.paging.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.klt.paging.model.Cat
import com.klt.paging.database.CatEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CatEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val height: Int,
    val width: Int,
    val url: String,
) {
    companion object {
        const val TABLE_NAME = "cat_table"
    }

    fun toVo() = Cat(
        id = id,
        photo = url
    )
}