package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.server.mvc.ControllerLinkRelationProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    // GET /users/1 or users/10
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);


        if (user == null) {
             throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        Integer ID = new Integer(id);


        /*
        * import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
        * 위와 같이 static import 함으로 써, 바로 linkTo 같이 사용이 가능하다.
        * */

        // 텍스트로 링크를 사용할 수도 있다.
        Link textLink = new Link("http://localhost:8088/test");
        // HATEOAS를 구현하게 해줄 EntityModel
        EntityModel<User> model =
                // of 를 사용해서, 여러 객체를 받는다.
                EntityModel.of(

                    // 리턴할 결과 user
                    user,

                    // ControllerLinkBuilder클래스의 methodOn 메소드를 이용해서 메소드를 참조하는 link를 만든다.
                    linkTo(methodOn(this.getClass()).retrieveAllUsers()).slash(ID).withSelfRel(),

                    // slash 는 말 그대로 슬래시 한 줄을 넣는것이고 받은 인자를 슬래시 다음에 위치시킨다. 예를 들어서 slash("temp") => /temp 가 된다.
                    linkTo(methodOn(AdminUserController.class).retrieveAllUsers()).slash(ID).withRel("admin-users").withType("GET"),

                    // withRel 은 보여줄 이름이며 withType 은 요청해야 하는 method이다.
                    textLink.withRel("test").withType("POST")
                );


        return model;
    }

    @Validated
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        User modifiedUser = service.updateById(id, user);

        if (modifiedUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        HttpHeaders header = new HttpHeaders();
        header.setLocation(
                ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(modifiedUser.getId())
                .toUri()
        );

        return new ResponseEntity(modifiedUser, header, HttpStatus.ACCEPTED);
    }



}
