package com.zhkeen.flyrise.fe.translate.utils;

import com.zhkeen.flyrise.fe.translate.model.JdbcConnectionModel;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUtil {

  private Logger logger = LoggerFactory.getLogger(DbUtil.class);

  private static final String TABLE_TRANSLATE = "TRANSLATE";
  private static final String TABLE_TRANSLATE_RELATE = "TRANSLATE_RELATE";
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
        .format("SELECT ID, %s FROM %s WHERE %s = ?", langFields, TABLE_TRANSLATE,
            defaultLanguage);
    logger.debug(selectSql);
    Connection connection = getConnection();
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, message);
    ResultSet rs = pstmt.executeQuery();
    TranslateResultModel model = buildResultModel(rs, supportLanguageMap);
    connection.close();
    return model;
  }

  public TranslateResultModel findById(String id,
      Map<String, String> supportLanguageMap) throws SQLException, ClassNotFoundException {
    String langFields = getSearchField(supportLanguageMap);
    String selectSql = String
        .format("SELECT ID, %s FROM %s WHERE ID = ?", langFields, TABLE_TRANSLATE);
    logger.debug(selectSql);
    Connection connection = getConnection();
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, id);
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
    pstmt.setString(1, model.getId());
    ResultSet rs = pstmt.executeQuery();

    if (rs.next()) {
      String updateSql = "UPDATE " + TABLE_TRANSLATE + " SET ";
      for (String lang : model.getTranslateMap().keySet()) {
        updateSql = updateSql + " " + lang + " = ?,";
      }
      updateSql = updateSql + " LASTUPDATE = ? WHERE ID = ?";
      logger.debug(updateSql);
      PreparedStatement psUpdate = connection.prepareStatement(updateSql);
      int i = 1;
      for (String lang : model.getTranslateMap().keySet()) {
        psUpdate.setString(i, model.getTranslateMap().get(lang));
        i++;
      }
      psUpdate.setDate(i, model.getLastUpdate());
      psUpdate.setString(i + 1, model.getId());
      psUpdate.executeUpdate();
    } else {
      String insertSql =
          "INSERT INTO " + TABLE_TRANSLATE + "(ID, " + getSearchField(model.getTranslateMap())
              + ", LASTUPDATE) VALUES(?,";
      int j = model.getTranslateMap().size();
      for (int i = 0; i < j; i++) {
        insertSql = insertSql + "?,";
      }
      insertSql = insertSql + "?)";
      logger.debug(insertSql);
      PreparedStatement psInsert = connection.prepareStatement(insertSql);
      psInsert.setString(1, model.getId());
      int m = 2;
      for (String lang : model.getTranslateMap().keySet()) {
        psInsert.setString(m, model.getTranslateMap().get(lang));
        m++;
      }
      psInsert.setDate(m, model.getLastUpdate());
      psInsert.execute();
    }
    if (StringUtils.isNotEmpty(model.getFileType()) && StringUtils
        .isNotEmpty(model.getFileName())) {
      String updateRelate = " IF NOT EXISTS (SELECT ID FROM " + TABLE_TRANSLATE_RELATE
          + " WHERE ID = ? AND RELATE_TYPE = ? AND RELATION = ?) "
          + " BEGIN INSERT INTO " + TABLE_TRANSLATE_RELATE
          + "(ID, RELATE_TYPE, RELATION) VALUES(?, ?, ?) END";

      PreparedStatement psUpdateRelate = connection.prepareStatement(updateRelate);
      psUpdateRelate.setString(1, model.getId());
      psUpdateRelate.setString(2, model.getFileType());
      psUpdateRelate.setString(3, model.getFileName());
      psUpdateRelate.setString(4, model.getId());
      psUpdateRelate.setString(5, model.getFileType());
      psUpdateRelate.setString(6, model.getFileName());
      psUpdateRelate.execute();
    }
    connection.close();
  }

  private TranslateResultModel buildResultModel(ResultSet rs,
      Map<String, String> supportLanguageMap)
      throws SQLException {
    if (rs.next()) {
      TranslateResultModel model = new TranslateResultModel();
      Map<String, String> map = new LinkedHashMap<>();
      model.setId(rs.getString("ID"));
      for (String lang : supportLanguageMap.keySet()) {
        map.put(lang, rs.getString(lang));
      }
      model.setLastUpdate(rs.getDate("LASTUPDATE"));
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
