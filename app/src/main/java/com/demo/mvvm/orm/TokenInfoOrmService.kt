package com.demo.mvvm.orm

class TokenInfoOrmService(
    private val dao: TokenInfoDao
) {
    /**
     * if request > 0 saved 表示保存成功，否则保存失败
     */
    fun saveToken(token: TokenInfo) = dao.saveToken(token) > 0

    fun getOneToken(type: Int): TokenInfo? = dao.getOneToken(type)

    fun deleteTokenByType(type: Int) = dao.deleteTokenByType(type)

}