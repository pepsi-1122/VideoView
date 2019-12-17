package com.ewaytek.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ewaytek.android.bean.AssistBean;
import com.ewaytek.android.bean.ComBean;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import android_serialport_api.SerialPort;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements View.OnClickListener {

    private static SerialHelper serialHelper;
    private static SerialHelper serialHelper2;
    private final String ProductNo1 = "玫瑰籽精油";
    private final String ProductNo2 = "第二号原液";
    private final String ProductNo3 = "第三号原液";
    private final String ProductNo4 = "第四号原液";
    private final String ProductNo5 = "第五号原液";
    private final String ProductNo6 = "第六号原液";
    private ImageView five;
    private ImageView fiveLine;
    private ImageView ten;
    private ImageView tenLine;
    private ImageView fifteen;
    private ImageView fifteenLine;
    private ImageView twenty;
    private ImageView twentyLine;
    private ImageView thirty;
    private ImageView thirtyLine;
    private ImageView bigImage;
    private Button btn_one;
    private Button btn_two;
    private Button btn_three;
    private Button btn_four;
    private Button btn_five;
    private Button btn_six;
    private Button btn_seven;
    private Button btn_eight;
    private Button cancel;
    private Button ok;
    private Button Twoml;
    private Button Fiveml;
    private TextView productchose;
    private TextView MLchose;
    private AlertDialog dialog;
    private ImageView element1;
    private ImageView element2;
    private ImageView element3;

    private LinearLayout elementGuide;
    private LinearLayout elementGuide2;

    private boolean isWork=false;
    private String Product;
    private int Production;
    private int Capacity;

    boolean stepOne,stepTwo;
    //使用handler来处理线程问题
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage( Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case R.id.btn1:
                    if(!stepOne) {
                        element1.setVisibility(View.VISIBLE);
                        elementGuide.setVisibility(View.VISIBLE);
                        setBackGround(bigImage, R.mipmap.fifteen, fifteenLine);
                        getAlphaAnimotion1(element1);
                        showProgress();
                        stepOne = true;
                    }else if((stepOne) && (!stepTwo)){
                        element2.setVisibility(View.VISIBLE);
                        elementGuide2.setVisibility(View.VISIBLE);
                        setBackGround(bigImage, R.mipmap.fifteen, fifteenLine);
                        getAlphaAnimotion2(element2);
                        showProgress2();
                        stepTwo=true;
                    }
                    break;
            }
        }
    };
    private Message msg;
    //进度条1的控件
    private CountDownTimer timer;
    private TextView progressBarText;
    private TextView progress_time_text;
    private TextView progress_text;
    private MyProgressView2 progressView;

    //进度条2的控件
    private CountDownTimer timer2;
    private TextView progressBarText2;
    private MyProgressView2 progressView2;
    private TextView progress_time_text2;
    private TextView progress_text2;

    private long totalTime = 33 * 1000;
    private int progressInt = 0;

    DeviceFun DeviceFun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainl);


        int orienrartion = getResources().getConfiguration().orientation;
        if(orienrartion == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_mainl);
        }else if(orienrartion == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_mainp);
        }

        initView();
        setClickListener();

        //控制板com
        serialHelper = new SerialHelper("/dev/ttyUSB0", 9600) {
            @Override
            protected void onDataReceived(ComBean ComRecData) {
                final String str = new String(ComRecData.bRec);
                Log.e("hex1", new String(ComRecData.sComPort));
                Log.e("hex1", str);
            }
        };
        //注射泵com
        serialHelper2 = new SerialHelper("/dev/ttyUSB1", 9600) {
            @Override
            protected void onDataReceived(ComBean ComRecData) {
                final String str = new String(ComRecData.bRec);
            }

        };
