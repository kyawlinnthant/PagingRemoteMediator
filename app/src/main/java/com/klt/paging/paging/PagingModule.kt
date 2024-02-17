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
        pageSize = Constant.LOAD_SIZE,
        enablePlaceholders = false,
//        prefetchDistance = 30,
//        initialLoadSize = 10,
//        maxSize = Constant.LOAD_SIZE + ( Constant.LOAD_SIZE * 2 ),
//        jumpThreshold = 1,
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
    fun provideNetworkPagingSource(apiService: ApiService) = CatNetworkPagingSource(apiService = apiService)
}