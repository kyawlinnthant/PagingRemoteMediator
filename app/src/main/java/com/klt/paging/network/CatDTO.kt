package com.klt.paging.network

import kotlinx.serialization.Serializable

@Serializable
data class CatDTO(
    val id: String,
    val height: Int,
    val url: String,
    val width: Int
) {

}
