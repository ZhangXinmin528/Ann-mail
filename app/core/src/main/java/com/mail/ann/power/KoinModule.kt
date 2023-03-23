package com.mail.ann.power

import android.content.Context
import com.mail.ann.mail.power.PowerManager
import org.koin.dsl.module

val powerModule = module {
    factory { get<Context>().getSystemService(Context.POWER_SERVICE) as android.os.PowerManager }
    single<PowerManager> { AndroidPowerManager(systemPowerManager = get()) }
}
