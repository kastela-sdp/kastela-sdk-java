package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class ProtectionSealInput {
  @SerializedName("protection_id")
  private String protectionID;
  @SerializedName("primary_keys")
  private Object[] primaryKeys;

  public ProtectionSealInput(String protectionID, Object[] primaryKeys) {
    this.protectionID = protectionID;
    this.primaryKeys = primaryKeys;
  }

  public void setProtectionID(String protectionID) {
    this.protectionID = protectionID;
  }

  public void setPrimaryKeys(Object[] primaryKeys) {
    this.primaryKeys = primaryKeys;
  }

  public String getProtectionID() {
    return protectionID;
  }

  public Object getPrimaryKeys() {
    return primaryKeys;
  }
}