package com.example.news.view.composable

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news.R
import com.example.news.view.WebViewActivity
import com.example.news.viewmodel.ArticleListActivityViewModel4

@Composable
fun ArticleList(
    viewModel4: ArticleListActivityViewModel4 = viewModel()
) {
    val articles by viewModel4.articles.observeAsState()
    val context = LocalContext.current
    articles?.let { articles ->
        LazyColumn(
            modifier = Modifier.background(
                color = Color(context.resources.getColor(R.color.card_background, null))
            )
        ) {
            itemsIndexed(
                items = articles
            ) { index, article ->
                ArticleCard(
                    article = article,
                    onClick = {
                        val intent = Intent(
                            context,
                            WebViewActivity::class.java
                        )
                        intent.putExtra(WebViewActivity.URL_EXTRA, article.url)
                        context.startActivity(intent)
                    })
            }
        }
    }
}
