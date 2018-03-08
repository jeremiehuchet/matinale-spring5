package com.point.searchengine.repository;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.model.GithubSearchResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@Component
public class GithubRepository {

    private final RestTemplate http;

    private final WebClient webClient;

    public List<GithubProject> search(String keywords) {
        return http.getForObject("https://api.github.com/search/repositories?q={keywords}", GithubSearchResponse.class, keywords).getItems();
    }

    public WebClient.ResponseSpec provideGitlabSpec(String keywords) {

        URI build = UriComponentsBuilder.fromHttpUrl("https://api.github.com/search/repositories")
            .queryParam("q", keywords)
            .build().toUri();

        return webClient.get().uri(build).retrieve();

    }
}
