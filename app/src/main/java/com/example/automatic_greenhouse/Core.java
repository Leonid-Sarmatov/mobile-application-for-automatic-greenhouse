package com.example.automatic_greenhouse;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Core {
    /**
     * 0 - нет ошибок
     * 1 - ошибка подключения к серверу
     * 2 - ошибка работы ядра
     * 3 - введены некорректные данные
     * 4 - ошибка в маин активити
     * 5 - ошибка на стороне сервера
     * 6 - ошибка на стороне расбери
     */
    private static int errorStatus = 0;

    // Конфигурации работы теплицы с натройками по умолчанию
    private static String userName = "test_user";
    private static String password = "test_password";
    private static int raspberryId = 0;
    private static Time timeOn = new Time(11, 40, 00);
    private static Time timeOff = new Time(22, 15, 00);
    private static int minimumSoilMoisture = 100;
    private static boolean port21On = false;

    // Объект конфигураций упакованный для отправки на сервер
    private static MessageToServer messageToMainServer =
            new MessageToServer(minimumSoilMoisture, timeOn, timeOff, port21On);

    // Адрес расбери (для отладки в локальной сети)
    private static final String RASPBERRY_URL = "http://192.168.1.73";//; //monitoring "http://192.168.1.73"

    // Адрес главного сервера(сервер с белым IP-адресом)
    private static final String MAIN_SERVER_URL = "http://80.90.190.200:6080"; //monitoring "http://192.168.1.67:8080"

    // Билдер и JSON-конвертер для прямого подключения к расбери
    private static Retrofit retrofitRaspberry = new Retrofit.Builder()
            .baseUrl(RASPBERRY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static Retrofit retrofitMainServer = new Retrofit.Builder()
            .baseUrl(MAIN_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Передаем в билдер интерфейс, в котором описаны методы запроса к серверу
    private static MessagesApi messagesApi = retrofitMainServer.create(MessagesApi.class);

    // Создает объект Response
    private static Response<MessageFromServer> response;

    // Запрос данных с расбери(для отладки в локальной сети)
    public static Response<MessageFromServer> requestingDataFromTheServer() {
        Call<MessageFromServer> messages = messagesApi.getData();

        messages.enqueue(new Callback<MessageFromServer>() {
            @Override
            public void onResponse(Call<MessageFromServer> call, Response<MessageFromServer> response) {
                // Если запрос успешен, то обновляем Response
                // и обнуляем индикатор ошибок
                setResponse(response);
                setErrorStatus(0);
            }

            @Override
            public void onFailure(Call<MessageFromServer> call, Throwable t) { setErrorStatus(1); }
        });
        return getResponse();
    }

    // Отправка данных на главный сервер
    public static Response<MessageFromServer> sendingDataToTheMainServer(MessageToServer messageToServer) {
        Call<MessageFromServer> message = messagesApi.sendData(messageToServer.isPort21On(),
                messageToServer.getTimeOnLighting(), messageToServer.getTimeOffLighting(),
                messageToServer.getMinimumSoilMoisture());

        message.enqueue(new Callback<MessageFromServer>() {
            @Override
            public void onResponse(Call<MessageFromServer> call, Response<MessageFromServer> response) {
                // Если запрос успешен, то обновляем Response
                // и обнуляем индикатор ошибок
                setErrorStatus(0);
                setResponse(response);
            }

            @Override
            public void onFailure(Call<MessageFromServer> call, Throwable t) {
                setErrorStatus(1);
            }
        });
        return getResponse();
    }

    public static void setErrorStatus(int errorStatus) {
        Core.errorStatus = errorStatus;
    }

    public static void setMessageToMainServer(MessageToServer messageToMainServer) {
        Core.messageToMainServer = messageToMainServer;
    }

    private static void setResponse(Response<MessageFromServer> response) {
        Core.response = response;
    }

    public static int getErrorStatus() {
        return errorStatus;
    }

    public static MessageToServer getMessageToMainServer() { return messageToMainServer; }

    public static Response<MessageFromServer> getResponse() {
        return response;
    }

    public static String getServerUrl() { return MAIN_SERVER_URL; }
}
