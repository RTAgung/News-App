package com.example.newsapp.presentation.main.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityHomeBinding
import com.example.newsapp.presentation.adapter.ViewPagerAdapter
import com.example.newsapp.presentation.auth.AuthActivity
import com.example.newsapp.utils.viewmodelfactory.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private var myMenu: Menu? = null
    private var tabPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        checkIntentExtra()
        setupTabView()
        setupViewModel()
        setupAppbar()
    }

    private fun setupTabView() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = ViewPagerAdapter.TAB_TITLE[position]
        }.attach()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabPosition = position
                showSearchMenu(position == ViewPagerAdapter.TAB_NUM_HEADLINES)
            }
        })
    }


    private fun showSearchMenu(isShow: Boolean) {
        if (myMenu != null)
            myMenu?.findItem(R.id.action_search_menu)?.isVisible = isShow
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[HomeViewModel::class.java]
    }

    private fun setupAppbar() {
        val toolbar = binding.toolbar
        toolbar.title = getString(R.string.articles)
        setSupportActionBar(toolbar)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
                myMenu = menu
                showSearchMenu(tabPosition == ViewPagerAdapter.TAB_NUM_HEADLINES)
                setupSearchMenu(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_logout_menu -> openLogoutDialog()
                }
                return true
            }
        })
    }

    private fun setupSearchMenu(menu: Menu) {
        val searchViewItem = menu.findItem(R.id.action_search_menu)
        val searchView = searchViewItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if (query?.isNotBlank() == true) {
                    val headlineFragment =
                        viewPagerAdapter.createFragment(tabPosition) as HomeFragment
                    headlineFragment.refreshHeadlineData(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        searchView.setOnCloseListener {
            val headlineFragment =
                viewPagerAdapter.createFragment(tabPosition) as HomeFragment
            headlineFragment.refreshHeadlineData()
            false
        }
    }

    private fun openLogoutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.are_you_sure_to_logout))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.logout)) { dialog, _ ->
                logoutProcess()
                dialog.dismiss()
            }
            .show()
    }

    private fun logoutProcess() {
        viewModel.clearLogin()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun checkIntentExtra() {
        val emailExtra = intent.extras?.getString(EXTRA_EMAIL)
        if (emailExtra != null) {
            showSnackbar("Welcome $emailExtra")
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
    }
}