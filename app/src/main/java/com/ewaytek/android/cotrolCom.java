package com.ewaytek.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ewaytek.android.bean.AssistBean;
import com.ewaytek.android.bean.ComBean;

import java.io.IOException;
import java.security.InvalidParameterException;

public class cotrolCom extends Activity{

    private static SerialHelper serialHelper;
    private static SerialHelper serialHelper2;

    private Button btn_up1cm,btn_up2cm,btn_up5cm;
    private Button btn_dowm1cm,btn_dowm2cm,btn_dowm5cm;
    private Button pallet1Pos,pallet1Neg;
    private Button pallet2Pos,pallet2Neg;
    private Button pallet3Pos,pallet3Neg;
//    DeviceFun DeviceFun;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testdemo);
//        DeviceFun = new DeviceFun();
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

        OpenSerial();
    }

    private  void OpenSerial(){
        OpenComPort(serialHelper);
        OpenComPort(serialHelper2);

        sendPortData(serialHelper2,AssistBean.InitDevices);
        sendPortData(serialHelper2,AssistBean.MAXSpeed);
    }

    private void setClickListener() {

        btn_up1cm.setOnClickListener(new ButtonClickEvent1());
        btn_up2cm.setOnClickListener(new ButtonClickEvent1());
        btn_up5cm.setOnClickListener(new ButtonClickEvent1());
        btn_dowm1cm.setOnClickListener(new ButtonClickEvent1());
//        btn_dowm2cm.setOnClickListener(new ButtonClickEvent1());
        btn_dowm5cm.setOnClickListener(new ButtonClickEvent1());
        pallet1Pos.setOnClickListener(new ButtonClickEvent1());
        pallet2Pos.setOnClickListener(new ButtonClickEvent1());
        pallet3Pos.setOnClickListener(new ButtonClickEvent1());
        pallet1Neg.setOnClickListener(new ButtonClickEvent1());
        pallet2Neg.setOnClickListener(new ButtonClickEvent1());
        pallet3Neg.setOnClickListener(new ButtonClickEvent1());

    }

    //监听按键事件
    class ButtonClickEvent1 implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == btn_up1cm){
                sendPortData(serialHelper2,AssistBean.TakeSample5mL);
            }
            if (v == btn_up2cm){
//                DeviceFun.setChannel(1);
//                DeviceFun.start();
            }
            if (v == btn_up5cm){
                sendPortData(serialHelper,AssistBean.SlidingTableUpEND);
            }
            if (v == btn_dowm1cm){
                sendPortData(serialHelper,AssistBean.pallet1Positive24);
            }
            if (v == btn_dowm2cm){
                sendPortData(serialHelper,AssistBean.pallet1Reversal24);
            }
            if (v == btn_dowm5cm){
                sendPortData(serialHelper,AssistBean.SlidingTableDowmEND);
            }
            if (v == pallet1Pos){
               sendPortData(serialHelper,AssistBean.pallet1Positive24);
                //   sendPortData(serialHelper2,AssistBean.TakeSample3mL);
            }
            if (v == pallet1Neg){
               sendPortData(serialHelper,AssistBean.pallet1Reversal24);
                //   sendPortData(serialHelper2,AssistBean.InjectSample3mL);
            }
            if (v == pallet3Pos){
              //  sendPortData(serialHelper,AssistBean.pallet3Positive24);
                sendPortData(serialHelper2,AssistBean.TakeSample1mL);
            }
            if (v == pallet3Neg){
              //  sendPortData(serialHelper,AssistBean.pallet3Reversal24);
                sendPortData(serialHelper2,AssistBean.InjectSample1mL);
            }
            if (v == pallet2Pos){
                sendPortData(serialHelper,AssistBean.pallet2LifeEND);
            }
            if (v == pallet2Neg){
                sendPortData(serialHelper,AssistBean.pallet2RightEND);
            }
        }
    }

    private void initView() {
        btn_up1cm = (Button)findViewById(R.id.button2);
        btn_up2cm = (Button)findViewById(R.id.button3);
        btn_up5cm = (Button)findViewById(R.id.button4);
        btn_dowm1cm = (Button)findViewById(R.id.button5);
        btn_dowm2cm = (Button)findViewById(R.id.button6);
        btn_dowm5cm = (Button)findViewById(R.id.button7);

        pallet1Pos = (Button)findViewById(R.id.button8);
        pallet1Neg = (Button)findViewById(R.id.button9);
        pallet2Pos = (Button)findViewById(R.id.button11);
        pallet2Neg = (Button)findViewById(R.id.button12);
        pallet3Pos = (Button)findViewById(R.id.button13);
        pallet3Neg = (Button)findViewById(R.id.button14);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloseComPort(serialHelper);
        CloseComPort(serialHelper2);
    }

