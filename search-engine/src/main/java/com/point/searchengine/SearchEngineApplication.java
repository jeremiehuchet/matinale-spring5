package com.point.searchengine;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.repository.GithubRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;

@AllArgsConstructor
@SpringBootConfiguration
@ComponentScan
public class SearchEngineApplication implements CommandLineRunner {

    public final GithubRepository github;
    public final GithubRepository gitlab;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Github results:");
        github.search("springframework").stream()
                .map(GithubProject::getFullName)
                .forEach(name -> System.out.println(name));

        System.out.println("Gitlab results:");
        gitlab.search("springframework").stream()
                .map(GithubProject::getFullName)
                .forEach(name -> System.out.println(name));

    }

    public static void main(String[] args) {
        SpringApplication.run(SearchEngineApplication.class, args);
    }
}
