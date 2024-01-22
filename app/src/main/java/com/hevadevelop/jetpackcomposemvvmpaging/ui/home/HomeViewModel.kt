package com.hevadevelop.jetpackcomposemvvmpaging.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hevadevelop.jetpackcomposemvvmpaging.data.repository.NewsRepositoryImpl
import com.hevadevelop.jetpackcomposemvvmpaging.data.response.NewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: NewsRepositoryImpl) : ViewModel() {

    fun getNews(): Flow<PagingData<NewsResponse.Articles>> =
        repository.getNews().cachedIn(viewModelScope)
}