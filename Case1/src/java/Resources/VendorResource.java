/*
 * Name: Katherine Haldane
 * Date: Oct 17, 2014
 * Description: Gets createVendorFromJson, getVendorsJson, updateVendorFromJson, deleteVendorFromJson
 *              from vendorModel.java
 */
package Resources;

import dtos.VendorDTO;
import java.net.URI;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.VendorModel;

@Path("vendor")
public class VendorResource {

    @Context
    private UriInfo context;

    //resource needs to be already defined in Glassfish
    @Resource(lookup="jdbc/Info5059db")
    DataSource ds;

    public VendorResource() {
    }

    
   /*
    * Name: createVendorFromJson
    * Params: VendorDTO 
    * Returns: Response
    * Description: Creates a vendor and returns response back to rest factory
    */    
    @POST
    @Consumes("application/json")
    public Response createVendorFromJson(VendorDTO vendor){
        VendorModel model = new VendorModel();
        int id = model.addVendor(vendor, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(id).build();
    }

   /*
    * Name: getVendorsJson
    * Params: None 
    * Returns: model
    * Description: Gets the vendor
    */    
    @GET
    @Produces("application/json")
    public ArrayList<VendorDTO> getVendorsJson(){
        VendorModel model = new VendorModel();
        return model.getVendors(ds);
    }

   /*
    * Name: updateVendorFromJson
    * Params: VendorDTO
    * Returns: Response
    * Description: Updates a vendor and returns response back to rest factory
    */      
    @PUT
    @Consumes("application/json")
    public Response updateVendorFromJson(VendorDTO vendor) {
        VendorModel model = new VendorModel();
        int numOfRowsUpdate = model.updateVendor(vendor, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numOfRowsUpdate).build();
    }
    
   /*
    * Name: deleteVendorFromJson
    * Params: PathParam, int
    * Returns: Response
    * Description: Deletes a vandor and returns response back to rest factory
    */        
    @DELETE
    @Path("/{vendorno}")
    @Consumes("application/json")
    public Response deleteVendorFromJson(@PathParam("vendorno")int vendorno) {
        VendorModel model = new VendorModel();
        int numOfRowsDeleted = model.deleteVendor(vendorno, ds);
        URI uri = context.getAbsolutePath();
        System.out.println("number of rows deleted " + numOfRowsDeleted);
        return Response.created(uri).entity(numOfRowsDeleted).build();
    }
}
