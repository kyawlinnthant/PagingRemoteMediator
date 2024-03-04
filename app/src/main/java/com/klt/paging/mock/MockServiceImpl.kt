package com.klt.paging.mock

import javax.inject.Inject

class MockServiceImpl @Inject constructor() : MockService {
    companion object {
        private val source = (1..1000).map {
            MockDto(
                id = it,
                name = "Name $it"
            )
        }
    }

    override suspend fun getData(page: Int, size: Int): List<MockDto> {
        return getBy(
            page = page,
            size = size
        )
    }

    private fun getBy(
        page: Int,
        size: Int
    ): List<MockDto> {
        val startIndex = if (page == 1) 0 else (page * size) - size5
        val endIndex = (startIndex + size) - 1
        if (page >= source.size || page < 1 || size >= source.size) return emptyList()
        return source.slice(
            indices = startIndex..endIndex
        )
    }
}