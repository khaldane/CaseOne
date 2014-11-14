/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dtos;

import java.math.BigDecimal;

/**
 *
 * @author Kat
 */
public class PurchaseOrderLineitemDTO {
    private BigDecimal costPrice;
    private String productCode;
    private int qty;
    private String productName;
    private BigDecimal ext;
    

    public void setPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public void setProductCode(String prodcode) {
        this.productCode = prodcode;
    }

    public void setQty(int qty) {
        this.qty= qty;
    }
    
    public void setProductName(String prodname){
       this.productName = prodname;
    }
    
    public String getProductName(){
       return this.productName;
    }

    public int getQty() {
        return this.qty;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public BigDecimal getPrice() {
        return this.costPrice;
    }

    public void setExt(BigDecimal bigDecimal) {
        this.ext = bigDecimal;
    }
    
    public BigDecimal getExt() {
        return this.ext;
    }
}
