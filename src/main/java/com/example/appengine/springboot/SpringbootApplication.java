/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appengine.springboot;

// [START gae_java11_helloworld]
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@CrossOrigin
@RestController
public class SpringbootApplication {

  @Value("${flickrServiceUrl}")
  private String flickrServiceUrl;

  @Value("${hardCodedHash}")
  private String hardCodedHash;

  public static void main(String[] args) {
    SpringApplication.run(SpringbootApplication.class, args);
  }

  @GetMapping("/search")
  public ResponseEntity<?> getSearchResults(@RequestParam String searchTerm) {
    try {
      String uri = flickrServiceUrl;
      UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri)
              // Add query parameter
              .queryParam("tags", searchTerm)
              .queryParam("tagmode", "any")
              .queryParam("format", "json")
              .queryParam("nojsoncallback", "1");
      String finalUri = builder.toUriString();

      RestTemplate restTemplate = new RestTemplate();
      String result = restTemplate.getForObject(finalUri, String.class);
      return new ResponseEntity<>(result, HttpStatus.OK);
    }catch (Exception e){
      e.printStackTrace();
      return new ResponseEntity<>("Flickr API call failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /** Check password against hardcoded hash stored in application.properties. */
  @PostMapping("/checkHash")
  public ResponseEntity<PasswordResponse> checkHash(@RequestBody String plainPassword) {
    PasswordResponse response = new PasswordResponse();

    if (BCrypt.checkpw(plainPassword, hardCodedHash)) {
      response.setStatus(HttpStatus.OK);
      response.setMessage("Password matches");
    } else {
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
      response.setMessage("Password doesn't match");
    }

    return new ResponseEntity<PasswordResponse>(response, response.getStatus());
  }

}
// [END gae_java11_helloworld]
