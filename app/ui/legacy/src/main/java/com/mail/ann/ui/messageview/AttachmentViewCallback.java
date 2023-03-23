package com.mail.ann.ui.messageview;


import com.mail.ann.mailstore.AttachmentViewInfo;


interface AttachmentViewCallback {
    void onViewAttachment(AttachmentViewInfo attachment);
    void onSaveAttachment(AttachmentViewInfo attachment);
}
