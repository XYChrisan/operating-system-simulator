package cn.nuist.os.main;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.PCB;
import cn.nuist.os.processdc.sync.DeviceController;

public class Main {
	//PCB最大容量
	public final static int PCB_MAX_CAPACITY = 50;
	//设备槽最大容量
	public final static int DEVICE_MAX_CAPACITY = 3;
	//系统最大内存 4096KB
	public final static int MAX_MEMORY = 4096;

	//空pcb数组创建
	public static PCB pcb[] = new PCB[PCB_MAX_CAPACITY];
	public static int totalMemory = MAX_MEMORY;
	public static int currentMemory;
	//空设备槽创建
	public static DeviceController dev[]=new DeviceController[DEVICE_MAX_CAPACITY];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ControlMainWindow.start();
	}
}
