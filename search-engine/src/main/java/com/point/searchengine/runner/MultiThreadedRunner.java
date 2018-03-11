package com.point.searchengine.runner;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.model.GitlabProject;
import com.point.searchengine.repository.GithubRepository;
import com.point.searchengine.repository.GitlabRepository;
import lombok.AllArgsConstructor;
import org.slf4j.profiler.Profiler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

@Component
@Profile("multithread")
@AllArgsConstructor
public class MultiThreadedRunner implements CommandLineRunner {

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final GithubRepository github;
    private final GitlabRepository gitlab;

    @Override
    public void run(String... args) throws Exception {
        CompletionService<List<String>> completionService = new ExecutorCompletionService<>(executor);

        Profiler pMain = new Profiler("search");
        Profiler pGitlab = new Profiler("gitlab");
        Profiler pGithub = new Profiler("github");

        completionService.submit(profile(pGitlab, () -> gitlab.search("springframework").stream()
                .map(GitlabProject::getFullName)
                .collect(toList())));

        completionService.submit(profile(pGithub, () -> github.search("springframework").stream()
                .map(GithubProject::getFullName)
                .collect(toList())));

        List<String> resultsA = completionService.take().get();
        resultsA.forEach(name -> System.out.println("1: " + name));

        List<String> resultsB = completionService.take().get();
        resultsB.forEach(name -> System.out.println("2: " + name));

        pGitlab.stop().print();
        pGithub.stop().print();
        pMain.stop().print();

        executor.shutdown();
    }

    private Callable<List<String>> profile(Profiler p, Callable<List<String>> supplier) {
        return () -> {
            p.start("begin");
            try {
                List<String> strings = supplier.call();
                return strings;
            } finally {
                p.stop();
            }
        };
    }
}
