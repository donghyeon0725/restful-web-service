package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    // 사용자 정보는 포함하고 싶지 않음
    @JsonIgnore
    // Entity관의 sub와 main관계를 설정하지 않으면 에러가 남. Main : Sub = Parent : Child
    // 하나의 엔티티에 매핑이 됨. 즉, sub 타입임
    // 지연 로딩 될 수 있도록 설정함 => 필요한 시점에만 불러오도록 설정하겠다는 의미임
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
