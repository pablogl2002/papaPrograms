/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facturasCorreo;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author pablo
 */
public class Mail {
    private String srcMail;
    private String dstMail;
    private String sbj;
    private String tMsg;
    private String psw;
    private int year;
    
    public Mail(String origen, String destino, String asunto, String txt, String contraseña, int anyo) {
        srcMail = origen;
        dstMail = destino;
        sbj = asunto;
        tMsg = txt;
        psw = contraseña;
        year = anyo;
    }
    
    public static void envioDeCorreos(String to,String month, int nFact, String file, int year){
		String from = "muebledos@outlook.com";
		String pwd = "mueble1963";
		
		String subject = "Factura " + month;
		String txt = "Adjunto factura n� " + nFact + " correspondiente al mes de " + month + " de "+ year + "\n" + "\n"
              + "Un saludo, Enrique Garc�a.\n" ;
		
		Mail m = new Mail(from, to, subject, txt, pwd, year);
	  	m.envioDeMensajes(file);
    }
    
    private void envioDeMensajes(String file){
    	try{
    		//System.out.println("Antes propiedades");
    		Properties props = new Properties();  
    		props.put("mail.smtp.socketFactory.fallback", "false");  
    		props.put("mail.smtp.quitwait", "false");
    		props.put("mail.smtp.socketFactory.port", "587");  
    		props.put("mail.host", "smtp.office365.com");
			     
    		props.setProperty("mail.transport.protocol", "smtp");
				     
    		props.setProperty("mail.smtp.port", "587");
    		props.setProperty("mail.smtp.ssl.trust", "*");
    		props.setProperty("mail.smtp.starttls.enable", String.valueOf(true));//True or False
    		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
    		props.setProperty("mail.smtp.timeout", "300000");
    		props.setProperty("mail.smtp.connectiontimeout", "300000");
    		props.setProperty("mail.smtp.writetimeout", "300000");
			  
    		Session s = Session.getDefaultInstance(props);
    		
    		//System.out.println("Despues propiedades, antes mensaje");
    		
    		//adjunto
    		BodyPart texto = new MimeBodyPart();
    		texto.setText(tMsg);
    		BodyPart adjunto = new MimeBodyPart();
    		//adjunto.setDataHandler(new DataHandler(new FileDataSource("D:/CLIENTES/a�o "+ year + "/pdf/" + file)));
    		//adjunto.setDataHandler(new DataHandler(new FileDataSource("C:/Users/Desktop/CLIENTES/a�o "+ year + "/pdf/" + file)));
                //adjunto.setDataHandler(new DataHandler(new FileDataSource("C:/Users/pablo/Desktop/archivo.txt")));
                adjunto.setDataHandler(new DataHandler(new FileDataSource("C:/Users/pablo/Desktop/CLIENTES/" + year + "/facturas terminadas/" + file)));
    		adjunto.setFileName(file);
    		MimeMultipart m = new MimeMultipart();
    		m.addBodyPart(texto);
    		m.addBodyPart(adjunto);
    		 
    		
    		//Mensaje texto
    		MimeMessage mensaje = new MimeMessage(s);
    		mensaje.setFrom(new InternetAddress(srcMail));
    		mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(dstMail));
    		mensaje.setSubject(sbj);
    		//mensaje.setText(mensajeDeTexto);
    		mensaje.setContent(m);
		  
    		//System.out.println("Despues mensaje, antes envio");
		  
    		Transport t = s.getTransport("smtp");
    		t.connect(srcMail, psw);
    		t.sendMessage(mensaje, mensaje.getAllRecipients());
    		t.close();
    		//System.out.println("exito")
                IntFactC.sentMessage();
	} catch (MessagingException ex) {
            IntFactC.messageErrorAlert(ex);
        } catch (NullPointerException e) {
            IntFactC.fileNotFoundAlert();
        }
    }
}
