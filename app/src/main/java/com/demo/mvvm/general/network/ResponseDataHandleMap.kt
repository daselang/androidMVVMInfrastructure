package com.demo.mvvm.general.network

import io.reactivex.functions.Function

abstract class ResponseDataHandleMap<T> : Function<ResponseData<T>, ResponseData<T>> {
    override fun apply(t: ResponseData<T>): ResponseData<T> {
        val errorMsg = t.wsResult?.error?.errMsg
        val errorCode = t.wsResult?.error?.errCode
        if (errorMsg != null) {
            throw ServiceException(errorCode, errorMsg)
        }
        return t
    }
}