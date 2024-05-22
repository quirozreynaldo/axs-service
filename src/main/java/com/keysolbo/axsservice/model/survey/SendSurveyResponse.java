package com.keysolbo.axsservice.model.survey;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SendSurveyResponse {
    private Boolean is_scheduled;
    private String scheduled_date;
    private String subject;
    private String body;
    private String type;
    private List<String> recipients;
    private String recipient_status;
    private Boolean embed_first_question;
}
