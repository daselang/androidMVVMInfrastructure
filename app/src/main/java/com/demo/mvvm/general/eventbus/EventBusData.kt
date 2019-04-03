package com.demo.mvvm.general.eventbus

data class EventBusData<T>(
    /**
     * 事件编码，用以区分事件类型
     */
    val code: Int = -1,
    /**
     * 调用者发送的具体数据对象
     */
    var data: T
)