package id.hash.kastela;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.hash.kastela.Client.PrivacyProxyRequestMethod;
import id.hash.kastela.Client.PrivacyProxyRequestType;
import id.hash.kastela.Client.SecureOperation;

import static spark.Spark.*;

public class AppTest {
    private static Gson gson = new Gson();

    public static void main(String[] args) throws Exception {

        Client kastelaClient = new Client("https://127.0.0.1:3100", "credentials/client.crt",
                "credentials/client.key", "credentials/ca.crt");

        port(4000);

        post("/api/vault/store", (req, res) -> {
            res.header("Content-Type", "application/json");
            ArrayList<Map<String, Object>> payload = gson.fromJson(req.body(),
                    new TypeToken<ArrayList<Map<String, Object>>>() {
                    }.getType());
            ArrayList<VaultStoreInput> input = new ArrayList<VaultStoreInput>();
            for (Map<String, Object> v : payload) {
                ArrayList<Object> values = (ArrayList<Object>) v.get("values");
                input.add(new VaultStoreInput((String) v.get("vault_id"), values.toArray()));
            }
            try {
                ArrayList<ArrayList<String>> resultStore = kastelaClient.vaultStore(input);
                String resultJson = gson.toJson(resultStore);
                return resultJson;
            } catch (Exception e) {
                res.status(500);
                return e.getMessage();
            }
        });// TODO: handle exception
        post("/api/vault/get", (req, res) -> {
            res.header("Content-Type", "application/json");
            ArrayList<Map<String, Object>> payload = gson.fromJson(req.body(),
                    new TypeToken<ArrayList<Map<String, Object>>>() {
                    }.getType());
            ArrayList<VaultGetInput> input = new ArrayList<VaultGetInput>();
            for (Map<String, Object> v : payload) {
                ArrayList<String> tokens = (ArrayList<String>) v.get("tokens");
                input.add(new VaultGetInput((String) v.get("vault_id"), tokens.toArray(new String[0])));
            }
            try {
                ArrayList<ArrayList<Object>> resultGet = kastelaClient.vaultGet(input);
                String result = gson.toJson(resultGet);
                return result;
            } catch (Exception e) {
                res.status(500);
                return e.getMessage();
            }
        });
        post("/api/vault/update", (req, res) -> {
            res.header("Content-Type", "application/json");
            ArrayList<Map<String, Object>> payload = gson.fromJson(req.body(),
                    new TypeToken<ArrayList<Map<String, Object>>>() {
                    }.getType());
            ArrayList<VaultUpdateInput> input = new ArrayList<VaultUpdateInput>();
            for (Map<String, Object> v : payload) {
                ArrayList<Map<String, Object>> rawV = (ArrayList<Map<String, Object>>) v.get("values");
                ArrayList<VaultUpdateInputValues> values = new ArrayList<VaultUpdateInputValues>();
                for (Map<String, Object> rV : rawV) {
                    values.add(new VaultUpdateInputValues((String) rV.get("token"), (Object) rV.get("value")));
                }

                input.add(new VaultUpdateInput((String) v.get("vault_id"), values));
            }
            try {
                kastelaClient.vaultUpdate(input);
                return "OK";
            } catch (Exception e) {
                res.status(500);
                return e.getMessage();
            }
        });
        post("/api/vault/fetch", (req, res) -> {
            res.header("Content-Type", "application/json");

            Map<String, String> p = gson.fromJson(req.body(),
                    new TypeToken<Map<String, String>>() {
                    }.getType());
            Integer size = null;
            if (p.get("size") != null) {
                size = Integer.parseInt(p.get("size"));
            }
            try {
                ArrayList<String> result = kastelaClient
                        .vaultFetch(new VaultFetchInput(p.get("vault_id"), p.get("search"),
                                size, p.get("after")));
                String resultJson = gson.toJson(result);
                return resultJson;
            } catch (Exception e) {
                res.status(500);
                return e.getMessage();
            }
        });
        post("/api/vault/delete", (req, res) -> {
            res.header("Content-Type", "application/json");
            ArrayList<Map<String, Object>> payload = gson.fromJson(req.body(),
                    new TypeToken<ArrayList<Map<String, Object>>>() {
                    }.getType());
            ArrayList<VaultDeleteInput> input = new ArrayList<VaultDeleteInput>();
            for (Map<String, Object> v : payload) {
                ArrayList<String> tokens = (ArrayList<String>) v.get("tokens");
                input.add(new VaultDeleteInput((String) v.get("vault_id"), tokens.toArray(new String[0])));
            }
            try {
                kastelaClient.vaultDelete(input);
                return "OK";
            } catch (Exception e) {
                res.status(500);
                return e.getMessage();
            }
        });

        post("/api/protection/seal", (req, res) -> {
            ArrayList<Map<String, Object>> payload = gson.fromJson(req.body(),
                    new TypeToken<ArrayList<Map<String, Object>>>() {
                    }.getType());
            ArrayList<ProtectionSealInput> input = new ArrayList<ProtectionSealInput>();
            for (Map<String, Object> v : payload) {
                ArrayList<String> pks = (ArrayList<String>) v.get("primary_keys");
                input.add(new ProtectionSealInput((String) v.get("protection_id"), pks.toArray()));
            }
            try {
                kastelaClient.protectionSeal(input);
                return "OK";
            } catch (Exception e) {
                res.status(500);
                return e.getMessage();
            }
        });

        post("/api/protection/open", (req, res) -> {
            res.header("Content-Type", "application/json");

            ArrayList<Map<String, Object>> payload = gson.fromJson(req.body(),
                    new TypeToken<ArrayList<Map<String, Object>>>() {
                    }.getType());
            ArrayList<ProtectionOpenInput> input = new ArrayList<ProtectionOpenInput>();
            for (Map<String, Object> v : payload) {
                ArrayList<String> tokens = (ArrayList<String>) v.get("tokens");
                input.add(new ProtectionOpenInput((String) v.get("protection_id"), tokens.toArray()));
            }
            try {
                ArrayList<ArrayList<Object>> result = kastelaClient.protectionOpen(input);
                String resultJson = gson.toJson(result);
                return resultJson;
            } catch (Exception e) {
                res.status(500);
                return e.getMessage();
            }
        });

        post("/api/proxy", (req, res) -> {
            res.header("Content-Type", "application/json");
            Map<String, Object> payload = gson.fromJson(req.body(), new TypeToken<Map<String, Object>>() {
            }.getType());
            Map<String, Object> options = new HashMap<>();
            options.put("headers", payload.get("headers"));
            options.put("params", payload.get("params"));
            options.put("body", payload.get("body"));
            options.put("query", payload.get("query"));
            options.put("rootTag", payload.get("rootTag"));

            Map<String, Object> common = new HashMap<>();
            common = (Map<String, Object>) payload.get("common");
            Object result = kastelaClient.privacyProxyRequest(
                    PrivacyProxyRequestType.valueOf(payload.get("type").toString()),
                    payload.get("url").toString(), PrivacyProxyRequestMethod.valueOf(payload.get("method").toString()),
                    common,
                    options);
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
                result = kastelaClient.secureProtectionInit(
                        SecureOperation.valueOf(payload.get("operation").toString()),
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