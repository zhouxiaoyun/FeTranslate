package com.zhkeen.flyrise.fe.translate.model;

import java.sql.Date;
import java.util.Map;

/**
 * 翻译模型
 */
public class TranslateResultModel {

    /**
     * 编号
     */
    private String id;
    /**
     * 是否包含在JS文件中
     */
    private String isJs;
    /**
     * 翻译MAP，语言种类-翻译结果
     */
    private Map<String, String> translateMap;
    /**
     * 最后更新时间，方便维护时按逆序排列
     */
    private Date lastUpdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsJs() {
        return isJs;
    }

    public void setIsJs(String isJs) {
        this.isJs = isJs;
    }

    public Map<String, String> getTranslateMap() {
        return translateMap;
    }

    public void setTranslateMap(Map<String, String> translateMap) {
        this.translateMap = translateMap;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
