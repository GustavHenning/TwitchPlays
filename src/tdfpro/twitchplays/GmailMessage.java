package tdfpro.twitchplays;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
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
		setAccountDetails("MAIL","PASS");//<<<<<<<<<<<<<<skriv in mail och lösenordet
	    String receivingHost="imap.gmail.com";//for imap protocol
	    String mailContent = "";
	    Properties props2=System.getProperties();
	    double donation = 0;
	    
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
	            //System.out.println(message[i].getSubject());
	            //läsa efter hur mycket donationen är på, från vem och vilket ord som gissats på
	            //System.out.println(message[i].getContent().toString());
	        	Address[] addresses =  message[i].getFrom();
	        	for (Address from : addresses) {
	        		if (from.toString().contains("member@paypal")) {
	        			Object content = message[i].getContent();
	        			if (content instanceof String)
	        			{
	        			    String body = (String)content;
	        			    mailContent += body;
	        			}
	        			else if (content instanceof Multipart)
	        			{
	        			    Multipart mp = (Multipart)content;
	        			    for (int j = 0; j < mp.getCount(); j++) {
	        			        BodyPart bp = mp.getBodyPart(j);
	        			        if (Pattern.compile(Pattern.quote("text/html"), Pattern.CASE_INSENSITIVE).matcher(bp.getContentType()).find()) {
	        			            // found html part
	        			        } else {
	        			            // some other bodypart...
	        			        	mailContent += (String) bp.getContent();
	        			        }
	        			    }
	        			}
	        		}
	        	}
	        }
	        BufferedReader bufReader = new BufferedReader(new StringReader(mailContent));
	        String line= null;
	        System.out.println(mailContent);
	        //Goes through the line strings of the mail
	        while ((line=bufReader.readLine()) != null) {
	        	if (line.contains("Meddelande")) {
	        		line = line.replace("Meddelande:", "");
	        		line = line.trim();
	        		System.out.println(line);
	        	}
	        	else if (line.contains("Totalbelopp")) {
	        		line = line.replace("Totalbelopp: ", "").replace("$","").replace("USD", "");
	        		line = line.trim();
	        		System.out.println(line);
	        	}
	        	else if (line.contains("Bidragsgivare")) {
	        		line = line.replace("Bidragsgivare: ", "");
	        		line = line.trim();
	        		System.out.println(line);
	        	}
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
