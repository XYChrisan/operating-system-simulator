package cn.nuist.os.utility;

import java.util.Date;
import java.util.Random;

import com.ibm.icu.text.SimpleDateFormat;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.CreateProcess;

public class RandomRemoveNeedDevice extends ControlMainWindow{
	public static void begin() {
		// 启动随机等待IO操作线程
		new Thread() {
			public void run() {
				while (true) {
					removeDevice();
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	// 随机等待IO操作函数
	public static void removeDevice() {
		tablePCB.getDisplay().asyncExec(new Runnable() {
			public void run() {
				Random rand = new Random();
				int i = rand.nextInt(50) + 0;
				if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == RUNNING && pcb[i].getHopeDeviceType()!=null && pcb[i].getHopeDeviceType()!="" && pcb[i].getNeedDeviceType()!="none") {
					for(int j=0;j<DEVICE_MAX_CAPACITY;j++) {
						if(dev[j].getDeviceName().equals(pcb[i].getUsingDevice())) {
							dev[j].setFree();
						}
					}
					pcb[i].setNeedDeviceType("none");
					pcb[i].setUsingDevice("");
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "Process " + pcb[i].getName() + " released device.\n");
				}
			}
		});
	}
}
