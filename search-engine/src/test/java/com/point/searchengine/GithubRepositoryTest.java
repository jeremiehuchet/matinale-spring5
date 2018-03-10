package com.point.searchengine;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.model.GithubSearchResponse;
import com.point.searchengine.repository.GithubRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GithubRepositoryTest {

    private GithubRepository github;

    @Before
    public void setup() {
        github = new GithubRepository(new RestTemplate(), WebClient.builder().build());
    }

    @Test
    public void can_search_repositories() {
        List<GithubProject> results = github.search("spring");
        assertThat(results.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void can_search_repositories_with_reactor() throws InterruptedException {

        List<GithubProject> results = github.find("spring")
            .map(GithubSearchResponse::getItems)
            .flatMapMany(Flux::fromIterable)
            .take(10)
            .collectList()
            .block();

        assertThat(results.size()).isGreaterThanOrEqualTo(1);

    }

}
