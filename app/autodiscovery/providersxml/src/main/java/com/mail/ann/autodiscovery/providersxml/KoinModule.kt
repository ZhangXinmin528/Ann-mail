package com.mail.ann.autodiscovery.providersxml

import org.koin.dsl.module

val autodiscoveryProvidersXmlModule = module {
    factory { ProvidersXmlProvider(context = get()) }
    factory { ProvidersXmlDiscovery(xmlProvider = get(), oAuthConfigurationProvider = get()) }
}
