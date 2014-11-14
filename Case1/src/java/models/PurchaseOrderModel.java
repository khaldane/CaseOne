/*
 * Name: Katherine Haldane
 * Date: Oct 17, 2014
 * Description: Purchase Order Model gets the PO Items, Adds the purchase ORder, and gets the pruchase order.
 */

package models;

import dtos.ProductDTO;
import dtos.PurchaseOrderDTO;
import dtos.PurchaseOrderLineitemDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.sql.DataSource;

@Named(value = "purchaseOrderModel")
@RequestScoped
public class PurchaseOrderModel implements Serializable{

    public PurchaseOrderModel() {}
    
    /*
     * Name: getPOItems
     * Params: int, datasource 
     * Returns: arraylist
     * Description: gets the purchase order items
     */
    private ArrayList<PurchaseOrderLineitemDTO> getPOItems(int pono, DataSource ds)
    {
      String sql = "SELECT * FROM PurchaseOrderLineitems WHERE poNumber = ?" ;
      PreparedStatement stmt;
      ResultSet rs;
      Connection con = null;
      ArrayList<PurchaseOrderLineitemDTO> items = new ArrayList();
      
      try{
          con = ds.getConnection();
          stmt = con.prepareStatement(sql);
          stmt.setInt(1, pono);
          rs = stmt.executeQuery();
          double calcExt = 0;
          BigDecimal ext;
          while (rs.next()){
              PurchaseOrderLineitemDTO item = new PurchaseOrderLineitemDTO();
              item.setPrice(rs.getBigDecimal("price"));
              item.setProductCode(rs.getString("prodcd"));
              ProductModel prm = new ProductModel();
              ProductDTO prodDTO = prm.getProduct(rs.getString("prodcd"), ds);
              item.setProductName(prodDTO.getProductName());
              item.setQty(rs.getInt("qty"));
              calcExt = rs.getInt("qty") * rs.getBigDecimal("Price").doubleValue();
              ext = new BigDecimal(calcExt);
              item.setExt(ext);
              items.add(item);
          }         
      }
      catch(SQLException se)
        {
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        } finally {
            try {
                if(con != null)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
            }
      }
      return items;
    }
    
    /*
     * Name: purchaseOrderAdd
     * Params: BigDecimal, Int, ArrayList, Datasource 
     * Returns: String
     * Description: Adds the purchase order
     */
    public String purchaseOrderAdd(BigDecimal total, int vendorno, ArrayList<PurchaseOrderLineitemDTO> items, DataSource ds) {
        
        Connection con = null;
        PreparedStatement pstmt;
        int poNum = -1;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
        String curDate = sdf.format(cal.getTime());
        String msg = "";
        String sql = "INSERT INTO PurchaseOrders (vendorno,podate,amount) VALUES (?,?,?)";    
        double poamt = total.doubleValue() * 1.13;
        
        try{
            con = ds.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, vendorno);
            pstmt.setString(2, curDate);
            pstmt.setBigDecimal(3, BigDecimal.valueOf(poamt));
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            poNum = rs.getInt(1);
            rs.close();
            pstmt.close();
            
            for(PurchaseOrderLineitemDTO item : items) {
                if(item.getQty() > 0){
                    sql = "INSERT INTO PurchaseOrderLineItems (PONumber,Prodcd,Qty,Price) VALUES (?,?,?,?)";
                    pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pstmt.setInt(1, poNum);
                    pstmt.setString(2, item.getProductCode());
                    pstmt.setInt(3, item.getQty());
                    pstmt.setBigDecimal(4, item.getPrice());
                    pstmt.execute();
                    }
            }
            con.commit();
            msg =  Integer.toString(poNum);
            con.close();            
        }
        catch(SQLException se){
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
            msg = "PO not added! - " + se.getMessage();
            try{
                con.rollback();
            }
            catch(SQLException sqx){
                System.out.println("Rollback failed - " + sqx.getMessage());;
            }
            catch(Exception e){
                System.out.println("other issue " + e.getMessage());
            } finally {
                try {
                    if (con != null){
                        con.close();
                    }
                } catch(SQLException s){
                    System.out.println("SQL issue on close " + s.getMessage());
                }// end finally try
            }
        }       
        return msg;
  
    }
    
    /*
     * Name: getPO
     * Params: int, datasource 
     * Returns: PurchaseOrderDTO
     * Description: Gets the purchase order
     */
    public PurchaseOrderDTO getPO(int pono, DataSource ds){
        String sql = "SELECT * FROM PurchaseOrders WHERE poNumber = ?";
        PreparedStatement stmt;
        ResultSet rs;
        Connection con = null;
        PurchaseOrderDTO details = new PurchaseOrderDTO();
        
        try{
            con = ds.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, pono);
            rs = stmt.executeQuery();
            rs.next();
            details.setPonumber(pono);
            details.setVendorno(rs.getInt("vendorno"));
            details.setAmount(rs.getBigDecimal("amount"));
            details.setPodate(Date.valueOf(rs.getString("podate")));
            details.setItems(getPOItems(pono,ds));
            stmt.close();
        }
        catch(SQLException se)
        {
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        } finally {
            try {
                if(con != null)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
            }
        }
        return details;
    }   
}
