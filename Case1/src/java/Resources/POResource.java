/*
 * Name: Katherine Haldane
 * Date: Oct 17, 2014
 * Description: Purchase order resource that creates the purchase order
 */
package Resources;
import dtos.PurchaseOrderDTO;
import java.net.URI;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
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
import models.PurchaseOrderModel;

@Path("purchaseorder")
@RequestScoped
public class POResource {
    @Context
    private UriInfo context;
    
        //resource needs to be already defined in Glassfish
    @Resource(lookup="jdbc/Info5059db")
    DataSource ds;

    public POResource() {}
    
/*
 * Name: createPO
 * Params: PurchaseOrderDTO 
 * Returns: Response
 * Description: Adds a purchase order and returns response back to rest factory
 */
    @POST
    @Consumes("application/json")
    public Response createPO(PurchaseOrderDTO po) {
        PurchaseOrderModel model = new PurchaseOrderModel();
        String msg = model.purchaseOrderAdd(po.getTotal(), po.getVendorno(), po.getItems(), ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(msg).build();
    }
}
