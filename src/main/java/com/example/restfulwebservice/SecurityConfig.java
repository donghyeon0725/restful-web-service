package com.example.restfulwebservice;

import com.example.restfulwebservice.security.JwtTokenProvider;
import com.example.restfulwebservice.securityFilter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 토큰을 생성하고 검증하는 컴포넌트를 완성했지만 실제로 이 컴포넌트를 이용하는 것은 인증 작업을 진행하는 Filter 입니다. 이 필터는 검증이 끝난 JWT로부터 유저정보를 받아와서 UsernamePasswordAuthenticationFilter 로 전달해야 할 것입니다.
 * https://webfirewood.tistory.com/115
 * */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*@Override
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
     */


    private final JwtTokenProvider jwtTokenProvider;

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                /* DB 요청에 대한 응답은 허용 */
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다


        http.headers().frameOptions().disable();
    }


}
