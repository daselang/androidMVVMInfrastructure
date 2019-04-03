package com.demo.mvvm.controllers

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.withState
import com.demo.mvvm.viewmodels.AccountListState
import com.demo.mvvm.viewmodels.AccountListViewModel

class AccountListEpoxyController(
    private val viewModel: AccountListViewModel,
    private val buildModelsCallback: EpoxyController.(accountListState: AccountListState) -> Unit
) : AsyncEpoxyController() {
    override fun buildModels() {
        withState(viewModel) { accountListState ->
            buildModelsCallback(accountListState)
        }
    }
}