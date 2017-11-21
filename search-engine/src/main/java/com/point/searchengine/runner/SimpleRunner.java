package com.point.searchengine.runner;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.repository.GithubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("simple")
@AllArgsConstructor
public class SimpleRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SimpleRunner.class);

    public final GithubRepository github;
    public final GithubRepository gitlab;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\nGithub results:");
        github.search("springframework").stream()
                .map(GithubProject::getFullName)
                .forEach(name -> System.out.println(name));

        System.out.println("\nGitlab results:");
        gitlab.search("springframework").stream()
                .map(GithubProject::getFullName)
                .forEach(name -> System.out.println(name));
    }
}
