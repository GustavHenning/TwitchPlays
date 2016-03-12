package tdfpro.twitchplays;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class GmailMessage {
	private String userName;
	private String password;
	
	public void setAccountDetails(String userName,String password){
		 
        this.userName=userName;//sender's email can also use as User Name
        this.password=password;
 
    }
	public void readGmail(){	 
	    /*this will print subject of all messages in the inbox of sender@gmail.com*/
		
	    String receivingHost="imap.gmail.com";//for imap protocol
	    
	    Properties props2=System.getProperties();
	    
	    props2.setProperty("mail.store.protocol", "imaps");
	    // I used imaps protocol here
	 
	    Session session2=Session.getDefaultInstance(props2, null);
	 
	    try {
	    	Store store=session2.getStore("imaps");
	        store.connect(receivingHost,this.userName, this.password);
	        Folder folder=store.getFolder("INBOX");//get inbox
	        folder.open(Folder.READ_ONLY);//open folder only to read
	        Message message[]=folder.getMessages();
	        for(int i=0;i<message.length;i++){
	        	
	        	
	            //print subjects of all mails in the inbox
	            System.out.println(message[i].getSubject());
	            //anything else you want
	            
	            
	        }
	        //close connections
	        	folder.close(true);
	        	store.close();
	    }
	    catch (Exception e) {
	    	System.out.println(e.toString());
	    }
    }  
}
