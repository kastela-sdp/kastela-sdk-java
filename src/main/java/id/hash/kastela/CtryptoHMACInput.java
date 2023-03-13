package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class CtryptoHMACInput {
  public enum HashMode {
    BLAKE2B_256,
    BLAKE2B_512,
    BLAKE2S_256,
    BLAKE3_256,
    BLAKE3_512,
    SHA256,
    SHA512,
    SHA3_256,
    SHA3_512
  }

  @SerializedName("key_id")
  private String keyID;
  private HashMode mode;
  private Object[] values;

  public CtryptoHMACInput(String keyID, HashMode mode, Object[] values) {
    this.keyID = keyID;
    this.mode = mode;
    this.values = values;
  }

  public String getKeyID() {
    return keyID;
  }

  public HashMode getMode() {
    return mode;
  }

  public Object[] getValues() {
    return values;
  }

  public void setKeyID(String keyID) {
    this.keyID = keyID;
  }

  public void setMode(HashMode mode) {
    this.mode = mode;
  }

  public void setValues(Object[] values) {
    this.values = values;
  }

}
