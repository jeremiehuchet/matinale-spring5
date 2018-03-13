package com.point.searchengine.runner;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.profiler.Profiler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.*;

@Slf4j
@Component
@Profile("perf")
@AllArgsConstructor
public class HundredRequestsRunner implements CommandLineRunner {

    //private static final String SERVER = "http://localhost:6060";
    private static final String SERVER = "https://google.fr/";
    private static final int CALL_COUNT = 500;

    private final Executor executor = Executors.newFixedThreadPool(8);
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    @Override
    public void run(String... args) throws Exception {
        // rest template
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
        Profiler pRestTemplate = new Profiler("rest template");
        for (int i = 0; i < CALL_COUNT; i++) {
            completionService.submit(() -> restTemplate.getForObject(SERVER, String.class));
        }
        for (int i = 0; i < CALL_COUNT; i++) {
            completionService.take().get();
        }
        pRestTemplate.stop().print();


        // web client
        Profiler pWebClient = new Profiler("reactive web client");
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(CALL_COUNT * 2);
        for (int i = 0; i < CALL_COUNT; i++) {
            Mono<String> mono = webClient.get()
                    .uri(SERVER + "/index.html")
                    .retrieve()
                    .bodyToMono(String.class);
            mono.subscribe(null, e -> queue.add("error"), () -> queue.add("complete"));
        }
        for (int i = 0; i < CALL_COUNT; i++) {
            queue.take();
        }
        pWebClient.stop().print();

        // web client server overload
        for (int i = 0; i < 80; i++) {
            Mono<String> mono = webClient.get()
                    .uri("http://perdu.com")
                    .retrieve()
                    .bodyToMono(String.class);
            mono.subscribe(null, System.err::println, null);
        }
    }
}
