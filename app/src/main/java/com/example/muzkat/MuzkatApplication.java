package com.example.muzkat;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class MuzkatApplication extends Application {
    public static final String YANDEX_METRICA_API_KEY = "71f245de-f33e-4b3e-bb1f-99fd50fd2210";
    @Override
    public void onCreate() {
        super.onCreate();
        YandexMetricaConfig config = YandexMetricaConfig
                .newConfigBuilder(YANDEX_METRICA_API_KEY)
                .build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking(this);
    }
}
