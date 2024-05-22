package com.keysolbo.axsservice.model.survey;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArecipientResponse {
    private String id;
    @JsonProperty("mail_status")
    private String mailStatus;
    @JsonProperty("remove_link")
    private String removeLink;
    @JsonProperty("survey_id")
    private String surveyId;
    @JsonProperty("survey_link")
    private String surveyLink;
    @JsonProperty("survey_response_status")
    private String surveyResponseStatus;
    private String href;
    private String email;
    @JsonProperty("custom_fields")
    private Map<String, String> customFields;
}
