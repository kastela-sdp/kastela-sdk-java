package com.hash.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hash.app.Client.secureOperation;

import static spark.Spark.*;

public class App {
    private static Gson gson = new Gson();

    public static void main(String[] args) throws Exception {

        Client kastelaClient = new Client("https://127.0.0.1:3100", "credentials/client.crt",
                "credentials/client.key", "credentials/ca.crt");

        port(4000);

        post("/api/vault/:vaultId/store", (req, res) -> {
            res.header("Content-Type", "application/json");
            ArrayList<Object> payload = gson.fromJson(req.body(), new TypeToken<ArrayList<Object>>() {
            }.getType());
            System.out.println(payload);
            ArrayList<String> resultStore = kastelaClient.vaultStore(req.params(":vaultId"), payload);
            String resultJson = gson.toJson(resultStore);
            return resultJson;
        });
        post("/api/vault/:vaultId/get", (req, res) -> {
            res.header("Content-Type", "application/json");
            ArrayList<String> ids = gson.fromJson(req.body(), new TypeToken<ArrayList<String>>() {
            }.getType());
            ArrayList<Object> resultGet = kastelaClient.vaultGet(req.params(":vaultId"), ids);
            String result = gson.toJson(resultGet);
            return result;
        });
        put("/api/vault/:vaultId/:token", (req, res) -> {
            res.header("Content-Type", "application/json");
            Map<String, Object> payload = gson.fromJson(req.body(), new TypeToken<HashMap<String, Object>>() {
            }.getType());
            kastelaClient.vaultUpdate(req.params(":vaultId"), req.params(":token"), payload);
            return "OK";
        });
        get("/api/vault/:vaultId", (req, res) -> {
            res.header("Content-Type", "application/json");

            Integer size = 0;
            if (req.queryParams("size") != null) {
                size = Integer.parseInt(req.queryParams("size"));
            }
            ArrayList<String> result = kastelaClient.vaultFetch(req.params(":vaultId"), req.queryParams("search"),
                    size, req.queryParams("after"));
            String resultJson = gson.toJson(result);
            return resultJson;
        });
        delete("/api/vault/:vaultId/:token", (req, res) -> {
            res.header("Content-Type", "application/json");
            kastelaClient.vaultDelete(req.params(":vaultId"), req.params(":token"));
            return "OK";
        });

        post("/api/protection/:protectionId/seal", (req, res) -> {
            res.header("Content-Type", "application/json");
            ArrayList<Object> ids = gson.fromJson(req.body(), new TypeToken<ArrayList<Object>>() {
            }.getType());
            kastelaClient.protectionSeal(req.params(":protectionId"), ids);
            return "OK";
        });
        post("/api/protection/:protectionId/open", (req, res) -> {
            res.header("Content-Type", "application/json");
            ArrayList<Object> ids = gson.fromJson(req.body(), new TypeToken<ArrayList<Object>>() {
            }.getType());
            ArrayList<Object> result = kastelaClient.protectionOpen(req.params("protectionId"), ids);
            String resultJson = gson.toJson(result);
            return resultJson;
        });

        post("/api/proxy", (req, res) -> {
            res.header("Content-Type", "application/json");
            Map<String, Object> payload = gson.fromJson(req.body(), new TypeToken<Map<String, Object>>() {
            }.getType());
            Map<String, Object> options = new HashMap<String, Object>();
            options.put("headers", payload.get("headers"));
            options.put("params", payload.get("params"));
            options.put("body", payload.get("body"));
            options.put("query", payload.get("query"));
            options.put("rootTag", payload.get("rootTag"));

            Map<String, Object> common = new HashMap<>();
            common = (Map<String, Object>) payload.get("common");
            Object result = kastelaClient.privacyProxyRequest(payload.get("type").toString(),
                    payload.get("url").toString(), payload.get("method").toString(), common, options);
            String resultJson = gson.toJson(result);
            return resultJson;
        });

        post("/api/secure/protection/init", (req, res) -> {
            res.header("Content-Type", "application/json");
            Map<String, Object> result = new HashMap<>();
            try {
                Map<String, Object> payload = gson.fromJson(req.body(), new TypeToken<Map<String, Object>>() {
                }.getType());
                Integer ttl = 0;
                Double ttlD = (Double) payload.get("ttl");
                if (payload.get("ttl") != null) {
                    ttl = ttlD.intValue();
                }
                result = kastelaClient.secureProtectionInit(secureOperation.valueOf(payload.get("operation").toString()),
                        (ArrayList<String>) payload.get("protection_ids"), ttl);
            } catch (Exception e) {
                result.put("error", e.toString());
                res.status(500);
            }
            String resultJson = gson.toJson(result);
            return resultJson;
        });
        post("/api/secure/protection/commit", (req, res) -> {
            res.header("Content-Type", "application/json");
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> payload = gson.fromJson(req.body(), new TypeToken<Map<String, Object>>() {
            }.getType());
            try {
                kastelaClient.secureProtectionCommit(payload.get("credential").toString());
                result.put("status", "OK");
            } catch (Exception e) {
                result.put("error", e.toString());
                res.status(500);
            }
            String resultJson = gson.toJson(result);
            return resultJson;
        });
    }
}