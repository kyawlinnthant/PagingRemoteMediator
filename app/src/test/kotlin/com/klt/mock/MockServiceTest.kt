package com.klt.mock

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.size
import com.klt.paging.mock.MockServiceImpl
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


class MockServiceTest {
    private lateinit var service: MockServiceImpl

    @BeforeEach
    fun setup() {
        service = MockServiceImpl()
    }


    @Test
    @DisplayName("page 0 is not defined returns empty")
    fun `page zero`() = runTest {
        val result = service.getData(
            page = 0,
            size = 100
        )
        assertThat(result).isEmpty()
    }

    @Test
    @DisplayName("page 1 and size less than source returns required data of size")
    fun `page 1 with size less than source`() = runTest {
        val size = 15
        val result = service.getData(
            page = 1,
            size = size
        )
        assertThat(result).size().isEqualTo(size)
    }

    @Test
    @DisplayName("request more data than we can provide returns remaining data")
    fun `more data`() = runTest {
        val page = 2
        val size = 800
        val remaining = service.getTotal() - (page - 1) * size
        val result = service.getData(
            page = page,
            size = size
        )
        assertThat(result).size().isEqualTo(remaining)
    }

    @Test
    @DisplayName("request more data from start than we can provide returns all the data")
    fun `more data with page one`() = runTest {
        val result = service.getData(
            page = 1,
            size = 4000
        )
        assertThat(result).size().isEqualTo(service.getTotal())
    }

}