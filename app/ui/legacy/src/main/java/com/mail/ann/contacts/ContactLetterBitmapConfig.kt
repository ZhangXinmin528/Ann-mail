package com.mail.ann.contacts

import android.content.Context
import android.util.TypedValue
import android.view.ContextThemeWrapper
import com.mail.ann.K9
import com.mail.ann.ui.R
import com.mail.ann.ui.base.Theme
import com.mail.ann.ui.base.ThemeManager

class ContactLetterBitmapConfig(context: Context, themeManager: ThemeManager) {
    val hasDefaultBackgroundColor: Boolean = !K9.isColorizeMissingContactPictures
    val useDarkTheme = themeManager.appTheme == Theme.DARK
    val defaultBackgroundColor: Int

    init {
        defaultBackgroundColor = if (hasDefaultBackgroundColor) {
            val outValue = TypedValue()
            val themedContext = ContextThemeWrapper(context, themeManager.appThemeResourceId)
            themedContext.theme.resolveAttribute(R.attr.contactPictureFallbackDefaultBackgroundColor, outValue, true)
            outValue.data
        } else {
            0
        }
    }
}
