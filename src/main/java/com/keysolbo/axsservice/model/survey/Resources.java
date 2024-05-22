package com.keysolbo.axsservice.model.survey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resources {
    private String respondent_id;
    private String recipient_id;
    private String survey_id;
    private String user_id;
    private String collector_id;
}
