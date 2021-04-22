package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
// 상속을 위해서, 기본 생성자가 필요하다.
@NoArgsConstructor
// description
@ApiModel(description = "사용자 상세 정보")
@Entity
//@JsonFilter("UserInfo")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주(세요.")
    @ApiModelProperty(notes = "사용자 이름을 입력해 주세)요.")
    private String name;
    @Past
    private Date joinDate;

    // description
    @ApiModelProperty(notes = "비밀번호를 입력해 주세요.")
    private String password;
    private String ssn;


    // user 테이블과 매핑이 되어야 함
    @OneToMany(mappedBy = "user")
    private List<Post> posts;


    public User(Integer id, @Size(min = 2, message = "Name은 2글자 이상 입력해 주(세요.") String name, @Past Date joinDate, String password, String ssn) {
        this.id = id;
        this.name = name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }
}
