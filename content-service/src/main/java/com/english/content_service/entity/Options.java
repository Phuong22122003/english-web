package com.english.content_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Options {
    @JsonProperty("A")
    private String A;
    @JsonProperty("B")
    private String B;
    @JsonProperty("C")
    private String C;
    @JsonProperty("D")
    private String D;
    // getter/setter   
}
