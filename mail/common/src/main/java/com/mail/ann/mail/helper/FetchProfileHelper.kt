package com.mail.ann.mail.helper

import com.mail.ann.mail.FetchProfile

fun fetchProfileOf(vararg items: FetchProfile.Item): FetchProfile {
    return FetchProfile().apply {
        for (item in items) {
            add(item)
        }
    }
}
