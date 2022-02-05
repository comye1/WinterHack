package com.comye1.wewon.network.models.dictionary

import com.squareup.moshi.Json

data class Channel(
    val title: String,
    val link: String,
    val description: String,
    val lastbuilddate: String,
    val total: Long,
    val start: Long,
    val num: Long,
    val item: List<Item>
)

data class Item(
    @Json(name = "target_code")
    val targetCode: Long,

    val word: String,

    @Json(name = "sup_no")
    val supNo: Long,

    val pos: String,
    @Json(name = "sense_no")
    val sense: Sense
)

data class Sense(
    val definition: String,
    val type: String
)