package com.klt.paging.database

import androidx.room.Entity
import com.klt.paging.database.CatEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CatEntity(
    val id: String,
    val height: Int,
    val width: Int,
    val url: String,
){
    companion object{
        const val TABLE_NAME = "cat_table"
    }
}