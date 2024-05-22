package com.keysolbo.axsservice.model.survey;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecipientRequestList {
    private List<String> contact_list_ids;
    private List<String> contact_ids;
}