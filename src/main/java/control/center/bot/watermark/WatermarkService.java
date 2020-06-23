package control.center.bot.watermark;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WatermarkService {

    private static String bliskavkaLink = "t.me/bliskavka2";
    private  static String command = "ffmpeg -i \"%s\" -vf \"drawtext=text='%s':x=15:y=H-th-10:fontfile=/pathto/font.ttf:fontsize=20:fontcolor=white:shadowcolor=black:shadowx=2:shadowy=2\" \"%s\"";

    public static String addWaterMark(String oldPath) {
        System.out.println("ADDING WATERMARK");
        String newPath = oldPath.replace(".mp4", "_w.mp4");
        String preparedCommand = String.format(command, oldPath, bliskavkaLink, newPath );
        executeBashCommand(preparedCommand);
        return newPath;
    }

    public static boolean executeBashCommand(String command) {
        boolean success = false;
        System.out.println("Executing BASH command:\n   " + command);
        Runtime r = Runtime.getRuntime();
        String[] commands = {"bash", "-c", command};
        try {
            Process p = r.exec(commands);

            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";

            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }

            b.close();
            success = true;
        } catch (Exception e) {
            System.err.println("Failed to execute bash with command: " + command);
            e.printStackTrace();
        }
        return success;
    }
}


