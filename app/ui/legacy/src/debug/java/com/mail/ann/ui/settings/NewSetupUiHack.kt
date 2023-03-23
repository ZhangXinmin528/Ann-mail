package com.mail.ann.ui.settings

import com.mail.ann.ui.R
import com.mail.ann.ui.settings.SettingsListFragment.SettingsListBuilder

@Deprecated("Remove this once we switch over to the new setup UI")
internal object NewSetupUiHack {
    fun addAction(builder: SettingsListBuilder) {
        builder.addAction(
            "Add account (NEW)",
            R.id.action_settingsListScreen_to_newAddAccountScreen,
            R.attr.iconSettingsAccountAdd
        )
    }
}
