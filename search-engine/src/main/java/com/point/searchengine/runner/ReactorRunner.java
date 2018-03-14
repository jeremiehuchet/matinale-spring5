package com.point.searchengine.runner;

import com.point.searchengine.model.GitfakeProject;
import com.point.searchengine.model.GithubProject;
import com.point.searchengine.model.GithubSearchResponse;
import com.point.searchengine.model.GitlabProject;
import com.point.searchengine.repository.GitfakeRepository;
import com.point.searchengine.repository.GithubRepository;
import com.point.searchengine.repository.GitlabRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.profiler.Profiler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

@Slf4j
@Component
@Profile("reactor")
@AllArgsConstructor
public class ReactorRunner implements CommandLineRunner {

    private GithubRepository github;
    private GitlabRepository gitlab;
    private GitfakeRepository gitfake;

    @Override
    public void run(String... args) throws Exception {

        Profiler p = new Profiler("search");
        // Flux github
        p.start("github");
        Flux<GithubProject> githubProjectFlux =
            github.find("spring")
                .map(GithubSearchResponse::getItems)
                .flatMapMany(Flux::fromIterable).take(10);

        // Flux gitlab
        p.start("gitlab");
        Flux<GitlabProject> gitlabProjectFlux = gitlab.find("spring").take(10);

        // Flux gitfake
        p.start("gitfake");
        Flux<GitfakeProject> gitfakeProjectFlux = gitfake.find("spring").take(50);

        // Merge flux and sort
        p.start("subscribe");
        Flux.merge(gitlabProjectFlux, githubProjectFlux, gitfakeProjectFlux)
            .sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName()))
            .map(o -> Tuples.of(o.getClass().getCanonicalName(), o.getName()))
            .subscribe(System.out::println, System.err::println, () -> p.stop().print());
    }
}
