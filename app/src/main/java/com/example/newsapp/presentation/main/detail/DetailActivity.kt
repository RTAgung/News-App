package com.example.newsapp.presentation.main.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.ActivityDetailBinding
import com.example.newsapp.di.AppModule
import com.example.newsapp.di.DaggerAppComponent
import com.example.newsapp.utils.Helper
import com.example.newsapp.utils.extension.parcelable
import com.example.newsapp.utils.extension.showToast
import javax.inject.Inject


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel

    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentData = intent.parcelable<Article>(EXTRA_ARTICLE)!!

        setupViewModel()
        setupAppbar()
        loadData(intentData)
    }

    private fun loadData(intentData: Article) {
        viewModel.article.observe(this) {
            article = it
            setupView()
        }
        viewModel.getDetailArticle(intentData.title)
    }

    private fun setupViewModel() {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build().inject(this)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[DetailViewModel::class.java]
    }

    private fun setupView() {
        binding.apply {
            Glide
                .with(this@DetailActivity)
                .load(article.urlToImage)
                .error(R.drawable.baseline_broken_image_24)
                .into(ivArticlePhoto)
            tvArticleAuthor.text = article.author
            tvArticlePublishedAt.text =
                Helper.changeDateFormat(article.publishedAt)
            tvArticleTitle.text = article.title
            tvArticleDesc.text = article.description
            tvArticleContent.text = article.content

            val bookmarkDrawable =
                if (article.isBookmark) R.drawable.round_bookmark_24
                else R.drawable.round_bookmark_border_24

            fabBookmark.setImageDrawable(
                ContextCompat.getDrawable(
                    this@DetailActivity,
                    bookmarkDrawable
                )
            )

            fabBookmark.setOnClickListener {
                bookmarkProcess()
            }
        }
    }

    private fun bookmarkProcess() {
        if (article.isBookmark) viewModel.deleteBookmark(article.title)
        else viewModel.insertBookmark(article)
        val message =
            if (article.isBookmark) getString(R.string.bookmark_removed) else getString(R.string.bookmark_inserted)
        showToast(message)
    }

    private fun setupAppbar() {
        val toolbar = binding.toolbar
        toolbar.title = "Detail Article"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.open_web_menu -> openWebMenuAction()
                    android.R.id.home -> finish()
                }
                return true
            }
        })
    }

    private fun openWebMenuAction() {
        val implicit = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        startActivity(implicit)
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}