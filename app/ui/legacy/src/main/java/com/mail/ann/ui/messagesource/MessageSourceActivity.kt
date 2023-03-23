package com.mail.ann.ui.messagesource

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import com.mail.ann.controller.MessageReference
import com.mail.ann.ui.R
import com.mail.ann.ui.base.AnnActivity

/**
 * Temporary Activity used until the fragment can be displayed in the main activity.
 */
class MessageSourceActivity : AnnActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout(R.layout.message_view_headers_activity)
        setTitle(R.string.show_headers_action)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            addMessageHeadersFragment()
        }
    }

    private fun addMessageHeadersFragment() {
        val messageReferenceString = intent.getStringExtra(ARG_REFERENCE) ?: error("Missing argument $ARG_REFERENCE")
        val messageReference = MessageReference.parse(messageReferenceString)
            ?: error("Invalid message reference: $messageReferenceString")

        val fragment = MessageHeadersFragment.newInstance(messageReference)
        supportFragmentManager.commit {
            add(R.id.message_headers_fragment, fragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val ARG_REFERENCE = "reference"

        fun createLaunchIntent(context: Context, messageReference: MessageReference): Intent {
            return Intent(context, MessageSourceActivity::class.java).apply {
                putExtra(ARG_REFERENCE, messageReference.toIdentityString())
            }
        }
    }
}
