package com.point.searchengine.repository;

import com.point.searchengine.model.GitlabProject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;

import static java.util.Arrays.asList;

@AllArgsConstructor
@Component
public class GitlabRepository {

    private final RestTemplate http;

    private final WebClient webClient;

    public List<GitlabProject> search(String keywords) {
        return asList(http.getForObject("https://gitlab.com/api/v4/projects?search={keywords}", GitlabProject[].class, keywords));
    }

    public Flux<GitlabProject> find(String keywords) {

        URI build = UriComponentsBuilder.fromHttpUrl("https://gitlab.com/api/v4/projects")
            .queryParam("search", keywords)
            .build().toUri();

        return webClient.get().uri(build).retrieve().bodyToFlux(GitlabProject.class);

    }

}
