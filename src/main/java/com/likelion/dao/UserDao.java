package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private ConnectionMaker connectionMaker;

    public UserDao(){
        this.connectionMaker = new AwsConnectionMaker();
    }
    public UserDao(ConnectionMaker connectionMaker){
        this.connectionMaker = connectionMaker;
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt){
        Connection c = null;
        PreparedStatement pstmt = null;
        try{
            c = connectionMaker.makeConnection();
            pstmt = stmt.makePreparedStatement(c);
            pstmt.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally {      //error가 나도 실행되는 블럭
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

    public void add(User user) throws SQLException {
        AddStrategy addStrategy = new AddStrategy(user);
        jdbcContextWithStatementStrategy(addStrategy);

    }

    public User findById(String id) throws SQLException {
        Connection conn = connectionMaker.makeConnection();

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
        jdbcContextWithStatementStrategy(new DeleteAllStrategy());
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = connectionMaker.makeConnection();
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
