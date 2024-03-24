package cn.nuist.os.utility;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.CreateProcess;

public class AutoFreshThread extends ControlMainWindow {
	static int refreshTime = 3000;
	public static void begin() {
		// 启动自动刷新线程
		new Thread() {
			public void run() {
				while (true) {
					refresh();
					try {
						Thread.sleep(refreshTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	// 设置刷新时间
	public static void setRefreshTime(int time) {
		refreshTime = time;
	}

	// 界面内容自动刷新函数
	public static void refresh() {
		tablePCB.getDisplay().asyncExec(new Runnable() {
			public void run() {
				int PCBAmount = 0;
				int readyAmount = 0;
				int runningAmount = 0;
				int blockedAmount = 0;
				int suspendAmount = 0;
				int usingMemory = 0;
				tablePCB.removeAll();
				tableReady.removeAll();
				tableRunning.removeAll();
				tableBlocked.removeAll();
				tableSuspend.removeAll();
				for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
					if (!CreateProcess.checkEmpty(pcb[i])) {
						PCBAmount++;
						usingMemory += pcb[i].getUsingMemory();
						TableItem item = new TableItem(tablePCB, SWT.NONE);
						item.setText(new String[] { String.valueOf(pcb[i].getPID()), pcb[i].getName(),
								pcb[i].getStatusStr(), String.valueOf(pcb[i].getPriority()), pcb[i].getUser(),
								String.valueOf(pcb[i].getNeedMemory()) + "KB", pcb[i].getErrorReason(),
								pcb[i].getUsingDevice(), String.valueOf(pcb[i].getNeedTime()),
								String.valueOf(pcb[i].getRunTime()), String.valueOf(pcb[i].getRestNeedTime()),
								String.valueOf(pcb[i].getArriveTime()), String.valueOf(pcb[i].getStartTime()),
								String.valueOf(pcb[i].getEndTime()),pcb[i].getMessage() });
					}
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == READY) {
						readyAmount++;
						TableItem item = new TableItem(tableReady, SWT.NONE);
						item.setText(new String[] { String.valueOf(pcb[i].getPID()), pcb[i].getName(),
								String.valueOf(pcb[i].getPriority()), String.valueOf(pcb[i].getRestNeedTime()) });
						usingMemory+=pcb[i].getNeedMemory();
					}
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == RUNNING) {
						runningAmount++;
						TableItem item = new TableItem(tableRunning, SWT.NONE);
						item.setText(new String[] { String.valueOf(pcb[i].getPID()), pcb[i].getName(),
								String.valueOf(pcb[i].getPriority()), String.valueOf(pcb[i].getRestTime()) });
						usingMemory+=pcb[i].getNeedMemory();
					}
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == BLOCKED) {
						blockedAmount++;
						TableItem item = new TableItem(tableBlocked, SWT.NONE);
						item.setText(new String[] { String.valueOf(pcb[i].getPID()), pcb[i].getName(),
								String.valueOf(pcb[i].getPriority()) });
					}
					if (!CreateProcess.checkEmpty(pcb[i]) && pcb[i].getStatusNum() == SUSPEND) {
						suspendAmount++;
						TableItem item = new TableItem(tableSuspend, SWT.NONE);
						item.setText(new String[] { String.valueOf(pcb[i].getPID()), pcb[i].getName(),
								String.valueOf(pcb[i].getPriority()), String.valueOf(pcb[i].getRestTime()) });
					}
					currentMemory=totalMemory - usingMemory;
				}
				labelFreePCBNum.setText(String.valueOf(PCB_MAX_CAPACITY - PCBAmount));
				labelReadyProcessNum.setText(String.valueOf(readyAmount));
				labelRunningProcessNum.setText(String.valueOf(runningAmount));
				labelBlockedProcessNum.setText(String.valueOf(blockedAmount));
				labelSuspendProcessNum.setText(String.valueOf(suspendAmount));
				labelFreeMemoryNum.setText(String.valueOf(currentMemory));
			}
		});
	}
}
