package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.Play;
import play.Logger;
import play.db.DB;
import models.*;

import javax.sql.DataSource;
import java.sql.Connection;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.HashMap;
import java.util.Date;  
import java.text.SimpleDateFormat;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.*;
import java.util.Scanner;
import java.util.Iterator;
import gui.ava.html.link.*;
import gui.ava.html.image.util.*;
import gui.ava.html.image.generator.*;
import org.apache.commons.mail.EmailAttachment;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;



/**
 * Manage a database of computers
 */
@SuppressWarnings("deprecation")
public class HomeController  extends Controller {

    
    private JasperReport jasper = new JasperReport();
    
    private FormFactory formFactory;

    @Inject MailerClient mailerClient;

    @Inject
    public HomeController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * This result directly redirect to application home.
     */
    public Result GO_HOME = Results.redirect(
        routes.HomeController.list(0, "id", "asc", "")
    );

        
    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return GO_HOME;
    }

    /**
     * Display the paginated list of computers.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
    public Result list(int page, String sortBy, String order, String filter) {
        return ok(
            views.html.list.render(
                Computer.page(page, 10, sortBy, order, filter),
                sortBy, order, filter
            )
        );
    }
    
    /**
     * Display the 'edit form' of a existing Computer.
     *
     * @param id Id of the computer to edit
     */
    public Result edit(Long id) {
        Form<Computer> computerForm = formFactory.form(Computer.class).fill(
            Computer.find.byId(id)
        );
        return ok(
            views.html.editForm.render(id, computerForm)
        );
    }
    

    public Result show(Long id) {
        Form<Computer> computerForm = formFactory.form(Computer.class).fill(
            Computer.find.byId(id)
        );
        return ok(
            views.html.showForm.render(id, computerForm)
        );
    }


    /**
     * Handle the 'edit form' submission 
     *
     * @param id Id of the computer to edit
     */
    public Result update(Long id) throws PersistenceException {
        Form<Computer> computerForm = formFactory.form(Computer.class).bindFromRequest();
        if(computerForm.hasErrors()) {
            return badRequest(views.html.editForm.render(id, computerForm));
        }

        Transaction txn = Ebean.beginTransaction();
        try {
            Computer savedComputer = Computer.find.byId(id);
            if (savedComputer != null) {
                Computer newComputerData = computerForm.get();
                savedComputer.company = newComputerData.company;
                savedComputer.discontinued = newComputerData.discontinued;
                savedComputer.introduced = newComputerData.introduced;
                savedComputer.name = newComputerData.name;

                savedComputer.update();
                flash("success", "Computer " + computerForm.get().name + " has been updated");
                txn.commit();
            }
        } finally {
            txn.end();
        }
        Logger.info("Cpmputer code "+id+" has been Updated");
        return GO_HOME;
    }




      public Result renamecomputers(Long id) throws PersistenceException 
      {
        
        Transaction txn = Ebean.beginTransaction();
        try 
        {
            Computer savedComputer = Computer.find.byId(id);
            if (savedComputer != null) 
            {
                
                savedComputer.name = "UPDATE COMPUTER NAME FROM JAVASCRIPT";

                savedComputer.update();
                
                txn.commit();
            }
        } 
        finally 
        {txn.end();}
        
        Logger.info("Computer code "+id+" has been Updated..");
        return GO_HOME;
    }
    
    




    /**
     * Display the 'new computer form'.
     */
    public Result create() {
        Form<Computer> computerForm = formFactory.form(Computer.class);
        return ok(
                views.html.createForm.render(computerForm)
        );
    }
    
    /**
     * Handle the 'new computer form' submission 
     */
    public Result save() {
        Form<Computer> computerForm = formFactory.form(Computer.class).bindFromRequest();
        if(computerForm.hasErrors()) {
            return badRequest(views.html.createForm.render(computerForm));
        }
        computerForm.get().save();
        flash("success", "Computer " + computerForm.get().name + " has been created");
        return GO_HOME;
    }
    
    /**
     * Handle computer deletion
     */
    public Result delete(Long id) {
        Computer.find.ref(id).delete();
        flash("success", "Computer has been deleted");
        return GO_HOME;
    }



  public Result remove(Long id) 
  {
    final Computer computer = Computer.find.ref(id);
    if (computer == null) 
    {return notFound(String.format("Computer "+id+" does not exist.", id));}
    
    Computer.find.ref(id).delete();
    
    Logger.info("Computer "+id+" has been deleted successfully");
    return GO_HOME;
  }


