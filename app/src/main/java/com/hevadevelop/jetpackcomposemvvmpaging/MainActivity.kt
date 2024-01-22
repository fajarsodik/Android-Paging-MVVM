package com.hevadevelop.jetpackcomposemvvmpaging

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.hevadevelop.jetpackcomposemvvmpaging.ui.graph.NewsNavGraph
import com.hevadevelop.jetpackcomposemvvmpaging.ui.theme.JetpackComposeMVVMPagingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeMVVMPagingTheme {
                // A surface container using the 'background' color from the theme
                NewsNavGraph()
            }
        }
    }
}