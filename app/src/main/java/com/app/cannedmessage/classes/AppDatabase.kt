package com.app.cannedmessage.classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.cannedmessage.interfaces.dao.MessageDAO
import com.app.cannedmessage.model.MessageModel


@Database(entities = [MessageModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessageDAO

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, "MyAllDB")
            .fallbackToDestructiveMigration()
            .build()
    })

}