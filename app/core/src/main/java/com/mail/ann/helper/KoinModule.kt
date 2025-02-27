package com.mail.ann.helper

import android.app.AlarmManager
import android.content.Context
import com.mail.ann.mail.ssl.KeyStoreDirectoryProvider
import org.koin.dsl.module

val helperModule = module {
    single { ClipboardManager(get()) }
    single { MessageHelper.getInstance(get()) }
    factory<KeyStoreDirectoryProvider> { AndroidKeyStoreDirectoryProvider(context = get()) }
    factory { get<Context>().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    single { AlarmManagerCompat(alarmManager = get()) }
}
