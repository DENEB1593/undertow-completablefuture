package com.example.springundertow;

import com.example.springundertow.fetcher.ApiFetcher;
import com.example.springundertow.fetcher.ApiFetcher2;
import com.example.springundertow.fetcher.ApiFetcher3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("api")
public class ApiController {

  private final Logger LOG = LoggerFactory.getLogger(ApiController.class);

  private final ApiFetcher apiFetcher;
  private final ApiFetcher2 apiFetcher2;
  private final ApiFetcher3 apiFetcher3;

  public ApiController(ApiFetcher apiFetcher,
                       ApiFetcher2 apiFetcher2,
                       ApiFetcher3 apiFetcher3) {
    this.apiFetcher = apiFetcher;
    this.apiFetcher2 = apiFetcher2;
    this.apiFetcher3 = apiFetcher3;
  }

  @GetMapping("data")
  public ResponseEntity<?> data() throws ExecutionException, InterruptedException {
    LOG.info("===== data request start ==== ");

    CompletableFuture<UserDto> fetch1 = CompletableFuture.supplyAsync(() -> apiFetcher.fetch("1"));
    CompletableFuture<UserDto> fetch2 = CompletableFuture.supplyAsync(() -> apiFetcher2.fetch("2"));
    CompletableFuture<UserDto> fetch3 = CompletableFuture.supplyAsync(() -> apiFetcher3.fetch("3"));

    List<UserDto> result = new ArrayList<>();

    try {
      result.addAll(
          List.of(fetch1.get(4, TimeUnit.SECONDS),
            fetch2.get(4, TimeUnit.SECONDS),
            fetch3.get(4, TimeUnit.SECONDS)));
    } catch (TimeoutException e) {
      LOG.error("timeout exception : {}", e.getMessage(), e);
      throw new RuntimeException(e);
    }
    LOG.info("===== data request finished ==== ");
    return ResponseEntity.ok(result);
  }

}
