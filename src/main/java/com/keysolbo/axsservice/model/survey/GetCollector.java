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
public class GetCollector {
    private String name;
    private String id;
    private String href;
    private String type;
    private String email;

    @JsonProperty("is_sender_email_verified")
    private boolean isSenderEmailVerified;
}
