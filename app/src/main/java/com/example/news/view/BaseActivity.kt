package com.example.news.view

import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.news.R

/**
 * Base Activity class managing the progress bar.
 * Any subclass will inherit the progress bar functionality.
 */
abstract class BaseActivity : AppCompatActivity() {

    var progressBar: ProgressBar? = null

    override fun setContentView(layoutResID: Int) {
        val rootView = layoutInflater.inflate(R.layout.activity_base, null)
        progressBar = rootView.findViewById(R.id.progress_bar)
        val activityContentView = rootView.findViewById<FrameLayout>(R.id.activity_content)

        // makes activityContentView the container for all the Activities that extend this class
        layoutInflater.inflate(layoutResID, activityContentView, true)

        super.setContentView(rootView)
    }

    fun showProgressBar(show: Boolean) {
        when (show) {
            true -> progressBar?.visibility = View.VISIBLE
            false -> progressBar?.visibility = View.GONE
        }
    }
}
