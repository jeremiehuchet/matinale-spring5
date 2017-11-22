package com.point.searchengine.runner;

import com.point.searchengine.model.GithubProject;
import com.point.searchengine.model.GitlabProject;
import com.point.searchengine.repository.GithubRepository;
import com.point.searchengine.repository.GitlabRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.profiler.Profiler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Slf4j
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

        Profiler p = new Profiler("search");
        p.setLogger(log);

        completionService.submit(profile(p, "gitlab", () -> gitlab.search("springframework").stream()
                .map(GitlabProject::getFullName)
                .collect(toList())));

        completionService.submit(profile(p, "github", () -> github.search("springframework").stream()
                .map(GithubProject::getFullName)
                .collect(toList())));

        List<String> resultsA = completionService.take().get();
        resultsA.forEach(name -> System.out.println("1: " + name));

        List<String> resultsB = completionService.take().get();
        resultsB.forEach(name -> System.out.println("2: " + name));

        p.stop().log();

        executor.shutdown();
    }

    private Callable<List<String>> profile(Profiler rootProfiler, String name, Callable<List<String>> supplier) {
        return () -> {
            Profiler nestedProfiler = rootProfiler.startNested(name);
            try {
                return supplier.call();
            } finally {
                nestedProfiler.stop();
            }
        };
    }
}
