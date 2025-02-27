package com.mail.ann.provider

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.mail.ann.EarlyInit
import com.mail.ann.R
import com.mail.ann.helper.PendingIntentCompat.FLAG_MUTABLE
import com.mail.ann.inject
import com.mail.ann.widget.unread.UnreadWidgetConfigurationActivity
import com.mail.ann.widget.unread.UnreadWidgetData
import com.mail.ann.widget.unread.UnreadWidgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Unread home screen widget "provider"
 *
 * IMPORTANT: This class must not be renamed or moved, otherwise unread widgets added to the home screen using an older
 * version of the app will stop working.
 *
 * The rest of the unread widget specific code can be found in the package [com.mail.ann.widget.unread].
 */
class UnreadWidgetProvider : AppWidgetProvider(), EarlyInit {
    private val repository: UnreadWidgetRepository by inject()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val pendingResult = goAsync()

        GlobalScope.launch(Dispatchers.IO) {
            updateWidgets(context, appWidgetManager, appWidgetIds)
            pendingResult.finish()
        }
    }

    private fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (widgetId in appWidgetIds) {
            val widgetData = repository.getWidgetData(widgetId) ?: continue
            updateWidget(context, appWidgetManager, widgetData)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            repository.deleteWidgetConfiguration(appWidgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, data: UnreadWidgetData) {
        val remoteViews = RemoteViews(context.packageName, R.layout.unread_widget_layout)

        val appWidgetId = data.configuration.appWidgetId
        var clickIntent: Intent? = null

        try {
            clickIntent = data.clickIntent
            val unreadCount = data.unreadCount

            if (unreadCount <= 0) {
                // Hide TextView for unread count if there are no unread messages.
                remoteViews.setViewVisibility(R.id.unread_count, View.GONE)
            } else {
                remoteViews.setViewVisibility(R.id.unread_count, View.VISIBLE)

                val displayCount = if (unreadCount <= MAX_COUNT) unreadCount.toString() else "$MAX_COUNT+"
                remoteViews.setTextViewText(R.id.unread_count, displayCount)
            }

            remoteViews.setTextViewText(R.id.title, data.title)
        } catch (e: Exception) {
            Timber.e(e, "Error getting widget configuration")
        }

        if (clickIntent == null) {
            // If the widget configuration couldn't be loaded we open the configuration
            // activity when the user clicks the widget.
            clickIntent = Intent(context, UnreadWidgetConfigurationActivity::class.java)
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        clickIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId,
            clickIntent,
            FLAG_UPDATE_CURRENT or FLAG_MUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.unread_widget_layout, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    companion object {
        private const val MAX_COUNT = 9999
    }
}
