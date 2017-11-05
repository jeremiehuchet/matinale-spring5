package com.point.searchengine;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.repository.GithubRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AllArgsConstructor
@SpringBootConfiguration
@ComponentScan
public class SearchEngineApplication implements CommandLineRunner {

    public final GithubRepository github;

    @Override
    public void run(String... args) throws Exception {
        github.search("springframework").stream()
                .map(GithubProject::getFullName)
                .forEach(name -> System.out.println(name));

    }

    public static void main(String[] args) {
        SpringApplication.run(SearchEngineApplication.class, args);
    }
}
