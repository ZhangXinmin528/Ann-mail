package com.mail.ann.ui.messagesource

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mail.ann.controller.MessageReference
import com.mail.ann.mail.Header
import com.mail.ann.mail.internet.MimeUtility
import com.mail.ann.ui.R
import com.mail.ann.ui.base.loader.observeLoading
import com.mail.ann.ui.withArguments
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageHeadersFragment : Fragment() {
    private val messageHeadersViewModel: MessageHeadersViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.message_view_headers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val messageReferenceString = requireArguments().getString(ARG_REFERENCE)
            ?: error("Missing argument $ARG_REFERENCE")
        val messageReference = MessageReference.parse(messageReferenceString)
            ?: error("Invalid message reference: $messageReferenceString")

        val messageHeaderView = view.findViewById<TextView>(R.id.message_source)

        messageHeadersViewModel.loadHeaders(messageReference).observeLoading(
            owner = this,
            loadingView = view.findViewById(R.id.message_headers_loading),
            errorView = view.findViewById(R.id.message_headers_error),
            dataView = view.findViewById(R.id.message_headers_data)
        ) { headers ->
            populateHeadersList(messageHeaderView, headers)
        }
    }

    private fun populateHeadersList(messageHeaderView: TextView, headers: List<Header>) {
        val sb = SpannableStringBuilder()
        var first = true
        for ((name, value) in headers) {
            if (!first) {
                sb.append("\n")
            } else {
                first = false
            }
            val boldSpan = StyleSpan(Typeface.BOLD)
            val label = SpannableString("$name: ")
            label.setSpan(boldSpan, 0, label.length, 0)
            sb.append(label)
            sb.append(MimeUtility.unfoldAndDecode(value))
        }
        messageHeaderView.text = sb
    }

    companion object {
        private const val ARG_REFERENCE = "reference"

        fun newInstance(reference: MessageReference): MessageHeadersFragment {
            return MessageHeadersFragment().withArguments(
                ARG_REFERENCE to reference.toIdentityString()
            )
        }
    }
}
