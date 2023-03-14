package id.hash.kastela;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.util.PemUtils;

public class Client {
  private String vaultPath = "/api/vault/";
  private String protectionPath = "/api/protection/";
  private String privacyProxy = "/api/proxy/";
  private String securePath = "/api/secure/";
  private String cryptoPath = "/api/crypto/";

  private HttpClient httpClient;
  private String kastelaUrl;

  private static Gson gson = new Gson();

  public enum SecureOperation {
    READ,
    WRITE
  }

  public enum PrivacyProxyRequestType {
    json,
    xml
  }

  public enum PrivacyProxyRequestMethod {
    get,
    post,
    put,
    delete,
    patch
  }

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

  private Map<String, Object> request(String method, URI url, Object body) throws Exception {
    System.out.println(body.getClass());
    BodyPublisher requestBody = BodyPublishers.noBody();
    if (body != null) {
      requestBody = BodyPublishers.ofString(gson.toJson(body));
    }
    System.out.println(gson.toJson(body));
    System.out.println(requestBody);
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
    Map<String, Object> result = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
    }.getType());
    if (response.statusCode() != 200) {
      throw new Exception(result.get("error").toString());
    }
    System.out.println(result);
    return result;
  }

  public ArrayList<ArrayList<String>> cryptoEncrypt(ArrayList<CryptoEncryptInput> input) throws Exception {
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(cryptoPath).concat("encrypt")),
        input);
    ArrayList<ArrayList<String>> result = (ArrayList<ArrayList<String>>) rawData.get("ciphertexts");
    return result;
  }

  public ArrayList<Object> cryptoDecrypt(ArrayList<String> input) throws Exception {
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(cryptoPath).concat("decrypt")),
        input);
    ArrayList<Object> result = (ArrayList<Object>) rawData.get("plaintexts");
    return result;
  }

  public ArrayList<ArrayList<String>> cryptoHMAC(ArrayList<CryptoHMACInput> input) throws Exception {
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(cryptoPath).concat("hmac")),
        input);
    ArrayList<ArrayList<String>> result = (ArrayList<ArrayList<String>>) rawData.get("hashes");
    return result;
  }

  public ArrayList<Boolean> cryptoEqual(ArrayList<CryptoEqualInput> input) throws Exception {
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(cryptoPath).concat("equal")),
        input);
    ArrayList<Boolean> result = (ArrayList<Boolean>) rawData.get("result");
    return result;
  }

  public ArrayList<ArrayList<String>> cryptoSign(ArrayList<CryptoSignInput> input) throws Exception {
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(cryptoPath).concat("sign")),
        input);
    ArrayList<ArrayList<String>> result = (ArrayList<ArrayList<String>>) rawData.get("signatures");
    return result;
  }

  public ArrayList<Boolean> cryptoVerify(ArrayList<CryptoVerifyInput> input) throws Exception {
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(cryptoPath).concat("verify")),
        input);
    ArrayList<Boolean> result = (ArrayList<Boolean>) rawData.get("result");
    return result;
  }

  public ArrayList<ArrayList<String>> vaultStore(ArrayList<VaultStoreInput> input) throws Exception {
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(vaultPath).concat("store")),
        input);
    ArrayList<ArrayList<String>> result = (ArrayList<ArrayList<String>>) rawData.get("tokens");
    return result;
  }

  public ArrayList<String> vaultFetch(VaultFetchInput input) throws Exception {
    URI url = URI.create(
        kastelaUrl.concat(vaultPath).concat("fetch"));
    Map<String, Object> rawData = request("post", url, input);
    ArrayList<String> result = (ArrayList<String>) rawData.get("tokens");
    return result;
  }

  public ArrayList<ArrayList<Object>> vaultGet(ArrayList<VaultGetInput> input) throws Exception {
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(vaultPath).concat("get")), input);
    ArrayList<ArrayList<Object>> result = (ArrayList<ArrayList<Object>>) rawData.get("values");
    return result;
  }

  public void vaultUpdate(ArrayList<VaultUpdateInput> input) throws Exception {
    request("post",
        URI.create(kastelaUrl.concat(vaultPath).concat("update")),
        input);
  }

  public void vaultDelete(ArrayList<VaultDeleteInput> input) throws Exception {
    request("post",
        URI.create(kastelaUrl.concat(vaultPath).concat("delete")), input);
  }

  public void protectionSeal(ArrayList<ProtectionSealInput> input) throws Exception {
    request("post", URI.create(kastelaUrl.concat(protectionPath).concat("seal")), input);
  }

  public ArrayList<ArrayList<Object>> protectionOpen(ArrayList<ProtectionOpenInput> input) throws Exception {
    Map<String, Object> rawData = request("post",
        URI.create(kastelaUrl.concat(protectionPath).concat("open")), input);
    ArrayList<ArrayList<Object>> result = (ArrayList<ArrayList<Object>>) rawData.get("data");
    return result;
  }

  public Object privacyProxyRequest(PrivacyProxyRequestType type, String url, PrivacyProxyRequestMethod method,
      Map<String, Object> common,
      Map<String, Object> options) throws Exception {

    Map<String, Object> payload = new HashMap<>();
    payload.put("type", type.toString());
    payload.put("url", url);
    payload.put("method", method.toString());
    payload.put("common", common);
    payload.put("options", options);

    Map<String, Object> data = request("post", URI.create(kastelaUrl.concat(privacyProxy)), payload);

    return data;
  }

  public Map<String, Object> secureProtectionInit(
      SecureOperation operation, ArrayList<String> protectionIds, Integer ttl) throws Exception {
    Map<String, Object> result = new HashMap<>();
    Map<String, Object> payload = new HashMap<>();
    payload.put("operation", operation);
    payload.put("protection_ids", protectionIds);
    payload.put("ttl", ttl);

    Map<String, Object> response = request("post", URI.create(kastelaUrl.concat(securePath).concat("protection/init")),
        payload);
    result.put("credential", response.get("credential"));

    return result;
  }

  public void secureProtectionCommit(String credential) throws Exception {
    Map<String, Object> payload = new HashMap<>();
    payload.put("credential", credential);

    request("post", URI.create(kastelaUrl.concat(securePath).concat("protection/commit")), payload);
  }
}