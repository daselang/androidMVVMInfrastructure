package com.demo.mvvm.general.network

class ServiceException(errorCode: String?, errorMsg: String?) : Exception(
    if (errorCode == null) (errorMsg ?: "") else (errorMsg ?: "").run {
        "$this($errorCode)"
    }
)
