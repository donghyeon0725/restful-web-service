package com.example.restfulwebservice.user;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    private static int userCount = 0;

    /**
     * DB에 값이 있다고 가정하고 작업하기 위해서 static으로 기본 데이터를 세팅함
     * */
    static {
        users.add(new User(1,  "Kenneth", new Date(), "pass1", "701010-1111111"));
        users.add(new User(2,  "Alice", new Date(), "pass2", "801010-2222222"));
        users.add(new User(3,  "Elena", new Date(), "pass3", "901010-3333333"));
        userCount = users.size();
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++userCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User deleteById(int id) {
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.getId() == id) {
                // 해당 포인트의 값을 제거하고 user 를 리턴
                iterator.remove();
                return user;
            }
        }

        return null;
    }

    public User updateById(int id, User data) {
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.getId() == id) {
                user.setId(data.getId());
                user.setName(data.getName());
                return user;
            }
        }

        return null;
    }

}
