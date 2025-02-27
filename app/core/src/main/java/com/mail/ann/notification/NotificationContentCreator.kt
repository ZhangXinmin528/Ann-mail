package com.mail.ann.notification

import android.content.Context
import android.text.SpannableStringBuilder
import com.mail.ann.Account
import com.mail.ann.Ann
import com.mail.ann.helper.Contacts
import com.mail.ann.helper.MessageHelper
import com.mail.ann.mail.Message
import com.mail.ann.mailstore.LocalMessage
import com.mail.ann.message.extractors.PreviewResult.PreviewType

internal class NotificationContentCreator(
    private val context: Context,
    private val resourceProvider: NotificationResourceProvider
) {
    fun createFromMessage(account: Account, message: LocalMessage): NotificationContent {
        val sender = getMessageSender(account, message)

        return NotificationContent(
            messageReference = message.makeMessageReference(),
            sender = getMessageSenderForDisplay(sender),
            subject = getMessageSubject(message),
            preview = getMessagePreview(message),
            summary = buildMessageSummary(sender, getMessageSubject(message))
        )
    }

    private fun getMessagePreview(message: LocalMessage): CharSequence {
        val snippet = getPreview(message)
        if (message.subject.isNullOrEmpty() && snippet != null) {
            return snippet
        }

        return SpannableStringBuilder().apply {
            val displaySubject = getMessageSubject(message)
            append(displaySubject)

            if (snippet != null) {
                append('\n')
                append(snippet)
            }
        }
    }

    private fun getPreview(message: LocalMessage): String? {
        val previewType = message.previewType ?: error("previewType == null")
        return when (previewType) {
            PreviewType.NONE, PreviewType.ERROR -> null
            PreviewType.TEXT -> message.preview
            PreviewType.ENCRYPTED -> resourceProvider.previewEncrypted()
        }
    }

    private fun buildMessageSummary(sender: String?, subject: String): CharSequence {
        return if (sender == null) {
            subject
        } else {
            SpannableStringBuilder().apply {
                append(sender)
                append(" ")
                append(subject)
            }
        }
    }

    private fun getMessageSubject(message: Message): String {
        val subject = message.subject.orEmpty()
        return subject.ifEmpty { resourceProvider.noSubject() }
    }

    private fun getMessageSender(account: Account, message: Message): String? {
        val contacts = if (Ann.isShowContactName) Contacts.getInstance(context) else null
        var isSelf = false

        val fromAddresses = message.from
        if (!fromAddresses.isNullOrEmpty()) {
            isSelf = account.isAnIdentity(fromAddresses)
            if (!isSelf) {
                return MessageHelper.toFriendly(fromAddresses.first(), contacts).toString()
            }
        }

        if (isSelf) {
            // show To: if the message was sent from me
            val recipients = message.getRecipients(Message.RecipientType.TO)
            if (!recipients.isNullOrEmpty()) {
                val recipientDisplayName = MessageHelper.toFriendly(recipients.first(), contacts).toString()
                return resourceProvider.recipientDisplayName(recipientDisplayName)
            }
        }

        return null
    }

    private fun getMessageSenderForDisplay(sender: String?): String {
        return sender ?: resourceProvider.noSender()
    }
}
