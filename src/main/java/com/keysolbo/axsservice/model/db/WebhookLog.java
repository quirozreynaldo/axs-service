package com.keysolbo.axsservice.model.db;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WebhookLog {
    private int webhookLogId;
    private String name;
    private String filterType;
    private String filterId;
    private String eventType;
    private String eventId;
    private String eventDatetime;
    private String objectType;
    private String objectId;
    private String respondentId;
    private String recipientId;
    private String surveyId;
    private String userId;
    private String collectorId;
    private String status;
    private String contactUpdated;
    private Timestamp recordDate;
}
