package com.demo.mvvm.orm

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.demo.mvvm.others.ConstantsCollection

@Database(entities = [TokenInfo::class], version = 1, exportSchema = false)
abstract class OrmDatabase : RoomDatabase() {

    abstract fun getTokenInfoDao(): TokenInfoDao

    companion object {

        private var instance: OrmDatabase? = null

        private val lock = Any()
        fun getInstance(context: Context): OrmDatabase {
            synchronized(lock) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        OrmDatabase::class.java,
                        ConstantsCollection.databaseName.run {
                            "${this.replace(".", "")}.db"
                        })
                        .build()
                }
                return instance!!
            }
        }

    }
}