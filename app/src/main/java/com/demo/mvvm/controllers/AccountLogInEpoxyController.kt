package com.demo.mvvm.controllers

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.withState
import com.demo.mvvm.viewmodels.AccountLogInState
import com.demo.mvvm.viewmodels.AccountLogInViewModel

class AccountLogInEpoxyController(
    private val viewModel: AccountLogInViewModel,
    private val buildModelsCallback: EpoxyController.(accountLogInState: AccountLogInState) -> Unit
) : AsyncEpoxyController() {
    override fun buildModels() {
        withState(viewModel) { accountLogInState ->
            buildModelsCallback(accountLogInState)
        }
    }
}