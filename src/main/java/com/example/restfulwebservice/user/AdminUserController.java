package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
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

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {
        List<User> users = service.findAll();
        // 간단한 필터 생성
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");

        // 위 필터를 우리가 사용 가능한 형태로 변경. UserInfo 을 대상으로 filter를 적용하겠다는 의미
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    /**
     * URI를 통한 버전 관리
     * */
    // GET /admin/users/1 or /admin/v1/users/10
    @GetMapping("/v1/users/{id}")
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
             throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // 간단한 필터 생성
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");

        // 위 필터를 우리가 사용 가능한 형태로 변경. UserInfo 을 대상으로 filter를 적용하겠다는 의미
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    // GET /admin/users/1 or /admin/v1/users/10
    @GetMapping("/v2/users/{id}")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        UserV2 userV2 = new UserV2();

        // 스프링 프레임워크에서 제공하는 기능. 인스턴스 생성, 공통적인 필드 카피하는 기능 등등이 있다.
        BeanUtils.copyProperties(user, userV2);
        userV2.setGrade("VIP");

        // 간단한 필터 생성
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");

        // 위 필터를 우리가 사용 가능한 형태로 변경. UserInfo 을 대상으로 filter를 적용하겠다는 의미
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }

    /**
     * 파라미터를 통한 버전 관리
     * */
    @GetMapping(value = "/users/{id}/", params = "version=1")
    public MappingJacksonValue retrieveUserByP1(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // 간단한 필터 생성
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate");

        // 위 필터를 우리가 사용 가능한 형태로 변경. UserInfo 을 대상으로 filter를 적용하겠다는 의미
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping(value = "/users/{id}/", params = "version=2")
    public MappingJacksonValue retrieveUserByP2(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        UserV2 userV2 = new UserV2();

        // 스프링 프레임워크에서 제공하는 기능. 인스턴스 생성, 공통적인 필드 카피하는 기능 등등이 있다.
        BeanUtils.copyProperties(user, userV2);
        userV2.setGrade("VIP");

        // 간단한 필터 생성
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");

        // 위 필터를 우리가 사용 가능한 형태로 변경. UserInfo 을 대상으로 filter를 적용하겠다는 의미
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }



    /**
     * 해더를 통한 버전 관리
     * */
    @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1")
    public MappingJacksonValue retrieveUserByH1(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // 간단한 필터 생성
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate");

        // 위 필터를 우리가 사용 가능한 형태로 변경. UserInfo 을 대상으로 filter를 적용하겠다는 의미
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2")
    public MappingJacksonValue retrieveUserByH2(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        UserV2 userV2 = new UserV2();

        // 스프링 프레임워크에서 제공하는 기능. 인스턴스 생성, 공통적인 필드 카피하는 기능 등등이 있다.
        BeanUtils.copyProperties(user, userV2);
        userV2.setGrade("VIP");

        // 간단한 필터 생성
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");

        // 위 필터를 우리가 사용 가능한 형태로 변경. UserInfo 을 대상으로 filter를 적용하겠다는 의미
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }

    /**
     * MIME 타입을 이용한 버전관리
     * */
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json")
    public MappingJacksonValue retrieveUserByM1(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // 간단한 필터 생성
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate");

        // 위 필터를 우리가 사용 가능한 형태로 변경. UserInfo 을 대상으로 filter를 적용하겠다는 의미
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUserByM2(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        UserV2 userV2 = new UserV2();

        // 스프링 프레임워크에서 제공하는 기능. 인스턴스 생성, 공통적인 필드 카피하는 기능 등등이 있다.
        BeanUtils.copyProperties(user, userV2);
        userV2.setGrade("VIP");

        // 간단한 필터 생성
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");

        // 위 필터를 우리가 사용 가능한 형태로 변경. UserInfo 을 대상으로 filter를 적용하겠다는 의미
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }
}
