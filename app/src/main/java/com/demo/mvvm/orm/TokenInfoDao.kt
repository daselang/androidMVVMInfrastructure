package com.demo.mvvm.orm

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface TokenInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveToken(token: TokenInfo): Long

    @Query(value = "select * from tokenInfo where type = :type limit 1")
    fun getOneToken(type: Int): TokenInfo

    @Query(value = "delete from tokenInfo where type = :type")
    fun deleteTokenByType(type: Int): Int

}