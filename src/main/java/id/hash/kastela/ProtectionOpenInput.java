package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class ProtectionOpenInput {
  @SerializedName("protection_id")
  private String protectionID;
  private Object[] tokens;

  public ProtectionOpenInput(String protectionID, Object[] tokens) {
    this.protectionID = protectionID;
    this.tokens = tokens;
  }

  public void setProtectionID(String protectionID) {
    this.protectionID = protectionID;
  }

  public void setTokens(Object[] tokens) {
    this.tokens = tokens;
  }

  public String getProtectionID() {
    return protectionID;
  }

  public Object getTokens() {
    return tokens;
  }
}