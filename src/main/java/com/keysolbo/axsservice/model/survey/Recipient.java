package com.keysolbo.axsservice.model.survey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Recipient {
    private String id;
    private String email;
    private String phone_number;
    private String href;
}
