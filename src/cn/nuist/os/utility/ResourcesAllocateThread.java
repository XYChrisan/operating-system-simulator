package cn.nuist.os.utility;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.CreateProcess;
import cn.nuist.os.processdc.control.PCB;

public class ResourcesAllocateThread extends ControlMainWindow {
	public static void begin() {
		// 启动随机等待IO操作线程
		new Thread() {
			public void run() {
				while (true) {
					allocate();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	// 随机等待IO操作函数
	public static void allocate() {
		tablePCB.getDisplay().asyncExec(new Runnable() {
			public void run() {
				for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == CREATE) {
						if (pcb[i].getNeedMemory() < currentMemory) {
							pcb[i].switchToReady();
						}
					}
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == BLOCKED && checkDev(pcb[i])) {
						if (pcb[i].getNeedMemory() < currentMemory) {
							pcb[i].switchToRunning();
						}
					}
				}
			}
		});
	}

	public static boolean checkDev(PCB pcb) {
		boolean result = false;
		if (pcb.getNeedDeviceType() != null) {
			for (int i = 0; i < DEVICE_MAX_CAPACITY; i++) {
				if (dev[i].getDeviceType() == pcb.getNeedDeviceType() && dev[i].getPower() == true
						&& dev[i].getLock() == false && pcb.getUsingDevice() == "") {
					pcb.setUsingDevice(dev[i].getDeviceName());
					dev[i].setUsing(pcb);
					result = true;
				}
			}
			return result;
		} else {
			return true;
		}
	}
}
