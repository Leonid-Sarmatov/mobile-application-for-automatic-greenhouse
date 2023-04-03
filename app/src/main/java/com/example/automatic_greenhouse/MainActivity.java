package com.example.automatic_greenhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    // Объекты окошек с параметрами работы
    private static TextView airTemperatureRequest;
    private static TextView lightingBrightnessRequest;
    private static TextView soilMoisture;
    private static TextView airHumidity;

    // Количество потоков соединения с главным сервером
    private static int counterStreamReadDataRun = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация окошек
        airTemperatureRequest = findViewById(R.id.air_temperature_request);
        lightingBrightnessRequest = findViewById(R.id.lighting_brightness_request);
        soilMoisture = findViewById(R.id.soil_moisture_request);
        airHumidity = findViewById(R.id.air_humidity_request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Слушатель для кнопки переключения в меню настроек(в разработке)
     * В меню можно настроить:
     * 1. Время включения освещения
     * 2. Время отключения освещения
     * 3. Поддерживаемая влажность почвы
     */
    public void startSettingsActivity(View view) {
        try {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } catch (Throwable е) {

        }
    }

    // Служебная кнопка для отладки
    public void sendMessageToServer(View view) {
        try {
            Response<MessageFromServer> h = Core.sendingDataToTheMainServer(
                    Core.getMessageToMainServer());
            //Toast.makeText(MainActivity.this, "Click!"+h.body().getAirHumidity(), Toast.LENGTH_LONG).show();
        } catch (Throwable t){
            //Toast.makeText(MainActivity.this, ""+t, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Слушатель для кнопки подключения к главному серверу
     * Для подключения использкется асинхронная функция
     * с бесконечным циклом. В нем POST запросом
     * отправляются конфигурации работы теплицы, принимаются данные датчиков и
     * обновляются данные в окошках
     */
    public void connectToServer(View view) {
        if (counterStreamReadDataRun > 2) {
            /**
             * Что бы пользователь не открыл много потоков,
             * тыкая кнопку, стоит ограничитель по количеству потоков
             */
        } else {
            counterStreamReadDataRun += 1;
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                while (true) {
                    MessageFromServer r = Core.sendingDataToTheMainServer(
                            Core.getMessageToMainServer()).body();
                    //Toast.makeText(MainActivity.this, "-> "+r, Toast.LENGTH_SHORT).show();
                    lightingBrightnessRequest.post(() -> {
                        lightingBrightnessRequest.setText(r.getLightingBrightness());
                    });

                    airTemperatureRequest.post(() -> {
                        airTemperatureRequest.setText(r.getAirTemperature());
                    });

                    soilMoisture.post(() -> {
                        soilMoisture.setText(r.getSoilMoisture());
                    });

                    airHumidity.post(() -> {
                        airHumidity.setText(r.getAirHumidity());
                    });
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            });
        }
    }
}