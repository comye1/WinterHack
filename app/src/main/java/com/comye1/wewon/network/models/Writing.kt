package com.comye1.wewon.network.models

data class Writing(
    val id: Int = -1,
    val title: String,
    val writer: String,
    val site_url: String? = null,
    val content: String,
    val category: Int,
)

const val CATEGORY_POEM = 1
const val CATEGORY_NOVEL = 2
const val CATEGORY_ESSAY = 3