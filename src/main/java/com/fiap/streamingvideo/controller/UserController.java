package com.fiap.streamingvideo.controller;

import com.fiap.streamingvideo.model.UserDTO;
import com.fiap.streamingvideo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @PostMapping
  public Mono<ResponseEntity<UserDTO>> createUser(@RequestBody Mono<UserDTO> userDTOMono) {
    return userService.create(userDTOMono)
        .map(savedUser -> ResponseEntity
            .status(HttpStatus.CREATED)
            .body(savedUser))
        .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<UserDTO>> getUserById(@PathVariable String id) {
    return userService.getById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
