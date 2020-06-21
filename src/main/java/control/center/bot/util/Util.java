package control.center.bot.util;


import control.center.bot.configuration.Configuration;
import control.center.bot.object.SendVideoFileHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static control.center.bot.configuration.Configuration.THUNDER_ID;
import static control.center.bot.configuration.Configuration.friendlyChats;

public class Util {

    public static String getType(Update update) {
        final Message message = update.getChannelPost();
        if (message.getAnimation() != null) {
            return TYPE.VIDEO.name();
        }

        if (message.getVideo() != null) {
            return TYPE.VIDEO.name();
        }

        if (message.getPhoto() != null) {
            return TYPE.PICTURE.name();
        }

        if (message.getDocument() != null) {
            if ("video/mp4".equals(message.getDocument().getMimeType())) {
                return TYPE.VIDEO.name();
            }

            return TYPE.VIDEO.name();
        }
        return null;
    }

    public static String form(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        String withoutHTML = Jsoup.parse(text).text();
        if (withoutHTML.length() > 255) {
            return withoutHTML.substring(0, 255);
        } else {
            return withoutHTML;
        }
    }

    public static Boolean isVideo(String url) {
        return url.contains(".mp4") ||
                url.contains(".webm") ||
                url.contains(".gif");
    }

    public static Boolean isPic(String url) {
        return url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png");
    }

    public static Long getChatId(Update update) {
        try {
            if (update.getMessage() == null) {
                return update.getCallbackQuery().getMessage().getChatId();
            } else {
                return update.getMessage().getChatId();
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("Cant recognize chat it");
        }
    }

    public static String getCommand(Update update) {
        if (update.getMessage() == null) {
            return update.getCallbackQuery().getData();
        } else {
            return update.getMessage().getText();
        }
    }

    public static InlineKeyboardButton createInlineButtonText(String message, String callback) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(callback);
        inlineKeyboardButton.setText(message);
        return inlineKeyboardButton;
    }

