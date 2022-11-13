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

@Service
public class ApiFetcher {

  private final Logger LOG = LoggerFactory.getLogger(ApiFetcher.class);

  private final Gson gson;

  public ApiFetcher(Gson gson) {
    this.gson = gson;
  }


  public UserDto fetch(String id) {
    LOG.info("===== ApiFetcher started =====");

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
      LOG.info("===== ApiFetcher finished =====");
      return userDto;
    }

    return null;
  }

}
