package cn.nuist.os.processdc.control;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

public class CreateProcess {
	protected final static int READY = 1;
	protected final static int RUNNING = 2;
	protected final static int BLOCKED = 3;
	protected final static int SUSPEND = 4;
	protected final static int CREATE = 5;
	protected final static int UNKNOWN = 0;

	// 判断pcb是否空
	public static boolean checkEmpty(PCB pcb) {
		if (pcb == null || pcb.isEmpty) {
			return true;
		} else {
			return false;
		}
	}

	// 判断各项数据合法性
	public static boolean CheckIntegrity(Text processName, Text prioritySelection, Combo userSelection, Text needTime,
			Text arriveTime, Text needMemory) {
		if (processName.getText() != "" && prioritySelection.getText() != "" && userSelection.getText() != ""
				&& needTime.getText() != "" && arriveTime.getText() != "" && needMemory.getText() != "") {
			return true;
		} else {
			return false;
		}
	}

	// 创建进程
	public static PCB createProcess(Text processName, Text priority, Combo user, Text needTime, Text arriveTime,
			int num, Text needMemory, Combo requestDevType) {
		PCB pcb = new PCB();
		if (CheckIntegrity(processName, priority, user, needTime, arriveTime, needMemory)) {
			pcb.isEmpty = false;
			pcb.setPID(num);
			pcb.setName(processName.getText());
			pcb.setPriority(Integer.parseInt(priority.getText()));
			pcb.setUser(user.getText());
			pcb.setNeedMemory(Integer.parseInt(needMemory.getText()));
			pcb.setErrorReason("");
			pcb.setHopeDeviceType(requestDevType.getText());
			pcb.setNeedDeviceType("none");
			pcb.setNeedTime(Integer.parseInt(needTime.getText()));
			pcb.setStatus(CREATE);
			pcb.setArriveTime(Integer.parseInt(arriveTime.getText()));
			pcb.setUsingMemory(0);
			pcb.setUsingDevice("");
			CreateStatus.start(true);
			CreateStatus.closeWindow();
			return pcb;
		} else {
			CreateStatus.start(false);
			return null;
		}

	}
	
	
	public static PCB oneKeyCreateProcess(String processName, int priority, String user, int needTime, int arriveTime,
			int num, int needMemory, String requestDevType) {
		PCB pcb = new PCB();
			pcb.isEmpty = false;
			pcb.setPID(num);
			pcb.setName(processName);
			pcb.setPriority(priority);
			pcb.setUser(user);
			pcb.setNeedMemory(needMemory);
			pcb.setErrorReason("");
			pcb.setHopeDeviceType(requestDevType);
			pcb.setNeedDeviceType("none");
			pcb.setNeedTime(needTime);
			pcb.setStatus(CREATE);
			pcb.setArriveTime(arriveTime);
			pcb.setUsingMemory(0);
			pcb.setUsingDevice("");
			pcb.ifEnd=false;
			return pcb;
	}
}