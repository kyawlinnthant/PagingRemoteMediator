package com.klt.paging.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.klt.paging.database.CatEntity
import com.klt.paging.repository.CatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: CatRepository
) : ViewModel() {
    val cats: Flow<PagingData<CatEntity>> = repo.getCats()
        .distinctUntilChanged()
        .cachedIn(viewModelScope)

}