    public static InlineKeyboardButton createInlineButtonLink(String text, String link) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(text);
        inlineKeyboardButton.setUrl(link);
        inlineKeyboardButton.setText(text);
        return inlineKeyboardButton;
    }

    public static InlineKeyboardMarkup createKeyBoardRow(List<InlineKeyboardButton> list) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(list));
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup createKeyFewBoardRow(List<List<InlineKeyboardButton>> list) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(list);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup createLikeMenu() {
        return Util.createKeyFewBoardRow(
                Arrays.asList(
                        Arrays.asList(
                                Util.createInlineButtonText("Блискавка 2.0", THUNDER_ID.toString()),
                                Util.createInlineButtonText("/DARK", Configuration.DARK_ID.toString())),
                        Arrays.asList(
                                Util.createInlineButtonText("/fap", Configuration.FAP_ID.toString()),
                                Util.createInlineButtonText("/music", Configuration.MUS_ID.toString()))
                ));
    }

    public static InlineKeyboardMarkup createLikeMenuWithUtube(SendVideoFileHolder videoFileHolder) {
        return Util.createKeyFewBoardRow(
                Arrays.asList(
                        Arrays.asList(
                                Util.createInlineButtonText("Блискавка 2.0", THUNDER_ID.toString()),
                                Util.createInlineButtonText("/DARK", Configuration.DARK_ID.toString())),
                        Arrays.asList(
                                Util.createInlineButtonText("/fap", Configuration.FAP_ID.toString()),
                                Util.createInlineButtonText("/music", Configuration.MUS_ID.toString())),
                        Arrays.asList(
                                Util.createInlineButtonText("utube", Util.getFileLocation(videoFileHolder))
                        )
                ));
    }

    private static String getFileLocation(SendVideoFileHolder videoFileHolder) {
        try {
            return videoFileHolder.getSendVideo().getVideo().getNewMediaFile().getAbsolutePath();

        } catch (Exception exc){
            return videoFileHolder.getPath();
        }
    }

    public static InlineKeyboardMarkup createThunderLikeMenu(Integer likes, Integer dislikes, Integer rofls, Integer shits) {
        return Util.createKeyBoardRow(
                Arrays.asList(
                        Util.createInlineButtonText("\uD83D\uDC4D️ " + likes, "like::" + likes),
                        Util.createInlineButtonText("\uD83D\uDC4E " + dislikes, "dislike::" + dislikes),
                        Util.createInlineButtonText("\uD83D\uDE2D " + rofls, "rofl::" + rofls),
                        Util.createInlineButtonLink("\uD83D\uDCB5", "https://www.donationalerts.com/r/bliskavka")
                ));
    }

    public static Optional<String> getVideoLink(Update update) {
        final Message message = update.getCallbackQuery().getMessage();
        if (message.getAnimation() != null) {
            return Optional.of(message.getAnimation().getFileId());
        }

        if (message.getVideo() != null) {
            return Optional.of(message.getVideo().getFileId());
        }

        if (message.getDocument() != null) {
            if ("video/mp4".equals(message.getDocument().getMimeType())) {
                return Optional.of(message.getDocument().getFileId());
            }

            return Optional.of(message.getVideo().getFileId());
        }
        return Optional.empty();
    }

    public static Pair<String, Integer> getReaction(String callback) {
        String separator = "::";
        String reaction = callback.substring(0, callback.indexOf(separator));
        Integer id = Integer.valueOf(callback.substring(callback.indexOf(separator) + separator.length()));
        return Pair.of(reaction, id);
    }

    public static Boolean isReaction(Update update) {
        try {
            if (update.getCallbackQuery().getMessage().getReplyMarkup().getKeyboard().get(0).size() == 4) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static Boolean forLikerPost(Update update) {
        try {
            if (update.getChannelPost().getVideo() != null) {
                return true;
            }
            if (update.getChannelPost().getPhoto() != null) {
                return true;
            }
            return update.getChannelPost().getAnimation() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getContentId(Update update) {
        final Message message = update.getChannelPost();
        if (message.getAnimation() != null) {
            return message.getAnimation().getFileId();
        }

        if (message.getVideo() != null) {
            return message.getVideo().getFileId();
        }

        if (message.getPhoto() != null) {
            return message.getPhoto().get(0).getFileId();
        }

        if (message.getDocument() != null) {
            if ("video/mp4".equals(message.getDocument().getMimeType())) {
                return message.getDocument().getFileId();
            }

            return message.getVideo().getFileId();
        }
        return null;
    }


    public static InlineKeyboardMarkup rebuildReplyMarkup(Update update) {
        List<InlineKeyboardButton> inlineKeyboardButtons = update
                .getCallbackQuery()
                .getMessage()
                .getReplyMarkup()
                .getKeyboard()
                .get(0).stream().filter(e -> e.getUrl() == null).collect(Collectors.toList());

        String reaction = getReaction(update.getCallbackQuery().getData()).getKey();
        Map<String, Integer> collect = inlineKeyboardButtons.stream()
                .map(e -> {
                    Pair<String, Integer> react = getReaction(e.getCallbackData());
                    if (react.getKey().equals(reaction)) {
                        return Pair.of(reaction, react.getValue() + 1);
                    }
                    return react;
                })
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        Integer shits = collect.get("shit");
        Integer likes = collect.get("like");
        Integer rofls = collect.get("rofl");
        Integer dislikes = collect.get("dislike");

        return createThunderLikeMenu(likes, dislikes, rofls, shits);
    }

    public static Integer getMessageId(Update update) {
        return update.getCallbackQuery().getMessage().getMessageId();
    }

    public static boolean isNews(Update update) {
        try {
            return StringUtils.containsIgnoreCase(
                    update.getChannelPost().getCaption(),
                    "постов");
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean isChannelPost(Update update) {
        return update.getChannelPost() != null;
    }

    public static String getResourceId(String link) {
        final String substring = link.substring(link.lastIndexOf("/") + 1);
        return substring.substring(0, substring.lastIndexOf("."));
    }

    public static void deleteFiles(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
            Files.deleteIfExists(Paths.get(path.replace("mp4", "webm")));
        } catch (IOException e) {
            System.out.println("could not delete video, error : " + e.getMessage());
        }


    }


    public static Boolean isForwardedFromFriendlyChat(Update update) {
        try {
            return friendlyChats.contains(update.getChannelPost().getForwardFromChat().getId());
        } catch (Exception e) {
            return false;
        }
    }
}

