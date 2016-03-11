package tdfpro.twitchplays;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {
    public static final String CONFIG_PATH = "config.json";

    public String username;
    public String oauthtoken;
    public String listenchannel;

    public static Config loadConfig() throws IOException {
        Gson gson = new Gson();
        StringBuilder json = new StringBuilder();
        Files.readAllLines(Paths.get(CONFIG_PATH)).stream().forEach(json::append);
        Config cfg = gson.fromJson(json.toString(), Config.class);
        return cfg;
    }
}
