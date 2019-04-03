package com.demo.mvvm.others

class RouterPath {
    companion object {
        /**
         * 绑定于 activity 的登录模块路由名
         */
        const val activity_login: String = "/login/activity/accountLogIn"

        /**
         * fragment片段 账套登录路由名
         */
        const val fragment_account_login: String = "/login/fragment/accountLogIn"

        /**
         * fragment片段 账套列表路由名
         */
        const val fragment_account_list: String = "/login/fragment/accountList"
    }
}