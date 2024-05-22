package com.keysolbo.axsservice.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecipientLog {
    private int recipientLogId;
    private String recipientId;
    private String email;
    private String phoneNumber;
    private String href;
    private String bounced; 
    private String existing;
    private String duplicate;
    private String invalids;
    private String optedOut;
    private String serviceComplain;
    private String collectorId;
    private String messageId;
    private String status;
    private String sent;
    private String responded;
    private Date recordDate;
}

