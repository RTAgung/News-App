package com.example.newsapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val title: String,
    val prevKey: Int?,
    val nextKey: Int?
)