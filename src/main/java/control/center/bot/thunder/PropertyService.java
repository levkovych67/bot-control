package control.center.bot.thunder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropertyService {

    //THIS IS FOR IDEA EXECUTION
    private static final String resources = "./src/main/resources/%s.properties";

    //THIS IS FOR TARGET EXECUTION
    //private static final String resources = "%s.properties";

    static void addRowToPropFile(String key, String val, String fileName) {
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

    public static void addCheckSum(String checkSum) {
        addRowToPropFile(checkSum, LocalDate.now().toString(), "sums");
    }

    public static String addLink(String link) {
        addRowToPropFile(link.replace("https://", ""), LocalDate.now().toString(), "links");
        return link;
    }

    public static List<String> getCheckSums() {
        Properties properties = new Properties();
        String sentPath = String.format(resources, "sums");

        try {
            properties.load(new FileInputStream(sentPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.keySet().stream().map(o -> (String) o).collect(Collectors.toList());
    }

    public static List<String> getLinks() {
        Properties properties = new Properties();
        String sentPath = String.format(resources, "links");

        try {
            properties.load(new FileInputStream(sentPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.keySet().stream().map(o -> (String) o).collect(Collectors.toList());
    }


}
