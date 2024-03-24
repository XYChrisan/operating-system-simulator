package cn.nuist.os.processdc.control;

import java.util.Date;

import com.ibm.icu.text.SimpleDateFormat;

public class PCB extends ControlMainWindow {

	SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");

	// ��ʶ��pid��int
	protected int pid;

	public int getPID() {
		return this.pid;
	}

	public void setPID(int pid) {
		this.pid = pid;
	}

	// ����name��String
	protected String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ״̬status��int
	protected int status;
	protected final int READY = 1;
	protected final int RUNNING = 2;
	protected final int BLOCKED = 3;
	protected final int SUSPEND = 4;
	protected final int CREATE = 5;
	protected final int UNKNOWN = 0;

	public int getStatusNum() {
		return this.status;
	}

	public String getStatusStr() {
		switch (this.status) {
		case READY:
			return "����";
		case RUNNING:
			return "��������";
		case BLOCKED:
			return "����";
		case SUSPEND:
			return "����";
		case CREATE:
			return "����";
		default:
			return "δ֪";
		}
	}

	public void setStatus(int status) {
		this.status = status;
	}

	// ���ȼ�priority��int
	protected int priority;

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	// �û�user��String
	protected String user;

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	// �����ڴ�ռ�
	protected int needMemory;

	public int getNeedMemory() {
		return this.needMemory;
	}

	public void setNeedMemory(int needMemory) {
		this.needMemory = needMemory;
	}

	// ����ʹ�õ��ڴ�ռ�
	protected int usingMemory;

	public int getUsingMemory() {
		return this.usingMemory;
	}

	public void setUsingMemory(int usingMemory) {
		this.usingMemory = usingMemory;
	}

	// �쳣ԭ��
	protected String errorReason;

	public String getErrorReason() {
		return this.errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	// ����ʹ�õ��豸
	protected String usingDevice;

	public String getUsingDevice() {
		return this.usingDevice;
	}

	public void setUsingDevice(String usingDevice) {
		this.usingDevice = usingDevice;
	}

	//��������ʹ�õ��豸����
	protected String hopeDeviceType;
	public String getHopeDeviceType() {
		return this.hopeDeviceType;
	}
	public void setHopeDeviceType(String hopeDeviceType) {
		this.hopeDeviceType=hopeDeviceType;
	}
	
	// ������Ҫʹ�õ��豸����
	protected String needDeviceType;

	public String getNeedDeviceType() {
		return this.needDeviceType;
	}

	public void setNeedDeviceType(String needDeviceType) {
		switch(needDeviceType) {
		case "��ӡ��":
			this.needDeviceType="printer";
			break;
		case "��¼��":
			this.needDeviceType="burner";
			break;
		case "none":
			this.needDeviceType="none";
			break;
		}
	}

	// ��Ҫʱ��needTime��int
	protected int needTime;

	public int getNeedTime() {
		return this.needTime;
	}

	public void setNeedTime(int needTime) {
		this.needTime = needTime;
	}

	// ʣ��ʱ��restTime��int
	protected int restTime;

	public int getRestTime() {
		return this.restTime;
	}

	public void setRestTime(int restTime) {
		this.restTime = restTime;
	}

	// ������ʱ��runTime��int
	protected int runTime;

	public int getRunTime() {
		return this.runTime;
	}

	public void setRunTime(int runTime) {
		this.runTime = runTime;
	}

	// ʣ����Ҫʱ��restNeedTime��int
	protected int restNeedTime;

	public int getRestNeedTime() {
		return this.needTime - this.runTime;
	}

	// ���б�ʶ��
	boolean isEmpty;

	// ����ʱ��arriveTime��int
	protected int arriveTime;

	public int getArriveTime() {
		return this.arriveTime;
	}

	public void setArriveTime(int arriveTime) {
		this.arriveTime = arriveTime;
	}

	// ��ʼʱ��startTime��int
	protected int startTime;

	public int getStartTime() {
		return this.startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	// ����ʱ��endTime��int
	protected int endTime;

	public int getEndTime() {
		return this.endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	// ����ʱ״̬
	protected int suspendStatus;

	public int getSuspendStatus() {
		return this.suspendStatus;
	}

	public void setSuspendStatus(int suspendStatus) {
		this.suspendStatus = suspendStatus;
	}

	protected String message;
	public String getMessage() {
		return this.message;
	}
	public void setMessage(String message) {
		this.message=message;
	}
	
	public boolean ifEnd;
	
	public void switchToReady() {
		this.status = READY;
		String time = sdf.format(new Date());
		textSystemLogs.append(time + "Process " + this.getName() + " is ready.\n");
	}

	public void switchToRunning() {
		this.status = RUNNING;
		String time = sdf.format(new Date());
		textSystemLogs.append(time + "Process " + this.getName() + " start running.\n");
		this.setErrorReason("");
	}

	public void switchToBlocked() {
		this.status = BLOCKED;
		String time = sdf.format(new Date());
		textSystemLogs.append(time + "Process " + this.getName() + " is blocked.");
	}

	public void switchToSuspend() {
		this.setSuspendStatus(this.status);
		this.status = SUSPEND;
		String time = sdf.format(new Date());
		textSystemLogs.append(time + "Process " + this.getName() + " is suspended.\n");
	}

	public void switchToActivated() {
		this.status = this.getSuspendStatus();
		String time = sdf.format(new Date());
		textSystemLogs.append(time + "Process " + this.getName() + " is activated.\n");
	}

	public void kill() {
		this.isEmpty = true;
		if(this.getUsingDevice()!=null) {
			for(int i=0;i<DEVICE_MAX_CAPACITY;i++) {
				if(dev[i].getDeviceName().equals(this.getUsingDevice())) {
					dev[i].setFree();
				}
			}
		}
		String time = sdf.format(new Date());
		textSystemLogs.append(time + "Process " + this.getName() + " is killed.\n");
	}
}
