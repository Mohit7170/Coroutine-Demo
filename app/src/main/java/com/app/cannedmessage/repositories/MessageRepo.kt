package com.app.cannedmessage.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.app.cannedmessage.classes.AppDatabase
import com.app.cannedmessage.model.MessageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


interface MessageRepoAbstract {

    suspend fun getAllMessages(): LiveData<List<MessageModel>>

    suspend fun addMessage(message: MessageModel)

}

//class MessageRepo(private val messageDao: MessageDAO) : MessageRepoAbstract {
//
//    override suspend fun getAllMessages(): LiveData<List<MessageModel>> = messageDao.getAllMessages()
//
//    override suspend fun addMessage(message: MessageModel) = messageDao.adNewMessage(message)
//
//}
class MessageRepo() {

    companion object {

        private lateinit var appDatabase: AppDatabase

        private fun initaliseDb(context: Context): AppDatabase {
            return AppDatabase.getInstance(context)
        }

        fun addMessage(context: Context, message: MessageModel) {
            appDatabase = initaliseDb(context)

            CoroutineScope(Dispatchers.IO).launch {
                appDatabase.messagesDao().adNewMessage(message)
            }

        }

        fun deleteMessage(context: Context, message: MessageModel) {
            appDatabase = initaliseDb(context)

            CoroutineScope(Dispatchers.IO).launch {
                appDatabase.messagesDao().delete(message)
            }

        }

        fun getAllMessage(context: Context): LiveData<List<MessageModel>> {
            appDatabase = initaliseDb(context)
            return appDatabase.messagesDao().getAllMessages()
        }

    }

}