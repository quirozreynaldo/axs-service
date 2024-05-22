package com.keysolbo.axsservice.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactList {
    private String id;
    //private String contactListId;
    private String name;
    private String href;
    //private String surveyId;
    //private String status;
    //private Timestamp record_date;
}
