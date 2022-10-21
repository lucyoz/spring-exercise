package com.likelion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//전략패턴
public interface StatementStrategy  {

    PreparedStatement makePreparedStatement(Connection connection) throws SQLException;

}
