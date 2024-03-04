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
    private lateinit var service : MockServiceImpl

    @BeforeEach
    fun setup() {
        service = MockServiceImpl()
    }

    @Test
    @DisplayName("correct page and size returns correct data")
    fun `test one`() = runTest{
        val first10 = service.getData(
            page = 1,
            size = 3
        )
        println(first10)
        assertThat(first10).size().isEqualTo(3)
        val second10 = service.getData(
            page = 2,
            size = 3
        )
        println(second10)
        assertThat(second10).size().isEqualTo(3)
    }

    @Test
    @DisplayName("incorrect page and size returns empty list")
    fun `test two`() = runTest{
        val largerPage = service.getData(
            page = 1001,
            size = 10
        )
        assertThat(largerPage).isEmpty()
        val smallerPage = service.getData(
            page = 0,
            size = 10
        )
        assertThat(smallerPage).isEmpty()
        val largerSize = service.getData(
            page = 1,
            size = 1001
        )
        assertThat(largerSize).isEmpty()
    }

}