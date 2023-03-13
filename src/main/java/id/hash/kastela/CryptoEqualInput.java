package id.hash.kastela;

public class CryptoEqualInput {
  private String hash;
  private Object value;

  public CryptoEqualInput(String hash, Object value) {
    this.hash = hash;
    this.value = value;
  }

  public String getHash() {
    return hash;
  }

  public Object getValue() {
    return value;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public void setValue(Object value) {
    this.value = value;
  }
}
