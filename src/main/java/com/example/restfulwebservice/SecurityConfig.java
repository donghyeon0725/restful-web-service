package com.example.restfulwebservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // /h2-console/** 으로 오는 모든 요청 허용
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        // cross site scripting disable 하는 기능
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("kneeth")
                .password("{noop}test1234") // {noop} 은 no operation으로, 어떠한 별도 동작도 없이 plain 텍스트 그대로 사용할 수 있도록 하는 옵션이다. 실제 어플리케이션은 이 부분을 적절한 인코딩 알고리즘으로 변경해서 사용할 수 있겠다.
                .roles("USER");
    }
}
