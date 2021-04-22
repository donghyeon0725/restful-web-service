package com.example.restfulwebservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Http Code
// 2XX -> Ok
// 4XX -> 사용자 잘못
// 5XX -> Server 에러
/**
 * 에러 처리를 위한 클래스.
 * 에러에 관한 정보를 담고 있고, 응답할 때
 * 어떤 응답 코드를 담을지 결정할 중요한 정보
 * */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
