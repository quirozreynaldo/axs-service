package com.keysolbo.axsservice.model.survey;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArecipientRequest {
    @JsonProperty("contact_id")
    private String contactId;
}
