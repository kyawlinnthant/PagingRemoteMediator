package com.klt.paging.paging

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
    fun provideNetworkPagingSource(apiService: ApiService) =
        CatNetworkPagingSource(apiService = apiService)
}