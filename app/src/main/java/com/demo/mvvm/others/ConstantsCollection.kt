package com.demo.mvvm.others

import com.demo.mvvm.BuildConfig

class ConstantsCollection {
    companion object {
        const val baseUrl: String = "https://your_Http_Restful_WebService_API_BaseUrl/"

        const val jwtToken: String = "jwtToken"

        const val accountTokenType = 1

        const val normalUserTokenType = 2

        const val superUserTokenType = 3

        /**
         * 分页步长，默认为10
         */
        const val pageSize = 10

        const val databaseName: String = BuildConfig.APPLICATION_ID

        const val clientTypeKey = "clientType"
        const val clientTypeValue = "android"

        /**
         * 当前程序版本 Key
         */
        const val clientAppVersionName = "appVersionName"
        /**
         * 当前程序名称 Key
         */
        const val clientAppName = "appName"

        const val deviceSystemVersion = "deviceSystemVersion"
        const val deviceSystemLanguage = "deviceSystemLanguage"
        const val deviceBrand = "deviceBrand"
        const val deviceModel = "deviceModel"
        /**
         * 设备 ID，一般为 IMEI 号
         */
        const val deviceId = "deviceId"
    }

}