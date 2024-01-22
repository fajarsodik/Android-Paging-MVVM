package com.hevadevelop.jetpackcomposemvvmpaging.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import timber.log.Timber
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val articlesData = viewModel.getNews().collectAsLazyPagingItems()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "The News") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("search_news") {
                            popUpTo("main_screen")
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            LazyColumn {
                items(count = articlesData.itemCount) {
                    val instant = Instant.parse(articlesData[it]!!.publishedAt)
                    val offsetDateTime = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC)
                    val formattedDate =
                        offsetDateTime.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))
                    val author = articlesData[it]?.author
                    val title = articlesData[it]?.title
                    val url_news = articlesData[it]?.url
                    Card(
                        onClick = {
                            url_news?.let { data ->
                                val newsData =
                                    URLEncoder.encode(data, StandardCharsets.UTF_8.toString())
                                navController.navigate("detail_news_screen/$newsData")
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Row(modifier = Modifier.padding(all = 16.dp)) {
                            Column(modifier = Modifier.weight(2f)) {
                                author?.let {
                                    Text(text = author)
                                }
                                title?.let {
                                    Text(
                                        text = it,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(text = formattedDate)
                            }
                            AsyncImage(
                                model = articlesData[it]?.urlToImage,
                                contentDescription = "${articlesData[it]?.description}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(100.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }

                articlesData.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Column(
                                    modifier = Modifier.fillParentMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = articlesData.loadState.refresh as LoadState.Error
                            Timber.e("errornya di akhir: ${error.endOfPaginationReached} || ${error.error} || $error")
                            item {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Oops, something went wrong",
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.weight(1f),
                                        maxLines = 2
                                    )
                                    OutlinedButton(onClick = { retry() }) {
                                        Text(text = "retry")
                                    }
                                }
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val error = articlesData.loadState.append as LoadState.Error
                            if (error.error.localizedMessage!! == "HTTP 426") {
                                Timber.e("error from server $error")
                            } else if (error.endOfPaginationReached) {
                                Timber.e("Finish pagination")
                            } else {
                                item {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Oops, something went wrong",
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 2
                                        )
                                        OutlinedButton(onClick = { retry() }) {
                                            Text(text = "retry")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }
    }
}