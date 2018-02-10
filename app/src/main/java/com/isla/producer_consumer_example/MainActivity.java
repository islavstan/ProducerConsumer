package com.isla.producer_consumer_example;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button bPause;
    private boolean isPause = false;
    private static final String LOG = "LOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bPause = (Button) findViewById(R.id.bPause);
        bPause.setText(R.string.pause);
        final Windows pc = new Windows();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pc.produce();
                } catch (InterruptedException e) {
                    Log.d(LOG, e.getMessage());
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pc.consume();
                } catch (InterruptedException e) {
                    Log.d(LOG, e.getMessage());
                }
            }
        });
        // Start both threads
        t1.start();
        t2.start();

        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPause) {
                    isPause = true;
                    bPause.setText(R.string.start);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            pc.onPause();
                        }
                    });

                } else {
                    isPause = false;
                    bPause.setText(R.string.pause);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            pc.onResume();
                        }
                    });
                }
            }
        });

    }
}
