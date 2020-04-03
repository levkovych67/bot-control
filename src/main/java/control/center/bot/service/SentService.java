package control.center.bot.service;

import control.center.bot.util.Util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class SentService {

    private static final String resources = "./src/main/resources/%s.properties";

    public static void addToSent(String link) {
        addRowToPropFile(Util.getResourceId(link), "sent", "sent");
    }

    private static void addRowToPropFile(String key, String val, String fileName) {
        Properties file = new Properties();
        String sentPath = String.format(resources, fileName);

        try {
            file.load(new FileInputStream(sentPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.getProperty(key) == null) {
            file.setProperty(key, val);
            try {
                file.store(new FileOutputStream(sentPath), null);
            } catch (IOException e) {
                System.out.println("TI LOX EI EXEPTION LOL PIDR");
            }
        }
    }

    public static Set<String> getSent() {
        Properties properties = new Properties();
        String sentPath = String.format(resources, "sent");

        try {
            properties.load(new FileInputStream(sentPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.keySet().stream().map(o -> (String) o).collect(Collectors.toSet());
    }

    public static Boolean isSent(String link) {
        final Set<String> sent = SentService.getSent();
        return sent.contains(Util.getResourceId(link));
    }
}
