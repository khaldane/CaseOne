/*
 * Name: Katherine Haldane
 * Date: Oct 17, 2014
 * Description: Set the purchase order dto
 */
package dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
public class PurchaseOrderDTO implements Serializable {
    
    private BigDecimal total;
    private ArrayList<PurchaseOrderLineitemDTO> items;
    private int vendorno;
    private int pono;
    private BigDecimal amount;
    private Date setPodate;
    
    public PurchaseOrderDTO(){
    }
    
    //Get Total
    public BigDecimal getTotal()
    {
        return this.total;
    }
    
    //Set Total
    public void setTotal(BigDecimal  i){
        this.total = i;
    }
    
    //Get Items
    public ArrayList<PurchaseOrderLineitemDTO> getItems()
    {
        return this.items;
    }
    
    //Set Items
     public void setItems(ArrayList<PurchaseOrderLineitemDTO> i)
    {
        this.items = i;
    }
    
     //Get Vendorno
    public int getVendorno(){
        return this.vendorno;
    }
    
    //Set Vendorno
    public void setVendorno(int vno){
        this.vendorno = vno;
    }
    
    //Set Ponumber
    public void setPonumber(int ponumber) {
        this.pono = ponumber;
    }
    
    //Get ponumber
    public int getPonumber() {
        return this.pono;
    }
    
    //Set Amount
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    //Get Amount
    public BigDecimal getAmount() {
        return this.amount;
    }
    
    //Set PoDate
    public void setPodate(Date podate) {
        this.setPodate = podate;
    }
    
    //Get PoDAte
    public Date getPodate() {
        return this.setPodate;
    }
    
}
