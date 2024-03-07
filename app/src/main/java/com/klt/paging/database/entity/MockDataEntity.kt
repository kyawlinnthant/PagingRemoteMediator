package com.klt.paging.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = MockDataEntity.TB_NAME
)
data class MockDataEntity(
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val name : String
){
    companion object{
        const val TB_NAME = "mock_table"
    }
}
