package control.center.bot.youtube;

import java.io.IOException;

public class YouTubeUploaderService {

    public static void upload(String location) throws IOException {
        System.out.println("uploading " + location + " to utube");
        if(isOnPc(location)){
            YoutubeUploader.uploadToYoutubeFromLocalFile(location);
        } else {
            YoutubeUploader.uploadToYoutubeFromUrl(location);
        }
    }

    private static Boolean isOnPc(String location){
        return location.contains("content");
    }


}
