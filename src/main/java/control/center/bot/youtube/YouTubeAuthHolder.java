package control.center.bot.youtube;

import com.google.api.services.youtube.YouTube;

public class YouTubeAuthHolder {


    private static YouTube youtube = null;

    static YouTube  getClient() {
        if (youtube == null){
            youtube = YoutubeAuthBuilder.buildYoutube();
        }
        return youtube;

    }

    public static void initClient(){
        youtube = YoutubeAuthBuilder.buildYoutube();
    }



}
