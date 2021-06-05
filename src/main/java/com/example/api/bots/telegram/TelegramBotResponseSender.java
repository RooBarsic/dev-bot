package com.example.api.bots.telegram;

import com.example.api.bots.BotResponseSender;
import com.example.message.DevBotResponse;
import com.example.message.data.DevBotButton;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for sending messages from Telegram bot to Telegram user
 *
 * @author Farrukh Karimov
 */
public class TelegramBotResponseSender extends DefaultAbsSender implements BotResponseSender {
    protected final String BOT_TOKEN;

    public TelegramBotResponseSender(@NotNull final DefaultBotOptions options, @NotNull final String BOT_TOKEN) {
        super(options);
        this.BOT_TOKEN = BOT_TOKEN;
    }

    @Override
    public boolean sendDevBotResponse(@NotNull final DevBotResponse DevBotResponse) {
        final String receiverChatId = DevBotResponse.getReceiverChatId();
        final String responseText = DevBotResponse.getMessage();

        // send files if has some

        final SendMessage telegramResponseMessage = new SendMessage();
        telegramResponseMessage.setChatId(receiverChatId);
        telegramResponseMessage.setText(responseText);

        // add buttons if has some
        if (DevBotResponse.hasButtons()) {
            if (DevBotResponse.isInlineButtons()) {
                final List<List<DevBotButton>> buttonsMatrix = DevBotResponse.getButtonsMatrix();
                final List<KeyboardRow> keyboardRowList = new ArrayList<>();
                for (final List<DevBotButton> buttonsInRow : buttonsMatrix) {
                    final KeyboardRow keyboardRow = new KeyboardRow();

                    for (final DevBotButton devBotButton : buttonsInRow) {
                        //keyboardRow.add(devBotButton.getButtonHiddenText());

                        KeyboardButton button = new KeyboardButton();
                        button.setText(devBotButton.getButtonHiddenText());
                        button.setRequestLocation(devBotButton.isRequiredLocation());
                        keyboardRow.add(button);

                    }
                    keyboardRowList.add(keyboardRow);
                }

                telegramResponseMessage.setReplyMarkup(
                        new ReplyKeyboardMarkup()
                                .setKeyboard(keyboardRowList)
                                .setResizeKeyboard(true)
                );
            } else {
                final List<List<DevBotButton>> buttonsMatrix = DevBotResponse.getButtonsMatrix();
                final List<List<InlineKeyboardButton>> telegramButtonsMatrix = new LinkedList<>();

                for (final List<DevBotButton> buttonsInRow : buttonsMatrix) {
                    telegramButtonsMatrix.add(new LinkedList<>());

                    for (final DevBotButton DevBotButton : buttonsInRow) {

                        final InlineKeyboardButton telegramKeyboardButton = new InlineKeyboardButton();
                        telegramKeyboardButton.setText(DevBotButton.getButtonText());
                        telegramKeyboardButton.setCallbackData(DevBotButton.getButtonHiddenText());

                        telegramButtonsMatrix
                                .get(telegramButtonsMatrix.size() - 1)
                                .add(telegramKeyboardButton);
                    }
                }
                telegramResponseMessage.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(telegramButtonsMatrix));
            }
        }

        // send response
        try {
            execute(telegramResponseMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
