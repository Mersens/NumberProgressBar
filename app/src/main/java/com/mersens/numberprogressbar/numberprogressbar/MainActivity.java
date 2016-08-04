package com.mersens.numberprogressbar.numberprogressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnProgressBarListener {
    private  NumberProgressBar progressBar;
    private Timer timer;
    private NumberProgressBarByView bnp;
    private static final int MSG_CODE=0X001;
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
            int pro=progressBar.getProgress();
            progressBar.setProgress(++pro);
            if(pro>=100){
                mHandler.removeMessages(MSG_CODE);
            }
            mHandler.sendEmptyMessageDelayed(MSG_CODE, 100);
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=(NumberProgressBar) findViewById(R.id.progressBar);
        mHandler.sendEmptyMessage(MSG_CODE);
        bnp = (NumberProgressBarByView) findViewById(R.id.numberbar1);
        bnp.setOnProgressBarListener(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bnp.incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 100);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
    @Override
    public void onProgressChange(int current, int max) {
        if(current == max) {
            Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_SHORT).show();
            bnp.setProgress(0);
        }
    }
}
