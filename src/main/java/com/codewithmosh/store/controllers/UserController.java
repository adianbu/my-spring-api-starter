package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getAllUser(@RequestParam(defaultValue = "", required = false, name = "sort") String sort){
        if(!Set.of("name","email").contains(sort))
            sort="name";

        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        var user = userRepository.findById(id).orElse(null);
    if (user == null){
        return ResponseEntity.notFound().build();
    }
    //var userDto= new UserDto(user.getId(), user.getName(), user.getEmail());
//        return new ResponseEntity<>(user, HttpStatus.OK);
        return  ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserDto> createUser(
            UriComponentsBuilder uriBuilder,
            @RequestBody RegisterUserRequest request){
        var user = userMapper.toEntity(request);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        var uri=uriBuilder.path("/api/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id,
                                              @RequestBody UpdateUserRequest request){
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        userMapper.update(user, request);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }


}
