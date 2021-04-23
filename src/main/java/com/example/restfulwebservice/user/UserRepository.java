package com.example.restfulwebservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DB와 연동 될 클래스임을 명시
 *
 * JpaRepository 를 상속하고, DB와 연동될 엔티티와 키 값을 전달한다.
 * */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * jwt 를 위해서, 이메일로 유저를 찾아오는 메소드 재정의
     * */
    Optional<User> findByEmail(String email);
}
