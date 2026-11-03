package com.knowhub.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * ES 工具：文档入库 + 关键词搜索。
 * 实际项目里请把 ES_URL 改成你自己的地址。连不上 ES 时，search 会 fallback 到内存里的 DocumentMockDAO。
 */
public class EsUtil {

    public static final String ES_URL = "http://localhost:9200";
    public static final String INDEX  = "knowhub-docs";

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    /** 把一条文档推送到 ES（upsert）。 */
    public static boolean indexDocument(Integer docId, String title, String content) {
        JsonObject body = new JsonObject();
        body.addProperty("title", title);
        body.addProperty("content", content);
        body.addProperty("docId", docId);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(ES_URL + "/" + INDEX + "/_doc/" + docId))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(5))
                .PUT(HttpRequest.BodyPublishers.ofString(JsonUtil.toJson(body)))
                .build();
        try {
            HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.statusCode() >= 200 && resp.statusCode() < 300;
        } catch (Exception e) {
            System.err.println("[EsUtil] indexDocument 失败: " + e.getMessage());
            return false;
        }
    }

    /** 关键词搜索，返回命中的 docId 列表。 */
    public static List<Integer> search(String keyword, int size) {
        List<Integer> ids = new ArrayList<>();
        if (keyword == null || keyword.isBlank()) return ids;

        JsonObject query = new JsonObject();
        JsonObject match = new JsonObject();
        match.addProperty("content", keyword);
        JsonObject should = new JsonObject();
        should.add("match", match);

        JsonObject matchTitle = new JsonObject();
        matchTitle.addProperty("title", keyword);
        JsonObject titleShould = new JsonObject();
        titleShould.add("match", matchTitle);

        JsonObject qObj = new JsonObject();
        qObj.add("match", match);

        JsonObject body = new JsonObject();
        body.add("query", qObj);
        body.addProperty("size", size);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(ES_URL + "/" + INDEX + "/_search"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(5))
                .POST(HttpRequest.BodyPublishers.ofString(JsonUtil.toJson(body)))
                .build();
        try {
            HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                JsonObject root = JsonUtil.parse(resp.body());
                JsonArray hits = root.getAsJsonObject("hits").getAsJsonArray("hits");
                for (int i = 0; i < hits.size(); i++) {
                    JsonObject src = hits.get(i).getAsJsonObject().getAsJsonObject("_source");
                    ids.add(src.get("docId").getAsInt());
                }
            }
        } catch (Exception e) {
            System.err.println("[EsUtil] search 失败: " + e.getMessage());
        }
        return ids;
    }
}
