package com.mail.ann.ui.settings.general

import androidx.preference.PreferenceDataStore
import com.mail.ann.Ann
import com.mail.ann.job.K9JobManager
import com.mail.ann.preferences.AppTheme
import com.mail.ann.preferences.GeneralSettingsManager
import com.mail.ann.preferences.SubTheme
import com.mail.ann.ui.base.AppLanguageManager

class GeneralSettingsDataStore(
    private val jobManager: K9JobManager,
    private val appLanguageManager: AppLanguageManager,
    private val generalSettingsManager: GeneralSettingsManager
) : PreferenceDataStore() {

    private var skipSaveSettings = false

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return when (key) {
            "fixed_message_view_theme" -> generalSettingsManager.getSettings().fixedMessageViewTheme
            "animations" -> Ann.isShowAnimations
            "show_unified_inbox" -> Ann.isShowUnifiedInbox
            "show_starred_count" -> Ann.isShowStarredCount
            "messagelist_stars" -> Ann.isShowMessageListStars
            "messagelist_show_correspondent_names" -> Ann.isShowCorrespondentNames
            "messagelist_sender_above_subject" -> Ann.isMessageListSenderAboveSubject
            "messagelist_show_contact_name" -> Ann.isShowContactName
            "messagelist_change_contact_name_color" -> Ann.isChangeContactNameColor
            "messagelist_show_contact_picture" -> Ann.isShowContactPicture
            "messagelist_colorize_missing_contact_pictures" -> Ann.isColorizeMissingContactPictures
            "messagelist_background_as_unread_indicator" -> Ann.isUseBackgroundAsUnreadIndicator
            "threaded_view" -> Ann.isThreadedViewEnabled
            "messageview_fixedwidth_font" -> Ann.isUseMessageViewFixedWidthFont
            "messageview_autofit_width" -> Ann.isAutoFitWidth
            "messageview_return_to_list" -> Ann.isMessageViewReturnToList
            "messageview_show_next" -> Ann.isMessageViewShowNext
            "quiet_time_enabled" -> Ann.isQuietTimeEnabled
            "disable_notifications_during_quiet_time" -> !Ann.isNotificationDuringQuietTimeEnabled
            "privacy_hide_useragent" -> Ann.isHideUserAgent
            "privacy_hide_timezone" -> Ann.isHideTimeZone
            "debug_logging" -> Ann.isDebugLoggingEnabled
            "sensitive_logging" -> Ann.isSensitiveDebugLoggingEnabled
            else -> defValue
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        when (key) {
            "fixed_message_view_theme" -> setFixedMessageViewTheme(value)
            "animations" -> Ann.isShowAnimations = value
            "show_unified_inbox" -> Ann.isShowUnifiedInbox = value
            "show_starred_count" -> Ann.isShowStarredCount = value
            "messagelist_stars" -> Ann.isShowMessageListStars = value
            "messagelist_show_correspondent_names" -> Ann.isShowCorrespondentNames = value
            "messagelist_sender_above_subject" -> Ann.isMessageListSenderAboveSubject = value
            "messagelist_show_contact_name" -> Ann.isShowContactName = value
            "messagelist_change_contact_name_color" -> Ann.isChangeContactNameColor = value
            "messagelist_show_contact_picture" -> Ann.isShowContactPicture = value
            "messagelist_colorize_missing_contact_pictures" -> Ann.isColorizeMissingContactPictures = value
            "messagelist_background_as_unread_indicator" -> Ann.isUseBackgroundAsUnreadIndicator = value
            "threaded_view" -> Ann.isThreadedViewEnabled = value
            "messageview_fixedwidth_font" -> Ann.isUseMessageViewFixedWidthFont = value
            "messageview_autofit_width" -> Ann.isAutoFitWidth = value
            "messageview_return_to_list" -> Ann.isMessageViewReturnToList = value
            "messageview_show_next" -> Ann.isMessageViewShowNext = value
            "quiet_time_enabled" -> Ann.isQuietTimeEnabled = value
            "disable_notifications_during_quiet_time" -> Ann.isNotificationDuringQuietTimeEnabled = !value
            "privacy_hide_useragent" -> Ann.isHideUserAgent = value
            "privacy_hide_timezone" -> Ann.isHideTimeZone = value
            "debug_logging" -> Ann.isDebugLoggingEnabled = value
            "sensitive_logging" -> Ann.isSensitiveDebugLoggingEnabled = value
            else -> return
        }

        saveSettings()
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return when (key) {
            "messagelist_contact_name_color" -> Ann.contactNameColor
            "message_view_content_font_slider" -> Ann.fontSizes.messageViewContentAsPercent
            else -> defValue
        }
    }

    override fun putInt(key: String?, value: Int) {
        when (key) {
            "messagelist_contact_name_color" -> Ann.contactNameColor = value
            "message_view_content_font_slider" -> Ann.fontSizes.messageViewContentAsPercent = value
            else -> return
        }

        saveSettings()
    }

    override fun getString(key: String, defValue: String?): String? {
        return when (key) {
            "language" -> appLanguageManager.getAppLanguage()
            "theme" -> appThemeToString(generalSettingsManager.getSettings().appTheme)
            "message_compose_theme" -> subThemeToString(generalSettingsManager.getSettings().messageComposeTheme)
            "messageViewTheme" -> subThemeToString(generalSettingsManager.getSettings().messageViewTheme)
            "messagelist_preview_lines" -> Ann.messageListPreviewLines.toString()
            "splitview_mode" -> Ann.splitViewMode.name
            "notification_quick_delete" -> Ann.notificationQuickDeleteBehaviour.name
            "lock_screen_notification_visibility" -> Ann.lockScreenNotificationVisibility.name
            "background_ops" -> Ann.backgroundOps.name
            "quiet_time_starts" -> Ann.quietTimeStarts
            "quiet_time_ends" -> Ann.quietTimeEnds
            "account_name_font" -> Ann.fontSizes.accountName.toString()
            "account_description_font" -> Ann.fontSizes.accountDescription.toString()
            "folder_name_font" -> Ann.fontSizes.folderName.toString()
            "folder_status_font" -> Ann.fontSizes.folderStatus.toString()
            "message_list_subject_font" -> Ann.fontSizes.messageListSubject.toString()
            "message_list_sender_font" -> Ann.fontSizes.messageListSender.toString()
            "message_list_date_font" -> Ann.fontSizes.messageListDate.toString()
            "message_list_preview_font" -> Ann.fontSizes.messageListPreview.toString()
            "message_view_sender_font" -> Ann.fontSizes.messageViewSender.toString()
            "message_view_to_font" -> Ann.fontSizes.messageViewTo.toString()
            "message_view_cc_font" -> Ann.fontSizes.messageViewCC.toString()
            "message_view_bcc_font" -> Ann.fontSizes.messageViewBCC.toString()
            "message_view_subject_font" -> Ann.fontSizes.messageViewSubject.toString()
            "message_view_date_font" -> Ann.fontSizes.messageViewDate.toString()
            "message_view_additional_headers_font" -> Ann.fontSizes.messageViewAdditionalHeaders.toString()
            "message_compose_input_font" -> Ann.fontSizes.messageComposeInput.toString()
            else -> defValue
        }
    }

    override fun putString(key: String, value: String?) {
        if (value == null) return

        when (key) {
            "language" -> appLanguageManager.setAppLanguage(value)
            "theme" -> setTheme(value)
            "message_compose_theme" -> setMessageComposeTheme(value)
            "messageViewTheme" -> setMessageViewTheme(value)
            "messagelist_preview_lines" -> Ann.messageListPreviewLines = value.toInt()
            "splitview_mode" -> Ann.splitViewMode = Ann.SplitViewMode.valueOf(value)
            "notification_quick_delete" -> {
                Ann.notificationQuickDeleteBehaviour = Ann.NotificationQuickDelete.valueOf(value)
            }
            "lock_screen_notification_visibility" -> {
                Ann.lockScreenNotificationVisibility = Ann.LockScreenNotificationVisibility.valueOf(value)
            }
            "background_ops" -> setBackgroundOps(value)
            "quiet_time_starts" -> Ann.quietTimeStarts = value
            "quiet_time_ends" -> Ann.quietTimeEnds = value
            "account_name_font" -> Ann.fontSizes.accountName = value.toInt()
            "account_description_font" -> Ann.fontSizes.accountDescription = value.toInt()
            "folder_name_font" -> Ann.fontSizes.folderName = value.toInt()
            "folder_status_font" -> Ann.fontSizes.folderStatus = value.toInt()
            "message_list_subject_font" -> Ann.fontSizes.messageListSubject = value.toInt()
            "message_list_sender_font" -> Ann.fontSizes.messageListSender = value.toInt()
            "message_list_date_font" -> Ann.fontSizes.messageListDate = value.toInt()
            "message_list_preview_font" -> Ann.fontSizes.messageListPreview = value.toInt()
            "message_view_sender_font" -> Ann.fontSizes.messageViewSender = value.toInt()
            "message_view_to_font" -> Ann.fontSizes.messageViewTo = value.toInt()
            "message_view_cc_font" -> Ann.fontSizes.messageViewCC = value.toInt()
            "message_view_bcc_font" -> Ann.fontSizes.messageViewBCC = value.toInt()
            "message_view_subject_font" -> Ann.fontSizes.messageViewSubject = value.toInt()
            "message_view_date_font" -> Ann.fontSizes.messageViewDate = value.toInt()
            "message_view_additional_headers_font" -> Ann.fontSizes.messageViewAdditionalHeaders = value.toInt()
            "message_compose_input_font" -> Ann.fontSizes.messageComposeInput = value.toInt()
            else -> return
        }

        saveSettings()
    }

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? {
        return when (key) {
            "confirm_actions" -> {
                mutableSetOf<String>().apply {
                    if (Ann.isConfirmDelete) add("delete")
                    if (Ann.isConfirmDeleteStarred) add("delete_starred")
                    if (Ann.isConfirmDeleteFromNotification) add("delete_notif")
                    if (Ann.isConfirmSpam) add("spam")
                    if (Ann.isConfirmDiscardMessage) add("discard")
                    if (Ann.isConfirmMarkAllRead) add("mark_all_read")
                }
            }
            "messageview_visible_refile_actions" -> {
                mutableSetOf<String>().apply {
                    if (Ann.isMessageViewDeleteActionVisible) add("delete")
                    if (Ann.isMessageViewArchiveActionVisible) add("archive")
                    if (Ann.isMessageViewMoveActionVisible) add("move")
                    if (Ann.isMessageViewCopyActionVisible) add("copy")
                    if (Ann.isMessageViewSpamActionVisible) add("spam")
                }
            }
            "volume_navigation" -> {
                mutableSetOf<String>().apply {
                    if (Ann.isUseVolumeKeysForNavigation) add("message")
                    if (Ann.isUseVolumeKeysForListNavigation) add("list")
                }
            }
            else -> defValues
        }
    }

    override fun putStringSet(key: String, values: MutableSet<String>?) {
        val checkedValues = values ?: emptySet<String>()
        when (key) {
            "confirm_actions" -> {
                Ann.isConfirmDelete = "delete" in checkedValues
                Ann.isConfirmDeleteStarred = "delete_starred" in checkedValues
                Ann.isConfirmDeleteFromNotification = "delete_notif" in checkedValues
                Ann.isConfirmSpam = "spam" in checkedValues
                Ann.isConfirmDiscardMessage = "discard" in checkedValues
                Ann.isConfirmMarkAllRead = "mark_all_read" in checkedValues
            }
            "messageview_visible_refile_actions" -> {
                Ann.isMessageViewDeleteActionVisible = "delete" in checkedValues
                Ann.isMessageViewArchiveActionVisible = "archive" in checkedValues
                Ann.isMessageViewMoveActionVisible = "move" in checkedValues
                Ann.isMessageViewCopyActionVisible = "copy" in checkedValues
                Ann.isMessageViewSpamActionVisible = "spam" in checkedValues
            }
            "volume_navigation" -> {
                Ann.isUseVolumeKeysForNavigation = "message" in checkedValues
                Ann.isUseVolumeKeysForListNavigation = "list" in checkedValues
            }
            else -> return
        }

        saveSettings()
    }

    private fun saveSettings() {
        if (skipSaveSettings) {
            skipSaveSettings = false
        } else {
            Ann.saveSettingsAsync()
        }
    }

    private fun setTheme(value: String) {
        skipSaveSettings = true
        generalSettingsManager.setAppTheme(stringToAppTheme(value))
    }

    private fun setMessageComposeTheme(subThemeString: String) {
        skipSaveSettings = true
        generalSettingsManager.setMessageComposeTheme(stringToSubTheme(subThemeString))
    }

    private fun setMessageViewTheme(subThemeString: String) {
        skipSaveSettings = true
        generalSettingsManager.setMessageViewTheme(stringToSubTheme(subThemeString))
    }

    private fun setFixedMessageViewTheme(fixedMessageViewTheme: Boolean) {
        skipSaveSettings = true
        generalSettingsManager.setFixedMessageViewTheme(fixedMessageViewTheme)
    }

    private fun appThemeToString(theme: AppTheme) = when (theme) {
        AppTheme.LIGHT -> "light"
        AppTheme.DARK -> "dark"
        AppTheme.FOLLOW_SYSTEM -> "follow_system"
    }

    private fun subThemeToString(theme: SubTheme) = when (theme) {
        SubTheme.LIGHT -> "light"
        SubTheme.DARK -> "dark"
        SubTheme.USE_GLOBAL -> "global"
    }

    private fun stringToAppTheme(theme: String?) = when (theme) {
        "light" -> AppTheme.LIGHT
        "dark" -> AppTheme.DARK
        "follow_system" -> AppTheme.FOLLOW_SYSTEM
        else -> throw AssertionError()
    }

    private fun stringToSubTheme(theme: String?) = when (theme) {
        "light" -> SubTheme.LIGHT
        "dark" -> SubTheme.DARK
        "global" -> SubTheme.USE_GLOBAL
        else -> throw AssertionError()
    }

    private fun setBackgroundOps(value: String) {
        val newBackgroundOps = Ann.BACKGROUND_OPS.valueOf(value)
        if (newBackgroundOps != Ann.backgroundOps) {
            Ann.backgroundOps = newBackgroundOps
            jobManager.scheduleAllMailJobs()
        }
    }
}
