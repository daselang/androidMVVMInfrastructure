package com.demo.mvvm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.fragmentViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.demo.mvvm.R
import com.demo.mvvm.components.loadingView
import com.demo.mvvm.controllers.AccountListEpoxyController
import com.demo.mvvm.general.core.CoreFragment
import com.demo.mvvm.general.eventbus.EventBusData
import com.demo.mvvm.models.Account
import com.demo.mvvm.models.AccountUser
import com.demo.mvvm.models.allIds
import com.demo.mvvm.others.RouterPath
import com.demo.mvvm.viewmodels.AccountListState
import com.demo.mvvm.viewmodels.AccountListViewModel
import com.demo.mvvm.views.accountListView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import kotlinx.android.synthetic.main.common_top_bar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@Route(path = RouterPath.fragment_account_list)
class AccountListFragment : CoreFragment() {

    private val viewModel by fragmentViewModel(AccountListViewModel::class)


    override fun getContextViewId(): Int = R.layout.fragment_account_list

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account_list, container, false).apply {
            recycleViewInstance = findViewById(R.id.mAccountListRecycleView)
            recycleViewInstance.setController(epoxyControllerInstance)
        }
        //配置当前fragment与滑动退出功能进行视图关联
        return attachToSwipeBack(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInitView()
        registerViewModelSubscribeEvent()
    }

    /**
     * 根据数据及状态进行 UI渲染
     */
    override fun fetchEpoxyController(): EpoxyController = AccountListEpoxyController(viewModel) { accountListState ->
        if (view == null || isRemoving) {
            return@AccountListEpoxyController
        }

        if (accountListState.request is Loading) {
            loadingView {
                id("loading${accountListState.accountList.size}")
            }
            return@AccountListEpoxyController
        }

        accountListState.accountList.forEach { account ->
            accountListView {
                id(account.id ?: "")
                data(account)
                clickListener { _ ->
                    selectAccountItem(account)
                }
            }
        }


    }

    override fun needRegisterEventBus(): Boolean = false


    /**
     * 注册状态监听回调事件
     */
    private fun registerViewModelSubscribeEvent() {

        viewModel.asyncSubscribe(asyncProp = AccountListState::request, onFail = {
            Toast.makeText(this.context?.applicationContext, it.message, Toast.LENGTH_SHORT).show()
        }, onSuccess = {
            it.wsResult?.result?.allIds()?.map(::print)
        })
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun testEventBus(event: EventBusData<AccountUser>) {
        when (event.code) {
            0x3 -> {
                Toast.makeText(this.context?.applicationContext, event.data.userName + "<><>Hello", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadInitView() {
        mTopBar.apply {
            setTitle(R.string.fragment_account_list_title)
            addLeftBackImageButton().setOnClickListener {
                executeActions {
                    launchUniversalPopUpStrategy()
                }
            }
        }
    }

    private fun selectAccountItem(account: Account) {
        QMUIDialog.MessageDialogBuilder(activity)
            .setTitle("选择账套")
            .setMessage("确定选择名为${account.accountName}\n(账套年度${account.accountYear})的账套？")
            .addAction("取消") { dialog, _ -> dialog.dismiss() }
            .addAction("确定") { dialog, _ ->
                executeActions {
                    dialog.dismiss()
                    ARouter.getInstance().build(RouterPath.activity_login)
                        .navigation()
                }
            }
            .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show()
    }

}
