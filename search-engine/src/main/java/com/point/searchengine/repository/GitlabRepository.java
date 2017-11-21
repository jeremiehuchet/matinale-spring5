package com.point.searchengine.repository;

import com.point.searchengine.model.GitlabProject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Arrays.asList;

@AllArgsConstructor
@Component
public class GitlabRepository {

    private final RestTemplate http;

    public List<GitlabProject> search(String keywords) {
        return asList(http.getForObject("https://gitlab.com/api/v4/projects?search={keywords}", GitlabProject[].class, keywords));
    }
}
