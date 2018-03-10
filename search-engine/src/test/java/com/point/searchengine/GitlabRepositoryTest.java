package com.point.searchengine;

import com.point.searchengine.model.GitlabProject;
import com.point.searchengine.repository.GitlabRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GitlabRepositoryTest {

    private GitlabRepository gitlab;

    @Before
    public void setup() {
        gitlab = new GitlabRepository(new RestTemplate(), WebClient.builder().build());
    }

    @Test
    public void can_search_repositories() {
        List<GitlabProject> results = gitlab.search("spring");
        assertThat(results.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void can_search_repositories_with_reactor() throws InterruptedException {

        List<GitlabProject> results = gitlab.find("spring").take(10).collectList().block();

        assertThat(results.size()).isGreaterThanOrEqualTo(1);

    }
}
