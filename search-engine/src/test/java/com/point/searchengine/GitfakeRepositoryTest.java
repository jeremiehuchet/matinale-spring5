package com.point.searchengine;

import com.point.searchengine.model.GitfakeProject;
import com.point.searchengine.repository.GitfakeRepository;
import com.point.searchengine.repository.GitlabRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GitfakeRepositoryTest {

    private GitfakeRepository gitfake;

    @Before
    public void setup() {
        gitfake = new GitfakeRepository(new RestTemplate(), WebClient.create());
    }

    @Test
    public void can_search_repositories_with_resttemplate() {
        List<GitfakeProject> results = gitfake.search("");
        assertThat(results)
                .hasSize(50);
    }

    @Test
    public void can_search_repositories_with_reactor() throws InterruptedException {

        List<GitfakeProject> results = gitfake.find("")
                .collectList()
                .block();

        assertThat(results)
                .hasSize(50);

    }
}
