/*
 * Created on May 30, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.tdc.web.dto;

/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubtestKeyVO 
{
    String itemSetId;
    String adsItemSetId;
    String asmtHash;
    String asmtEncryptionKey;
    String item_encryption_key;
    String contentArea;
    String adaptive;
    
    public String getAdaptive() {
		return adaptive;
	}
	public void setAdaptive(String adaptive) {
		this.adaptive = adaptive;
	}
	public SubtestKeyVO() 
    {
        super();
    }
    /**
     * @return Returns the adsItemSetId.
     */
    public String getAdsItemSetId() {
        return adsItemSetId;
    }
    /**
     * @param adsItemSetId The adsItemSetId to set.
     */
    public void setAdsItemSetId(String adsItemSetId) {
        this.adsItemSetId = adsItemSetId;
    }
    /**
     * @return Returns the asmtEncryptionKey.
     */
    public String getAsmtEncryptionKey() {
        return asmtEncryptionKey;
    }
    /**
     * @param asmtEncryptionKey The asmtEncryptionKey to set.
     */
    public void setAsmtEncryptionKey(String asmtEncryptionKey) {
        this.asmtEncryptionKey = asmtEncryptionKey;
    }
    /**
     * @return Returns the asmtHash.
     */
    public String getAsmtHash() {
        return asmtHash;
    }
    /**
     * @param asmtHash The asmtHash to set.
     */
    public void setAsmtHash(String asmtHash) {
        this.asmtHash = asmtHash;
    }
    /**
     * @return Returns the item_encryption_key.
     */
    public String getItem_encryption_key() {
        return item_encryption_key;
    }
    /**
     * @param item_encryption_key The item_encryption_key to set.
     */
    public void setItem_encryption_key(String item_encryption_key) {
        this.item_encryption_key = item_encryption_key;
    }
    /**
     * @return Returns the itemSetId.
     */
    public String getItemSetId() {
        return itemSetId;
    }
    /**
     * @param itemSetId The itemSetId to set.
     */
    public void setItemSetId(String itemSetId) {
        this.itemSetId = itemSetId;
    }
	public String getContentArea() {
		return contentArea;
	}
	public void setContentArea(String contentArea) {
		this.contentArea = contentArea;
	}
}
