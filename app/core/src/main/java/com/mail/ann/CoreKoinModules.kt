package com.mail.ann

import com.mail.ann.autocrypt.autocryptModule
import com.mail.ann.controller.controllerModule
import com.mail.ann.controller.push.controllerPushModule
import com.mail.ann.crypto.openPgpModule
import com.mail.ann.helper.helperModule
import com.mail.ann.job.jobModule
import com.mail.ann.logging.loggingModule
import com.mail.ann.mailstore.mailStoreModule
import com.mail.ann.message.extractors.extractorModule
import com.mail.ann.message.html.htmlModule
import com.mail.ann.message.quote.quoteModule
import com.mail.ann.network.connectivityModule
import com.mail.ann.notification.coreNotificationModule
import com.mail.ann.power.powerModule
import com.mail.ann.preferences.preferencesModule
import com.mail.ann.search.searchModule

val coreModules = listOf(
    mainModule,
    openPgpModule,
    autocryptModule,
    mailStoreModule,
    searchModule,
    extractorModule,
    htmlModule,
    quoteModule,
    coreNotificationModule,
    controllerModule,
    controllerPushModule,
    jobModule,
    helperModule,
    preferencesModule,
    connectivityModule,
    powerModule,
    loggingModule
)
