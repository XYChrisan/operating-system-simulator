package cn.nuist.os.processdc.sync;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ibm.icu.text.SimpleDateFormat;
import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.PCB;

public class DeviceController extends ControlMainWindow{
	
	SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
	
	//�豸ID
	int deviceID;
	public int getDeviceID() {
		return this.deviceID;
	}
	public void setDeviceID(int deviceID) {
		this.deviceID=deviceID;
	}
	
	//�豸����
	String deviceType;
	public String getDeviceType() {
		return this.deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType=deviceType;
	}
	
	//�豸����
	String deviceName;
	public String getDeviceName() {
		return this.deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName=deviceName;
	}
	
	//�豸�ӿڣ��ӿ�����_Hub��ţ��ӿ����
	String deviceInterface;
	public String getDeviceInterface() {
		return this.deviceInterface;
	}
	public void setDeviceInterface(String deviceInterface) {
		this.deviceInterface=deviceInterface;
	}
	
	//�豸����false��ʾ���У�true��ʾʹ��
	boolean lock;
	public boolean getLock() {
		return this.lock;
	}
	public void setLock(boolean lock) {
		this.lock=lock;
	}
	
	//�豸����
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
			labelStatus1.setText("���ڱ�����"+pcb.getName()+"ʹ��");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is allocated to process "+pcb.getName()+".\n");
			break;
		case 1:
			labelLED2.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/redled.png"));
			labelStatus2.setText("���ڱ�����"+pcb.getName()+"ʹ��");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is allocated to process "+pcb.getName()+".\n");
			break;
		case 2:
			labelLED3.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/redled.png"));
			labelStatus3.setText("���ڱ�����"+pcb.getName()+"ʹ��");
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
			labelStatus1.setText("����");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is free now.\n");
			break;
		case 1:
			labelLED2.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/greenled.png"));
			labelStatus2.setText("����");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is free now.\n");
			break;
		case 2:
			labelLED3.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/greenled.png"));
			labelStatus3.setText("����");
			textSystemLogs.append(time+"Printer "+this.getDeviceName()+" is free now.\n");
			break;
		}
	}
}
