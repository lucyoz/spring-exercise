package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private DataSource dataSource;
    private JdbcContext jdbcContext;

    public UserDao(DataSource dataSource){
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext(dataSource);
    }

    public void add(User user) throws SQLException {
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO users(id, name, password) values(?, ?, ?)"
                );
                ps.setString(1, user.getId());
                ps.setString(2,user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
    }

    public User findById(String id) throws SQLException {
        Connection conn = dataSource.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, name, password FROM users WHERE id = ?"
        );

        ps.setString(1, id);


        ResultSet rs = ps.executeQuery();
        User user = null;
        if(rs.next()){
            user = new User(rs.getString("id"),
                    rs.getString("name"), rs.getString("password"));
        }

        rs.close();
        ps.close();
        conn.close();

        if (user==null) throw new EmptyResultDataAccessException(1);

        return user;

    }



    public void deleteAll() throws SQLException {
        this.jdbcContext.executeSql("delete from users");
        //쿼리만 넘기게 함.
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            pstmt = c.prepareStatement("SELECT count(*) FROM users");
            rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e){
                }
            }
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (c!=null){
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }

    }


}
