package com.avaki.cuBot.services.notification.tasks;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.models.User;

public class CleaningRemindingTask implements Runnable {
    private final User user;
    private final TelegramBot bot;

    public CleaningRemindingTask(User user, TelegramBot bot) {
        this.user = user;
        this.bot = bot;
    }

    @Override
    public void run() {
        bot.sendMessage(user.getChatId(), "На этих выходных ваша очередь уборки общего коридора." +
                "\nСпасибо, что следите за чистотой в квартире!");

        bot.sendMessage(bot.getAdminId(), String.format("На этих выходных общий коридор убирает:" +
                        "\nUsername: %s" +
                        "\nСтудия: %s"
                , user.getUserName(), user.getApartment().getNumber()));
    }
}
