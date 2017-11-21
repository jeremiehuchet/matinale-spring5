package com.point.searchengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class GitlabProject {

    private Long id;

    private String name;

    @JsonProperty("path_with_namespace")
    private String fullName;

    private String description;


    @JsonProperty("http_url_to_repo")
    private URL url;

    @JsonProperty("html_url")
    private URL htmlUrl;

    @JsonProperty("avatar_url")
    private URL avatarUrl;
}
