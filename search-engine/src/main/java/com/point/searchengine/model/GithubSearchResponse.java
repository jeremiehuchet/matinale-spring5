package com.point.searchengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GithubSearchResponse {

    @JsonProperty("total_count")
    private Long totalCount;

    @JsonProperty("incomplete_results")
    private Boolean incompleteResult;

    private List<GithubProject> items;
}
