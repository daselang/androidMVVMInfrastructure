package com.demo.mvvm.general.network

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.NetworkUtils
import com.demo.mvvm.others.ConstantsCollection
import com.demo.mvvm.BuildConfig
import com.demo.mvvm.others.SystemHelper
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiManager private constructor() {

    private var retrofit: Retrofit

    /**
     * 创建 API 访问服务代理实例
     */
    fun <T> createProxyService(service: Class<T>): T {
        return this.retrofit.create(service)
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiManager() }
    }

    init {
        val commonParamsInterceptor = CommonParamsInterceptor.Builder()
            .addHeaderParam(ConstantsCollection.clientTypeKey, ConstantsCollection.clientTypeValue)
            .addHeaderParam(ConstantsCollection.clientAppName, AppUtils.getAppName())
            .addHeaderParam(ConstantsCollection.clientAppVersionName, AppUtils.getAppVersionName())
            .addHeaderParam(ConstantsCollection.deviceSystemVersion, DeviceUtils.getSDKVersionName())
            .addHeaderParam(ConstantsCollection.deviceSystemLanguage, SystemHelper.getSystemLanguage())
            .addHeaderParam(ConstantsCollection.deviceBrand, SystemHelper.getDeviceBrand())
            .addHeaderParam(ConstantsCollection.deviceModel, DeviceUtils.getModel())
            .build()



        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        //发起请求前检查网络连接可用性
        okHttpClientBuilder.addInterceptor {
            if (!NetworkUtils.isConnected()) {
                throw Exception("网络未连接，请打开并连接可用网络")
            }
            val request = it.request()
            it.proceed(request)
        }

        okHttpClientBuilder.addInterceptor(commonParamsInterceptor)

        if (BuildConfig.DEBUG) {
            val okHttpProfilerInterceptor = OkHttpProfilerInterceptor()
            okHttpClientBuilder.addInterceptor(okHttpProfilerInterceptor)
        }

        val initRetrofit = Retrofit.Builder()
            .baseUrl(ConstantsCollection.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClientBuilder.build()).build()

        retrofit = initRetrofit
    }
}