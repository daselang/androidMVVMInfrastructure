package com.demo.mvvm.general.network

import com.google.gson.annotations.SerializedName

data class WsError(
    var errCode: String? = null,
    var errMsg: String? = null
)

data class WsResult<T>(
    var result: T? = null,
    var error: WsError? = null
)

data class ResponseData<T>(
    @SerializedName("WSResult")
    var wsResult: WsResult<T>? = null
)

data class PaginationResult<T>(
    var count: Long = -1,
    var data: T? = null
)