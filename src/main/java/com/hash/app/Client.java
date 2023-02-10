package com.hash.app;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;

import com.github.zafarkhaja.semver.Version;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.util.PemUtils;

public class Client {
  private String expectedKastelaVersion = "0.2";
  private String vaultPath = "/api/vault/";
  private String protectionPath = "/api/protection/";
  private String privacyProxy = "/api/proxy/";
  private String secureChannelPath = "/api/secure-channel/";

  private HttpClient httpClient;
  private String kastelaUrl;

  private static Gson gson = new Gson();

  public Client(String kastelaUrl, String clientCertPath, String clientKeyPath, String caCertPath) {
    this.kastelaUrl = kastelaUrl;
    X509ExtendedKeyManager keyManager = PemUtils.loadIdentityMaterial(Paths.get(clientCertPath),
        Paths.get(clientKeyPath));
    X509ExtendedTrustManager trustManager = PemUtils.loadTrustMaterial(Paths.get(caCertPath));

    SSLFactory sslFactory = SSLFactory.builder().withIdentityMaterial(keyManager).withTrustMaterial(trustManager)
        .build();

    SSLContext sslContext = sslFactory.getSslContext();

    httpClient = HttpClient.newBuilder().sslContext(sslContext).build();
  }

  private Map<String, Object> request(String method, URI url, Map<String, Object> body) throws Exception {
    BodyPublisher requestBody = BodyPublishers.noBody();
    if (body != null) {
      requestBody = BodyPublishers.ofString(gson.toJson(body));
    }

    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(url);
    switch (method) {
      case "get":
        requestBuilder.GET();
        break;
      case "post":
        requestBuilder.POST(requestBody);
        break;
      case "put":
        requestBuilder.PUT(requestBody);
        break;
      case "delete":
        requestBuilder.DELETE();
        break;
      default:
        throw new Exception("Method Not Supported");
    }
    HttpRequest request = requestBuilder.build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    Map<String, List<String>> headers = response.headers().map();
    String actualVersion = headers.get("x-kastela-version").get(0).substring(1);
    Version v = Version.valueOf(actualVersion);
    Map<String, Object> result = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
    }.getType());
    if (!v.satisfies(expectedKastelaVersion.concat("| 0.0.0"))) {
      throw new Exception("kastela server version mismatch, expeced: v".concat(expectedKastelaVersion)
          .concat(".x, actual: v").concat(actualVersion));
    }
    if (response.statusCode() != 200) {
      throw new Exception(result.get("error").toString());
    }
    return result;
  }

  public ArrayList<String> vaultStore(String vaultId, ArrayList<Object> data) throws Exception {
    Map<String, Object> payload = new HashMap<>();
    payload.put("data", data);
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(vaultPath).concat(vaultId).concat("/store")),
        payload);
    ArrayList<String> result = (ArrayList<String>) rawData.get("ids");
    return result;
  }

  public ArrayList<String> vaultFetch(String vaultId, String search, Integer size, String after) throws Exception {
    ArrayList<String> result = new ArrayList<>();
    String querySearch = "search=".concat(search);
    String querySize = "", queryAfter = "";
    if (size != null) {
      querySize = "&size=".concat(size.toString());
    }
    if (after != null) {
      queryAfter = "&after=".concat(after);
    }
    URI url = URI.create(kastelaUrl.concat(vaultPath).concat(vaultId).concat("?").concat(querySearch).concat(querySize)
        .concat(queryAfter));
    Map<String, Object> rawData = request("get", url, null);
    result = (ArrayList<String>) rawData.get("ids");
    return result;
  }

  public ArrayList<Object> vaultGet(String vaultId, ArrayList<String> ids) throws Exception {
    Map<String, Object> payload = new HashMap<>();
    payload.put("ids", ids);
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(vaultPath).concat(vaultId).concat("/get")), payload);
    ArrayList<Object> result = (ArrayList<Object>) rawData.get("data");
    return result;
  }

  public void vaultUpdate(String vaultId, String token, Map<String, Object> updatePayload) throws Exception {
    request("put",
        URI.create(kastelaUrl.concat(vaultPath).concat(vaultId).concat("/").concat(token)),
        updatePayload);
  }

  public void vaultDelete(String vaultId, String token) throws Exception {
    request("delete",
        URI.create(kastelaUrl.concat(vaultPath).concat(vaultId).concat("/").concat(token)), null);
  }

  public void protectionSeal(String protectionId, ArrayList<Object> ids) throws Exception {
    Map<String, Object> payload = new HashMap<>();
    payload.put("ids", ids);
    System.out.println(ids);
    System.out.println(protectionId);
    request("post", URI.create(kastelaUrl.concat(protectionPath).concat(protectionId).concat("/seal")), payload);
  }

  public ArrayList<Object> protectionOpen(String protectionId, ArrayList<Object> ids) throws Exception {
    Map<String, Object> payload = new HashMap<>();
    payload.put("ids", ids);
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(protectionPath).concat(protectionId).concat("/open")), payload);
    ArrayList<Object> result = (ArrayList<Object>) rawData.get("data");
    return result;
  }

  public Object privacyProxyRequest(String type, String url, String method, Map<String, Object> common,
      Map<String, Object> options) throws Exception {
    String[] types = new String[] { "json", "xml" };
    String[] methods = new String[] { "get", "post", "put", "delete", "patch" };
    if (!Arrays.asList(types).contains(type)) {
      throw new Exception("Invalid type.");
    }
    if (!Arrays.asList(methods).contains(method)) {
      throw new Exception("Invalid method.");
    }
    if ((type == "xml") && (options.get("rootTag") == null)) {
      throw new Exception("rootTag is required for xml.");
    }

    Map<String, Object> payload = new HashMap<>();
    payload.put("type", type);
    payload.put("url", url);
    payload.put("method", method);
    payload.put("common", common);
    payload.put("options", options);

    Map<String, Object> data = request("post", URI.create(kastelaUrl.concat(privacyProxy)), payload);

    return data;
  }

  public Map<String, Object> secureChannelBegin(String protectionId, String clientPublicKey, Integer ttl)
      throws Exception {
    Map<String, Object> result = new HashMap<>();
    Map<String, Object> payload = new HashMap<>();
    payload.put("protection_id", protectionId);
    payload.put("client_public_key", clientPublicKey);
    payload.put("ttl", ttl);

    Map<String, Object> data = request("post", URI.create(kastelaUrl.concat(secureChannelPath).concat("begin")),
        payload);
    result.put("id", data.get("id"));
    result.put("serverPublicKey", data.get("server_public_key"));
    return result;
  }

  public void secureChannelCommit(String secureChannelId) throws Exception {
    request("post", URI.create(kastelaUrl.concat(secureChannelPath).concat(secureChannelId).concat("/commit")), null);
  }
}