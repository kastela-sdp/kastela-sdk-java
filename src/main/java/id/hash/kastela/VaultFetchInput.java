package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class VaultFetchInput {
  @SerializedName("vault_id")
  private String vaultID;
  private Object search;
  private Integer size;
  private String after;

  public VaultFetchInput(String vaultID, Object search, Integer size, String after) {
    this.vaultID = vaultID;
    this.search = search;
    this.size = size;
    this.after = after;
  }

  public void setVaultID(String vaultID) {
    this.vaultID = vaultID;
  }

  public void setSearch(Object search) {
    this.search = search;
  }

  public void setAfter(String after) {
    this.after = after;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getVaultID() {
    return vaultID;
  }

  public Object getSearch() {
    return search;
  }

  public Integer getSize() {
    return size;
  }

  public String getAfter() {
    return after;
  }
}
