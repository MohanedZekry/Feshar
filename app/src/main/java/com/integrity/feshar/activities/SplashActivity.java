package com.integrity.feshar.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.integrity.feshar.R;
import java.util.concurrent.TimeUnit;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Completable.timer(4, TimeUnit.SECONDS,
                AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                });
    }
}