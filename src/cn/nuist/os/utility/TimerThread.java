package cn.nuist.os.utility;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.CreateProcess;

public class TimerThread extends ControlMainWindow {
	protected final static int READY = 1;
	protected final static int RUNNING = 2;
	protected final static int BLOCKED = 3;
	protected final static int SUSPEND = 4;
	protected final static int UNKNOWN = 0;

	public static void begin() {
		// 启动运行计时器
		new Thread() {
			public void run() {
				while (true) {
					addTime();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	// 运行计时器函数
	public static void addTime() {
		tablePCB.getDisplay().asyncExec(new Runnable() {
			public void run() {
				for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == RUNNING) {
						pcb[i].setRunTime(pcb[i].getRunTime() + 1);
					}
				}
			}
		});
	}
}