/*
  public Result send() 
  {

    Logger.info("send mail Method Begins");
    
     Email email = new Email()
                     
           .setSubject("Simple email")
       
           .setFrom("placebocurejava@gmail.com")
       
           .addTo("placebocurejava@gmail.com")
           // Multiple To Addresses :    .setTo(EmailsTo)
       
           // Copies of Email :       Single      .addCc("abdelalitazichibi@gmail.com")
            
           //.addAttachment("attachment.pdf", new File("./public/some/path/attachment.pdf"))
           //.addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE)
           //.addAttachment("image.jpg", new File("./public/some/path/image.jpg"), cid)
       
           //.addAttachment(reportname+".pdf", new File(exportpath+".pdf"))
           //.addAttachment(reportname+".xls", new File(exportpath+".xls"))
           //.addAttachment(reportname+".html", new File(exportpath+".html"))
           // Thinking to Add HTML Folder too    .addAttachment(exportpath+".html_files", new File(exportpath+".html_files"))
                             
           .setBodyText("Envoi Simple sans selection de lignes !");
           //Set HTML code Directly :   .setBodyHtml("<html><body><p>An <b>html</b> message with cid <img src=\"cid:" + cid + "\"></p></body></html>");
           
           //Call Template HTML file :  
           //.setBodyHtml(html_trimmed);
       
           mailerClient.send(email);

           Logger.info("Email sent successfully");
    
    return GO_HOME;
  }

*/




public Result checkbox() 
{
        
           
          String[] postAction = request().body().asFormUrlEncoded().get("action");
          
          String action = postAction[0];

          if (postAction == null || postAction.length == 0) 
          {return badRequest("Veuillez selectionner une Action Valide");} 
        
          else
          {  

  /* Actions With Params  */
              String[] computers = request().body().asFormUrlEncoded().get("computercode"); 
              
              if (computers != null && ("PDF Report".equals(action) || "HTML Report".equals(action) || "XLS Report".equals(action) || "New Edit".equals(action))  ) 
              {
                  String ids = ListUtils.mkString(computers, s -> "" + s, ";");
                  Logger.info("ids Selected are : "+ids);
           
                  switch (action) 
                  {

                       case     "PDF Report"       : return GenerateReport(ids,"pdf");
                       case     "XLS Report"       : return GenerateReport(ids,"xls");
                       case     "HTML Report"      : return GenerateReport(ids,"html");
                       case     "New Edit"         : return EditNames(ids,"DELTA");
                       default                     : return ok("default switch : Action <<"+action+">> avec Parametres Error>>"); 
                  } 
              }

  /* Actions Without Params  */
              else if (computers == null && ("PDF Report".equals(action) || "HTML Report".equals(action) || "XLS Report".equals(action) || "New Edit".equals(action))  ) 
              {
               return ok("Cette Action necessite au moins un parametre d'entré");     
              } 

            
              else
              {
                  switch (action) 
                  {


                       case     "Add New"             : return Results.redirect(routes.HomeController.create());
                       case     "Send Simple Mail"    : return SendSimpleEmail();
                       case     "Send Complex Mail"   : return SendComplexEmail();
                       case     "Open IMG File"       : return OpenIMGfile("./public/images/wall.jpg");
                    default                           : return ok("default switch : Action <<"+action+">> sans Parametres Error>>"); 
                  } 

              }  



          }

}











static public String currentSpecificDate() 
{
           Date now = new Date();  
           SimpleDateFormat date_pattern = new SimpleDateFormat("dd-MM-yyy     HH_mm_ss");
           return date_pattern.format(now);

}



public Result OpenIMGfile(String pathfile) 
{
           return ok(new java.io.File(pathfile));
}




public static class ListUtils 
{
     static public <E> String mkString(E[] list, Function<E,String> stringify, String delimiter) 
     {
            int i = 0;
            StringBuilder s = new StringBuilder();
             for (E e : list) 
             {
                if (i != 0) { s.append(delimiter); }
                s.append(stringify.apply(e));
                i++;
             }
            return s.toString();
     }
}



public Result EditNames(String selected, String type) 
{

  String[] ids = selected.split(";");

  for (String temp : ids) 
  {
        Transaction txn = Ebean.beginTransaction();
        try 
        {
            Computer savedComputer = Computer.find.byId(Long.parseLong(temp));
            if (savedComputer != null) 
            {
                
                savedComputer.name = "UPDATE COMUTER NAME FROM ACTION JAVA";

                savedComputer.update();
                
                txn.commit();
            }
        } 
        finally 
        {txn.end();}
  
  Logger.info("Computer code "+temp+" has been Updated..");
  }      
        return GO_HOME;
          
}



