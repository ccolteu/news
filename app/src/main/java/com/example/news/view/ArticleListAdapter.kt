package com.example.news.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.databinding.ArticleRowBinding
import com.example.news.model.Article

/**
 * Adapter binding the articles data to the RecyclerView UI component.
 */
class ArticlesAdapter(
    private val context: Context,
    private val articles: List<Article>,
    private val listener: OnArticleClickListener
) : RecyclerView.Adapter<ArticlesAdapter.ArticleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val viewsBinding = ArticleRowBinding.inflate(LayoutInflater.from(context))
        return ArticleHolder(viewsBinding)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
        holder.itemView.setOnClickListener { listener.onArticleClick(article) }
    }

    override fun getItemCount() = articles.size

    // SRP (Single Responsibility Principle): A class should have only one reason to change.
    // Here we only do one thing: bind data to views.
    class ArticleHolder(private val viewsBinding: ArticleRowBinding) : RecyclerView.ViewHolder(viewsBinding.root) {
        private var article: Article? = null

        fun bind(article: Article) {
            this.article = article
            viewsBinding.articleRowImage.apply {
                if (article.imageUrl.isNotEmpty()) {
                    this.visibility = View.VISIBLE
                    Glide.with(this)
                        .load(article.imageUrl)
                        .into(this)
                } else {
                    this.visibility = View.GONE
                    this.setImageDrawable(null)
                }
            }
            viewsBinding.articleRowSource.text = article.sourceName
            viewsBinding.articleRowDate.text = article.publishedDate
            viewsBinding.articleRowTitle.text = article.title
            viewsBinding.articleRowContent.apply {
                if (article.content.isEmpty()) {
                    this.visibility = View.GONE
                } else {
                    this.visibility = View.VISIBLE
                    this.text = article.content
                }
            }
        }
    }
}
