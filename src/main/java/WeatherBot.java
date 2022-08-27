import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherBot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi =
                    new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new WeatherBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        sendMessage.enableHtml(true);

        try {

            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return "SuperPuperFrankyWeatherBot";
    }

    @Override
    public String getBotToken() {
        return "5529004931:AAEDy2e7EqgxFSVlS3breBmNik3HJrlS38s";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {

                case "/start":
                    sendMsg(message, "Френки бот погоды приветствует тебя! Напиши название своего города и узнаешь что происходит за окном!");
                    break;
                case "/help":
                    sendMsg(message, "Здравствуй дорогой друг, чем могу помочь?");
                    break;
                case "/settings":
                    sendMsg(message, "Ну тут настроечки всякие будут, чтобы выбрать город свой и все такое");
                    break;
                case "/secret":
                    sendMsg(message, "кто замер, тот молодец");
                    break;
                default:

                        try {
                            sendMsg(message, Weather.getWeather(message.getText(),model));
                        } catch (IOException e) {
                            sendMsg(message, "Такой город не найден!");
                        }

            }
        }

    }

    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/start"));
        keyboardFirstRow.add(new KeyboardButton("/help"));


        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

}