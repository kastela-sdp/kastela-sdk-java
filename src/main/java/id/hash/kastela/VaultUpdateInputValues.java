package id.hash.kastela;

public class VaultUpdateInputValues {
  private String token;
  private Object value;

  public VaultUpdateInputValues(String token, Object value) {
    this.token = token;
    this.value = value;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getToken() {
    return token;
  }

  public Object getValue() {
    return value;
  }
}
