package control.center.bot.youtube; /**
 * Sample Java code for youtube.channels.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/guides/code_samples#java
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@Component
public class YoutubeUploader {



    public static void uploadToYoutubeFromUrl(String url) throws IOException {
        Video metadata = new Video();

        metadata.setStatus(new VideoStatus().setPrivacyStatus("public"));

        VideoSnippet snippet = new VideoSnippet().setTitle(generateVideoName()).setDescription(generateVideoName());

        metadata.setSnippet(snippet);

        InputStreamContent mediaContent = new InputStreamContent("application/octet-stream", new BufferedInputStream(new URL(url).openStream()));

        YouTube youtube = YouTubeAuthHolder.getClient();

        YouTube.Videos.Insert insert = youtube.videos().insert("snippet,statistics,status", metadata, mediaContent);

        MediaHttpUploader uploader = insert.getMediaHttpUploader();

        uploader.setDirectUploadEnabled(false);
  insert.execute();


    }

    public static void uploadToYoutubeFromLocalFile(String path) throws IOException {
        Video metadata = new Video();
        metadata.setStatus(new VideoStatus().setPrivacyStatus("public"));
        VideoSnippet snippet = new VideoSnippet().setTitle(generateVideoName()).setDescription(generateVideoName());
        metadata.setSnippet(snippet);

        File mediaFile = new File(path);
        InputStreamContent mediaContent =
                new InputStreamContent("application/octet-stream",
                new BufferedInputStream(
                new FileInputStream(mediaFile)));


        YouTube youtube = YouTubeAuthHolder.getClient();

        YouTube.Videos.Insert videoinsert = youtube.videos().insert("snippet,statistics,status", metadata, mediaContent);
        MediaHttpUploader uploader = videoinsert.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(false);
        Video execute = videoinsert.execute();
        System.out.println("uploaded to youtube");

    }


    private static String generateVideoName(){
        return "Блискавка " + new Random().nextInt(99999);
    }


}