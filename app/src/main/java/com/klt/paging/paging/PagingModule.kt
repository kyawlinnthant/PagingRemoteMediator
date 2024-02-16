package com.klt.paging.paging

import androidx.paging.PagingConfig
import com.klt.paging.database.CatDatabase
import com.klt.paging.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

    @Provides
    @Singleton
    fun providePagingConfig() = PagingConfig(
        pageSize = 10,
        enablePlaceholders = false
    )

    @Provides
    @Singleton
    fun provideRemoteMediator(
        apiService: ApiService,
        database: CatDatabase
    ) = CatRemoteMediator(
        apiService = apiService,
        database = database
    )

    @Provides
    @Singleton
    fun providePagingSource(apiService: ApiService) = CatPagingSource(apiService = apiService)
}