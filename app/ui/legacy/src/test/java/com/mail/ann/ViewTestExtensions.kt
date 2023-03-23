package com.mail.ann

import android.widget.TextView

val TextView.textString: String
    get() = text.toString()
