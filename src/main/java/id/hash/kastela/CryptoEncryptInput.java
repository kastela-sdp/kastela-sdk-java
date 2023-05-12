package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class CryptoEncryptInput {
  public enum EncryptionMode {
    AES_GCM,
    CHACHA20_POLY1305,
    XCHACHA20_POLY1305,
    RSA_OAEP
  }

  @SerializedName("key_id")
  private String keyID;
  private EncryptionMode mode;
  private Object[] plaintexts;

  public CryptoEncryptInput(String keyID, EncryptionMode mode, Object[] plaintexts) {
    this.keyID = keyID;
    this.mode = mode;
    this.plaintexts = plaintexts;
  }

  public String getKeyID() {
    return keyID;
  }

  public EncryptionMode getMode() {
    return mode;
  }

  public Object[] getPlaintexts() {
    return plaintexts;
  }

  public void setKeyID(String keyID) {
    this.keyID = keyID;
  }

  public void setMode(EncryptionMode mode) {
    this.mode = mode;
  }

  public void setPlaintexts(Object[] plaintexts) {
    this.plaintexts = plaintexts;
  }

}
