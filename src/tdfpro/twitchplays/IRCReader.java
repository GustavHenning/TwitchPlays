package tdfpro.twitchplays;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.newdawn.slick.util.Log;

import java.io.IOException;

public class IRCReader extends PircBot {

    public IRCReader(String name, String oauth, String channel){
        try {
//            setVerbose(true);
            setName(name);
            connect("irc.twitch.tv", 6667, oauth);
            joinChannel(channel);
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        System.out.println(sender + ": " + message);
    }
}
