package com.mail.ann.backend.jmap

import com.mail.ann.backend.api.Backend
import com.mail.ann.backend.api.BackendPusher
import com.mail.ann.backend.api.BackendPusherCallback
import com.mail.ann.backend.api.BackendStorage
import com.mail.ann.backend.api.SyncConfig
import com.mail.ann.backend.api.SyncListener
import com.mail.ann.mail.BodyFactory
import com.mail.ann.mail.Flag
import com.mail.ann.mail.Message
import com.mail.ann.mail.Part
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import rs.ltt.jmap.client.JmapClient
import rs.ltt.jmap.client.http.BasicAuthHttpAuthentication
import rs.ltt.jmap.client.http.HttpAuthentication
import rs.ltt.jmap.common.method.call.core.EchoMethodCall

class JmapBackend(
    backendStorage: BackendStorage,
    okHttpClient: OkHttpClient,
    config: JmapConfig
) : Backend {
    private val httpAuthentication = config.toHttpAuthentication()
    private val jmapClient = createJmapClient(config, httpAuthentication)
    private val accountId = config.accountId
    private val commandRefreshFolderList = CommandRefreshFolderList(backendStorage, jmapClient, accountId)
    private val commandSync = CommandSync(backendStorage, jmapClient, okHttpClient, accountId, httpAuthentication)
    private val commandSetFlag = CommandSetFlag(jmapClient, accountId)
    private val commandDelete = CommandDelete(jmapClient, accountId)
    private val commandMove = CommandMove(jmapClient, accountId)
    private val commandUpload = CommandUpload(jmapClient, okHttpClient, httpAuthentication, accountId)
    override val supportsFlags = true
    override val supportsExpunge = false
    override val supportsMove = true
    override val supportsCopy = true
    override val supportsUpload = true
    override val supportsTrashFolder = true
    override val supportsSearchByDate = true
    override val isPushCapable = false // FIXME
    override val isDeleteMoveToTrash = true

    override fun refreshFolderList() {
        commandRefreshFolderList.refreshFolderList()
    }

    override fun sync(folder: String, syncConfig: SyncConfig, listener: SyncListener) {
        commandSync.sync(folder, syncConfig, listener)
    }

    override fun downloadMessage(syncConfig: SyncConfig, folderServerId: String, messageServerId: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun downloadMessageStructure(folderServerId: String, messageServerId: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun downloadCompleteMessage(folderServerId: String, messageServerId: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setFlag(folderServerId: String, messageServerIds: List<String>, flag: Flag, newState: Boolean) {
        commandSetFlag.setFlag(messageServerIds, flag, newState)
    }

    override fun markAllAsRead(folderServerId: String) {
        commandSetFlag.markAllAsRead(folderServerId)
    }

    override fun expunge(folderServerId: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun expungeMessages(folderServerId: String, messageServerIds: List<String>) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun deleteMessages(folderServerId: String, messageServerIds: List<String>) {
        commandDelete.deleteMessages(messageServerIds)
    }

    override fun deleteAllMessages(folderServerId: String) {
        commandDelete.deleteAllMessages(folderServerId)
    }

    override fun moveMessages(
        sourceFolderServerId: String,
        targetFolderServerId: String,
        messageServerIds: List<String>
    ): Map<String, String>? {
        commandMove.moveMessages(targetFolderServerId, messageServerIds)
        return messageServerIds.associateWith { it }
    }

    override fun moveMessagesAndMarkAsRead(sourceFolderServerId: String, targetFolderServerId: String, messageServerIds: List<String>): Map<String, String>? {
        commandMove.moveMessagesAndMarkAsRead(targetFolderServerId, messageServerIds)
        return messageServerIds.associateWith { it }
    }

    override fun copyMessages(
        sourceFolderServerId: String,
        targetFolderServerId: String,
        messageServerIds: List<String>
    ): Map<String, String>? {
        commandMove.copyMessages(targetFolderServerId, messageServerIds)
        return messageServerIds.associateWith { it }
    }

    override fun search(
        folderServerId: String,
        query: String?,
        requiredFlags: Set<Flag>?,
        forbiddenFlags: Set<Flag>?,
        performFullTextSearch: Boolean
    ): List<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun fetchPart(folderServerId: String, messageServerId: String, part: Part, bodyFactory: BodyFactory) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun findByMessageId(folderServerId: String, messageId: String): String? {
        return null
    }

    override fun uploadMessage(folderServerId: String, message: Message): String? {
        return commandUpload.uploadMessage(folderServerId, message)
    }

    override fun checkIncomingServerSettings() {
        jmapClient.call(EchoMethodCall()).get()
    }

    override fun sendMessage(message: Message) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun checkOutgoingServerSettings() {
        checkIncomingServerSettings()
    }

    override fun createPusher(callback: BackendPusherCallback): BackendPusher {
        throw UnsupportedOperationException("not implemented")
    }

    private fun JmapConfig.toHttpAuthentication(): HttpAuthentication {
        return BasicAuthHttpAuthentication(username, password)
    }

    private fun createJmapClient(jmapConfig: JmapConfig, httpAuthentication: HttpAuthentication): JmapClient {
        return if (jmapConfig.baseUrl == null) {
            JmapClient(httpAuthentication)
        } else {
            val baseHttpUrl = jmapConfig.baseUrl.toHttpUrlOrNull()
            JmapClient(httpAuthentication, baseHttpUrl)
        }
    }
}
