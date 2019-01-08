package com.study.boot1.dao;

import com.study.boot1.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO {
    int insertUser(User user);
}
