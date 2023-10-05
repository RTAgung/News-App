package com.example.newsapp.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.presentation.main.home.HomeFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private var headlineFragment: HomeFragment? = null
    private var bookmarkFragment: HomeFragment? = null

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                if (headlineFragment == null)
                    headlineFragment = HomeFragment.newInstance(position)
                return headlineFragment as HomeFragment
            }

            1 -> {
                if (bookmarkFragment == null)
                    bookmarkFragment = HomeFragment.newInstance(position)
                return bookmarkFragment as HomeFragment
            }
        }
        return HomeFragment.newInstance(position)
    }

    companion object {
        const val NUM_TABS = 2
        val TAB_TITLE = arrayOf("HEADLINES", "BOOKMARKS")
        const val TAB_NUM_HEADLINES = 0
        const val TAB_NUM_BOOKMARKS = 1
    }
}