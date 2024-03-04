package com.klt.paging.model

import com.klt.paging.database.entity.KeyEntity
import com.klt.paging.database.entity.MockDataEntity
import com.klt.paging.mock.MockDto

fun MockDto.toEntity() = MockDataEntity(
    id = this.id,
    name = this.name
)

fun MockDto.toKey(
    nextPage: Int?, prevPage: Int?, currentPage: Int
) = KeyEntity(
    id = this.id,
    nextPage = nextPage,
    prevPage = prevPage,
    currentPage = currentPage,
)

fun MockDataEntity.toVo() = MockVo(
    id = this.id,
    name = this.name
)