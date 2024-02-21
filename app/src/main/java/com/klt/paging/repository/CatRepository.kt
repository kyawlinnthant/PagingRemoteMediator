package com.klt.paging.repository

import androidx.paging.Pager
import com.klt.paging.database.CatEntity

interface CatRepository {
    fun getCats(): Pager<Int, CatEntity>
}