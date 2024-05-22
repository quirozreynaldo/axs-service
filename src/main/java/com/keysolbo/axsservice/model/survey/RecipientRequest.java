package com.keysolbo.axsservice.model.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecipientRequest {
    private String[] contact_list_ids;
    private String[] contact_ids;
}
