package cn.nuist.os.utility;

import java.util.Date;
import java.util.Random;

import com.ibm.icu.text.SimpleDateFormat;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.CreateProcess;

public class RandomStatusSwitchThread extends ControlMainWindow {

	public static void begin() {
		// ��������ȴ�IO�����߳�
		new Thread() {
			public void run() {
				while (true) {
					waitForIO();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	// ����ȴ�IO��������
	public static void waitForIO() {
		tablePCB.getDisplay().asyncExec(new Runnable() {
			public void run() {
				Random rand = new Random();
				int i = rand.nextInt(50) + 0;
				if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == RUNNING) {
					pcb[i].setStatus(3);
					pcb[i].setErrorReason("�ȴ�I/O");
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "Process " + pcb[i].getName() + " blocked. Waiting for I/O.\n");
				}
			}
		});
	}
}
