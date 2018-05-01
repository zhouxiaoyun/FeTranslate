package com.zhkeen.flyrise.fe.FeTranslate.Utils;

import com.zhkeen.flyrise.fe.FeTranslate.Model.JdbcConnectionModel;
import com.zhkeen.flyrise.fe.FeTranslate.Model.TranslateResultModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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

  public TranslateResultModel findByMessage(String defaultLanguage, String message,
      Map<String, String> supportLanguageMap) throws SQLException, ClassNotFoundException {
    String langFields = getSearchField(supportLanguageMap);
    String selectSql = String
        .format("SELECT ID, %s FROM %s WHERE %s = ?", langFields, TABLE_TRANSLATE, defaultLanguage);
    logger.debug(selectSql);
    Connection connection = getConnection();
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, message);
    ResultSet rs = pstmt.executeQuery();
    TranslateResultModel model = buildResultModel(rs, supportLanguageMap);
    connection.close();
    return model;
  }

  public TranslateResultModel findById(long id,
      Map<String, String> supportLanguageMap) throws SQLException, ClassNotFoundException {
    String langFields = getSearchField(supportLanguageMap);
    String selectSql = String
        .format("SELECT ID, %s FROM %s WHERE ID = ?", langFields, TABLE_TRANSLATE);
    logger.debug(selectSql);
    Connection connection = getConnection();
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setLong(1, id);
    ResultSet rs = pstmt.executeQuery();
    TranslateResultModel model = buildResultModel(rs, supportLanguageMap);
    connection.close();
    return model;
  }

  public void update(TranslateResultModel model) throws SQLException, ClassNotFoundException {
    String selectSql = String
        .format("SELECT ID FROM %s WHERE ID = ?", TABLE_TRANSLATE);
    Connection connection = getConnection();
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setLong(1, model.getId());
    ResultSet rs = pstmt.executeQuery();

    if (rs.next()) {
      String updateSql = "UPDATE FROM " + TABLE_TRANSLATE + " SET ";
      for (String lang : model.getTranslateMap().keySet()) {
        updateSql = updateSql + lang + " = ? ,";
      }
      if (updateSql.endsWith(",")) {
        updateSql = updateSql.substring(0, updateSql.length() + 1);
      }
      updateSql = updateSql + " WHERE ID = ?";
      logger.debug(updateSql);
      PreparedStatement psUpdate = connection.prepareStatement(updateSql);
      int i = 1;
      for (String lang : model.getTranslateMap().keySet()) {
        psUpdate.setString(i, model.getTranslateMap().get(lang));
        i++;
      }
      psUpdate.setLong(i, model.getId());
      psUpdate.executeUpdate();
    } else {
      String insertSql =
          "INSERT INTO " + TABLE_TRANSLATE + "(ID, " + getSearchField(model.getTranslateMap())
              + ") VALUES(";
      int j = model.getTranslateMap().size();
      for (int i = 0; i < j; i++) {
        insertSql = insertSql + "?,";
      }
      insertSql = insertSql + "?)";
      logger.debug(insertSql);
      PreparedStatement psInsert = connection.prepareStatement(insertSql);
      int m = 2;
      psInsert.setLong(1, model.getId());
      for (String lang : model.getTranslateMap().keySet()) {
        psInsert.setString(m, model.getTranslateMap().get(lang));
        m++;
      }
      psInsert.execute();
    }
  }

  private TranslateResultModel buildResultModel(ResultSet rs,
      Map<String, String> supportLanguageMap)
      throws SQLException {
    if (rs.next()) {
      TranslateResultModel model = new TranslateResultModel();
      Map<String, String> map = new LinkedHashMap<>();
      model.setId(rs.getLong("ID"));
      for (String lang : supportLanguageMap.keySet()) {
        map.put(lang, rs.getString(lang));
      }
      model.setTranslateMap(map);
      return model;
    } else {
      return null;
    }
  }

  private String getSearchField(Map<String, String> supportLanguageMap) {
    String field = "";
    for (String lang : supportLanguageMap.keySet()) {
      field = field + lang + ",";
    }
    field = field.substring(0, field.length() - 1);
    return field;
  }

}
