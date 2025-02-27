package com.mail.ann.ui

import com.mail.ann.ui.base.ThemeProvider

// TODO: Move this class and the theme resources to the main app module
class K9ThemeProvider : ThemeProvider {
    override val appThemeResourceId = R.style.Theme_K9_DayNight
    override val appLightThemeResourceId = R.style.Theme_K9_Light
    override val appDarkThemeResourceId = R.style.Theme_K9_Dark
    override val dialogThemeResourceId = R.style.Theme_K9_Dialog_DayNight
    override val translucentDialogThemeResourceId = R.style.Theme_K9_Dialog_Translucent_DayNight
}
