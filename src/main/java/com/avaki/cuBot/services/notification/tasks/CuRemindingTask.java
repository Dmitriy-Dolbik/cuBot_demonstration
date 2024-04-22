package com.avaki.cuBot.services.notification.tasks;

import com.avaki.cuBot.businessLogic.TelegramBot;
import com.avaki.cuBot.models.User;

public class CuRemindingTask implements Runnable {
    private final User user;
    private final TelegramBot bot;

    public CuRemindingTask(User user, TelegramBot bot) {
        this.user = user;
        this.bot = bot;
    }

    @Override
    public void run() {
        bot.sendMessage(user.getChatId(), "Завтра расчетная дата." +
                "\nНажмите /cu, чтобы передать показания счетчиков и узнать стоимость коммунальных услуг.");

        bot.sendMessage(bot.getAdminId(), String.format("Завтра расчетная дата для арендатора" +
                        "\nUsername: %s" +
                        "\nСтудия: %s"
                , user.getUserName(), user.getApartment().getNumber()));
    }
}
