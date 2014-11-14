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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Evan
 */
@WebServlet(name = "PDFSampleServlet", urlPatterns = {"/PDFSample"})
public class PDFSampleServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            buildpdf(response);
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }    
    }
    private void buildpdf(HttpServletResponse response) {
        Font catFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        String IMG = getServletContext().getRealPath("/img/abcLogo2.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph preface = new Paragraph();
            // We add one empty line
            Image image1 = Image.getInstance(IMG);
            image1.setAbsolutePosition(55f, 760f);
            preface.add(image1);
            preface.setAlignment(Element.ALIGN_RIGHT);
            // Lets write a big header
            Paragraph mainHead = new Paragraph(String.format("%55s", "Some Heading"), catFont);
            preface.add(mainHead);
            preface.add(new Paragraph(String.format("%82s", "subhead"), subFont));
            addEmptyLine(preface, 5);
            // 3 column table
            PdfPTable table = new PdfPTable(5);
            PdfPCell cell = new PdfPCell(new Paragraph("Product Code", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Product Description", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Quantity Sold", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Price", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Ext Price", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("pc"));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("pd"));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("qs"));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("p"));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("ep"));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total:"));
            cell.setColspan(2);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("$9,999.99"));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBackgroundColor(BaseColor.YELLOW);            
            table.addCell(cell);
            preface.add(table);
            addEmptyLine(preface, 3);
            preface.setAlignment(Element.ALIGN_CENTER);
            preface.add(new Paragraph(String.format("%60s", new Date()), subFont));
            document.add(preface);
            document.close();

            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
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