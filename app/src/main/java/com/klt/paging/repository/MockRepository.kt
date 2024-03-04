package com.klt.paging.repository

import androidx.paging.Pager
import com.klt.paging.database.entity.MockDataEntity

interface MockRepository {
    fun getData(): Pager<Int, MockDataEntity>
}