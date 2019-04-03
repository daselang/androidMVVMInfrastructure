package com.demo.mvvm.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.demo.mvvm.R
import com.demo.mvvm.components.LoadingDialog
import com.demo.mvvm.controllers.AccountLogInEpoxyController
import com.demo.mvvm.general.core.CoreFragment
import com.demo.mvvm.general.eventbus.EventBusData
import com.demo.mvvm.models.Account
import com.demo.mvvm.models.AccountUser
import com.demo.mvvm.others.RouterPath
import com.demo.mvvm.others.asSequence
import com.demo.mvvm.viewmodels.AccountLogInState
import com.demo.mvvm.viewmodels.AccountLogInViewModel
import com.demo.mvvm.views.AccountLogInView
import com.demo.mvvm.views.accountLogInView
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.common_top_bar.*
import kotlinx.android.synthetic.main.fragment_account_login.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@Route(path = RouterPath.fragment_account_login)
class AccountLogInFragment : CoreFragment() {

    private val viewModel by fragmentViewModel(AccountLogInViewModel::class)

//    @JvmField
//    @Autowired(name = MvRx.KEY_ARG)
//    var params: Account? = null

    private val params: Account by args()


    override fun getContextViewId(): Int = R.layout.fragment_account_login

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_account_login, container, false).apply {
            recycleViewInstance = findViewById(R.id.mAccountLoginRecycleView)
            recycleViewInstance.setController(epoxyControllerInstance)
        }
        //配置当前fragment与滑动退出功能进行视图关联
        return attachToSwipeBack(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setSwipeBackEnable(false)
        loadInitView()
        registerMvRxSubscribeEvent()
    }

    /**
     * 根据数据及状态进行 UI渲染
     */
    override fun fetchEpoxyController(): EpoxyController = AccountLogInEpoxyController(viewModel) { accountLogInState ->
        if (view == null || isRemoving) {
            return@AccountLogInEpoxyController
        }
        if (accountLogInState.request is Loading) {
            accountLogInView {
                id("accountUserLogin")
                accountUser(accountLogInState.accountUser)
            }
        } else {
            accountLogInView {
                id("accountUserLogin")
                accountUser(accountLogInState.accountUser)
                onLoginClicked(doOnLogInBtnClick())
            }
        }

    }


    override fun needRegisterEventBus(): Boolean = false


    /**
     * 注册MvRx架构状态监听回调事件
     */
    private fun registerMvRxSubscribeEvent() {

        viewModel.asyncSubscribe(AccountLogInState::request, onFail = {
            RxToast.error(context?.applicationContext!!, it.message ?: "", Toast.LENGTH_SHORT, true).show()
            viewModel.resetState()
        }, onSuccess = {
        })
    }


    private fun doOnLogInBtnClick(): View.OnClickListener? = View.OnClickListener {
        Toast.makeText(context?.applicationContext, params.id, Toast.LENGTH_SHORT).show()

        val accountUser = mAccountLoginRecycleView.asSequence().filterIsInstance<AccountLogInView>().first().getUIData()
//        EventBusUtils.post(accountUser, 0x3, true)
        //创建 loadView
        var dialog: Dialog? = null

        viewModel.login(accountUser = accountUser, doAfterLoginSuccess = { token ->
            dialog?.dismiss()
            viewModel.saveAccountToken(token) {
                popToNextFragment(RouterPath.fragment_account_list)
            }
        }, doBeforeLogin = {
            executeActions {
                dialog = activity?.let { LoadingDialog(activity!!).create() ?: null }
                dialog?.show()
            }

        }, doAfterLoginFail = {
            dialog?.dismiss()
        })

    }


    /**
     * 进入页面时初始化 UI的必要显示元素
     */
    private fun loadInitView() {
        mTopBar.apply {
            setTitle(R.string.fragment_account_login_title)
            addLeftBackImageButton().apply {
                setOnClickListener {
                    executeActions {
                        launchUniversalPopUpStrategy()
                    }

                }
            }
        }
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun testEventBus(event: EventBusData<AccountUser>) {
        when (event.code) {
            0x3 -> {
                Toast.makeText(this.context?.applicationContext, event.data.userName, Toast.LENGTH_SHORT).show()
            }
        }
    }


}
