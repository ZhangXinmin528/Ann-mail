package com.mail.ann

import com.mail.ann.account.accountModule
import com.mail.ann.activity.activityModule
import com.mail.ann.autodiscovery.providersxml.autodiscoveryProvidersXmlModule
import com.mail.ann.contacts.contactsModule
import com.mail.ann.fragment.fragmentModule
import com.mail.ann.ui.account.accountUiModule
import com.mail.ann.ui.base.uiBaseModule
import com.mail.ann.ui.changelog.changelogUiModule
import com.mail.ann.ui.choosefolder.chooseFolderUiModule
import com.mail.ann.ui.endtoend.endToEndUiModule
import com.mail.ann.ui.folders.foldersUiModule
import com.mail.ann.ui.managefolders.manageFoldersUiModule
import com.mail.ann.ui.messagelist.messageListUiModule
import com.mail.ann.ui.messagesource.messageSourceModule
import com.mail.ann.ui.settings.settingsUiModule
import com.mail.ann.ui.uiModule
import com.mail.ann.view.viewModule

val uiModules = listOf(
    uiBaseModule,
    activityModule,
    uiModule,
    settingsUiModule,
    endToEndUiModule,
    foldersUiModule,
    messageListUiModule,
    manageFoldersUiModule,
    chooseFolderUiModule,
    fragmentModule,
    contactsModule,
    accountModule,
    autodiscoveryProvidersXmlModule,
    viewModule,
    changelogUiModule,
    messageSourceModule,
    accountUiModule
)
