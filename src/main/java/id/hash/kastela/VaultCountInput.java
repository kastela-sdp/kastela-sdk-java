package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class VaultCountInput {
  @SerializedName("vault_id")
  private String vaultID;
  private Object search;

  public VaultCountInput(String vaultID, Object search) {
    this.vaultID = vaultID;
    this.search = search;
  }

  public void setVaultID(String vaultID) {
    this.vaultID = vaultID;
  }

  public void setSearch(Object search) {
    this.search = search;
  }

  public String getVaultID() {
    return vaultID;
  }

  public Object getSearch() {
    return search;
  }
}
