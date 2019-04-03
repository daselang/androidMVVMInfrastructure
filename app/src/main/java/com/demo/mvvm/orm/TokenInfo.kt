package com.demo.mvvm.orm

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "tokenInfo")
data class TokenInfo @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "token")
    var token: String = "",
    @ColumnInfo(name = "type")
    var type: Int = 0
)