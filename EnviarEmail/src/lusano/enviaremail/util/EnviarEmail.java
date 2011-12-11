/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lusano.enviaremail.util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeMessage;   
import javax.mail.Authenticator;  
import javax.mail.PasswordAuthentication;

  
//clase que retorna uma autenticacao para ser enviada e verificada pelo servidor smtp  
/**
 *
 * @author mmiranda1984
 */
public class EnviarEmail {

    private String mailSMTPServer;  
    private String mailSMTPServerPort;  
      

    /* 
     * quando instanciar um Objeto ja sera atribuido o servidor SMTP do GMAIL  
     * e a porta usada por ele 
     */  
    public EnviarEmail() {
        mailSMTPServer = Util.obterValorPropriedade("smtp");  
        mailSMTPServerPort = Util.obterValorPropriedade("port");  
    }
      
    /* 
     * caso queira mudar o servidor e a porta, so enviar para o contrutor 
     * os valor como string 
     */  
    public EnviarEmail(String mailSMTPServer, String mailSMTPServerPort) { //Para outro Servidor  
        this.mailSMTPServer = mailSMTPServer;  
        this.mailSMTPServerPort = mailSMTPServerPort;  
    }

    public Boolean enviarEmail(String destinatario,  String copiaPara, String copiaOcultaPara, String assunto, String corpoDoEmail) {  
          
        Properties props = new Properties();  
  
        props.put("mail.transport.protocol", "smtp"); //define protocolo de envio como SMTP  
        props.put("mail.smtp.host", mailSMTPServer); //server SMTP do GMAIL  
        props.put("mail.smtp.user", Util.obterValorPropriedade("from")); //usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)  
        props.put("mail.smtp.port", mailSMTPServerPort); //porta  
        props.put("mail.smtp.auth", "true"); //ativa autenticacao  
          
        //Cria um autenticador que sera usado a seguir  
        SimpleAuth auth = null;  
        auth = new SimpleAuth (Util.obterValorPropriedade("username"), Util.obterValorPropriedade("password"));  
          
        //Session - objeto que ira realizar a conexão com o servidor  
        /*Como há necessidade de autenticação é criada uma autenticacao que 
         * é responsavel por solicitar e retornar o usuário e senha para  
         * autenticação */  
        Session session = Session.getDefaultInstance(props, auth);  
        session.setDebug(true); //Habilita o LOG das ações executadas durante o envio do email  
  
        //Objeto que contém a mensagem  
        Message msg = new MimeMessage(session);  
  
        try {  
            //Setando o destinatário  
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));  
            if (copiaPara != "")
                msg.setRecipient(Message.RecipientType.CC, new InternetAddress(copiaPara));
            if (copiaOcultaPara != "")
                msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(copiaOcultaPara));
            //Setando a origem do email  
            msg.setFrom(new InternetAddress(Util.obterValorPropriedade("from")));  
            //Setando o assunto  
            msg.setSubject(assunto);  
            //Setando o conteúdo/corpo do email  
            msg.setContent(corpoDoEmail,"text/html");  
            //Setando a data  
            msg.setSentDate(new Date());  
  
        } catch (Exception e) {  
            System.out.println(">> Erro: Completar Mensagem");  
            e.printStackTrace();
            return false;
        }  
          
        //Objeto encarregado de enviar os dados para o email  
        Transport tr;  
        try {  
            tr = session.getTransport("smtp"); //define smtp para transporte  
            /* 
             *  1 - define o servidor smtp 
             *  2 - seu nome de usuario do gmail 
             *  3 - sua senha do gmail 
             */  
            tr.connect(mailSMTPServer, Util.obterValorPropriedade("username"), Util.obterValorPropriedade("password"));  
            msg.saveChanges(); // don't forget this  
            //envio da mensagem  
            tr.sendMessage(msg, msg.getAllRecipients());  
            tr.close();  
        } catch (Exception e) {  
            System.out.println(">> Erro: Envio Mensagem");  
            e.printStackTrace();
            return false;
        }  
        
        return true;
    }  
}  
class SimpleAuth extends Authenticator {  
    public String username = null;  
    public String password = null;  
  
  
    public SimpleAuth(String user, String pwd) {  
        username = user;  
        password = pwd;  
    }  
  
    protected PasswordAuthentication getPasswordAuthentication() {  
        return new PasswordAuthentication (username,password);  
    }      

}
