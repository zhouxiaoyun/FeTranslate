package com.zhkeen.flyrise.fe.FeTranslate.Utils;

import com.intellij.openapi.vfs.VirtualFile;
import com.zhkeen.flyrise.fe.FeTranslate.Model.JdbcConnectionModel;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslatePluginManager {

    private final Logger logger = LoggerFactory.getLogger(TranslatePluginManager.class);

    private static TranslatePluginManager instance;
    private boolean needMultiLanguage = false;
    private String defaultLanguage;
    private Map<String, String> supportLanguageMap;
    private DbUtil dbUtil;

    private TranslatePluginManager() {
    }

    public static TranslatePluginManager getInstance() {
        if (instance == null) {
            instance = new TranslatePluginManager();
        }
        return instance;
    }

    public void inital(VirtualFile baseDir) {
        try {
            if (FileUtil.existsFeProjectFile(baseDir) && FileUtil.existsMultiLanguageFile(baseDir)) {
                needMultiLanguage = true;
            } else {
                needMultiLanguage = false;
            }
            this.defaultLanguage = FileUtil.readDefaultLanguage(baseDir);
            this.supportLanguageMap = FileUtil.readSupportLanguage(baseDir);
            JdbcConnectionModel jdbcConnectionModel = FileUtil.readJdbcConnectionModel(baseDir);
            this.dbUtil = new DbUtil(jdbcConnectionModel);
        } catch (Exception e) {
            needMultiLanguage = false;
            logger.error(e.getMessage());
        }
    }

    public DbUtil getDbUtil() {
        return dbUtil;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public boolean isNeedMultiLanguage() {
        return needMultiLanguage;
    }

    public Map<String, String> getSupportLanguageMap() {
        return supportLanguageMap;
    }
}
