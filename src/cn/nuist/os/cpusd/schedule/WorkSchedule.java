package cn.nuist.os.cpusd.schedule;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.processdc.control.CreateProcess;

import java.util.LinkedList;

import com.ibm.icu.text.SimpleDateFormat;

import java.util.Date;
import java.util.Iterator;
import java.math.MathContext;
import java.math.BigDecimal;

public class WorkSchedule extends ControlMainWindow {
	static SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
	enum workState {
		// �ȴ������������
		Wait, Ready, Finished
	}

	static class Work {
		// ����ʱ��
		int arriveTime;
		// ����ʱ��
		int serveTime;
		// ��ʼʱ��
		int startTime;
		// ���ʱ��
		int finishTime;
		// ��תʱ��
		int turnaroundTime;
		// ��Ȩ��תʱ��
		BigDecimal weightedTurnaroundTime;
		// ����״̬
		workState state = workState.Wait;
	}

	// ��ҵ����
	private static LinkedList<Work> workList = new LinkedList<Work>();
	// ������ҵ����
	private static LinkedList<Work> readyWorkList = new LinkedList<Work>();
	// ʱ����
	private static int times;
	// ����תʱ��
	private static BigDecimal sumTurnaroundTime = new BigDecimal(0);
	// �ܴ�Ȩ��תʱ��
	private static BigDecimal sumWeightedTurnaroundTime = new BigDecimal(0);

	private static void createWork(int arriveTime, int serveTime) {
		Work work = new Work();
		work.arriveTime = arriveTime;
		work.serveTime = serveTime;
		workList.addLast(work);
	}

	private static void runWork(Work workList) {
		// ��ʼʱ��
		workList.startTime = times;
		// ���ʱ��
		workList.finishTime = times + workList.serveTime;
		// ��תʱ��
		workList.turnaroundTime = workList.finishTime - workList.arriveTime;
		// ��Ȩ��תʱ��
		workList.weightedTurnaroundTime = new BigDecimal(workList.turnaroundTime)
				.divide(new BigDecimal(workList.serveTime), MathContext.DECIMAL32);
		// ����״̬
		workList.state = workState.Finished;
		times = times + workList.serveTime;
		load();
		output(workList);
	}

	private static void load() {
		// ���ص�ǰtimes֮ǰ������readyJobs��
		Iterator<Work> iterator = workList.iterator();
		while (iterator.hasNext()) {
			Work work = iterator.next();
			if (work.arriveTime <= times && work.state == workState.Wait) {
				readyWorkList.addLast(work);
				work.state = workState.Ready;
				iterator.remove();
			}
		}
	}

	private static void output(Work workList) {
		// ���������ҵ
		String time = sdf.format(new Date());
		textSystemLogs.append(time+"ʱ������" + times + " ����ʱ�䣺" + workList.arriveTime + " ����ʱ�䣺" + workList.serveTime
				+ " ��ʼʱ�䣺" + workList.startTime + " ���ʱ�䣺" + workList.finishTime + " ��תʱ�䣺" + workList.turnaroundTime
				+ " ��Ȩ��תʱ�䣺" + workList.weightedTurnaroundTime + "\n");
		sumTurnaroundTime = sumTurnaroundTime.add(BigDecimal.valueOf(workList.turnaroundTime));
		sumWeightedTurnaroundTime = sumWeightedTurnaroundTime.add(workList.weightedTurnaroundTime);
	}

	private static void output() {
		// ���������ҵ
		textSystemLogs.append("              ����תʱ�䣺" + sumTurnaroundTime + " �ܴ�Ȩ��תʱ�䣺" + sumWeightedTurnaroundTime + "\n");
	}

	public static void FCFS() {
		// �����ȷ���
		// ��ʼ����������
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (!CreateProcess.checkEmpty(pcb[i])) {
				createWork(pcb[i].getArriveTime(), pcb[i].getNeedTime());
			}
		}
		load();
		// ���times=0ʱû�����񱻼��أ���ôtimes++��ֱ�������񱻼���λ��
		while (readyWorkList.isEmpty()) {
			times++;
			load();
		}
		// ��ʼִ������
		while (!readyWorkList.isEmpty()) {
			Work work = readyWorkList.removeFirst();
			runWork(work);
		}
		output();
	}

	public static void SJF() {
		// ����ҵ����
		// ��ʼ����������
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (!CreateProcess.checkEmpty(pcb[i])) {
				createWork(pcb[i].getArriveTime(), pcb[i].getNeedTime());
			}
		}
		load();
		// ���times=0ʱû�����񱻼��أ���ôtimes++��ֱ�������񱻼���λ��
		while (readyWorkList.isEmpty()) {
			times++;
			load();
		}
		// ��ʼִ������
		while (!readyWorkList.isEmpty()) {
			Work workmin = readyWorkList.removeFirst();
			// �ҵ�����ʱ����̵�����
			for (Work work1 : readyWorkList) {
				if (work1.serveTime < workmin.serveTime) {
					workmin = work1;
				}
			}
			runWork(workmin);
		}
		output();
	}

	public static void HRRN() {
		// ����Ӧ������
		// ��ʼ����������
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (!CreateProcess.checkEmpty(pcb[i])) {
				createWork(pcb[i].getArriveTime(), pcb[i].getNeedTime());
			}
		}
		load();
		// ���times=0ʱû�����񱻼��أ���ôtimes++��ֱ�������񱻼���λ��
		while (readyWorkList.isEmpty()) {
			times++;
			load();
		}
		// ��ʼִ������
		while (!readyWorkList.isEmpty()) {
			Work highPrioritywork = null;
			BigDecimal highPriority = new BigDecimal(0);
			for (Work work : readyWorkList) {
				BigDecimal priority = BigDecimal.valueOf(times - work.arriveTime)
						.divide(BigDecimal.valueOf(work.serveTime), MathContext.DECIMAL32).add(BigDecimal.ONE);
				if (priority.compareTo(highPriority) > 0) {
					highPriority = priority;
					highPrioritywork = work;
				}
			}
			readyWorkList.remove(highPrioritywork);
			runWork(highPrioritywork);
		}
		output();
	}
}
