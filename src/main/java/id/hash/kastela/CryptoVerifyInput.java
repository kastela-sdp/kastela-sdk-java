package id.hash.kastela;

public class CryptoVerifyInput {
  private String signature;
  private Object value;

  public CryptoVerifyInput(String signature, Object value) {
    this.signature = signature;
    this.value = value;
  }

  public String getSignature() {
    return signature;
  }

  public Object getValue() {
    return value;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public void setValue(Object value) {
    this.value = value;
  }
}
