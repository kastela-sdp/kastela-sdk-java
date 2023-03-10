package id.hash.kastela;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class VaultUpdateInput {
  @SerializedName("vault_id")
  private String vaultID;
  private ArrayList<VaultUpdateInputValues> values;

  public VaultUpdateInput(String vaultID, ArrayList<VaultUpdateInputValues> values) {
    this.vaultID = vaultID;
    this.values = values;
  }

  public void setVaultID(String vaultID) {
    this.vaultID = vaultID;
  }

  public void setValues(ArrayList<VaultUpdateInputValues> values) {
    this.values = values;
  }

  public String getVaultID() {
    return vaultID;
  }

  public ArrayList<VaultUpdateInputValues> getValues() {
    return values;
  }

}