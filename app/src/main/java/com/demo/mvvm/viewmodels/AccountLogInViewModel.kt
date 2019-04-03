package com.demo.mvvm.viewmodels

import com.airbnb.mvrx.*
import com.blankj.utilcode.util.ObjectUtils
import com.demo.mvvm.general.network.ApiManager
import com.demo.mvvm.general.core.CoreViewModel
import com.demo.mvvm.general.network.NetWorkRequestException
import com.demo.mvvm.general.network.NetworkScheduler
import com.demo.mvvm.general.network.ResponseData
import com.demo.mvvm.general.network.ResponseDataHandleMap
import com.demo.mvvm.models.AccountUser
import com.demo.mvvm.orm.OrmDatabase
import com.demo.mvvm.orm.TokenInfo
import com.demo.mvvm.orm.TokenInfoOrmService
import com.demo.mvvm.others.ConstantsCollection
import com.demo.mvvm.others.ObjectsSerializedMapHelper
import com.demo.mvvm.requests.AccountService

data class AccountLogInState(
    val accountUser: AccountUser = AccountUser("", "", ""),
    val request: Async<ResponseData<String>> = Uninitialized
) : MvRxState

class AccountLogInViewModel(
    initialState: AccountLogInState,
    private val accountService: AccountService,
    private val tokenOrmService: TokenInfoOrmService
) :
    CoreViewModel<AccountLogInState>(initialState) {

    companion object : MvRxViewModelFactory<AccountLogInViewModel, AccountLogInState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: AccountLogInState
        ): AccountLogInViewModel {
            val service = ApiManager.instance.createProxyService(AccountService::class.java)
            val ormService = TokenInfoOrmService(OrmDatabase.getInstance(viewModelContext.activity).getTokenInfoDao())
            return AccountLogInViewModel(state, service, ormService)
        }
    }

    init {
        logStateChanges()
    }

    fun login(
        accountUser: AccountUser,
        doBeforeLogin: () -> Unit,
        doAfterLoginFail: () -> Unit,
        doAfterLoginSuccess: (token: String?) -> Unit
    ) = withState { it ->
        //正在登录时就不要再执行重复登录操作
        if (it.request is Loading) {
            return@withState
        }

        if (ObjectUtils.isEmpty(accountUser.userName)) {
            setState {
                copy(request = Fail(Exception("用户名不能为空")))
            }
            return@withState
        }
        if (ObjectUtils.isEmpty(accountUser.userPwd)) {
            setState {
                copy(request = Fail(Exception("密码不能为空")))
            }
            return@withState
        }

        val userMap = ObjectsSerializedMapHelper<AccountUser>().getObjectMap(accountUser).toMap()
        accountService.login(userMap)
            .compose(NetworkScheduler.compose())
            .map(object : ResponseDataHandleMap<String>() {})
            .doOnSubscribe {
                doBeforeLogin()
                setState { copy(accountUser = accountUser, request = Loading()) }
            }
            .doOnNext {
                setState { copy(request = Success(it)) }
                doAfterLoginSuccess(it.wsResult?.result)
            }
            .doOnError {
                doAfterLoginFail()
                setState {
                    copy(request = Fail(NetWorkRequestException(it)))
                }
            }.doOnComplete {
            }
            .execute {
                copy(
                    request = request,
                    accountUser = accountUser
                )
            }
    }

    /**
     * @param token 要保存的 account user 登录的 token
     * @param doAfterSaved 保存成功后执行的回调函数，常用于UI跳转
     */
    fun saveAccountToken(
        token: String? = "",
        doAfterSaved: () -> Unit
    ) {
        executeActions {
            val type = ConstantsCollection.accountTokenType
            tokenOrmService.deleteTokenByType(type)
            val saved = tokenOrmService.saveToken(TokenInfo(token = token ?: "", type = type))
            if (saved) {
                doAfterSaved()
            }
        }
    }

    fun resetState() {
        withState {
            setState {
                copy(request = Uninitialized)
            }
        }
    }


}
