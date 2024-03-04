package com.klt.paging.mock

import javax.inject.Inject

class MockServiceImpl @Inject constructor() : MockService {
    companion object {
        private val source = (1..100).map {
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

    override fun getTotal(): Int {
        return source.size
    }

    private fun getBy(
        page: Int,
        size: Int
    ): List<MockDto> {

        if (page < 1) return emptyList()

        if ((page * size) > source.size) {
            val length = page * size
            if (length - source.size <= size) {
                val start = if(page == 1) 0 else length - size
                val end = source.size - 1
                return source.slice(
                    indices = start..end
                )
            }
            return emptyList()
        }

        if (page == 1) {
            val start = 0
            val end = size - 1
            return source.slice(
                indices = start..end
            )
        }

        val startIndex = (page * size) - size
        val endIndex = (startIndex + size) - 1
        return source.slice(
            indices = startIndex..endIndex
        )
    }

}