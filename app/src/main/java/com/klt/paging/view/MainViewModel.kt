package com.klt.paging.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.klt.paging.model.toVo
import com.klt.paging.repository.MockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MockRepository
) : ViewModel() {

    val data = repo.getData()
        .flow
        .map { pagingData ->
            pagingData.map {
                it.toVo()
            }
        }
        .cachedIn(viewModelScope)

}
