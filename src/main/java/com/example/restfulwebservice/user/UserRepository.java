package com.example.restfulwebservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DB와 연동 될 클래스임을 명시
 *
 * JpaRepository 를 상속하고, DB와 연동될 엔티티와 키 값을 전달한다.
 * */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
