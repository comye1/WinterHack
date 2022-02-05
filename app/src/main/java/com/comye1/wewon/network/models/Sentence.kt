package com.comye1.wewon.network.models

data class Sentence(
    val sentence: String,
    val title:String,
    val username:String,
    val sentenceId:Int,
)

data class SentenceResponse(
    val id: Int,
    val sentence: String,
    val title: String
)
