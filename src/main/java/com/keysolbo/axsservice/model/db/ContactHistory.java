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
public class ContactHistory {
    private Integer contactHistoryId;
    private String smContactId;
    private String email;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String state;
    private String data;
    private Timestamp timestamp;
    private String href;
}
