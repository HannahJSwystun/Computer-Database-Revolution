package controllers;


import play.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;


public class JasperReport 
{


   private Connection conn = null;
  
   @SuppressWarnings("deprecation")
   
   
   
   
   public void GenerateReport(HashMap<String, Object> params, String reportfolder, String reportname,String type, String exportpath) 
   {

      String report_project_root = "./reports/";
      String fileName            = report_project_root + reportfolder + reportname + ".jasper";
      String outFileName         = exportpath +"."+type;
   
      try 
      {

      conn = play.db.DB.getConnection();
      
      JasperPrint print = JasperFillManager.fillReport(fileName, params, conn);
      


/**
 *    Generation "PDF" Report File
 **/      
        if(type.equals("pdf"))     
        {      
        JRExporter exporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,outFileName);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        exporter.exportReport();   
        }
      
      

/**
 *    Generation "XLS" Report File
 **/        
       else if(type.equals("xls"))  
       {
       JRXlsExporter exporterXLS = new JRXlsExporter();
       exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.TRUE);       
       exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,outFileName);
       exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
       exporterXLS.exportReport();
       }
      


/**
 *    Generation "HTML" Report File with Embeded Images
 **/                
       else
       {
       final JRHtmlExporter exporter = new JRHtmlExporter();

       try {
                final OutputStream stream = new FileOutputStream(exportpath +"."+type);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
            } 
       catch (FileNotFoundException ex) {Logger.info("Error HTML Report");}

       exporter.setImageHandler(new HtmlResourceHandler() {

       private final Map<String, byte[]> imageMap = new HashMap<>();

       @Override
       public void handleResource(final String id, final byte[] data) 
       {this.imageMap.put(id, data);}

       @Override
       public String getResourcePath(final String id) 
       {return "data:image/x-png;base64," + Base64.encodeBase64String(this.imageMap.get(id));}
        
       });

       exporter.exportReport();

       }  
       

      
     conn.close();
    
     } 

     catch (JRException e) 
     {e.printStackTrace();} 
     catch (SQLException e) 
     {e.printStackTrace();} 

     Logger.info("Generate Report "+type+" << "+reportfolder+" >> Successfuly in the Folder <<"+exportpath+">>");

   }


}
