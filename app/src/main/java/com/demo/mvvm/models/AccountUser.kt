package com.demo.mvvm.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class AccountUser @JvmOverloads constructor(
    var id: String? = null,
    var userName: String? = null,
    var userPwd: String? = null
) : Parcelable
typealias AccountUserList = List<AccountUser>

fun AccountUserList.findById(id: String?) = firstOrNull { it.id == id }