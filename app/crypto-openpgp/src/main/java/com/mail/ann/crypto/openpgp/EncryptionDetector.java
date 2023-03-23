package com.mail.ann.crypto.openpgp;


import androidx.annotation.NonNull;

import com.mail.ann.crypto.MessageCryptoStructureDetector;
import com.mail.ann.mail.Body;
import com.mail.ann.mail.BodyPart;
import com.mail.ann.mail.Message;
import com.mail.ann.mail.Multipart;
import com.mail.ann.mail.Part;
import com.mail.ann.message.extractors.TextPartFinder;

import static com.mail.ann.mail.internet.MimeUtility.isSameMimeType;


//FIXME: Make this only detect OpenPGP messages. Move support for S/MIME messages to separate module.
class EncryptionDetector {
    private final TextPartFinder textPartFinder;


    EncryptionDetector(TextPartFinder textPartFinder) {
        this.textPartFinder = textPartFinder;
    }

    public boolean isEncrypted(@NonNull Message message) {
        return isPgpMimeOrSMimeEncrypted(message) || containsInlinePgpEncryptedText(message);
    }

    private boolean isPgpMimeOrSMimeEncrypted(Message message) {
        return containsPartWithMimeType(message, "multipart/encrypted", "application/pkcs7-mime");
    }

    private boolean containsInlinePgpEncryptedText(Message message) {
        Part textPart = textPartFinder.findFirstTextPart(message);
        return MessageCryptoStructureDetector.isPartPgpInlineEncrypted(textPart);
    }

    private boolean containsPartWithMimeType(Part part, String... wantedMimeTypes) {
        String mimeType = part.getMimeType();
        if (isMimeTypeAnyOf(mimeType, wantedMimeTypes)) {
            return true;
        }

        Body body = part.getBody();
        if (body instanceof Multipart) {
            Multipart multipart = (Multipart) body;
            for (BodyPart bodyPart : multipart.getBodyParts()) {
                if (containsPartWithMimeType(bodyPart, wantedMimeTypes)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isMimeTypeAnyOf(String mimeType, String... wantedMimeTypes) {
        for (String wantedMimeType : wantedMimeTypes) {
            if (isSameMimeType(mimeType, wantedMimeType)) {
                return true;
            }
        }

        return false;
    }
}