public Result GenerateReport(String selected, String type) 
{
           String reportfolder     =  "ReportTest/";
           String root_report_path =  Play.application().path()+"/reports/"+reportfolder;
           String reportname       =  "RapportJRXMLprincipal";
           String exportdir        =  root_report_path + "/Generated/";
           String exportpath       =  exportdir + currentSpecificDate();

           // verify paths for output
           boolean completed = new File(exportdir).mkdirs(); // use value of completed if needed, mkdirs() will create all intermediary folders in the path

           HashMap<String, Object> params = new HashMap<String, Object>();  
           params.put("Play_ReportPath",root_report_path);
           params.put("param_ids", selected);                
           
           jasper.GenerateReport(params,reportfolder,reportname,type,exportpath);

           return ok(new java.io.File(exportpath+"."+type));
}









static public String scannerhtml(String pathfile) 
{
         
      File f = new File(pathfile);
      
      String html_trimmed="";
      Scanner scanner = null;
      try {scanner = new Scanner(new FileReader(f));} 
      catch (FileNotFoundException e) {e.printStackTrace();}
      
      try {
            while ( scanner.hasNextLine() )
           {
            String s=scanner.nextLine();
            String tmp= s.trim();
            html_trimmed= html_trimmed.concat(tmp);
           }
          Logger.info("Template Completed : "+html_trimmed);
         }
      
      
      finally {scanner.close();} 
      return html_trimmed;
            
}




















public Result SendSimpleEmail() 
{
      String cid = "12345";
      Email email = new Email()
                 .setSubject("Objet du Mail")
                 .setFrom("placebocurejava@gmail.com")
                 .addTo("placebocurejava@gmail.com")
                 .addCc("placebocurejava@gmail.com")
                 .addAttachment("attachment.pdf", new File("./public/some/path/attachment.pdf"))
                 .addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE)
                 .addAttachment("image.jpg", new File("./public/some/path/image.jpg"), cid)
                 .setBodyText("Corps du Message Simple");
           
      mailerClient.send(email);
      
      // retirer target=_blank      implique de mettre     return GO_HOME; 
      return ok("Email Simple envoyé avec succès");
}





public Result SendComplexEmail() 
{
      
      String cid = "1234";
      
      List<String> EmailsTo = Arrays.asList("j2eealidev@gmail.com", "placebocurejava@gmail.com", "abdelalitazichibi@gmail.com");
           
          
      Logger.info("=> Loop Example");
      for (int i = 0; i < EmailsTo.size(); i++) 
      {
      Logger.info("   Email "+i+" : "+EmailsTo.get(i));
      }
      
    
      Logger.info("=> Loop Advance Example");
      for (String temp : EmailsTo) 
      {
      Logger.info("   Email : "+temp);
      }
      
    
      Logger.info("=> Iterator Example forwards data");
      Iterator<String> ItEmailsTo = EmailsTo.iterator();
      while (ItEmailsTo.hasNext()) 
      {
      Logger.info("   Emails : "+ItEmailsTo.next());
      }
    
      
      Logger.info("=> While Loop Example");
      int i = 0;
      while (i < EmailsTo.size()) 
      {
      Logger.info("   Emails : "+i+" : "+EmailsTo.get(i));
      i++;
      }
    
    
      Logger.info("=> collection stream() util");
      EmailsTo.forEach(
      (temp) -> 
      {   
      Logger.info("   Emails : "+temp);
      }
      );
      
        

      Email email = new Email()
                   
           .setSubject("Complex Email HERE")
       
           .setFrom("placebocurejava@gmail.com")
       
           .setTo(EmailsTo)
       
           .setCc(EmailsTo)
            
           .addAttachment("attachment.pdf", new File("./public/some/path/attachment.pdf"))
           .addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE)
           .addAttachment("image.jpg", new File("./public/some/path/image.jpg"), cid)
       
           //.addAttachment(reportname+".pdf", new File(exportpath+".pdf"))
           //.addAttachment(reportname+".xls", new File(exportpath+".xls"))
                              
           //Set HTML code Directly :   .setBodyHtml("<html><body><p>An <b>html</b> message with cid <img src=\"cid:" + cid + "\"></p></body></html>");
           
           .setBodyHtml(scannerhtml("./public/some/html_templates/sample signature.html"));
           
           mailerClient.send(email);

           // retirer target=_blank      implique de mettre     return GO_HOME; 
           return ok("Email Complexe envoyé avec succès");
      
}











}
            
