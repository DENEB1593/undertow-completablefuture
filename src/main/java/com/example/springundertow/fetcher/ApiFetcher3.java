package com.example.springundertow.fetcher;

import com.example.springundertow.UserDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.TimeUnit;

@Service
public class ApiFetcher3 {

  private final Logger LOG = LoggerFactory.getLogger(ApiFetcher3.class);

  private final Gson gson;

  public ApiFetcher3(Gson gson) {
    this.gson = gson;
  }


  public UserDto fetch(String id)  {
    LOG.info("===== ApiFetcher 3 started =====");
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    UriComponents uriComponents = UriComponentsBuilder.newInstance()
      .scheme("https")
      .host("reqres.in/api/users")
      .path("/"+ id)
      .build();

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.getForEntity(uriComponents.toUri(), String.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      JsonObject json = gson.fromJson(response.getBody(), JsonObject.class);
      UserDto userDto = gson.fromJson(json.get("data"), UserDto.class);
      LOG.info("===== ApiFetcher 3 finished =====");
      return userDto;
    }


    return null;
  }

}
