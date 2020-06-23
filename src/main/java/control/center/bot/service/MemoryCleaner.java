package control.center.bot.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MemoryCleaner {

    private static final String VIDEO_CRON = "0 0 */3 * * *";

    @Scheduled(cron = VIDEO_CRON)
    private void recreateContentFolder(){
        System.out.println("CONTENT REMOVED");
    }
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (int i = 0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        boolean delete = dir.delete();
        return delete;
    }


}
