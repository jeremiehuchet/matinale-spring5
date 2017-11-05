package com.point.searchengine;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.repository.GithubRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GithubRepositoryTest {

    private GithubRepository github;

    @Before
    public void setup() {
        github = new GithubRepository(new RestTemplate());
    }

    @Test
    public void can_search_repositories() {
        List<GithubProject> results = github.search("spring");
        assertThat(results.size()).isGreaterThanOrEqualTo(1);
    }

}
