package io.soffa.foundation.models.mail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.soffa.foundation.models.files.Attachment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class Email {

    private String subject;
    private EmailAddress sender;
    private List<EmailAddress> to;
    private List<EmailAddress> cc;
    private List<EmailAddress> bcc;
    private String textMessage;
    private String htmlMessage;
    private List<Attachment> attachments;

    public Email(String subjet, EmailAddress sender, List<EmailAddress> to, String textMessage, String htmlMessage) {
        this.subject = subjet;
        this.sender = sender;
        this.to = to;
        this.textMessage = textMessage;
        this.htmlMessage = htmlMessage;
    }

    public Email(String subject, List<EmailAddress> to, String textMessage, String htmlMessage) {
        this.subject = subject;
        this.to = to;
        this.textMessage = textMessage;
        this.htmlMessage = htmlMessage;
    }

    public Email(String subjet, EmailAddress to, String textMessage, String htmlMessage) {
        this.subject = subjet;
        this.to = Collections.singletonList(to);
        this.textMessage = textMessage;
        this.htmlMessage = htmlMessage;
    }

    @JsonIgnore
    public boolean hasMessage() {
        if (textMessage != null && !textMessage.trim().isEmpty()) {
            return true;
        }
        return htmlMessage != null && !htmlMessage.trim().isEmpty();
    }

}
