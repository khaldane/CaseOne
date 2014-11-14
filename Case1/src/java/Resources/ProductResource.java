/*
 * Name: Katherine Haldane
 * Date: Oct 17, 2014
 * Description: Gets createProductFromJson, getProductsJson, deleteProductFromJson, getVendorProductsJson
 *              from the ProductModel.java
 */
package Resources;
import dtos.ProductDTO;
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
import models.ProductModel;

@Path("product")
public class ProductResource {
    
    @Context
    private UriInfo context;
    
    //resource needs to be already defined in Glassfish
    @Resource(lookup="jdbc/Info5059db")
    DataSource ds;
    
    public ProductResource() {
    }

   /*
    * Name: createProductFromJson
    * Params: ProductDTO 
    * Returns: Response
    * Description: Creates a product and returns response back to rest factory
    */
    @POST
    @Consumes("application/json")
    public Response createProductFromJson(ProductDTO product){
        ProductModel model = new ProductModel();
        String id = model.addProduct(product,ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(id).build();
    }
    
   /*
    * Name: getProductsJson
    * Params: None 
    * Returns: model
    * Description: Gets the products
    */
    @GET
    @Produces("applicaton/json")
    public ArrayList<ProductDTO> getProductsJson(){
        ProductModel model = new ProductModel();
        return model.getProducts(ds);
    }
    
   /*
    * Name: updateProductFromJson
    * Params: ProductDTO
    * Returns: Response
    * Description: Updates a product and returns response back to rest factory
    */  
    @PUT
    @Consumes("application/json")
    public Response updateProductFromJson(ProductDTO product){
        ProductModel model = new ProductModel();
        int numOfRowsUpdate = model.updateProduct(product, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numOfRowsUpdate).build();
    }
    
   /*
    * Name: createProductFromJson
    * Params: PathParam, String
    * Returns: Response
    * Description: Deletes a product and returns response back to rest factory
    */    
    @DELETE
    @Path("/{productcode}")
    @Consumes("application/json")
    public Response deleteProductFromJson(@PathParam("productcode")String productCode) {
        ProductModel model = new ProductModel();
        int numOfRowsDeleted = model.deleteProduct(productCode, ds);
        URI uri = context.getAbsolutePath();
        System.out.println("number of rows deleted " + numOfRowsDeleted);
        return Response.created(uri).entity(numOfRowsDeleted).build();
    }
    
   /*
    * Name: getVendorProductsJson
    * Params: PathParam, int
    * Returns: model
    * Description: Gets all the products depending on vendor no
    */
    @GET
    @Path("/{vendorno}")
    public ArrayList<ProductDTO> getVendorProductsJson(@PathParam("vendorno") int vendorno){
        ProductModel model = new ProductModel();
        return model.getAllProductsForVendor(vendorno, ds);
    }
}
