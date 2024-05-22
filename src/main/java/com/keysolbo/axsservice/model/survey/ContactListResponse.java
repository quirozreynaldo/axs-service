package com.keysolbo.axsservice.model.survey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ContactListResponse {
 private String id;
 private String name;
 private String href;
}
