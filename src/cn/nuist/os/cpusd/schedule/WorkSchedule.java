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
		// 等待，就绪，完成
		Wait, Ready, Finished
	}

	static class Work {
		// 到达时间
		int arriveTime;
		// 服务时间
		int serveTime;
		// 开始时间
		int startTime;
		// 完成时间
		int finishTime;
		// 周转时间
		int turnaroundTime;
		// 带权周转时间
		BigDecimal weightedTurnaroundTime;
		// 工作状态
		workState state = workState.Wait;
	}

	// 作业队列
	private static LinkedList<Work> workList = new LinkedList<Work>();
	// 就绪作业队列
	private static LinkedList<Work> readyWorkList = new LinkedList<Work>();
	// 时间量
	private static int times;
	// 总周转时间
	private static BigDecimal sumTurnaroundTime = new BigDecimal(0);
	// 总带权周转时间
	private static BigDecimal sumWeightedTurnaroundTime = new BigDecimal(0);

	private static void createWork(int arriveTime, int serveTime) {
		Work work = new Work();
		work.arriveTime = arriveTime;
		work.serveTime = serveTime;
		workList.addLast(work);
	}

	private static void runWork(Work workList) {
		// 开始时间
		workList.startTime = times;
		// 完成时间
		workList.finishTime = times + workList.serveTime;
		// 周转时间
		workList.turnaroundTime = workList.finishTime - workList.arriveTime;
		// 带权周转时间
		workList.weightedTurnaroundTime = new BigDecimal(workList.turnaroundTime)
				.divide(new BigDecimal(workList.serveTime), MathContext.DECIMAL32);
		// 工作状态
		workList.state = workState.Finished;
		times = times + workList.serveTime;
		load();
		output(workList);
	}

	private static void load() {
		// 加载当前times之前的任务到readyJobs中
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
		// 输出单个作业
		String time = sdf.format(new Date());
		textSystemLogs.append(time+"时间量：" + times + " 到达时间：" + workList.arriveTime + " 服务时间：" + workList.serveTime
				+ " 开始时间：" + workList.startTime + " 完成时间：" + workList.finishTime + " 周转时间：" + workList.turnaroundTime
				+ " 带权周转时间：" + workList.weightedTurnaroundTime + "\n");
		sumTurnaroundTime = sumTurnaroundTime.add(BigDecimal.valueOf(workList.turnaroundTime));
		sumWeightedTurnaroundTime = sumWeightedTurnaroundTime.add(workList.weightedTurnaroundTime);
	}

	private static void output() {
		// 输出所有作业
		textSystemLogs.append("              总周转时间：" + sumTurnaroundTime + " 总带权周转时间：" + sumWeightedTurnaroundTime + "\n");
	}

	public static void FCFS() {
		// 先来先服务
		// 初始化就绪队列
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (!CreateProcess.checkEmpty(pcb[i])) {
				createWork(pcb[i].getArriveTime(), pcb[i].getNeedTime());
			}
		}
		load();
		// 如果times=0时没有任务被加载，那么times++，直到有任务被加载位置
		while (readyWorkList.isEmpty()) {
			times++;
			load();
		}
		// 开始执行任务
		while (!readyWorkList.isEmpty()) {
			Work work = readyWorkList.removeFirst();
			runWork(work);
		}
		output();
	}

	public static void SJF() {
		// 短作业优先
		// 初始化就绪队列
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (!CreateProcess.checkEmpty(pcb[i])) {
				createWork(pcb[i].getArriveTime(), pcb[i].getNeedTime());
			}
		}
		load();
		// 如果times=0时没有任务被加载，那么times++，直到有任务被加载位置
		while (readyWorkList.isEmpty()) {
			times++;
			load();
		}
		// 开始执行任务
		while (!readyWorkList.isEmpty()) {
			Work workmin = readyWorkList.removeFirst();
			// 找到服务时间最短的任务
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
		// 高响应比优先
		// 初始化就绪队列
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (!CreateProcess.checkEmpty(pcb[i])) {
				createWork(pcb[i].getArriveTime(), pcb[i].getNeedTime());
			}
		}
		load();
		// 如果times=0时没有任务被加载，那么times++，直到有任务被加载位置
		while (readyWorkList.isEmpty()) {
			times++;
			load();
		}
		// 开始执行任务
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
