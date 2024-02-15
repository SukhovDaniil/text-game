import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;

public class Application {

    public static void main(String[] args) {
        TelegramBot telegramBot = new TelegramBot("");

        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                    System.out.println(update.message());
                    telegramBot.execute(
                        new SendMessage(update.message().from().id(), update.message().text())
                            .replyToMessageId(update.message().messageId())
                    );
                });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
