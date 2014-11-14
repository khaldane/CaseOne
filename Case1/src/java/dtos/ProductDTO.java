/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dtos;

/**
 *
 * @author Kat
 */
import java.io.Serializable;
public class ProductDTO implements Serializable {
    
    public ProductDTO(){
    }
    
    private String productcode;
    private int vendorno;
    private String vendorsku;
    private String productname;
    private String costprice;
    private String msrp;
    private int rop;
    private int eoq;
    private int qoh;
    private int qoo;
    //QRCODE BLOB
    //QRCODETXT
    
    public String getProductCode() {
        return this.productcode;
    }

    public void setProductCode(String inValue) {
        this.productcode = inValue;
    }
    
    public int getVendorno() {
        return this.vendorno;
    }

    public void setVendorno(int inValue) {
        this.vendorno = inValue;
    }

    public String getVendorsku() {
        return this.vendorsku;
    }

    public void setVendorsku(String inValue) {
        this.vendorsku = inValue;
    }

    public String getProductName() {
        return this.productname;
    }

    public void setProductName(String inValue) {
        this.productname = inValue;
    }

    public String getCostPrice() {
        return this.costprice;
    }

    public void setCostPrice(String inValue) {
        this.costprice = inValue;
    }

    public String getMsrp() {
        return this.msrp;
    }

    public void setMsrp(String inValue) {
        this.msrp = inValue;
    } 
    
    public int getRop() {
        return this.rop;
    }

    public void setRop(int inValue) {
        this.rop = inValue;
    }
    
    public int getEoq() {
        return this.eoq;
    }

    public void setEoq(int inValue) {
        this.eoq = inValue;
    }
    
    public int getQoh() {
        return this.qoh;
    }

    public void setQoh(int inValue) {
        this.qoh = inValue;
    }
    
    public int getQoo() {
        return this.qoo;
    }

    public void setQoo(int inValue) {
        this.qoo = inValue;
    }
}
