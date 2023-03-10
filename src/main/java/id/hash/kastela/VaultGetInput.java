package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class VaultGetInput {
  @SerializedName("vault_id")
  private String vaultID;
  private String[] tokens;

  public VaultGetInput(String vaultID, String[] tokens) {
    this.vaultID = vaultID;
    this.tokens = tokens;
  }

  public void setVaultID(String vaultID) {
    this.vaultID = vaultID;
  }

  public void setTokens(String[] tokens) {
    this.tokens = tokens;
  }

  public String getVaultID() {
    return vaultID;
  }

  public String[] getTokens() {
    return tokens;
  }
}