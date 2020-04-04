package control.center.bot.thunder;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Utils {

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public static String getCommand(Update update) {
        if (update.getMessage() == null) {
            return update.getCallbackQuery().getData();
        } else {
            return update.getMessage().getText();
        }
    }

    public static boolean isBoardName(String command) {
        return Configuration.boards.contains(command);
    }

    public static boolean validCommand(String command) {
        return command.contains("/res/");
    }

    public static Integer getPage(String command) {
        final String page = StringUtils.substringBetween(command, "/page", "/");
        System.out.println(command);
        return Integer.valueOf(page);
    }

    public static String readPaginated(String command) {
        return command.substring(0, command.indexOf("/page"));
    }
}
