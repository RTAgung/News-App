package com.example.newsapp.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.newsapp.data.source.local.LocalDataSource
import com.example.newsapp.data.source.local.entity.ArticleWithBookmark
import com.example.newsapp.data.source.local.entity.RemoteKeys
import com.example.newsapp.data.source.remote.RemoteDataSource
import com.example.newsapp.utils.extension.toArticleEntity

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val query: String
) : RemoteMediator<Int, ArticleWithBookmark>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleWithBookmark>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData =
                remoteDataSource.getArticles(
                    query = query,
                    page = page,
                    pageSize = state.config.pageSize
                )
            val listArticle = responseData.articles?.map { it.toArticleEntity() }
            val endOfPaginationReached = listArticle?.isEmpty() ?: true

            localDataSource.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.deleteRemoteKeys()
                    localDataSource.deleteAllArticles()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = listArticle?.map {
                    RemoteKeys(title = it.title, prevKey = prevKey, nextKey = nextKey)
                }!!
                localDataSource.insertKeys(keys)
                localDataSource.insertArticles(listArticle)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ArticleWithBookmark>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            localDataSource.getRemoteKeys(data.articleEntity.title)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleWithBookmark>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            localDataSource.getRemoteKeys(data.articleEntity.title)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ArticleWithBookmark>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.articleEntity?.title?.let { title ->
                localDataSource.getRemoteKeys(title)
            }
        }
    }

}