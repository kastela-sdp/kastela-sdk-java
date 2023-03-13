package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class CryptoSignInput {
  @SerializedName("key_id")
  private String keyID;
  private Object[] values;

  public CryptoSignInput(String keyID, Object[] values) {
    this.keyID = keyID;
    this.values = values;
  }

  public String getKeyID() {
    return keyID;
  }

  public Object[] getValues() {
    return values;
  }

  public void setKeyID(String keyID) {
    this.keyID = keyID;
  }

  public void setValues(Object[] values) {
    this.values = values;
  }
}
