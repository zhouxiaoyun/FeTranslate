package com.zhkeen.flyrise.fe.translate.utils;

import com.zhkeen.flyrise.fe.translate.model.JdbcConnectionModel;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUtil {

  private Logger logger = LoggerFactory.getLogger(DbUtil.class);

  private static final String TABLE_TRANSLATE = "TRANSLATE";

  private List<JdbcConnectionModel> models;

  public DbUtil(List<JdbcConnectionModel> models) {
    this.models = models;
  }

  private Connection getConnection(JdbcConnectionModel model)
      throws SQLException, ClassNotFoundException {
    Class.forName(model.getDriverName());
    return DriverManager
        .getConnection(model.getJdbcUrl(), model.getJdbcUser(), model.getJdbcPassword());
  }

  private String getTableName(int dbType) {
    if (dbType == 3) {
      return "`" + TABLE_TRANSLATE + "`";
    }
    return TABLE_TRANSLATE;
  }

  public TranslateResultModel findByMessage(String message)
      throws SQLException, ClassNotFoundException {

    String selectSql = "SELECT ID,CODE,CN,TW,US,UPDATETIME,UNITCODE,USERID FROM %s WHERE CN = ?";
    TranslateResultModel model = null;
    String tableName;
    for (JdbcConnectionModel jdbcConnectionModel : models) {
      Connection connection = getConnection(jdbcConnectionModel);
      PreparedStatement pstmt = connection.prepareStatement(
          String.format(selectSql, getTableName(jdbcConnectionModel.getDbType())));
      pstmt.setString(1, message);
      ResultSet rs = pstmt.executeQuery();
      model = buildResultModel(rs);
      connection.close();
    }
    return model;
  }

  public TranslateResultModel findByCode(String code) throws SQLException, ClassNotFoundException {
    String selectSql = "SELECT ID,CODE,CN,TW,US,UPDATETIME,UNITCODE,USERID FROM %s WHERE CODE = ?";
    logger.debug(selectSql);
    TranslateResultModel model = null;
    for (JdbcConnectionModel jdbcConnectionModel : models) {
      Connection connection = getConnection(jdbcConnectionModel);
      PreparedStatement pstmt = connection.prepareStatement(
          String.format(selectSql, getTableName(jdbcConnectionModel.getDbType())));
      pstmt.setString(1, code);
      ResultSet rs = pstmt.executeQuery();
      model = buildResultModel(rs);
      connection.close();
    }
    return model;
  }

  public void insertTranslate(TranslateResultModel model)
      throws SQLException, ClassNotFoundException {
    String selectSql = "SELECT ID FROM %s WHERE CODE = ? OR CN = ?";
    for (JdbcConnectionModel jdbcConnectionModel : models) {
      Connection connection = getConnection(jdbcConnectionModel);
      PreparedStatement pstmt = connection.prepareStatement(
          String.format(selectSql, getTableName(jdbcConnectionModel.getDbType())));
      pstmt.setString(1, model.getCode());
      pstmt.setString(2, model.getCn());

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        throw new SQLException("已经存在相同的编码[CODE]或者条目[CN]!");
      } else {
        int maxId = 1;
        String maxIdSql = String.format("SELECT MAX(ID) AS MAXID FROM %s", getTableName(jdbcConnectionModel.getDbType()));
        PreparedStatement pstmtMaxId = connection.prepareStatement(maxIdSql);
        ResultSet rsMaxId = pstmtMaxId.executeQuery();
        if (rsMaxId.next()) {
          maxId = rsMaxId.getInt("MAXID") + 1;
        }
        String insertSql = String
            .format(
                "INSERT INTO %s(ID,CODE,CN,TW,US,UPDATETIME,UNITCODE,USERID) VALUES(?,?,?,?,?,?,?,?)",
                getTableName(jdbcConnectionModel.getDbType()));
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
  }

  public void updateTranslate(TranslateResultModel model)
      throws SQLException, ClassNotFoundException {
    for (JdbcConnectionModel jdbcConnectionModel : models) {
      Connection connection = getConnection(jdbcConnectionModel);
      String selectSql = String
          .format("SELECT ID FROM %s WHERE (CODE = ? OR CN = ?) AND ID <> ?", getTableName(jdbcConnectionModel.getDbType()));
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
                "UPDATE %s SET CODE = ?, CN = ?, TW = ?, US = ?, UPDATETIME = ?, UNITCODE = ?, USERID = ? WHERE ID = ?",
                getTableName(jdbcConnectionModel.getDbType()));
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
