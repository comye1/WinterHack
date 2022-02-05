package com.comye1.wewon.network.models

data class ScrapBody(
    val username: String,
    val literatureTitle: String,
)


data class ScrapResponse(
    val success:Boolean,
    val data:String,
)