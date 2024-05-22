package com.keysolbo.axsservice.model.survey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageRequest {
    private String from_collector_id;
    private String from_message_id;
    private boolean include_recipients;
}
