package com.mail.ann.mail.store.imap;

import java.util.List;

import com.mail.ann.mail.MessagingException;

import static com.mail.ann.mail.store.imap.ImapResponseParser.equalsIgnoreCase;


class NegativeImapResponseException extends MessagingException {
    private static final long serialVersionUID = 3725007182205882394L;

    private final List<ImapResponse> responses;
    private String alertText;

    public NegativeImapResponseException(String message, List<ImapResponse> responses) {
        super(message, true);
        this.responses = responses;
    }

    public String getAlertText() {
        if (alertText == null) {
            ImapResponse lastResponse = getLastResponse();
            alertText = AlertResponse.getAlertText(lastResponse);
        }
        
        return alertText;
    }

    public boolean wasByeResponseReceived() {
        for (ImapResponse response : responses) {
            if (response.getTag() == null && response.size() >= 1 && equalsIgnoreCase(response.get(0), Responses.BYE)) {
                return true;
            }
        }

        return false;
    }

    public ImapResponse getLastResponse() {
        return responses.get(responses.size() - 1);
    }
}
