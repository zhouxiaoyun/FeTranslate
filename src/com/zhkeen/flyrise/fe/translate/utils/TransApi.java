package com.zhkeen.flyrise.fe.translate.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TransApi {

  private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

  private String appid;
  private String securityKey;

  public TransApi(String appid, String securityKey) {
    this.appid = appid;
    this.securityKey = securityKey;
  }

  public String getTransResult(String query, String from, String to)
      throws UnsupportedEncodingException {
    Map<String, String> params = buildParams(query, from, to);
    String result = HttpGet.get(TRANS_API_HOST, params);
    JsonParser parser = new JsonParser();
    return ((JsonObject) (parser.parse(result))).getAsJsonArray("trans_result").get(0)
        .getAsJsonObject().get("dst").getAsString();
  }

  private Map<String, String> buildParams(String query, String from, String to)
      throws UnsupportedEncodingException {
    Map<String, String> params = new HashMap<String, String>();
    params.put("q", query);
    params.put("from", from);
    params.put("to", to);

    params.put("appid", appid);

    // 随机数
    String salt = String.valueOf(System.currentTimeMillis());
    params.put("salt", salt);

    // 签名
    String src = appid + query + salt + securityKey; // 加密前的原文
    params.put("sign", MD5.md5(src));

    return params;
  }

}
