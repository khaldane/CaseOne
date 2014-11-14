/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import dtos.ProductDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 *
 * @author Kat
 */
@Named(value = "productModel")
@RequestScoped
public class ProductModel implements Serializable{

    public ProductModel(){
        
    }
    
    public String addProduct(ProductDTO product, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String productno = "";
        
        String sql = "INSERT INTO Products (productcode,vendorno,vendorsku,productname,costprice,msrp,rop,eoq,qoh,qoo) "
                + " VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);//, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getProductCode());
            pstmt.setInt(2, product.getVendorno());
            pstmt.setString(3, product.getVendorsku());
            pstmt.setString(4, product.getProductName());
            pstmt.setString(5, product.getCostPrice());
            pstmt.setString(6, product.getMsrp());
            pstmt.setInt(7, product.getRop());
            pstmt.setInt(8, product.getEoq());
            pstmt.setInt(9, product.getQoh());
            pstmt.setInt(10, product.getQoo());
            pstmt.execute();

            productno = product.getProductCode();
            con.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        return productno;
    }

    public ArrayList<ProductDTO> getProducts(DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String sql = "SELECT * FROM Products";
        ArrayList<ProductDTO> products;
        products = new ArrayList<>();
        
        try{
            con= ds.getConnection();
            pstmt = con.prepareStatement(sql);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    ProductDTO product = new ProductDTO();
                    product.setProductCode(rs.getString("ProductCode"));
                    product.setVendorsku(rs.getString("Vendorsku"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setCostPrice(rs.getString("CostPrice"));
                    product.setMsrp(rs.getString("Msrp"));
                    product.setRop(rs.getInt("Rop"));
                    product.setEoq(rs.getInt("Eoq"));
                    product.setQoh(rs.getInt("Qoh"));
                    product.setQoo(rs.getInt("Qoo"));
                    product.setVendorno(rs.getInt("vendorno"));
                    products.add(product);
                }
            }
            con.close();
        }
        catch (SQLException se)
        {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        }
        catch (Exception ex){
            //Hndle other errors
            System.out.println("other issue " + ex.getMessage());
        } finally {
            //finally block used tclose resources
            try{
                if(con !=null)
                {
                    con.close();
                }
            } catch (SQLException se)
            {
                System.out.println("SQL  conn issue " + se.getMessage());
            } 
        }
       //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return products;
    }

    public int updateProduct(ProductDTO product, DataSource ds) {
    PreparedStatement pstmt;
    Connection con = null;
    String msg = "";
    int numOfRows = -1;
    
    String sql = "UPDATE Products SET productcode = ?, vendorsku = ?, productname = ?, costprice = ?, msrp = ?, rop = ?, eoq = ?, qoh = ?, qoo = ?, vendorno = ? WHERE productcode = ?";
    
    try{
        con= ds.getConnection();
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, product.getProductCode());
        pstmt.setString(2, product.getVendorsku());
        pstmt.setString(3, product.getProductName());
        pstmt.setString(4, product.getCostPrice());
        pstmt.setString(5, product.getMsrp());
        pstmt.setInt(6, product.getRop());
        pstmt.setInt(7, product.getEoq());
        pstmt.setInt(8, product.getQoh());
        pstmt.setInt(9, product.getQoo());
        pstmt.setInt(10, product.getVendorno());
        pstmt.setString(11, product.getProductCode());
        numOfRows = pstmt.executeUpdate();
        
    } catch (SQLException se)
    {
        System.out.println("SQL issue " + se.getMessage());
    }
        return numOfRows;
         //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}
       

    public int deleteProduct(String productcode, DataSource ds) {
       PreparedStatement pstmt;
        Connection con = null;
        int numOfRows = -1;
        String sql = "DELETE From Products WHERE productcode = ?";
        try{
            con= ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, productcode);
            numOfRows = pstmt.executeUpdate();
        }
        catch( SQLException se)
        {
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        } finally {
            //Fially block used to close resources
            try{
                if (con != null) {
                    con.close();
                }
            }
            catch (Exception ex) {
                
            }
        }
         return numOfRows;
   }

    public ArrayList<ProductDTO> getAllProductsForVendor(int vendorno, DataSource ds) {
        
        PreparedStatement pstmt;
        Connection con = null;
        String sql = "SELECT * FROM Products where vendorno = " + vendorno;
        ArrayList<ProductDTO> products;
        products = new ArrayList<>();
        
        try{
            con= ds.getConnection();
            pstmt = con.prepareStatement(sql);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    ProductDTO product = new ProductDTO();
                    product.setProductCode(rs.getString("ProductCode"));
                    product.setVendorsku(rs.getString("Vendorsku"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setCostPrice(rs.getString("CostPrice"));
                    product.setMsrp(rs.getString("Msrp"));
                    product.setRop(rs.getInt("Rop"));
                    product.setEoq(rs.getInt("Eoq"));
                    product.setQoh(rs.getInt("Qoh"));
                    product.setQoo(rs.getInt("Qoo"));
                    product.setVendorno(rs.getInt("vendorno"));
                    products.add(product);
                }
            }
            con.close();
        }
        catch (SQLException se)
        {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        }
        catch (Exception ex){
            //Hndle other errors
            System.out.println("other issue " + ex.getMessage());
        } finally {
            //finally block used tclose resources
            try{
                if(con !=null)
                {
                    con.close();
                }
            } catch (SQLException se)
            {
                System.out.println("SQL  conn issue " + se.getMessage());
            } 
        }
        return products;
    }

    public ProductDTO getProduct(String productcode, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String sql = "SELECT ProductName, CostPrice FROM Products where productcode = ?"; 
        ProductDTO product = new ProductDTO();
        try{
            con= ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, productcode);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    product.setProductName(rs.getString("productname"));
                    product.setCostPrice(rs.getString("CostPrice"));
                }
            }
            con.close();
        }
        catch (SQLException se)
        {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        }
        catch (Exception ex){
            //Hndle other errors
            System.out.println("other issue " + ex.getMessage());
        } finally {
            //finally block used tclose resources
            try{
                if(con !=null)
                {
                    con.close();
                }
            } catch (SQLException se)
            {
                System.out.println("SQL  conn issue " + se.getMessage());
            } 
        }
        return product;
    }
}
