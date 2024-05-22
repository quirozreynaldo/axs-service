package com.keysolbo.axsservice.model.survey;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CollectorTypeResponse {
 private String status;
    private String id;
    private String survey_id;
    private String type;
    private String name;
    private String thank_you_message;
    private ThankYouPage thank_you_page;
    private String disqualification_type;
    private String disqualification_message;
    private String disqualification_url;
    private String closed_page_message;
    private String redirect_type;
    private String redirect_url;
    private boolean display_survey_results;
    private String edit_response_type;
    private String anonymous_type;
    private boolean allow_multiple_responses;
    private OffsetDateTime date_modified;
    private OffsetDateTime date_created;
    private int response_count;
    private boolean password_enabled;
    private Integer response_limit;
    private boolean respondent_authentication;
    private String sender_email;
    private OffsetDateTime close_date;
    private String url;
    private String href;
}
