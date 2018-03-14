package com.point.searchengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.net.URL;

@Data
public class GithubProject implements HasProjectName {

    private String id;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    private String description;

    @JsonProperty("private")
    private Boolean privateRepo;

    @JsonProperty("fork")
    private Boolean forkedRepo;

    private URL url;

    @JsonProperty("html_url")
    private URL htmlUrl;
}
