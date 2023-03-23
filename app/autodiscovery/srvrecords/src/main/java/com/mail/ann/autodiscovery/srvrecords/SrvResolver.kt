package com.mail.ann.autodiscovery.srvrecords

interface SrvResolver {
    fun lookup(domain: String, type: SrvType): List<MailService>
}
