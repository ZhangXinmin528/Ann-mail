package com.mail.ann.network

import android.content.Context
import org.koin.dsl.module
import android.net.ConnectivityManager as SystemConnectivityManager

internal val connectivityModule = module {
    single { get<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as SystemConnectivityManager }
    single { ConnectivityManager(systemConnectivityManager = get()) }
}
