package com.klt.paging.network

import com.klt.paging.database.CatEntity
import com.klt.paging.database.RemoteKeyEntity
import com.klt.paging.model.Cat
import kotlinx.serialization.Serializable

@Serializable
data class CatDTO(
    val id: String,
    val height: Int,
    val url: String,
    val width: Int
) {
    fun toVo() = Cat(id = id, photo = url)
    fun toEntity() = CatEntity(id = id, height = height, width = width, url = url)
    fun toRemoteKey(nextPage: Int?, prevPage: Int?, currentPage: Int) =
        RemoteKeyEntity(
            id = id,
            nextPage = nextPage,
            prevPage = prevPage,
            currentPage = currentPage
        )
}
