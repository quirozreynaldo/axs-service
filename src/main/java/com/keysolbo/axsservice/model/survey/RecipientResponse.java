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
public class RecipientResponse {
    private List<Recipient> succeeded;
    private List<String> bounced; 
    private List<String> existing;
    private List<String> duplicate;
    private List<String> invalids;
    private List<String> opted_out;
}
