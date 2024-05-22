package com.keysolbo.axsservice.model.survey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageResponse {
    private String status;
    private boolean is_scheduled;
    private String subject;
    private String body;
    private boolean is_branding_enabled;
    private String date_created;
    private String type;
    private String id;
    private String recipient_status;
    private String scheduled_date;
    private String edit_message_link;
    private boolean embed_first_question;
    private String href;
}
