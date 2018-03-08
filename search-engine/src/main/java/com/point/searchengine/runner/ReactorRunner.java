package com.point.searchengine.runner;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.model.GithubSearchResponse;
import com.point.searchengine.model.GitlabProject;
import com.point.searchengine.repository.GithubRepository;
import com.point.searchengine.repository.GitlabRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

@Slf4j
@Component
@Profile("reactor")
@AllArgsConstructor
public class ReactorRunner implements CommandLineRunner {
    private GithubRepository github;

    private GitlabRepository gitlab;

    @Override
    public void run(String... args) throws Exception {

        // Web spec
        WebClient.ResponseSpec gitlabSpec = gitlab.provideGitlabSpec("spring");
        WebClient.ResponseSpec githubSpec = github.provideGitlabSpec("spring");


        // Flux github
        Flux<GithubProject> githubProjectFlux =
            githubSpec
                .bodyToMono(GithubSearchResponse.class)
                .map(GithubSearchResponse::getItems)
                .flatMapMany(Flux::fromIterable).take(10);

        // Flux gitlab
        Flux<GitlabProject> gitlabProjectFlux = gitlabSpec.bodyToFlux(GitlabProject.class).take(10);

        // Merge flux and sort
        Flux.merge(gitlabProjectFlux, githubProjectFlux)
            .sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName()))
            .map(o -> Tuples.of(o.getClass().getCanonicalName(), o.getName()))
            .subscribe(System.out::println);


    }
}
