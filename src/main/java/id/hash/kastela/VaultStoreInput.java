package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class VaultStoreInput {
  @SerializedName("vault_id")
  private String vaultID;
  private Object[] values;

  public VaultStoreInput(String vaultID, Object[] values) {
    this.vaultID = vaultID;
    this.values = values;
  }

  public void setVaultID(String vaultID) {
    this.vaultID = vaultID;
  }

  public void setValues(Object[] values) {
    this.values = values;
  }

  public String getVaultID() {
    return vaultID;
  }

  public Object[] getValues() {
    return values;
  }
}

