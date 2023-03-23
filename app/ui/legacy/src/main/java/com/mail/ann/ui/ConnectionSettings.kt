package com.mail.ann.ui

import com.mail.ann.mail.ServerSettings

data class ConnectionSettings(val incoming: ServerSettings, val outgoing: ServerSettings)
