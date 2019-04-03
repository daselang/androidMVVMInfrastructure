package com.demo.mvvm.general.eventbus

import org.greenrobot.eventbus.EventBus

class EventBusUtils {
    companion object {

        /**
         * 注册事件订阅体，如果已经注册则不重复注册
         */
        fun register(subscriber: Any) {
            //防止重复注册
            if (EventBus.getDefault().isRegistered(subscriber)) {
                return
            }
            EventBus.getDefault().register(subscriber)
        }

        fun unregister(subscriber: Any) {
            EventBus.getDefault().unregister(subscriber)
        }

        /**
         * 范型 post，最终的数据实例由调用者决定
         * @param data 业务数据对象
         * @param code 事件类别编码，方便业务回调方法做区分
         * @param sticky 使用粘性事件机制，默认 false 不使用
         */
        fun <T> post(data: T, code: Int = -1, sticky: Boolean = false) {
            val event = EventBusData(code, data)
            if (sticky) {
                EventBus.getDefault().postSticky(event)
            } else {
                EventBus.getDefault().post(event)
            }
        }
    }
}