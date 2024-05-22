package com.keysolbo.axsservice.model.survey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CollectorMessageRequest {
  private String type;
  private String subject;
  private String body_text;
  private Boolean is_branding_enabled;
}
