package com.android.lv.kuawo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech mSpeech;
    AlarmManager alarmManager;
    Calendar calendar = Calendar.getInstance(Locale.CHINESE);
    private Button btn = null;
    private Button btn1 = null;
    private TextView tv = null;
    private static final int CODE = 100;
    private Random rnd = new Random();
    private static String[] list_first = {"小卷卷好 ","小卷卷你好",
            "小卷卷你怎么这么","小卷卷真"};
    private static String[] list_mid = {"美","聪明","美丽","机智","善良","善解人意",
            "可爱","酷","冰雪聪明","贤惠端庄","小鸟依人","大方","俊俏","亭亭玉立",
            "温柔","有气质","有女人味","笨笨"};
    private static String[] list_last = {"呀！","的呀！","啊，嘤嘤嘤嘤！",
            "的呦！","的，对吧？","，爱你呦！"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkTTS();
        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
        tv = (TextView) findViewById(R.id.tv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String evStr = ev.getText().toString().trim();
                // CREATE A STRING TO READ HERE
                prideMe();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setClock();
            }
        });
    }

    private void prideMe() {
        String kuaStr = aNewPride();
        if (!TextUtils.isEmpty(kuaStr)) {
            tv.setText(kuaStr);
            // SPEECH THE WORDS HERE
            mSpeech.speak(kuaStr, TextToSpeech.QUEUE_FLUSH,
                    null, null);
        }
    }
    public void prideMeTwice() {
        String kuaStr;
        for (int i = 10; i>0; i--) {
            kuaStr = aNewPride();
            if (!TextUtils.isEmpty(kuaStr)) {
                // SPEECH THE WORDS HERE
                mSpeech.speak(kuaStr, TextToSpeech.QUEUE_FLUSH,
                        null, null);
            }
        }
    }

    private void setClock() {
        Date date = new Date();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR,hour);
                calendar.set(Calendar.MINUTE,minute);
            }
        }, hour, minute, true).show();

        Intent intent = new Intent();
        intent.setClass(this, AlarmBroadcastReceiver.class);
        Log.e("Ting Ting!", "pride me!");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
    }

    private String aNewPride() {
        String first = first = list_first[rnd.nextInt(list_first.length)];
        String mid = list_mid[rnd.nextInt(list_mid.length)];
        String last = list_last[rnd.nextInt(list_last.length)];
        return (first + mid + last);
    }

    private void checkTTS() {
        Intent in = new Intent();
        in.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(in, CODE);
    }

    public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                //LANGUAGE NEEDS TO BE SET HERE, in English by Locale.ENGLISH
                int result = mSpeech.setLanguage(Locale.CHINA);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("Language Tag", "not available");
                } else {
                    //INIT lists if needed
                    //A START WHICH MEANS EVERYTHING IS FINE!
                    btn.setEnabled(true);
                    mSpeech.speak("Hello, 你好！", TextToSpeech.QUEUE_FLUSH,
                            null,null);
                }
            }else {
                Toast.makeText(this, "TTS初始化失败", Toast.LENGTH_SHORT).show();
            }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE) {
            switch (resultCode) {
                case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
                    Toast.makeText(this, "TTS可用", Toast.LENGTH_SHORT).show();
                    mSpeech = new TextToSpeech(this, this);
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
                    Toast.makeText(this, "说不出口", Toast.LENGTH_SHORT).show();
                    // SPEECH FAIL
                    break;
            }
        }
    }

/*
    private void installTTS() {
        AlertDialog.Builder alertInstall = new AlertDialog.Builder(this)
                .setTitle("缺少语音包")
                .setMessage("下载语音包")
                .setPositiveButton("去下载",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // DOWNLOAD eyes-free AUDIO PACKAGE
                                String ttsDataUrl = "http://eyes-free.googlecode.com/files/tts_3.1_market.apk";
                                Uri ttsDataUri = Uri.parse(ttsDataUrl);
                                Intent ttsIntent = new Intent(
                                        Intent.ACTION_VIEW, ttsDataUri);
                                startActivity(ttsIntent);
                            }
                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertInstall.create().show();
    }*/
}
