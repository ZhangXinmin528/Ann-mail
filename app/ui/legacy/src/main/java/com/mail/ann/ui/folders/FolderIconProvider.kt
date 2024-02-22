package com.mail.ann.ui.folders

import android.content.res.Resources
import android.util.TypedValue
import com.mail.ann.mailstore.FolderType
import com.mail.ann.ui.R
import com.mail.ann.mail.FolderType as LegacyFolderType

class FolderIconProvider(private val theme: Resources.Theme) {
    //收件箱
    private val iconFolderInboxResId: Int
    //发件箱
    private val iconFolderOutboxResId: Int
    //已发送
    private val iconFolderSentResId: Int
    //已删除
    private val iconFolderTrashResId: Int
    //草稿箱
    private val iconFolderDraftsResId: Int
    //已发送
    private val iconFolderArchiveResId: Int
    //垃圾邮件
    private val iconFolderSpamResId: Int
    var iconFolderResId: Int

    init {
        iconFolderInboxResId = getResId(R.attr.iconFolderInbox)
        iconFolderOutboxResId = getResId(R.attr.iconFolderOutbox)
        iconFolderSentResId = getResId(R.attr.iconFolderSent)
        iconFolderTrashResId = getResId(R.attr.iconFolderTrash)
        iconFolderDraftsResId = getResId(R.attr.iconFolderDrafts)
        iconFolderArchiveResId = getResId(R.attr.iconFolderArchive)
        iconFolderSpamResId = getResId(R.attr.iconFolderSpam)
        iconFolderResId = getResId(R.attr.iconFolder)
    }

    private fun getResId(resAttribute: Int): Int {
        val typedValue = TypedValue()
        val found = theme.resolveAttribute(resAttribute, typedValue, true)
        if (!found) {
            throw AssertionError("Couldn't find resource with attribute $resAttribute")
        }
        return typedValue.resourceId
    }

    fun getFolderIcon(type: FolderType): Int = when (type) {
        FolderType.INBOX -> iconFolderInboxResId
        FolderType.OUTBOX -> iconFolderOutboxResId
        FolderType.SENT -> iconFolderSentResId
        FolderType.TRASH -> iconFolderTrashResId
        FolderType.DRAFTS -> iconFolderDraftsResId
        FolderType.ARCHIVE -> iconFolderArchiveResId
        FolderType.SPAM -> iconFolderSpamResId
        else -> iconFolderResId
    }

    fun getFolderIcon(type: LegacyFolderType): Int = when (type) {
        LegacyFolderType.INBOX -> iconFolderInboxResId
        LegacyFolderType.OUTBOX -> iconFolderOutboxResId
        LegacyFolderType.SENT -> iconFolderSentResId
        LegacyFolderType.TRASH -> iconFolderTrashResId
        LegacyFolderType.DRAFTS -> iconFolderDraftsResId
        LegacyFolderType.ARCHIVE -> iconFolderArchiveResId
        LegacyFolderType.SPAM -> iconFolderSpamResId
        else -> iconFolderResId
    }
}
