package cn.nuist.os.main;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.PCB;
import cn.nuist.os.processdc.sync.DeviceController;

public class Main {
	//PCB�������
	public final static int PCB_MAX_CAPACITY = 50;
	//�豸���������
	public final static int DEVICE_MAX_CAPACITY = 3;
	//ϵͳ����ڴ� 4096KB
	public final static int MAX_MEMORY = 4096;

	//��pcb���鴴��
	public static PCB pcb[] = new PCB[PCB_MAX_CAPACITY];
	public static int totalMemory = MAX_MEMORY;
	public static int currentMemory;
	//���豸�۴���
	public static DeviceController dev[]=new DeviceController[DEVICE_MAX_CAPACITY];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ControlMainWindow.start();
	}
}
