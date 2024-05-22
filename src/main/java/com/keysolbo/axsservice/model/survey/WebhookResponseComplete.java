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
public class WebhookResponseComplete {
    private String name;
    private String filter_type;
    private String filter_id;
    private String event_type;
    private String event_id;
    private String event_datetime;
    private String object_type;
    private String object_id;
    private Resources resources;
}
