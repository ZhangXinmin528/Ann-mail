package com.mail.ann.storage.messages

import android.content.ContentValues
import com.mail.ann.mailstore.LockableDatabase

internal class UpdateMessageOperations(private val lockableDatabase: LockableDatabase) {

    fun setNewMessageState(folderId: Long, messageServerId: String, newMessage: Boolean) {
        lockableDatabase.execute(false) { database ->
            val values = ContentValues().apply {
                put("new_message", if (newMessage) 1 else 0)
            }

            database.update(
                "messages",
                values,
                "folder_id = ? AND uid = ?",
                arrayOf(folderId.toString(), messageServerId)
            )
        }
    }

    fun clearNewMessageState() {
        lockableDatabase.execute(false) { database ->
            database.execSQL("UPDATE messages SET new_message = 0")
        }
    }
}
