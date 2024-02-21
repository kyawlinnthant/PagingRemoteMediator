package com.klt.paging.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.klt.paging.model.toVo
import com.klt.paging.repository.CatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: CatRepository
) : ViewModel() {
    val cats = repo.getCats()
        .flow
        .map { pagingData ->
            pagingData.map {
                it.toVo()
            }
        }
        .cachedIn(viewModelScope)

}