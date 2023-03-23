@file:JvmName("Preconditions")

package com.mail.ann.controller

import com.mail.ann.Ann

fun requireValidUids(uidMap: Map<String?, String?>?) {
    requireNotNull(uidMap)
    for ((sourceUid, destinationUid) in uidMap) {
        requireNotLocalUid(sourceUid)
        requireNotNull(destinationUid)
    }
}

fun requireValidUids(uids: List<String?>?) {
    requireNotNull(uids)
    for (uid in uids) {
        requireNotLocalUid(uid)
    }
}

private fun requireNotLocalUid(uid: String?) {
    requireNotNull(uid)
    require(!uid.startsWith(Ann.LOCAL_UID_PREFIX)) { "Local UID found: $uid" }
}
