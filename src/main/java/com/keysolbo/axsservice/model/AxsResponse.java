package com.keysolbo.axsservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AxsResponse<T> {
  private String errorCode;
  private String description;
  private T data;
}
