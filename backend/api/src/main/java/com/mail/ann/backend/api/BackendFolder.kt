package com.mail.ann.backend.api

import com.mail.ann.mail.Flag
import com.mail.ann.mail.Message
import com.mail.ann.mail.MessageDownloadState
import java.util.Date

// FIXME: add documentation
interface BackendFolder {
    val name: String
    val visibleLimit: Int

    fun getMessageServerIds(): Set<String>
    fun getAllMessagesAndEffectiveDates(): Map<String, Long?>
    fun destroyMessages(messageServerIds: List<String>)
    fun clearAllMessages()
    fun getMoreMessages(): MoreMessages
    fun setMoreMessages(moreMessages: MoreMessages)
    fun setLastChecked(timestamp: Long)
    fun setStatus(status: String?)
    fun isMessagePresent(messageServerId: String): Boolean
    fun getMessageFlags(messageServerId: String): Set<Flag>
    fun setMessageFlag(messageServerId: String, flag: Flag, value: Boolean)
    fun saveMessage(message: Message, downloadState: MessageDownloadState)
    fun getOldestMessageDate(): Date?
    fun getFolderExtraString(name: String): String?
    fun setFolderExtraString(name: String, value: String?)
    fun getFolderExtraNumber(name: String): Long?
    fun setFolderExtraNumber(name: String, value: Long)

    enum class MoreMessages {
        UNKNOWN,
        FALSE,
        TRUE
    }
}
