package com.demo.mvvm.general.network

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetWorkRequestException(t: Throwable) : Throwable(
    when (t) {
        //超时
        is SocketTimeoutException -> {
            "连接服务器超时"
        }
        //连接错误
        is ConnectException -> {
            "连接服务器发生错误"
        }
        //未找到主机
        is UnknownHostException -> {
            "未找到服务器"
        }
        else -> {
            t.message
        }
    }
)