//
      OpenSerial();

        DeviceFun = new DeviceFun();
    }

    //初始化控件
    private void initView() {

        five = (ImageView) findViewById(R.id.five);
        fiveLine = (ImageView) findViewById(R.id.five_line);
        ten = (ImageView) findViewById(R.id.ten);
        tenLine = (ImageView) findViewById(R.id.ten_line);
        fifteen = (ImageView) findViewById(R.id.fifteen);
        fifteenLine = (ImageView)findViewById(R.id.fifteen_line);
        twenty = (ImageView)findViewById(R.id.twenty);
        twentyLine = (ImageView)findViewById(R.id.twenty_line);
        thirty = (ImageView)findViewById(R.id.thirty);
        thirtyLine = (ImageView)findViewById(R.id.thirty_line);
        bigImage = (ImageView)findViewById(R.id.big_image);
        btn_one = (Button) findViewById(R.id.btn1);
        btn_two = (Button)findViewById(R.id.btn2);
        btn_three = (Button)findViewById(R.id.btn3);
        btn_four = (Button)findViewById(R.id.btn4);
        btn_five = (Button)findViewById(R.id.btn5);
        btn_six = (Button)findViewById(R.id.btn6);
        btn_seven = (Button)findViewById(R.id.btn7);
        btn_eight = (Button)findViewById(R.id.btn8);
        element1 = (ImageView) findViewById(R.id.element1);
        element2 = (ImageView) findViewById(R.id.element2);

        //进度条1
        progressView = (MyProgressView2) findViewById(R.id.element_progress);
        progressBarText = (TextView) findViewById(R.id.progress_bar_text);
        progress_time_text = (TextView) findViewById(R.id.progress_time_text);
        progress_text = (TextView) findViewById(R.id.progress_text);

        //进度条2
        progressBarText2 = (TextView) findViewById(R.id.progress_bar_text2);
        progressView2 = (MyProgressView2) findViewById(R.id.element_progress2);
        progress_text2 = (TextView) findViewById(R.id.progress_text2);
        progress_time_text2 = (TextView) findViewById(R.id.progress_time_text2);

        elementGuide = (LinearLayout) findViewById(R.id.element_guide);
        elementGuide2 = (LinearLayout) findViewById(R.id.element_guide2);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_style, null, false);
        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();
        cancel = (Button) view.findViewById(R.id.cancel);
        ok = (Button) view.findViewById(R.id.ok);
        Twoml = (Button) view.findViewById(R.id.Twoml);
        Fiveml = (Button) view.findViewById(R.id.Fiveml);
        //dialog textview

        productchose=(TextView)view.findViewById(R.id.productchose);
        MLchose=(TextView)view.findViewById(R.id.MLchose);
        //进度条1计时器
        timer = new CountDownTimer(totalTime, 330) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                progressInt = (int) (((totalTime - millisUntilFinished) / (totalTime * 1.f)) * 100.f);
                long time = (totalTime - millisUntilFinished) / 1000;
                String timeStr = (time < 10 ? "0:0" + time : "0:" + time);

                int changeTipInt = progressInt / 33;
                if(changeTipInt == 0){
                    progressBarText.setText("正在抽取");
                }else if(changeTipInt == 1){
                    progressBarText.setText("正在注入");
                }else if(changeTipInt == 2){
                    progressBarText.setText("正在清洁");
                }
                progress_time_text.setText(timeStr);
                progress_text.setText(progressInt + "%");
                progressView.setProgress(progressInt);
                //progressView.startAnim(progressInt);
                cancelClick(btn_one);
                cancelClick(btn_two);
                cancelClick(btn_three);
                cancelClick(btn_four);
                cancelClick(btn_five);
                cancelClick(btn_six);
                cancelClick(btn_seven);
                cancelClick(btn_eight);
                cancelClick(five);
                cancelClick(ten);
                cancelClick(fifteen);
                cancelClick(twenty);
                cancelClick(thirty);
            }

            @Override
            public void onFinish() {
                progressBarText.setText("已经完成");
                progressInt = 100;
                progress_text.setText(progressInt + "%");
                progressView.setProgress(100);
                //progressView.startAnim(100);
                canClick(btn_one);
                canClick(btn_two);
                canClick(five);
                canClick(ten);
                canClick(fifteen);
                canClick(twenty);
                canClick(thirty);

            }
        };

        //进度条2计时器
        timer2 = new CountDownTimer(totalTime, 330) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                progressInt = (int) (((totalTime - millisUntilFinished) / (totalTime * 1.f)) * 100.f);
                long time = (totalTime - millisUntilFinished) / 1000;
                String timeStr = (time < 10 ? "0:0" + time : "0:" + time);

                int changeTipInt = progressInt / 33;
                if(changeTipInt == 0){
                    progressBarText2.setText("正在抽取");
                }else if(changeTipInt == 1){
                    progressBarText2.setText("正在注入");
                }else if(changeTipInt == 2){
                    progressBarText2.setText("正在清洁");
                }
                progress_time_text2.setText(timeStr);
                progress_text2.setText(progressInt + "%");
                progressView2.setProgress(progressInt);
                //progressView2.startAnim(progressInt);
                cancelClick(btn_one);
                cancelClick(btn_two);
                cancelClick(btn_three);
                cancelClick(btn_four);
                cancelClick(btn_five);
                cancelClick(btn_six);
                cancelClick(btn_seven);
                cancelClick(btn_eight);
                cancelClick(five);
                cancelClick(ten);
                cancelClick(fifteen);
                cancelClick(twenty);
                cancelClick(thirty);
            }

            @Override
            public void onFinish() {
                progressBarText2.setText("已经完成");
                progressInt = 100;
                progress_text2.setText(progressInt + "%");
                progressView2.setProgress(100);
                //progressView2.startAnim(100);
                canClick(btn_one);
                canClick(btn_two);
                canClick(five);
                canClick(ten);
                canClick(fifteen);
                canClick(twenty);
                canClick(thirty);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false)
                        .setMessage("步骤已完成，点击确定回到最初状态")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                elementGuide.setVisibility(View.GONE);
                                elementGuide2.setVisibility(View.GONE);
                                element1.setVisibility(View.GONE);
                                element2.setVisibility(View.GONE);
                                element3.setVisibility(View.GONE);
                                bigImage.setVisibility(View.GONE);
                                fifteenLine.setVisibility(View.GONE);
                                stepOne=false;
                                stepTwo=false;
                                isWork=false;
                                MLchose.setText("");
                                productchose.setText("");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        };
        cancelClick(btn_three);
        cancelClick(btn_four);
        cancelClick(btn_five);
        cancelClick(btn_six);
        cancelClick(btn_seven);
        cancelClick(btn_eight);
    }

    //绑定监听
    private void setClickListener() {
        five.setOnClickListener(new ButtonClickEvent());
        ten.setOnClickListener(new ButtonClickEvent());
        fifteen.setOnClickListener(new ButtonClickEvent());
        twenty.setOnClickListener(new ButtonClickEvent());
        thirty.setOnClickListener(new ButtonClickEvent());
        btn_one.setOnClickListener(new ButtonClickEvent());
        btn_two.setOnClickListener(new ButtonClickEvent());
        btn_three.setOnClickListener(new ButtonClickEvent());
        btn_four.setOnClickListener(new ButtonClickEvent());
        btn_five.setOnClickListener(new ButtonClickEvent());
        btn_six.setOnClickListener(new ButtonClickEvent());
        btn_seven.setOnClickListener(new ButtonClickEvent());
        btn_eight.setOnClickListener(new ButtonClickEvent());
        cancel.setOnClickListener(new ButtonClickEvent());

    }

    private void canClick (View view) {
        view.setClickable(true);
    }


    //监听按键事件
    class ButtonClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == five){
                setBackGround(bigImage, R.mipmap.five, fiveLine);
                setLine(tenLine);
                setLine(fifteenLine);
                setLine(twentyLine);
                setLine(thirtyLine);
                //   Log.e("jkj", "five " );
            }else if(v == ten){
                setBackGround(bigImage, R.mipmap.ten, tenLine);
                setLine(fiveLine);
                setLine(fifteenLine);
                setLine(twentyLine);
                setLine(thirtyLine);
            }else if(v == fifteen){
                setBackGround(bigImage, R.mipmap.fifteen, fifteenLine);
                setLine(tenLine);
                setLine(fiveLine);
                setLine(twentyLine);
                setLine(thirtyLine);
            }else if(v == twenty){
                setBackGround(bigImage, R.mipmap.twenty, twentyLine);
                setLine(tenLine);
                setLine(fifteenLine);
                setLine(fiveLine);
                setLine(thirtyLine);
            }else if(v == thirty){
                setBackGround(bigImage, R.mipmap.thirty, thirtyLine);
                setLine(tenLine);
                setLine(fifteenLine);
                setLine(twentyLine);
                setLine(fiveLine);
            }else if (v == btn_one){
                showDialog1();
                if (isWork)
                {    makeDialog();}
                Production=1;
                Product= ProductNo1;
            }else if (v == btn_two){
                showDialog1();
                if (isWork)
                {    makeDialog();}
                Production=2;
                Product= ProductNo2;
            }else if (v == btn_three){
                showDialog3();
                Product= ProductNo3;
            }else if (v == btn_four){
                showDialog4();
                Product= ProductNo4;
            }else if (v == btn_five){
                showDialog5();
                Product= ProductNo5;
            }else if (v == btn_six){
                showDialog6();
                Product= ProductNo6;
            }else if (v == btn_seven){
                showDialog7();
            }else if (v == btn_eight){
                showDialog8();
            }else if(v == cancel) {
                dialogCancel();
                MLchose.setText("");
                productchose.setText("");
            }
        }
    }

    private void makeDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setMessage("是否重新开始配置？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        elementGuide.setVisibility(View.GONE);
                        elementGuide2.setVisibility(View.GONE);
                        element1.setVisibility(View.GONE);
                        element2.setVisibility(View.GONE);
                        bigImage.setVisibility(View.GONE);
                        element3.setVisibility(View.GONE);
                        fifteenLine.setVisibility(View.GONE);
                        stepOne=false;
                        stepTwo=false;
                        isWork=false;
                        MLchose.setText("");
                        productchose.setText("");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void cancelClick (View view) {
        view.setClickable(false);
    }
    private void setLine(ImageView imageView) {
        imageView.setVisibility(View.INVISIBLE);
    }

    private void setBackGround(ImageView bigImage, int ResId, ImageView line) {
        bigImage.setBackgroundResource(0);
        bigImage.setBackgroundResource(ResId);
        line.setVisibility(View.VISIBLE);
    }

    //设置成分1的透明过度
    private void getAlphaAnimotion1(ImageView imageView) {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50000);
        imageView.startAnimation(animation);
    }

    //设置成分2的透明过度
    private void getAlphaAnimotion2(ImageView imageView) {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50000);
        imageView.startAnimation(animation);
    }

    //成分1进度条显示
    private void showProgress() {

        /*new Thread(new Runnable() {
            public void run() {
                int i = 0;
                msg = new Message();
                while (i < 50) {
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (i > 0 && i < 8) {
                        *//*msg.obj = "正在抽取";
                        handler.sendMessage(msg);*//*
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBarText.setText("正在抽取");
                            }
                        });
                    } else if (i >= 8 && i < 16) {
                        *//*msg.obj = "正在注入";
                        handler.sendMessage(msg);*//*
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBarText.setText("正在注入");
                            }
                        });
                    } else if (i >= 16 && i < 24) {
                        *//*msg.obj = "正在清洁";
                        handler.sendMessage(msg);*//*
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBarText.setText("正在清洁");
                            }
                        });
                    } else if (i == 24){
                        *//*msg.obj = "已经完成";
                        handler.sendMessage(msg);*//*
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBarText.setText("已经完成");
                            }
                        });
                    }
                    i++;
                }
            }
        }).start();*/
        timer.start();
        //progressView.startAnim();
    }

    //成分2进度条显示
    private void showProgress2() {
        timer2.start();
        //progressView2.startAnim();
    }

    private void showDialog1() {
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent =new Intent(MainActivity.this,cotrolCom.class);
//                startActivity(intent);
//                ShowMessage("thirty");
                DeviceFun.setParameter(Production);
                DeviceFun.setCapacity(Capacity);
                DeviceFun.start();
                isWork=true;
                msg = new Message();
                msg.what = R.id.btn1;
                handler.sendMessage(msg);
                dialog.dismiss();
            }
        });
        Twoml.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MLchose.setText("");
                    productchose.setText("");
                    MLchose.setText("2ML");
                    productchose.setText(Product);
                    Capacity=3;

                }
        });
        Fiveml.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShowMessage("thirty");
                MLchose.setText("");
                productchose.setText("");
                MLchose.setText("5ML");
                productchose.setText(Product);
                Capacity=5;
            }
        });
    }
    private void showDialog2(){
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = R.id.btn2;
                dialog.dismiss();

            }
        });
    }
    private void showDialog3(){
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ShowMessage("123");
                dialog.dismiss();
            }
        });
    }
    private void showDialog4(){
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //    InjectSample(5);
                dialog.dismiss();
            }
        });
    }
    private void showDialog5(){
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //    TakeSample(5);
                dialog.dismiss();
            }
        });
    }
    private void showDialog6(){
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void showDialog7(){
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void showDialog8(){
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void dialogCancel() {
        dialog.dismiss();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloseComPort(serialHelper);
        CloseComPort(serialHelper2);
    }

    private  void OpenSerial(){
        OpenComPort(serialHelper);
        OpenComPort(serialHelper2);
        sendPortData(serialHelper2,AssistBean.MAXSpeed);//注射泵最大速度
    }
    //----------------------------------------------------打开串口
    private void OpenComPort(SerialHelper ComPort){
        try
        {
            ComPort.open();
           ShowMessage("打开串口成功");

        } catch (SecurityException e) {
            ShowMessage("打开串口失败:没有串口读s'z/写权限!");
        } catch (IOException e) {
            ShowMessage("打开串口失败:未知错误!");
        } catch (InvalidParameterException e) {
            ShowMessage("打开串口失败:参数错误!");
        }
    }
    //------------------------------------------显示消息
    private void ShowMessage(String sMsg)
    {
        Toast.makeText(this, sMsg, Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------------------关闭串口
    private void CloseComPort(SerialHelper ComPort){
        if (ComPort!=null){
            ComPort.stopSend();
            ComPort.close();
        }
    }
    //----------------------------------------------------串口发送
    public void sendPortData(SerialHelper ComPort,String sOut){
        if (ComPort!=null && ComPort.isOpen())
        {
            ComPort.sendHex(sOut);
        }
    }

    private  class  DeviceFun extends Thread{

        private Object mPauseLock ;
        private boolean mPauseFlag ;
        public int mchannel;
        public int ML;
        public DeviceFun()
        {
            mPauseLock = new Object() ;
            mPauseFlag = false ;
        }

        public int getParameter()
        {
            return mchannel;
        }
        public void setParameter(int  mchannel){  this.mchannel=mchannel;}

        public int getCapacity()
        {
            return ML;
        }
        public void setCapacity(int ML){ this.ML=ML;}
        public DeviceFun(String sPort) {
            mPauseLock = new Object() ;
            mPauseFlag = false ;
        }
        public void onPause() {
            synchronized (mPauseLock) {
                mPauseFlag = true;
            }
        }
        public void onResume() {
            synchronized (mPauseLock) {
                mPauseFlag = false ;
                mPauseLock.notifyAll() ;
            }
        }
        private void pauseThread() {
            synchronized (mPauseLock) {
                if(mPauseFlag)
                {
                    try{
                        mPauseLock.wait() ;
                    }catch(Exception e){
                        //	Log.v("thread", "fails") ;
                    }
                }
            }
        }

        @Override
        public synchronized  void run() {
            synchronized(DeviceFun) {
                super.run();
               ML=getCapacity();
                Log.d(TAG, "ML="+String.valueOf(ML));
                Log.d(TAG, "mchannel="+String.valueOf(mchannel));
                switch(mchannel)
                {
                    case 1:
                        Function(ML);
                        break;
                    case 2:
                        sendPortData(serialHelper,AssistBean.pallet2LifeEND);//托盘电机2向左转80°
                        SystemClock.sleep(1500);
                        sendPortData(serialHelper,AssistBean.pallet1Positive24);
                        SystemClock.sleep(1500);
                        sendPortData(serialHelper,AssistBean.SlidingTableDowmEND);//滑台电机向下移动
                        SystemClock.sleep(6000);
                        TakeSample(ML);                                  //注射泵开始工作注入5ml液体
                        SystemClock.sleep(6500);
                        sendPortData(serialHelper,AssistBean.SlidingTableUpEND);//滑台电机向上移动
                        SystemClock.sleep(6000);
                       sendPortData(serialHelper,AssistBean.pallet2RightEND);//托盘电机2向右转80°
                        SystemClock.sleep(1500);
                        sendPortData(serialHelper,AssistBean.SlidingTableDowm3cm);//滑台电机向下移动
                        SystemClock.sleep(3000);
                        InjectSample(ML);      //注射泵开始工作注射5ml液体
                        SystemClock.sleep(6500);
                        sendPortData(serialHelper,AssistBean.SlidingTableU3pcm);//滑台电机向上移动

                        break;
                }

			/*	if(mchannel==1) {
                       ChoseSample(2, 5);
                    //   InjectSample(3, 5);
                    //    CleanTunnel(4, 5);
                    //    TakeAir(6, 5);
                }*/
            }
        }

        private void Function(int ML) {
            SystemClock.sleep(100);
            sendPortData(serialHelper,AssistBean.pallet2LifeEND);//托盘电机2向左转80°
            SystemClock.sleep(1500);
            sendPortData(serialHelper,AssistBean.SlidingTableDowmEND);//滑台电机向下移动
            SystemClock.sleep(6000);
            TakeSample(ML);                                  //注射泵开始工作注入5ml液体
            SystemClock.sleep(6500);
            sendPortData(serialHelper,AssistBean.SlidingTableUpEND);//滑台电机向上移动
            SystemClock.sleep(6000);
            sendPortData(serialHelper,AssistBean.pallet2RightEND);//托盘电机2向右转80°
            SystemClock.sleep(1500);
            sendPortData(serialHelper,AssistBean.SlidingTableDowm3cm);//滑台电机向下移动
            SystemClock.sleep(3000);
            InjectSample(ML);      //注射泵开始工作注射5ml液体
            SystemClock.sleep(6500);
            sendPortData(serialHelper,AssistBean.SlidingTableU3pcm);//滑台电机向上移动
        }
    }
    private  void  TakeSampleA(int SampleAChannel,int mL ){
//        sendPortData(serialHelper2,AssistBean.MAXSpeed);
//        SystemClock.sleep(100);
        sendPortData(serialHelper,AssistBean.pallet2LifeEND);//托盘电机2向左转80°
        SystemClock.sleep(1000);
//        sendPortData(serialHelper,AssistBean.SlidingTableDowmEND);//滑台电机向下移动
//        SystemClock.sleep(14000);
//        TakeSample(5);                                  //注射泵开始工作注入5ml液体
//        SystemClock.sleep(6500);
//        sendPortData(serialHelper,AssistBean.SlidingTableUpEND);//滑台电机向上移动
//        SystemClock.sleep(13000);
        sendPortData(serialHelper,AssistBean.pallet2RightEND);//托盘电机2向右转80°
        SystemClock.sleep(1000);
//        sendPortData(serialHelper,AssistBean.SlidingTableDowmEND);//滑台电机向下移动
//        SystemClock.sleep(13000);
//        InjectSample(5);
//        //注射泵开始工作注射5ml液体
//        SystemClock.sleep(6500);
//        sendPortData(serialHelper,AssistBean.SlidingTableUpEND);//滑台电机向上移动
    }

    private  void InjectSample(int mL)  //注射泵注射样品
    {
        switch (mL)
        {
            case 1:
                sendPortData(serialHelper2,AssistBean.InjectSample1mL);//注射泵注射样品1mL
                break;

            case 3:
                sendPortData(serialHelper2,AssistBean.InjectSample3mL);//注射泵注射样品3mL
                break;

            case 5:
                sendPortData(serialHelper2,AssistBean.InjectSample5mL);//注射泵注射样品5mL
                break;
        }
    }

    private  void TakeSample(int mL)    //往注射泵注入样品
    {
        switch (mL)
        {
            case 1:
                sendPortData(serialHelper2,AssistBean.TakeSample1mL);//注入样品1mL
                break;

            case 3:
                sendPortData(serialHelper2,AssistBean.TakeSample3mL);//注入样品3mL
                break;

            case 5:
                sendPortData(serialHelper2,AssistBean.TakeSample5mL);//注入样品5mL
                break;
        }
    }
}
