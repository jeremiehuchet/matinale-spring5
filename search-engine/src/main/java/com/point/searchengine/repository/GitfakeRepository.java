package com.point.searchengine.repository;

import com.point.searchengine.model.GitfakeProject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Component
public class GitfakeRepository {

    private final RestTemplate http;

    private final WebClient webClient;

    public List<GitfakeProject> search(String uselessKeywords) {
        return IntStream.range(0, 50)
                .mapToObj(this::randomProjectFromRestTemplate)
                .collect(toList());
    }

    private GitfakeProject randomProjectFromRestTemplate(int i) {
        return http.getForObject("https://api.whatdoestrumpthink.com/api/v1/quotes/random", GitfakeProject.class);
    }

    public Flux<GitfakeProject> find(String uselessKeywords) {
        List<Mono<GitfakeProject>> monos = IntStream.range(0, 50)
                .mapToObj(this::randomProjectFromWebclient)
                .collect(toList());
        return Flux.merge(monos);

    }

    private Mono<GitfakeProject> randomProjectFromWebclient(int i) {
        return webClient.get()
                .uri("https://api.whatdoestrumpthink.com/api/v1/quotes/random")
                .retrieve()
                .bodyToMono(GitfakeProject.class);
    }
}
