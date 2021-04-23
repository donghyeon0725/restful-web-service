package com.example.restfulwebservice.user;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
public class UserJpaController {

    /**
     *  아래 코드를 넣어 회원가입시 권한을 부여하고 로그인시에 토근을 주면 된다.
     * */
    /**
     private final PasswordEncoder passwordEncoder;
     private final JwtTokenProvider jwtTokenProvider;
     private final UserRepository userRepository;

     // 회원가입
     @PostMapping("/join")
     public Long join(@RequestBody Map<String, String> user) {
     return userRepository.save(User.builder()
     .email(user.get("email"))
     .password(passwordEncoder.encode(user.get("password")))
     .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
     .build()).getId();
     }

     // 로그인
     @PostMapping("/login")
     public String login(@RequestBody Map<String, String> user) {
     User member = userRepository.findByEmail(user.get("email"))
     .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
     if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
     throw new IllegalArgumentException("잘못된 비밀번호입니다.");
     }
     return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
     }
     */

    private UserRepository userRepository;

    private PostRepository postRepository;

    public UserJpaController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        // Optional 객체를 반환한다는 사실을 잊지 말자
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s} not found", id));
        }

        // HATEOAS를 구현하게 해줄 EntityModel
        EntityModel<User> model =
                // of 를 사용해서, 여러 객체를 받는다.
                EntityModel.of(

                        // 리턴할 결과 user
                        user.get(),

                        // ControllerLinkBuilder클래스의 methodOn 메소드를 이용해서 메소드를 참조하는 link를 만든다.
                        linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users").withType("GET")
                );


        return model;
    }


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public  ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUSer = userRepository.save(user);

        HttpHeaders header = new HttpHeaders();
        header.setLocation(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedUSer.getId())
                        .toUri()
        );

        return new ResponseEntity(savedUSer, header, HttpStatus.ACCEPTED);
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostByUser(@PathVariable int id) {
        // Optional 객체를 반환한다는 사실을 잊지 말자
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s} not found", id));
        }

        return user.get().getPosts();
    }

    /**
     * validation check => User에 필요한 태그가 있어야 합니다.
     * */
    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createUser(@PathVariable int id, @RequestBody Post post) {
        Optional<User> opt_user = userRepository.findById(id);

        if (!opt_user.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s} not found", id));
        }

        post.setUser(opt_user.get());
        Post savedPost = postRepository.save(post);


        HttpHeaders header = new HttpHeaders();
        header.setLocation(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedPost.getId())
                        .toUri()
        );

        return new ResponseEntity(savedPost, header, HttpStatus.ACCEPTED);
    }

}
