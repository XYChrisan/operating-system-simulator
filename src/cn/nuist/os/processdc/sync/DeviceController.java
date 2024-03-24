package cn.nuist.os.processdc.sync;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ibm.icu.text.SimpleDateFormat;
import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.PCB;

public class DeviceController extends ControlMainWindow{
	
	SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
	
	//设备ID
	int deviceID;
	public int getDeviceID() {
		return this.deviceID;
	}
	public void setDeviceID(int deviceID) {
		this.deviceID=deviceID;
	}
	
	//设备类型
	String deviceType;
	public String getDeviceType() {
		return this.deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType=deviceType;
	}
	
	//设备名称
	String deviceName;
	public String getDeviceName() {
		return this.deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName=deviceName;
	}
	
	//设备接口：接口类型_Hub序号：接口序号
	String deviceInterface;
	public String getDeviceInterface() {
		return this.deviceInterface;
	}
	public void setDeviceInterface(String deviceInterface) {
		this.deviceInterface=deviceInterface;
	}
	
	//设备锁：false表示空闲，true表示使用
	boolean lock;
	public boolean getLock() {
		return this.lock;
	}
	public void setLock(boolean lock) {
		this.lock=lock;
	}
	
	//设备供电
	boolean power;
	public boolean getPower() {
		return this.power;
	}
	public void setPower(boolean power) {
		this.power=power;
	}
	
	public void connect() {
		this.power=true;
		String time = sdf.format(new Date());
		textSystemLogs.append(time + "Device " + this.getDeviceName() + " connected! Device type: "+this.getDeviceType()+"; Device ID: "+this.getDeviceID()+"\n");
	}
	
	public void disconnect() {
		this.power=false;
		String time = sdf.format(new Date());
		textSystemLogs.append(time + "Device " + this.getDeviceName() + " disconnected!\n");
	}
	
	public void setUsing(PCB pcb) {
		this.setLock(true);
		String time = sdf.format(new Date());
		switch(this.getDeviceID()) {
		case 0:
			labelLED1.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/redled.png"));
			labelStatus1.setText("正在被进程"+pcb.getName()+"使用");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is allocated to process "+pcb.getName()+".\n");
			break;
		case 1:
			labelLED2.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/redled.png"));
			labelStatus2.setText("正在被进程"+pcb.getName()+"使用");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is allocated to process "+pcb.getName()+".\n");
			break;
		case 2:
			labelLED3.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/redled.png"));
			labelStatus3.setText("正在被进程"+pcb.getName()+"使用");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is allocated to process "+pcb.getName()+".\n");
			break;
		}
	}
	
	public void setFree() {
		this.setLock(false);
		String time = sdf.format(new Date());
		switch(this.getDeviceID()) {
		case 0:
			labelLED1.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/greenled.png"));
			labelStatus1.setText("就绪");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is free now.\n");
			break;
		case 1:
			labelLED2.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/greenled.png"));
			labelStatus2.setText("就绪");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is free now.\n");
			break;
		case 2:
			labelLED3.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/greenled.png"));
			labelStatus3.setText("就绪");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is free now.\n");
			break;
		}
	}
}
