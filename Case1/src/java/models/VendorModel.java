package models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named; 
import javax.sql.DataSource;  
import dtos.VendorDTO;
import java.util.ArrayList;

/*
 * VendorModel.java
 *
 * Created on Aug 31, 2013, 3:03 PM
 *  Purpose:    Contains methods for supporting db access for vendor information
 *              Usually consumed by the ViewModel Class via DTO
 *  Revisions: 
 */
@Named(value = "vendorModel")  
@RequestScoped
public class VendorModel implements Serializable {

    public VendorModel() {
    }
    public int addVendor(VendorDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String msg = "";
        int vendorno = -1;
        
        String sql = "INSERT INTO Vendors (Address1,City,Province,PostalCode,"
                + "Phone,VendorType,Name,Email) "
                + " VALUES (?,?,?,?,?,?,?,?)";

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getAddress1());
            pstmt.setString(2, details.getCity());
            pstmt.setString(3, details.getProvince());
            pstmt.setString(4, details.getPostalCode());
            pstmt.setString(5, details.getPhone());
            pstmt.setString(6, details.getType());
            pstmt.setString(7, details.getName());
            pstmt.setString(8, details.getEmail());
            pstmt.execute();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                rs.next();
                vendorno = rs.getInt(1);
            }
            con.close();

            if (vendorno > 0) {
                msg = "Vendor " + vendorno + " Added!";
            } else {
                msg = "Vendor Not Added!";
            }
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
        return vendorno;
    }

    public ArrayList<VendorDTO> getVendors(DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String sql = "SELECT * FROM Vendors";
        ArrayList<VendorDTO> vendors;
        vendors = new ArrayList<>();
        
        try{
            con= ds.getConnection();
            pstmt = con.prepareStatement(sql);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    VendorDTO vendor = new VendorDTO();
                    vendor.setAddress1(rs.getString("Address1"));
                    vendor.setCity(rs.getString("City"));
                    vendor.setEmail(rs.getString("Email"));
                    vendor.setName(rs.getString("Name"));
                    vendor.setPhone(rs.getString("Phone"));
                    vendor.setProvince(rs.getString("Province"));
                    vendor.setPostalCode(rs.getString("PostalCode"));
                    vendor.setType(rs.getString("VendorType"));
                    vendor.setVendorno(rs.getInt("vendorno"));
                    vendors.add(vendor);
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
        return vendors;
    }   

    public int updateVendor(VendorDTO details, DataSource ds) {
       
    PreparedStatement pstmt;
    Connection con = null;
    String msg = "";
    int numOfRows = -1;
    
    String sql = "UPDATE Vendors SET Address1 = ?, City = ?, Province = ?, PostalCode = ?, Phone = ?, VendorType = ?, Name = ?, Email = ? WHERE vendorno = ?";
    
    try{
        con= ds.getConnection();
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, details.getAddress1());
        pstmt.setString(2, details.getCity());
        pstmt.setString(3, details.getProvince());
        pstmt.setString(4, details.getPostalCode());
        pstmt.setString(5, details.getPhone());
        pstmt.setString(6, details.getType());
        pstmt.setString(7, details.getName());
        pstmt.setString(8, details.getEmail());
        pstmt.setInt(9, details.getVendorno());
        numOfRows = pstmt.executeUpdate();
        
    } catch (SQLException se)
    {
        System.out.println("SQL issue " + se.getMessage());
    }
        return numOfRows;
    }
    
    //Delete
    public int deleteVendor(int vendorno, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int numOfRows = -1;
        String sql = "DELETE From Vendors WHERE vendorno = ?";
        try{
            con= ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, vendorno);
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

    public VendorDTO getVendors(int vendorno, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String sql = "SELECT * FROM Vendors WHERE vendorno = ?";
        VendorDTO vendor = new VendorDTO();
        
        try{
            con= ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, vendorno);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    vendor.setAddress1(rs.getString("Address1"));
                    vendor.setCity(rs.getString("City"));
                    vendor.setEmail(rs.getString("Email"));
                    vendor.setName(rs.getString("Name"));
                    vendor.setPhone(rs.getString("Phone"));
                    vendor.setProvince(rs.getString("Province"));
                    vendor.setPostalCode(rs.getString("PostalCode"));
                    vendor.setType(rs.getString("VendorType"));
                    vendor.setVendorno(rs.getInt("vendorno"));
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
        return vendor;
    }
}