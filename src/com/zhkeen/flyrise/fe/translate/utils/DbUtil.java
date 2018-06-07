package com.zhkeen.flyrise.fe.translate.utils;

import com.zhkeen.flyrise.fe.translate.model.JdbcConnectionModel;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUtil {

  private Logger logger = LoggerFactory.getLogger(DbUtil.class);

  private static final String TABLE_TRANSLATE = "TRANSLATE";

  private JdbcConnectionModel model;

  public DbUtil(JdbcConnectionModel model) {
    this.model = model;
  }

  private Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName(model.getDriverName());
    Connection connection = DriverManager
        .getConnection(model.getJdbcUrl(), model.getJdbcUser(), model.getJdbcPassword());
    return connection;
  }

  public TranslateResultModel findByMessage(String message)
      throws SQLException, ClassNotFoundException {

    String selectSql = String
        .format("SELECT ID,CODE,CN,TW,US,UPDATETIME,UNITCODE,USERID FROM %s WHERE CN = ?",
            TABLE_TRANSLATE);
    Connection connection = getConnection();
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, message);
    ResultSet rs = pstmt.executeQuery();
    TranslateResultModel model = buildResultModel(rs);
    connection.close();
    return model;
  }

  public TranslateResultModel findByCode(String code) throws SQLException, ClassNotFoundException {
    String selectSql = String
        .format("SELECT ID,CODE,CN,TW,US,UPDATETIME,UNITCODE,USERID FROM %s WHERE CODE = ?",
            TABLE_TRANSLATE);
    logger.debug(selectSql);
    Connection connection = getConnection();
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, code);
    ResultSet rs = pstmt.executeQuery();
    TranslateResultModel model = buildResultModel(rs);
    connection.close();
    return model;
  }

  public void insertTranslate(TranslateResultModel model)
      throws SQLException, ClassNotFoundException {
    String selectSql = String
        .format("SELECT ID FROM %s WHERE CODE = ? OR CN = ?", TABLE_TRANSLATE);
    Connection connection = getConnection();
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, model.getCode());
    pstmt.setString(2, model.getCn());

    ResultSet rs = pstmt.executeQuery();
    if (rs.next()) {
      throw new SQLException("已经存在相同的编码[CODE]或者条目[CN]!");
    } else {
      int maxId = 1;
      String maxIdSql = String.format("SELECT MAX(ID) AS MAXID FROM %s", TABLE_TRANSLATE);
      PreparedStatement pstmtMaxId = connection.prepareStatement(maxIdSql);
      ResultSet rsMaxId = pstmtMaxId.executeQuery();
      if (rsMaxId.next()) {
        maxId = rsMaxId.getInt("MAXID") + 1;
      }
      String insertSql = String
          .format(
              "INSERT INTO %s(ID,CODE,CN,TW,US,UPDATETIME,UNITCODE,USERID) VALUES(?,?,?,?,?,?,?,?)",
              TABLE_TRANSLATE);
      PreparedStatement pstmtInsert = connection.prepareStatement(insertSql);
      pstmtInsert.setInt(1, maxId);
      pstmtInsert.setString(2, model.getCode());
      pstmtInsert.setString(3, model.getCn());
      pstmtInsert.setString(4, model.getTw());
      pstmtInsert.setString(5, model.getUs());
      pstmtInsert.setString(6, model.getUpdateTime());
      pstmtInsert.setString(7, model.getUnitCode());
      pstmtInsert.setString(8, model.getUserId());
      pstmtInsert.executeUpdate();
    }
    connection.close();
  }

  public void updateTranslate(TranslateResultModel model)
      throws SQLException, ClassNotFoundException {

    Connection connection = getConnection();
    String selectSql = String
        .format("SELECT ID FROM %s WHERE (CODE = ? OR CN = ?) AND ID <> ?", TABLE_TRANSLATE);
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, model.getCode());
    pstmt.setString(2, model.getCn());
    pstmt.setInt(3, model.getId());

    ResultSet rs = pstmt.executeQuery();
    if (rs.next()) {
      throw new SQLException("已经存在相同的编码[CODE]或者条目[CN]!");
    } else {
      String insertSql = String
          .format(
              "UPDATE %s SET CODE = ?, CN = ?, TW = ?, US = ?, UPDATETIME = ?, UNITCODE = ?, USERID = ? WHERE ID = ?", TABLE_TRANSLATE);
      PreparedStatement pstmtUpdate = connection.prepareStatement(insertSql);
      pstmtUpdate.setString(1, model.getCode());
      pstmtUpdate.setString(2, model.getCn());
      pstmtUpdate.setString(3, model.getTw());
      pstmtUpdate.setString(4, model.getUs());
      pstmtUpdate.setString(5, model.getUpdateTime());
      pstmtUpdate.setString(6, model.getUnitCode());
      pstmtUpdate.setString(7, model.getUserId());
      pstmtUpdate.setInt(8, model.getId());
      pstmtUpdate.executeUpdate();
      connection.close();
    }
  }

  private TranslateResultModel buildResultModel(ResultSet rs)
      throws SQLException {
    if (rs.next()) {
      TranslateResultModel model = new TranslateResultModel();
      model.setId(rs.getInt("ID"));
      model.setCode(rs.getString("CODE"));
      model.setCn(rs.getString("CN"));
      model.setTw(rs.getString("TW"));
      model.setUs(rs.getString("US"));
      model.setUpdateTime(rs.getString("UPDATETIME"));
      model.setUnitCode(rs.getString("UNITCODE"));
      model.setUserId(rs.getString("USERID"));
      return model;
    } else {
      return null;
    }
  }

}
