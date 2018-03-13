package com.point.searchengine.runner;

import com.point.searchengine.model.GitfakeProject;
import com.point.searchengine.model.GithubProject;
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

@Slf4j
@Component
@Profile("simple")
@AllArgsConstructor
public class SimpleRunner implements CommandLineRunner {

    private final GithubRepository github;
    private final GitlabRepository gitlab;
    private final GitfakeRepository gitfake;

    @Override
    public void run(String... args) throws Exception {
        Profiler p = new Profiler("search");
        p.setLogger(log);

        p.start("gitlab");

        System.out.println("\nGitlab results:");
        gitlab.search("springframework").stream()
                .map(GitlabProject::getFullName)
                .forEach(name -> System.out.println(name));

        p.start("github");

        System.out.println("\nGithub results:");
        github.search("springframework").stream()
                .map(GithubProject::getFullName)
                .forEach(name -> System.out.println(name));

        p.start("gitfake");

        System.out.println("\nGitfake results:");
        gitfake.search("springframework").stream()
                .map(GitfakeProject::getName)
                .forEach(name -> System.out.println(name));

        p.stop()
                .log();
    }
}
