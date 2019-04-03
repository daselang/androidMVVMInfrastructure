package com.demo.mvvm.requests


import com.demo.mvvm.general.network.ResponseData
import com.demo.mvvm.models.AccountList
import com.demo.mvvm.others.ConstantsCollection
import io.reactivex.Observable
import retrofit2.http.*

interface AccountService {

    /**
     * @param accountUser AccountUser类的实例转 map
     * @see AccountUser
     */
    @FormUrlEncoded
    @POST("api/accountUser/login.json")
    @JvmSuppressWildcards
    fun login(@FieldMap accountUser: Map<String, Any>): Observable<ResponseData<String>>

    @GET("api/accountUser/accounts.json")
    fun fetchAccountList(@Header(ConstantsCollection.jwtToken) token: String?): Observable<ResponseData<AccountList>>
}