package com.keysolbo.axsservice.model.survey;

import java.util.List;
import java.util.stream.Collector;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetCollectorsResponse {
 @JsonProperty("data")
    private List<GetCollector> data;

    @JsonProperty("per_page")
    private int perPage;

    private int page;
    private int total;
    private GetLinks links;

}
