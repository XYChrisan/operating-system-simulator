package cn.nuist.os.utility;

import java.util.Date;

import com.ibm.icu.text.SimpleDateFormat;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.CreateProcess;

public class CheckTimeThread extends ControlMainWindow {

	public static void begin() {
		new Thread() {
			public void run() {
				while (true) {
					checkStatus();
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

	public static void checkStatus() {
		tablePCB.getDisplay().asyncExec(new Runnable() {
			public void run() {
				for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == BLOCKED
							&& pcb[i].getRestTime() == 0 && !pcb[i].ifEnd) {
						pcb[i].setEndTime(Integer.parseInt(labelSystemTimeNum.getText()));
						pcb[i].ifEnd=true;
					}
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == READY
							&& pcb[i].getArriveTime() == Integer.parseInt(labelSystemTimeNum.getText())) {
						pcb[i].switchToRunning();
						pcb[i].setStartTime(Integer.parseInt(labelSystemTimeNum.getText()));
					}
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == RUNNING
							&& pcb[i].getRunTime() == pcb[i].getNeedTime()) {
						pcb[i].switchToBlocked();
						pcb[i].setEndTime(Integer.parseInt(labelSystemTimeNum.getText()));
						textSystemLogs.append(" Runtime Out.\n");
						pcb[i].setErrorReason("Ê±¼äÆ¬Íê");
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
			}
		});
	}
}
