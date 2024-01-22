package com.hevadevelop.jetpackcomposemvvmpaging.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hevadevelop.jetpackcomposemvvmpaging.BuildConfig.api_key
import com.hevadevelop.jetpackcomposemvvmpaging.data.response.NewsResponse
import com.hevadevelop.jetpackcomposemvvmpaging.util.network.NewsApi
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(private val newsApi: NewsApi): PagingSource<Int, NewsResponse.Articles>() {
    override fun getRefreshKey(state: PagingState<Int, NewsResponse.Articles>): Int? =state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsResponse.Articles> {
        return try {
            val page = params.key ?: 1
            val response =
                newsApi.getNews(authorization = api_key, pageSize = 10, page = page, q = "business")

            LoadResult.Page(
                data = response.articles,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.articles.isEmpty()) null else page.plus(1)
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}