package com.demo.mvvm.activities

import android.os.Bundle
import com.blankj.utilcode.util.PermissionUtils
import com.demo.mvvm.R
import com.demo.mvvm.general.core.CoreActivity
import com.demo.mvvm.models.Account
import com.demo.mvvm.others.RouterPath
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle

class LogInActivity : CoreActivity() {

    override fun getContextViewId(): Int = R.id.mContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadInitView()
        requestPermissions()

    }


    private fun loadInitView() {
        loadRootFragmentView(RouterPath.fragment_account_login, Account(id = "12345"))
    }

    /**
     * 打开 app第一个 activity时要发起权限请求，常用在启动页
     */
    private fun requestPermissions() {
        //获取未经同意的权限列表,过滤掉已经经过用户同意的，无需用户再次确认
        val unGrantedPerms =
            PermissionUtils.getPermissions().filter { !PermissionUtils.isGranted(it) }.toTypedArray()
        if (unGrantedPerms.isEmpty()) {
            return
        }

        val launchPerms = RxPermissions(this)
            .requestEach(
                *unGrantedPerms
            )
            .bindToLifecycle(this)
            .subscribe { permission ->
                when {
                    permission.granted -> {
                    }
                    permission.shouldShowRequestPermissionRationale -> {

                    }
                    else -> {
                    }
                }
            }
    }
}
