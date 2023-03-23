package com.mail.ann.activity

import com.mail.ann.Ann
import com.mail.ann.preferences.AppTheme
import com.mail.ann.preferences.GeneralSettingsManager
import com.mail.ann.preferences.SubTheme

data class MessageListActivityAppearance(
    val appTheme: AppTheme,
    val isShowUnifiedInbox: Boolean,
    val isShowMessageListStars: Boolean,
    val isShowCorrespondentNames: Boolean,
    val isMessageListSenderAboveSubject: Boolean,
    val isShowContactName: Boolean,
    val isChangeContactNameColor: Boolean,
    val isShowContactPicture: Boolean,
    val isColorizeMissingContactPictures: Boolean,
    val isUseBackgroundAsUnreadIndicator: Boolean,
    val contactNameColor: Int,
    val messageViewTheme: SubTheme,
    val messageListPreviewLines: Int,
    val splitViewMode: Ann.SplitViewMode,
    val fontSizeMessageListSubject: Int,
    val fontSizeMessageListSender: Int,
    val fontSizeMessageListDate: Int,
    val fontSizeMessageListPreview: Int,
    val fontSizeMessageViewSender: Int,
    val fontSizeMessageViewTo: Int,
    val fontSizeMessageViewCC: Int,
    val fontSizeMessageViewBCC: Int,
    val fontSizeMessageViewAdditionalHeaders: Int,
    val fontSizeMessageViewSubject: Int,
    val fontSizeMessageViewDate: Int,
    val fontSizeMessageViewContentAsPercent: Int
) {

    companion object {
        fun create(generalSettingsManager: GeneralSettingsManager): MessageListActivityAppearance {
            val settings = generalSettingsManager.getSettings()
            return MessageListActivityAppearance(
                appTheme = settings.appTheme,
                isShowUnifiedInbox = Ann.isShowUnifiedInbox,
                isShowMessageListStars = Ann.isShowMessageListStars,
                isShowCorrespondentNames = Ann.isShowCorrespondentNames,
                isMessageListSenderAboveSubject = Ann.isMessageListSenderAboveSubject,
                isShowContactName = Ann.isShowContactName,
                isChangeContactNameColor = Ann.isChangeContactNameColor,
                isShowContactPicture = Ann.isShowContactPicture,
                isColorizeMissingContactPictures = Ann.isColorizeMissingContactPictures,
                isUseBackgroundAsUnreadIndicator = Ann.isUseBackgroundAsUnreadIndicator,
                contactNameColor = Ann.contactNameColor,
                messageViewTheme = settings.messageViewTheme,
                messageListPreviewLines = Ann.messageListPreviewLines,
                splitViewMode = Ann.splitViewMode,
                fontSizeMessageListSubject = Ann.fontSizes.messageListSubject,
                fontSizeMessageListSender = Ann.fontSizes.messageListSender,
                fontSizeMessageListDate = Ann.fontSizes.messageListDate,
                fontSizeMessageListPreview = Ann.fontSizes.messageListPreview,
                fontSizeMessageViewSender = Ann.fontSizes.messageViewSender,
                fontSizeMessageViewTo = Ann.fontSizes.messageViewTo,
                fontSizeMessageViewCC = Ann.fontSizes.messageViewCC,
                fontSizeMessageViewBCC = Ann.fontSizes.messageViewBCC,
                fontSizeMessageViewAdditionalHeaders = Ann.fontSizes.messageViewAdditionalHeaders,
                fontSizeMessageViewSubject = Ann.fontSizes.messageViewSubject,
                fontSizeMessageViewDate = Ann.fontSizes.messageViewDate,
                fontSizeMessageViewContentAsPercent = Ann.fontSizes.messageViewContentAsPercent
            )
        }
    }
}
