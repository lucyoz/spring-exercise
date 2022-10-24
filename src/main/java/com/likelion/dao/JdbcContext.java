package com.likelion.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
    private DataSource dataSource;

    public JdbcContext(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void executeSql(String sql){
        this.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement(sql);
            }
        });
    }

    public void workWithStatementStrategy(StatementStrategy stmt){
        Connection c = null;
        PreparedStatement pstmt = null;
        try{
            c = dataSource.getConnection();
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
}
