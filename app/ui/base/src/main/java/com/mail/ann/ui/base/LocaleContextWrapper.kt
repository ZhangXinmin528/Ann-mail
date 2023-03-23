package com.mail.ann.ui.base

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import com.mail.ann.ui.base.extensions.currentLocale
import java.util.Locale

/**
 * In combination with `AppCompatActivity` this will override the locale in the configuration.
 */
internal class LocaleContextWrapper(baseContext: Context, private val locale: Locale) : ContextWrapper(baseContext) {
    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        overrideConfiguration.currentLocale = locale
        return super.createConfigurationContext(overrideConfiguration)
    }
}
