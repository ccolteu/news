package com.example.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.model.Repository3

class ArticleListActivityViewModel3Factory(private val repository3: Repository3) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleListActivityViewModel3(repository3) as T
    }
}
