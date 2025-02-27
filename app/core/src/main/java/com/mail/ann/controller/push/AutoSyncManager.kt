package com.mail.ann.controller.push

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.mail.ann.Ann
import timber.log.Timber

/**
 * Listen for changes to the system's auto sync setting.
 */
internal class AutoSyncManager(private val context: Context) {
    val isAutoSyncDisabled: Boolean
        get() = respectSystemAutoSync && !ContentResolver.getMasterSyncAutomatically()

    val respectSystemAutoSync: Boolean
        get() = Ann.backgroundOps == Ann.BACKGROUND_OPS.WHEN_CHECKED_AUTO_SYNC

    private var isRegistered = false
    private var listener: AutoSyncListener? = null

    private val intentFilter = IntentFilter().apply {
        addAction("com.android.sync.SYNC_CONN_STATUS_CHANGED")
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val listener = synchronized(this@AutoSyncManager) { listener }
            listener?.onAutoSyncChanged()
        }
    }

    @Synchronized
    fun registerListener(listener: AutoSyncListener) {
        if (!isRegistered) {
            Timber.v("Registering auto sync listener")
            isRegistered = true
            this.listener = listener
            context.registerReceiver(receiver, intentFilter)
        }
    }

    @Synchronized
    fun unregisterListener() {
        if (isRegistered) {
            Timber.v("Unregistering auto sync listener")
            isRegistered = false
            context.unregisterReceiver(receiver)
        }
    }
}

internal fun interface AutoSyncListener {
    fun onAutoSyncChanged()
}
