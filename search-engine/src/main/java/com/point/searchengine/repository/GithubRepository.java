package com.point.searchengine.repository;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.model.GithubSearchResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@AllArgsConstructor
@Component
public class GithubRepository {

    private final RestTemplate http;

    public List<GithubProject> search(String keywords) {
        return http.getForObject("https://api.github.com/search/repositories?q={keywords}", GithubSearchResponse.class, keywords).getItems();
    }
}
