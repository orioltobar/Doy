package com.napptilians.diskdatasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.napptilians.diskdatasource.models.ChatMessageDbModel

@Dao
interface ChatDao {

    @Query("SELECT * FROM chatMessages WHERE chatId = :chatId ORDER BY timeStamp ASC")
    suspend fun getChatMessages(chatId: String): List<ChatMessageDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessageToChat(message: ChatMessageDbModel)
}