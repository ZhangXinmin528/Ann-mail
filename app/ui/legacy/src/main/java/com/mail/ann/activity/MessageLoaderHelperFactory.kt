package com.mail.ann.activity

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.loader.app.LoaderManager
import com.mail.ann.activity.MessageLoaderHelper.MessageLoaderCallbacks
import com.mail.ann.mailstore.MessageViewInfoExtractorFactory
import com.mail.ann.ui.helper.HtmlSettingsProvider

class MessageLoaderHelperFactory(
    private val messageViewInfoExtractorFactory: MessageViewInfoExtractorFactory,
    private val htmlSettingsProvider: HtmlSettingsProvider
) {
    fun createForMessageView(
        context: Context,
        loaderManager: LoaderManager,
        fragmentManager: FragmentManager,
        callback: MessageLoaderCallbacks
    ): MessageLoaderHelper {
        val htmlSettings = htmlSettingsProvider.createForMessageView()
        val messageViewInfoExtractor = messageViewInfoExtractorFactory.create(htmlSettings)
        return MessageLoaderHelper(context, loaderManager, fragmentManager, callback, messageViewInfoExtractor)
    }

    fun createForMessageCompose(
        context: Context,
        loaderManager: LoaderManager,
        fragmentManager: FragmentManager,
        callback: MessageLoaderCallbacks
    ): MessageLoaderHelper {
        val htmlSettings = htmlSettingsProvider.createForMessageCompose()
        val messageViewInfoExtractor = messageViewInfoExtractorFactory.create(htmlSettings)
        return MessageLoaderHelper(context, loaderManager, fragmentManager, callback, messageViewInfoExtractor)
    }
}
