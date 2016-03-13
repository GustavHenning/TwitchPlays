package tdfpro.twitchplays;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

public class GmailMessage {
	private String userName;
	private String password;
	
	public void setAccountDetails(String userName,String password){
		 
        this.userName=userName;//sender's email can also use as User Name
        this.password=password;
 
    }
	public void readGmail(){	 
	    /*this will print subject of all messages in the inbox of sender@gmail.com*/
		setAccountDetails("MAIL HÄR","SKRIV LÖSEN HÄR");//<<<<<<<<<<<<<<skriv in mail och lösenordet
	    String receivingHost="imap.gmail.com";//for imap protocol
	    
	    Properties props2=System.getProperties();
	    
	    props2.setProperty("mail.store.protocol", "imaps");
	 
	    Session session2=Session.getDefaultInstance(props2, null);
	 
	    try {
	    	Store store=session2.getStore("imaps");
	        store.connect(receivingHost,this.userName, this.password);
	        Folder folder=store.getFolder("INBOX");//get inbox
	        folder.open(Folder.READ_ONLY);//open folder only to read
	        Flags seen = new Flags(Flags.Flag.SEEN);
	        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
	        
	        SearchTerm searchTerm = unseenFlagTerm;
	        //ifall fler söktermer läggs till
	        //SearchTerm searchTerm = new AndTerm(unseenFlagTerm, recentFlagTerm);
	        Message message[]=folder.search(searchTerm);
	        for(int i=0;i<message.length;i++){
	        	
	        	//kan kolla ifall det är en donation
	            System.out.println(message[i].getSubject());
	            //läsa efter hur mycket donationen är på, från vem och vilket ord som gissats på
	            System.out.println(message[i].getContent().toString());
	           
	            
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
