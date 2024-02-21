package com.klt.paging.model

import com.klt.paging.database.CatEntity
import com.klt.paging.database.RemoteKeyEntity
import com.klt.paging.network.CatDTO

fun CatDTO.toEntity(): CatEntity {
    return CatEntity(
        id = id, height = height, width = width, url = url
    )
}

fun CatDTO.toRemoteKey(
    nextPage: Int?, prevPage: Int?, currentPage: Int
): RemoteKeyEntity {
    return RemoteKeyEntity(
        id = id, nextPage = nextPage, prevPage = prevPage, currentPage = currentPage
    )
}

fun CatDTO.toVo(): CatVo {
    return CatVo(id = id, photo = url)
}

fun CatEntity.toVo() : CatVo{
    return CatVo(id = id, photo = url)
}