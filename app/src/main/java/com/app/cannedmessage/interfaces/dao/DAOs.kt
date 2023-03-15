package com.app.cannedmessage.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.cannedmessage.model.MessageModel


@Dao
interface MessageDAO {
    @Query("select * from MyAllDB")
    fun getAllMessages(): LiveData<List<MessageModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun adNewMessage(vararg item: MessageModel)

    @Delete
    suspend fun delete(message: MessageModel)
}