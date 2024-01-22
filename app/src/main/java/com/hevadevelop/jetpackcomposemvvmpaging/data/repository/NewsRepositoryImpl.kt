package com.hevadevelop.jetpackcomposemvvmpaging.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hevadevelop.jetpackcomposemvvmpaging.data.repository.paging.NewsPagingSource
import com.hevadevelop.jetpackcomposemvvmpaging.util.network.NewsApi
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val api: NewsApi) {

    fun getNews() = Pager(
        config = PagingConfig(pageSize = 10, prefetchDistance = 2),
        pagingSourceFactory = {
            NewsPagingSource(api)
        }
    ).flow
}