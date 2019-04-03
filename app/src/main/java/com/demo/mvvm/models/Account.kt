package com.demo.mvvm.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 *
 *
 * 账套信息列表
 *
 *
 * @author xuzhongyu
 * @since 2018-02-07
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Account @JvmOverloads constructor(

    /**
     * 主键ID
     */
    var id: String? = null,
    /**
     * 和数据库名称对应，用来显示，数据库名称为了安全不予展示
     */
    var accountName: String? = null,
    var accountYear: Int = 0,
    var isOpenAccount: Boolean = false,
    var openDateTime: Long = 0,

    var isNegativeStock: Boolean = false,
    /**
     * 是否分仓核算
     */
    var isDifferentWarehouseCalc: Boolean = false,
    var isCostSharing: Boolean = false,
    var isStopUse: Boolean = false,
    /**
     * 所属账套用户ID(account_user id)
     */
    var ownerId: String? = null,
    /**
     * 创建日期DateTime
     */
    var createDateTime: Long = 0,

    /**
     * 当前数据最新的更新日期
     */
    var updateDateTime: Long = 0,

    /**
     * 创建当前条目信息的企业用户ID
     */
    var creatorId: String? = null,
    /**
     * 更新当前条目信息的企业用户ID
     */
    var updatorId: String? = null

) : Parcelable

typealias AccountList = List<Account>

/**
 * 给 AccountList 添加的扩展方法，根据 id查找单个对象
 */
fun AccountList.findById(id: String?) = firstOrNull { it.id == id }

/**
 * 给 AccountList 添加的扩展方法，返回所有 id的集合
 */
fun AccountList.allIds() = map { it.id }
