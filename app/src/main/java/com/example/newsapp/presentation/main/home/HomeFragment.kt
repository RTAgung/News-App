package com.example.newsapp.presentation.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.presentation.adapter.ArticleAdapterCallback
import com.example.newsapp.presentation.adapter.ArticleBookmarkAdapter
import com.example.newsapp.presentation.adapter.ArticleHeadlineAdapter
import com.example.newsapp.presentation.adapter.LoadingStateAdapter
import com.example.newsapp.presentation.adapter.ViewPagerAdapter
import com.example.newsapp.presentation.main.detail.DetailActivity
import com.example.newsapp.utils.viewmodelfactory.ViewModelFactory

class HomeFragment : Fragment() {
    private var numTab: Int? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var viewModel: HomeViewModel? = null

    private lateinit var headlineAdapter: ArticleHeadlineAdapter
    private lateinit var bookmarkAdapter: ArticleBookmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numTab = it.getInt(ARG_NUM_TAB)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAdapter()
        loadData()
    }

    private fun loadData() {
        when (numTab) {
            ViewPagerAdapter.TAB_NUM_HEADLINES -> loadHeadlineData()
            ViewPagerAdapter.TAB_NUM_BOOKMARKS -> loadBookmarkData()
        }
    }

    private val adapterCallback = object : ArticleAdapterCallback {
        override fun onItemClick(article: Article) {
            moveToDetail(article)
        }

        override fun onBookmarkClick(article: Article) {
            bookmarkProcess(article)
        }
    }

    private fun bookmarkProcess(article: Article) {
        if (article.isBookmark) viewModel?.deleteBookmark(article.title)
        else viewModel?.insertBookmark(article)
    }

    private fun setupAdapter() {
        when (numTab) {
            ViewPagerAdapter.TAB_NUM_HEADLINES -> {
                headlineAdapter = ArticleHeadlineAdapter(adapterCallback)
                binding.apply {
                    rvHome.layoutManager = LinearLayoutManager(requireActivity())
                    rvHome.adapter = headlineAdapter.withLoadStateFooter(
                        footer = LoadingStateAdapter {
                            headlineAdapter.retry()
                        }
                    )
                }
            }

            ViewPagerAdapter.TAB_NUM_BOOKMARKS -> {
                bookmarkAdapter = ArticleBookmarkAdapter(adapterCallback)
                binding.apply {
                    rvHome.layoutManager = LinearLayoutManager(requireActivity())
                    rvHome.adapter = bookmarkAdapter
                }
            }
        }
    }

    private fun loadBookmarkData() {
        viewModel?.listArticle?.observe(viewLifecycleOwner) {
            bookmarkAdapter.submitList(it)
        }
        bookmarkAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvHome.scrollToPosition(0)
            }
        })
    }

    private fun loadHeadlineData() {
        viewModel?.pagingListArticle?.observe(viewLifecycleOwner) {
            headlineAdapter.submitData(lifecycle, it)
        }
        refreshHeadlineData()
    }

    fun refreshHeadlineData(query: String = "") {
        if (viewModel != null)
            viewModel?.getArticles(query)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireActivity())
        )[HomeViewModel::class.java]
    }

    private fun moveToDetail(article: Article) {
        val detailIntent = Intent(requireActivity(), DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_ARTICLE, article)
        activity?.startActivity(detailIntent)
    }

    companion object {
        private const val ARG_NUM_TAB = "num_tab"

        @JvmStatic
        fun newInstance(numTab: Int) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_NUM_TAB, numTab)
                }
            }
    }
}