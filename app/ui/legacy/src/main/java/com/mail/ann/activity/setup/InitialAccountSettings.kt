package com.mail.ann.activity.setup

import android.os.Parcelable
import com.mail.ann.mail.AuthType
import kotlinx.parcelize.Parcelize

@Parcelize
data class InitialAccountSettings(
    val authenticationType: AuthType,
    val email: String,
    val password: String?,
    val clientCertificateAlias: String?
) : Parcelable
