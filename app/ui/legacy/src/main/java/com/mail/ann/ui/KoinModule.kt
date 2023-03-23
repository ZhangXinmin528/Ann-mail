package com.mail.ann.ui

import android.content.Context
import com.mail.ann.ui.base.ThemeProvider
import com.mail.ann.ui.helper.DisplayHtmlUiFactory
import com.mail.ann.ui.helper.HtmlSettingsProvider
import com.mail.ann.ui.helper.HtmlToSpanned
import com.mail.ann.ui.helper.SizeFormatter
import com.mail.ann.ui.messageview.LinkTextHandler
import com.mail.ann.ui.share.ShareIntentBuilder
import org.koin.dsl.module

val uiModule = module {
    single { HtmlToSpanned() }
    single<ThemeProvider> { K9ThemeProvider() }
    single { HtmlSettingsProvider(get()) }
    single { DisplayHtmlUiFactory(get()) }
    factory { (context: Context) -> SizeFormatter(context.resources) }
    factory { ShareIntentBuilder(resourceProvider = get(), textPartFinder = get(), quoteDateFormatter = get()) }
    factory { LinkTextHandler(context = get(), clipboardManager = get()) }
}
