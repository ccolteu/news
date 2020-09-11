package com.example.news.view

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.MyApplication
import com.example.news.R
import com.example.news.model.Article
import com.example.news.util.InjectorUtil
import com.example.news.view.WebViewActivity.Companion.URL_EXTRA
import com.example.news.viewmodel.ArticleListActivityViewModel2
import com.example.news.viewmodel.ArticleListActivityViewModel2.Companion.TOP_HEADLINES
import com.example.news.viewmodel.ArticleListActivityViewModel2Factory
import kotlinx.android.synthetic.main.activity_article_list.*

class ArticleListActivity2 : BaseActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var searchMenu: MenuItem

    private val listener = object : OnArticleClickListener {
        override fun onArticleClick(article: Article) {
            val intent = Intent(this@ArticleListActivity2, WebViewActivity::class.java)
            intent.putExtra(URL_EXTRA, article.url)
            this@ArticleListActivity2.startActivity(intent)
        }
    }

    private val viewModel2: ArticleListActivityViewModel2 by viewModels {
        val repository2 = InjectorUtil.provideRepository2(application as MyApplication)
        ArticleListActivityViewModel2Factory(repository2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)
        initUI()
        initObservers()
        saveQueryToRecentSuggestions(TOP_HEADLINES)
    }

    private fun initUI() {
        linearLayoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE ->
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            else -> LinearLayoutManager(this)
        }
        articles_recyclerview.layoutManager = linearLayoutManager
    }

    private fun initObservers() {
        lifecycle.addObserver(viewModel2)

        // init data observers and update the UI
        viewModel2.articles.observe(this, { articles ->
            articles_recyclerview.adapter = ArticlesAdapter(articles, listener)
        })
        viewModel2.errorMessage.observe(this, { msg ->
            msg?.let {
                showAlertDialog(msg)
            }
        })
        viewModel2.query.observe(this, { query ->
            supportActionBar?.title = query
        })
        viewModel2.showProgress.observe(this, { show ->
            showProgressBar(show)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_article_list_menu, menu)
        searchMenu = menu.findItem(R.id.activity_article_list_menu_search)
        val searchView = searchMenu.actionView as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(true)
        }
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == Intent.ACTION_SEARCH) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                saveQueryToRecentSuggestions(query)
                search(query)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.activity_article_list_menu_clear_history -> {
                SearchRecentSuggestions(
                    this,
                    ArticleSearchSuggestionProvider.AUTHORITY,
                    ArticleSearchSuggestionProvider.MODE
                )
                    .clearHistory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun search(query: String) {
        query?.let {
            this@ArticleListActivity2.hideKeyboard()
            searchMenu.collapseActionView()
            viewModel2.setQuery(query)
        }
    }

    private fun saveQueryToRecentSuggestions(query: String) {
        SearchRecentSuggestions(
            this,
            ArticleSearchSuggestionProvider.AUTHORITY,
            ArticleSearchSuggestionProvider.MODE
        )
            .saveRecentQuery(query, null)
    }

    private fun showAlertDialog(message: String?) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setMessage(message ?: "Something went wrong...")
            .setCancelable(false)
            .setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
                run {
                    viewModel2.showError(null)
                }
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Error")
        alert.show()
    }
}