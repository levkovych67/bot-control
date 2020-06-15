package control.center.bot.service;

import control.center.bot.holder.Holder;
import control.center.bot.object.SendVideoFileHolder;
import control.center.bot.util.SendVideoPreprocessor;
import control.center.bot.util.Util;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ContentGetter {

    private Holder holder;

    private ContentGetter() {
    }

    public ContentGetter(List<String> searchWords, List<String> exceptionalWords) {
        holder = new Holder(searchWords, exceptionalWords);
    }

    public SendVideoFileHolder getVideo() {
        Optional<String> video = holder.getVideo();
        return video.map(s ->
                SendVideoFileHolder.init(
                SendVideoPreprocessor.process(s),
                s
        )).orElse(null);

    }



    public Optional<String> getPic() {
        return holder.getPicture();
    }


}
