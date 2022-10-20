package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=UserDaoFactory.class)
class UserDaoTest {
    @Autowired
    ApplicationContext context;

    @Test
    void addAndSelect() throws SQLException {
        UserDao userDao = context.getBean("awsUserDao",UserDao.class);
        UserDao userDao2 = context.getBean("awsUserDao",UserDao.class);

        System.out.println(userDao);
        System.out.println(userDao2);

        String id="24";
        userDao.add(new User(id, "nunu","123qwe"));

        User user = userDao.findById(id);
        assertEquals("nunu",user.getName());
        assertEquals("123qwe",user.getPassword());
    }
}