package com.point.searchengine.web;

import com.point.searchengine.model.*;
import com.point.searchengine.repository.GitfakeRepository;
import com.point.searchengine.repository.GithubRepository;
import com.point.searchengine.repository.GitlabRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/classic-search")
@AllArgsConstructor
public class SearchController {

    private GithubRepository github;
    private GitlabRepository gitlab;
    private GitfakeRepository gitfake;

    @GetMapping
    public Flux<HasProjectName> search(@RequestParam String keywords) {

        Flux<GithubProject> githubProjectFlux =
                github.find("spring")
                        .map(GithubSearchResponse::getItems)
                        .flatMapMany(Flux::fromIterable).take(10);

        Flux<GitlabProject> gitlabProjectFlux = gitlab.find("spring").take(10);

        Flux<GitfakeProject> gitfakeProjectFlux = gitfake.find("spring").take(50);

        return Flux.merge(gitlabProjectFlux, githubProjectFlux, gitfakeProjectFlux)
                .map(p -> (HasProjectName) () -> p.getName());
    }
}
