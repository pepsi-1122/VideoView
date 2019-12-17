package com.ewaytek.android;

import android.widget.Toast;

import com.ewaytek.android.bean.ComBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;


public abstract class SerialHelper {

	private SerialPort mSerialPort;

	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private SendThread mSendThread;

	private String sPort="/dev/s3c2410_serial0";
	private int iBaudRate=9600;
	private boolean _isOpen=false;
	private byte[] _bLoopData=new byte[]{0x30};
	private int iDelay=500;
	//----------------------------------------------------
	public SerialHelper(String sPort, int iBaudRate){
		this.sPort = sPort;
		this.iBaudRate=iBaudRate;
	}
	public SerialHelper(){
		this("/dev/s3c2410_serial0",9600);
	}
	public SerialHelper(String sPort){
		this(sPort,9600);
	}
	public SerialHelper(String sPort, String sBaudRate){
		this(sPort,Integer.parseInt(sBaudRate));
	}
	//----------------------------------------------------
	public void open() throws SecurityException, IOException,InvalidParameterException {
		mSerialPort =  new SerialPort(new File(sPort), iBaudRate, 0);
		mOutputStream = mSerialPort.getOutputStream();
		mInputStream = mSerialPort.getInputStream();
		mReadThread = new ReadThread();
		mReadThread.start();
		mSendThread = new SendThread();
		mSendThread.setSuspendFlag();
		mSendThread.start();
		_isOpen=true;
	}
	//----------------------------------------------------
	public void close(){
		if (mReadThread != null)
			mReadThread.interrupt();
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
		_isOpen=false;
	}
	//----------------------------------------------------
	public void send(byte[] bOutArray){
		try
		{
			mOutputStream.write(bOutArray);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	//----------------------------------------------------
	public void sendHex(String sHex){
		byte[] bOutArray = MyFunc.HexToByteArr(sHex);
		send(bOutArray);		
	}
	//----------------------------------------------------
	public void sendTxt(String sTxt){
		byte[] bOutArray =sTxt.getBytes();
		send(bOutArray);		
	}

	//----------------------------------------------------
	private class ReadThread extends Thread {
		@Override
		public void run() {
			byte[] buffer=new byte[1024];
			int size =0;
			int size_total =0;
			int timerCounter =0;
			size =0;
			size_total =0;
			timerCounter =0;
			super.run();
			while(!isInterrupted()) {

				try
				{
					if (mInputStream == null) return;
					//byte[] buffer=new byte[512];
					//available
					//int size = mInputStream.read(buffer);
					if(mInputStream.available()!=0)
					{
						byte[] bufferTemp=new byte[512];
						size = mInputStream.read(bufferTemp);
						System.arraycopy(bufferTemp,0,buffer,size_total,size);
						size_total=size_total+size;
						timerCounter=0;
						//Log.e("SerialHelper", "size_total = "+size_total);
					}
					timerCounter++;
					
					if((timerCounter>0)||(size_total>256))
					{
						
						if (size_total > 0){
							ComBean ComRecData = new ComBean(sPort,buffer,size_total);
							onDataReceived(ComRecData);
							size=0;
							size_total=0;
						}
						timerCounter=0;
					}
					
					try
					{
						Thread.sleep(1);//��ʱ1ms
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				} catch (Throwable e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}
	//----------------------------------------------------
	private class SendThread extends Thread {
		public boolean suspendFlag = true;// �����̵߳�ִ��
		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				synchronized (this)
				{
					while (suspendFlag)
					{
						try
						{
							wait();
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
				send(getbLoopData());
				try
				{
					Thread.sleep(iDelay);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		//�߳���ͣ
		public void setSuspendFlag() {
		this.suspendFlag = true;
		}
		
		//�����߳�
		public synchronized void setResume() {
		this.suspendFlag = false;
		notify();
		}
	}
	//----------------------------------------------------
	public int getBaudRate()
	{
		return iBaudRate;
	}
	public boolean setBaudRate(int iBaud)
	{
		if (_isOpen)
		{
			return false;
		} else
		{
			iBaudRate = iBaud;
			return true;
		}
	}
	public boolean setBaudRate(String sBaud)
	{
		int iBaud = Integer.parseInt(sBaud);
		return setBaudRate(iBaud);
	}
	//----------------------------------------------------
	public String getPort()
	{
		return sPort;
	}
	public boolean setPort(String sPort)
	{
		if (_isOpen)
		{
			return false;
		} else
		{
			this.sPort = sPort;
			return true;
		}
	}
	//----------------------------------------------------
	public boolean isOpen()
	{
		return _isOpen;
	}
	//----------------------------------------------------
	public byte[] getbLoopData()
	{
		return _bLoopData;
	}
	//----------------------------------------------------
	public void setbLoopData(byte[] bLoopData)
	{
		this._bLoopData = bLoopData;
	}
	//----------------------------------------------------
	public void setTxtLoopData(String sTxt){
		this._bLoopData = sTxt.getBytes();
	}
	//----------------------------------------------------
	public void setHexLoopData(String sHex){
		this._bLoopData = MyFunc.HexToByteArr(sHex);
	}
	//----------------------------------------------------
	public int getiDelay()
	{
		return iDelay;
	}
	//----------------------------------------------------
	public void setiDelay(int iDelay)
	{
		this.iDelay = iDelay;
	}
	//----------------------------------------------------
	public void startSend()
	{
		if (mSendThread != null)
		{
			mSendThread.setResume();
		}
	}
	//----------------------------------------------------
	public void stopSend()
	{
		if (mSendThread != null)
		{
			mSendThread.setSuspendFlag();
		}
	}
	//----------------------------------------------------
	protected abstract void onDataReceived(ComBean ComRecData);

	//----------------------------------------------------打开串口
	public void OpenComPort(SerialHelper ComPort){
		try
		{
			ComPort.open();
		} catch (SecurityException e) {
		} catch (IOException e) {
		} catch (InvalidParameterException e) {
		}
	}

	//----------------------------------------------------关闭串口
	public void CloseComPort(SerialHelper ComPort){
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
}

