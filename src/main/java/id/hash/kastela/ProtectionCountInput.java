package id.hash.kastela;

import com.google.gson.annotations.SerializedName;

public class ProtectionCountInput {
  @SerializedName("protection_id")
  private String protectionID;
  private Object search;

  public ProtectionCountInput(String protectionID, Object search) {
    this.protectionID = protectionID;
    this.search = search;
  }

  public void setProtectionID(String protectionID) {
    this.protectionID = protectionID;
  }

  public void setSearch(Object search) {
    this.search = search;
  }

  public String getProtectionID() {
    return protectionID;
  }

  public Object getSearch() {
    return search;
  }
}
