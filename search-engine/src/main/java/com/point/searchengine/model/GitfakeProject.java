package com.point.searchengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitfakeProject implements HasProjectName {

    @JsonProperty("message")
    private String name;
}
