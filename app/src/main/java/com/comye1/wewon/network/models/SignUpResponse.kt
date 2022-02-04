package com.comye1.wewon.network.models

data class SignUpResponse(
    val message: String,
    val status: String,
)

const val STATUS_SUCCESS = "200"
const val STATUS_DUPLICATED = "400"
