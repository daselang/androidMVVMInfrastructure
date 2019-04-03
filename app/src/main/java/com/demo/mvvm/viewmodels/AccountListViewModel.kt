package com.demo.mvvm.viewmodels

import com.airbnb.mvrx.*
import com.demo.mvvm.general.network.ApiManager
import com.demo.mvvm.general.core.CoreViewModel
import com.demo.mvvm.general.network.NetWorkRequestException
import com.demo.mvvm.general.network.NetworkScheduler
import com.demo.mvvm.general.network.ResponseData
import com.demo.mvvm.general.network.ResponseDataHandleMap
import com.demo.mvvm.models.Account
import com.demo.mvvm.orm.OrmDatabase
import com.demo.mvvm.orm.TokenInfoOrmService
import com.demo.mvvm.others.ConstantsCollection
import com.demo.mvvm.requests.AccountService


data class AccountListState(
    val request: Async<ResponseData<List<Account>>> = Uninitialized,
    val accountList: List<Account> = emptyList()
) : MvRxState

class AccountListViewModel(
    initialState: AccountListState,
    private val accountService: AccountService,
    private val tokenOrmService: TokenInfoOrmService
) :
    CoreViewModel<AccountListState>(initialState) {

    companion object : MvRxViewModelFactory<AccountListViewModel, AccountListState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: AccountListState
        ): AccountListViewModel {
            val service = ApiManager.instance.createProxyService(AccountService::class.java)
            val ormService = TokenInfoOrmService(OrmDatabase.getInstance(viewModelContext.activity).getTokenInfoDao())
            return AccountListViewModel(state, service, ormService)

        }
    }

    init {
        logStateChanges()
        //初始化时自动加载最初的数据，一般建议在 viewModel 的调用者里调用此方法，
        // 当然得看生命周期,viewModel 的生命周期在 onCreate 创建在 onDestroy 才结束，不会在activity 别的 lifeCycle回调方法中多次触发
        fetchAccountList()
    }

    fun fetchAccountList() = withState { it ->
        //正在加载时就不加载，即依次加载
        if (it.request is Loading) {
            return@withState
        }
        executeActions {
            val token = fetchToken()
            fetchAccountListByToken(token)
        }
    }
//
//    private fun loadToken(doAfterTokenLoaded: (token: String) -> Unit) {
//        Single.create<String> {
//            it.onSuccess(fetchToken())
//        }.toObservable()
//            .subscribeOn(Schedulers.io())
//            .doOnNext {
//                doAfterTokenLoaded(it)
//            }.subscribe().disposeOnClear()
//    }


    private fun fetchAccountListByToken(token: String) {

        accountService.fetchAccountList(token)
            .compose(NetworkScheduler.compose())
            .map(object : ResponseDataHandleMap<List<Account>>() {})
            .doOnSubscribe {
                setState { copy(request = Loading()) }
            }
            .doOnNext {
                setState { copy(request = Success(it), accountList = it.wsResult?.result ?: emptyList()) }
            }
            .doOnError {
                setState {
                    copy(request = Fail(NetWorkRequestException(it)))
                }
            }.doOnComplete {

            }
            .execute {
                copy(
                    request = request,
                    accountList = accountList
                )
            }
    }

    private fun fetchToken(): String {
        return tokenOrmService.getOneToken(ConstantsCollection.accountTokenType)?.token ?: ""
    }


}
