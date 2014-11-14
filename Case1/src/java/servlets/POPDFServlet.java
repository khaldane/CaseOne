package servlets;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dtos.PurchaseOrderDTO;
import dtos.VendorDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import models.PurchaseOrderModel;
import models.VendorModel;

/**
 *
 * @author Evan
 */
@WebServlet(name = "POPDF", urlPatterns = {"/POPDFServlet"})
public class POPDFServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Resource(lookup = "jdbc/Info5059db")
    DataSource ds; 
    PurchaseOrderModel pom = new PurchaseOrderModel();
    VendorModel vm = new VendorModel();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)// DataSource ds
            throws ServletException, IOException {
        try{
            int pono = Integer.parseInt(request.getParameter("po"));
            PurchaseOrderDTO poDTO = pom.getPO(pono,ds);
            int vendorno = poDTO.getVendorno();
            VendorDTO venDTO = vm.getVendors(vendorno, ds);     
            
            buildpdf(poDTO, venDTO, response);
        } catch (Exception e){
            System.out.println("Error " + e.getMessage());
            //response.setStatus(500);
            response.sendError(500, e.getMessage());
        }  
    }
    
    private void buildpdf(PurchaseOrderDTO poDTO, VendorDTO venDTO, HttpServletResponse response) throws Exception {
        Font catFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        String IMG = getServletContext().getRealPath("/img/iceberg.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph preface = new Paragraph();
            // We add one empty line
            Image image1 = Image.getInstance(IMG);
            image1.setAbsolutePosition(50f, 700f);
            preface.add(image1);
            preface.setAlignment(Element.ALIGN_RIGHT);
            // Lets write a big header
            Paragraph mainHead = new Paragraph(String.format("%55s", "Purchase Order"), catFont);
            preface.add(mainHead);
            preface.add(new Paragraph(String.format("%82s", "PO#: " + poDTO.getPonumber()), subFont));
            addEmptyLine(preface, 3);             
            PdfPTable table1 = new PdfPTable(2);   
            table1.setHorizontalAlignment(Element.ALIGN_LEFT);            
            table1.setWidthPercentage(35);            
             PdfPCell cell1 = new PdfPCell(new Phrase("vendor:",smallBold));           
            cell1.setBorder(0);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell1);
            cell1 = new PdfPCell(new Phrase(String.format(venDTO.getName() + "\n" + venDTO.getCity() + "\n" + venDTO.getProvince() + "\n" +  venDTO.getPostalCode() + "\n" +  venDTO.getAddress1()), normalFont));
             cell1.setBorder(0);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table1.addCell(cell1);
             preface.add(table1);
             addEmptyLine(preface, 3); 
            
            PdfPTable table = new PdfPTable(5);
            PdfPCell cell = new PdfPCell(new Paragraph("Product Code", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Prod Desc", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Quantity Sold", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Price", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Total Ext", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            BigDecimal total = new BigDecimal(0.0);
            BigDecimal tax = new BigDecimal(0.13);
            BigDecimal grandTotal = new BigDecimal(0.0);

            for (int i = 0; i < poDTO.getItems().size(); i++) {

                cell = new PdfPCell(new Phrase(String.format("%10s", poDTO.getItems().get(i).getProductCode()), normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.format("%10s", poDTO.getItems().get(i).getProductName()), normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.format("%10s", poDTO.getItems().get(i).getQty()), normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.format("%10s", "$" + formatter.format(poDTO.getItems().get(i).getPrice().setScale(2, RoundingMode.HALF_UP)), normalFont)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                //String ext = poDTO.getItems().get(i).getPrice().multiply(new BigDecimal(poDTO.getItems().get(i).getQty())).setScale(2, RoundingMode.HALF_UP).toString();
                cell = new PdfPCell(new Phrase(String.format("%10s", "$" + formatter.format(poDTO.getItems().get(i).getExt().setScale(2, RoundingMode.HALF_UP)), normalFont)));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                total = total.add(poDTO.getItems().get(i).getExt());

            }

            tax = tax.multiply(total).setScale(2, RoundingMode.HALF_UP);
            grandTotal = total.add(tax);
            preface.add(table);
 
           cell = new PdfPCell(new Phrase("Total:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(String.format("%10s", "$" + formatter.format(total.setScale(2, RoundingMode.HALF_UP)), normalFont)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Tax:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(String.format("%10s", "$" + formatter.format(tax.setScale(2, RoundingMode.HALF_UP)), normalFont)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Order Total:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(String.format("%10s", "$" + formatter.format(grandTotal.setScale(2, RoundingMode.HALF_UP)), normalFont)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBackgroundColor(BaseColor.YELLOW);
            table.addCell(cell);

            addEmptyLine(preface, 3);
            preface.setAlignment(Element.ALIGN_CENTER);
            preface.add(new Paragraph(String.format("%60s", new Date()), subFont));
            document.add(preface);
            document.close();

            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment; filename=\"Case1.pdf\"");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            throw e;
        }

    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}