//    private  class  DeviceFun extends Thread{
//
//        private Object mPauseLock ;
//        private boolean mPauseFlag ;
//        public int mchannel;
//
//        public DeviceFun() {
//            mPauseLock = new Object() ;
//            mPauseFlag = false ;
//        }
//        public int getChannel()
//        {
//            return mchannel;
//        }
//        public void setChannel(int  mchannel){  this.mchannel=mchannel;}
//        public DeviceFun(String sPort) {
//            mPauseLock = new Object() ;
//            mPauseFlag = false ;
//        }
//        public void onPause()
//        {
//            synchronized (mPauseLock) {
//                mPauseFlag = true;
//            }
//        }
//        public void onResume()
//        {
//            synchronized (mPauseLock) {
//                mPauseFlag = false ;
//                mPauseLock.notifyAll() ;
//            }
//        }
//        private void pauseThread()
//        {
//            synchronized (mPauseLock) {
//                if(mPauseFlag)
//                {
//                    try{
//                        mPauseLock.wait() ;
//                    }catch(Exception e){
//                        //	Log.v("thread", "fails") ;
//                    }
//                }
//            }
//        }
//
//        @Override
//        public synchronized  void run() {
//            synchronized(DeviceFun) {
//                super.run();
//                switch(mchannel)
//                {
//                    case 1:
//                        TakeSampleA(5,1);
//                        break;
//                    case 2:
//
//                        break;
//                }
//
//            }
//        }
//    }
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


    //----------------------------------------------------串口发送
    public void sendPortData(SerialHelper ComPort,String sOut){
        if (ComPort!=null && ComPort.isOpen())
        {
            ComPort.sendHex(sOut);
        }
    }
    //----------------------------------------------------关闭串口
    private void CloseComPort(SerialHelper ComPort){
        if (ComPort!=null){
            ComPort.stopSend();
            ComPort.close();
        }
    }

    private  void  TakeSampleA(int SampleAChannel,int mL ){
//        sendPortData(serialHelper2,AssistBean.MAXSpeed);
//        SystemClock.sleep(100);
        sendPortData(serialHelper,AssistBean.pallet2LifeEND);//托盘电机2向左转80°
        SystemClock.sleep(1500);
        sendPortData(serialHelper,AssistBean.SlidingTableDowmEND);//滑台电机向下移动
        SystemClock.sleep(14000);
        TakeSample(5);                                  //注射泵开始工作注入5ml液体
        SystemClock.sleep(6500);
      sendPortData(serialHelper,AssistBean.SlidingTableUpEND);//滑台电机向上移动
       SystemClock.sleep(13000);
        sendPortData(serialHelper,AssistBean.pallet2RightEND);//托盘电机2向右转80°
        SystemClock.sleep(1000);
        sendPortData(serialHelper,AssistBean.SlidingTableDowmEND);//滑台电机向下移动
        SystemClock.sleep(13000);
        InjectSample(5);
        //注射泵开始工作注射5ml液体
        SystemClock.sleep(6500);
        sendPortData(serialHelper,AssistBean.SlidingTableUpEND);//滑台电机向上移动
    }

    private  void  TakeSampleB(int SampleAChannel,int mL ){

        sendPortData(serialHelper2,AssistBean.MAXSpeed);
        ChoseSample(SampleAChannel,mL);
        InjectSample(3,5);
        CleanTunnel(4,5);
        TakeAir(6,5);
    }

    private void CleanTunnel(int channel,int mL){

        Changechannel(4);    //管道注入清水清洗
        SystemClock.sleep(2400);
        TakeSample(5);
        SystemClock.sleep(6500);
        Changechannel(5);
        SystemClock.sleep(2400);
        InjectSample(5);		//清洗完注入废液中
        SystemClock.sleep(6500);

    }


    private  void TakeAir(int channel,int mL){

        Changechannel(6);		//管道注入空气
        SystemClock.sleep(2400);
        TakeSample(5);		//将成料的管道完全清入成料瓶中
        SystemClock.sleep(6500);

        Changechannel(3);
        SystemClock.sleep(2400);
        InjectSample(5);
        SystemClock.sleep(6500);


    }

    private  void ChoseSample(int channel,int mL){

        Changechannel(channel);
        SystemClock.sleep(2800);
        TakeSample(mL);
        SystemClock.sleep(6500);
    }

    private  void InjectSample(int channel,int mL) {
        Changechannel(channel);
        SystemClock.sleep(2800);
        InjectSample(mL);
        SystemClock.sleep(6500);

    }

    public void  InitDevice(){

        //	sendPortData(ComA, AssistData.InitDevices);

        sendPortData(serialHelper,AssistBean.InitDevices);
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

    private  void Changechannel(int channel ){
        // 改变切换阀道
        switch (channel){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

}
