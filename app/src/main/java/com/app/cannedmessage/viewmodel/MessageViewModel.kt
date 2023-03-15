package com.app.cannedmessage.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cannedmessage.model.MessageModel
import com.app.cannedmessage.repositories.MessageRepo

class MessageViewModel : ViewModel() {

    fun addMessage(context: Context, message: MessageModel) {
        MessageRepo.addMessage(context, message = message)
    }

    fun deleteMessage(context: Context, message: MessageModel) {
        MessageRepo.deleteMessage(context, message = message)
    }

    fun getAllMessage(context: Context): LiveData<List<MessageModel>> {
        return MessageRepo.getAllMessage(context)
    }


}