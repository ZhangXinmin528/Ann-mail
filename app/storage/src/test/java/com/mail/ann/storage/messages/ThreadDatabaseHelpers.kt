package com.mail.ann.storage.messages

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.mail.ann.helper.getLongOrNull
import com.mail.ann.helper.map

fun SQLiteDatabase.createThread(
    messageId: Long,
    root: Long? = null,
    parent: Long? = null
): Long {
    val values = ContentValues().apply {
        put("message_id", messageId)
        put("root", root)
        put("parent", parent)
    }

    return insert("threads", null, values)
}

fun SQLiteDatabase.readThreads(): List<ThreadEntry> {
    return rawQuery("SELECT * FROM threads", null).use { cursor ->
        cursor.map {
            ThreadEntry(
                id = cursor.getLongOrNull("id"),
                messageId = cursor.getLongOrNull("message_id"),
                root = cursor.getLongOrNull("root"),
                parent = cursor.getLongOrNull("parent")
            )
        }
    }
}

data class ThreadEntry(
    val id: Long?,
    val messageId: Long?,
    val root: Long?,
    val parent: Long?
)
