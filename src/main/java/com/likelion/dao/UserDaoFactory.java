package com.likelion.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class UserDaoFactory {

    @Bean
    public UserDao awsUserDao(){
        return new UserDao(awsDataSource());
    }

//    @Bean
//    public UserDao localUserDao(){
//        UserDao userDao = new UserDao(new LocalConnectionMaker());
//        return userDao;
//    }

    @Bean
    public DataSource awsDataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        Map<String, String> env = System.getenv();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl(env.get("DB_HOST"));
        dataSource.setUsername(env.get("DB_USER"));
        dataSource.setPassword(env.get("DB_PASSWORD"));
        return dataSource;
    }
}
