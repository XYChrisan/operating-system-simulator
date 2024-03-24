package cn.nuist.os.cpusd.schedule;

import java.util.LinkedList;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import cn.nuist.os.processdc.control.ControlMainWindow;

public class ProcessSchedule extends ControlMainWindow {
	 // ���̵���
	public static LinkedList init_PCB(int num) {
		// ��ʼ������
		LinkedList<PCB> PCBList = new LinkedList<PCB>();
		for (int i = 0; i < num; i++) {
			int pid = pcb[i].getPID();
			int priority = pcb[i].getPriority();
			int arriveTime = pcb[i].getArriveTime();
			int serveTime = pcb[i].getNeedTime();
			PCB pcb = new PCB(pid, priority, arriveTime, serveTime);
			PCBList.add(pcb);
		}
		for (int i = 0; i < PCBList.size() - 1; i++) {
			for (int j = i + 1; j < PCBList.size(); j++) {
				if (PCBList.get(i).arriveTime > PCBList.get(j).arriveTime) {
					PCB temp = PCBList.get(i);
					PCBList.set(i, PCBList.get(j));
					PCBList.set(j, temp);
				}
			}
		}
		return PCBList;
	}

	public static LinkedList sort_list(LinkedList<PCB> PCBList) {
		// ��������
		for (int i = 0; i < PCBList.size() - 1; i++) {
			for (int j = i + 1; j < PCBList.size(); j++) {
				if (PCBList.get(i).pid > PCBList.get(j).pid) {
					PCB temp = PCBList.get(i);
					PCBList.set(i, PCBList.get(j));
					PCBList.set(j, temp);
				}
			}
		}
		for (PCB i : PCBList) {
			if (i.startTime == 999) {
				i.initial_PCB();
			} else {
				i.finish_PCB();
			}
		}
		return PCBList;
	}

	public static LinkedList staticPriorityAlgorithm(LinkedList<PCB> PCBList) {
		// ��̬���ȼ������㷨
		int time = 0;
		int AverageTurnaroundTime = 0;
		int AverageWeightedTurnaroundTime = 0;
		LinkedList<PCB> PCBList2 = new LinkedList<PCB>();
		int minTime = 999;
		int runPCB = 1;

		while (PCBList.size() != 0) {
			labelSystemTimeNum.setText(String.valueOf(time));
			textSystemLogs.append("Timer:" + time + "\n");
			if (runPCB == 0) {
				int readyNum = 0;
				for (int j = 0; j < PCBList.size(); j++) {
					if (PCBList.get(j).arriveTime <= time) {
						readyNum++;
						if (PCBList.get(j).serveTime < minTime) {
							PCB tempPCB = PCBList.get(j);
							PCBList.remove(j);
							PCBList.addFirst(tempPCB);
						}
					}
					if (readyNum > 1) {
						for (int k = 0; k < readyNum - 1; k++) {
							if (PCBList.get(k).priority > PCBList.get(k + 1).priority) {
								PCB tempPCB = PCBList.get(k);
								PCBList.set(k, PCBList.get(k + 1));
								PCBList.set(k + 1, tempPCB);
							}
						}
					}
				}
			}
			if (PCBList.get(0).arriveTime <= time) {
				runPCB = 1;
				if (PCBList.get(0).roundTime == 0) {
					textSystemLogs.append("����" + PCBList.get(0).pid + "��ʼ����\n");
					PCBList.get(0).startTime = time;
					PCBList.get(0).roundTime = 1;
					pcb[PCBList.get(0).pid].switchToRunning();
				}
				PCBList.get(0).cpuTime++;
				PCBList.get(0).dynamicRunning_PCB();
				if (PCBList.get(0).cpuTime == PCBList.get(0).serveTime) {
					textSystemLogs.append("����" + PCBList.get(0).pid + "���н���\n");
					PCBList.get(0).finishTime = time + 1;
					pcb[PCBList.get(0).pid].switchToBlocked();
					textSystemLogs.append("\n");
					PCBList.get(0).turnaroundTime = PCBList.get(0).finishTime - PCBList.get(0).arriveTime;
					PCBList.get(0).weightedTurnaroundTime = (float) PCBList.get(0).turnaroundTime
							/ PCBList.get(0).serveTime;
					PCBList2.add(PCBList.get(0));
					PCBList.remove(0);
					runPCB = 0;
				}
			}
			time++;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < PCBList2.size(); i++) {
			AverageTurnaroundTime += PCBList2.get(i).turnaroundTime;
			AverageWeightedTurnaroundTime += PCBList2.get(i).weightedTurnaroundTime;
		}
		try {
			AverageTurnaroundTime = AverageTurnaroundTime / PCBList2.size();
		} catch (ArithmeticException e) {
			textSystemLogs.append("ERROR\n");
		}
		try {
			AverageWeightedTurnaroundTime = AverageWeightedTurnaroundTime / PCBList2.size();
		} catch (ArithmeticException e) {
			textSystemLogs.append("ERROR\n");
		}
		textSystemLogs.append("ƽ����תʱ�䣺" + AverageTurnaroundTime + "\n");
		textSystemLogs.append("ƽ����Ȩ��תʱ�䣺" + AverageWeightedTurnaroundTime + "\n");
		textSystemLogs.append("��ϸ���������\n");
		return PCBList2;
	}

	public static LinkedList dynamicPriorityAlgorithm(LinkedList<PCB> PCBList) {
		// ��̬���ȼ������㷨
		int time = 0;
		int AverageTurnaroundTime = 0;
		int AverageWeightedTurnaroundTime = 0;
		LinkedList<PCB> PCBList2 = new LinkedList<PCB>();
		int minTime = 999;
		int runPCB = 1;

		while (PCBList.size() != 0) {
			labelSystemTimeNum.setText(String.valueOf(time));
			textSystemLogs.append("Timer:" + time + "\n");
			if (runPCB == 0) {
				int readyNum = 0;
				for (int j = 0; j < PCBList.size(); j++) {
					if (PCBList.get(j).arriveTime <= time) {
						readyNum++;
						if (PCBList.get(j).serveTime < minTime) {
							PCB tempPCB = PCBList.get(j);
							PCBList.remove(j);
							PCBList.addFirst(tempPCB);
						}
					}
					if (readyNum > 1) {
						for (int k = 0; k < readyNum - 1; k++) {
							if (PCBList.get(k).dynamicPriority > PCBList.get(k + 1).dynamicPriority) {
								PCB tempPCB = PCBList.get(k);
								PCBList.set(k, PCBList.get(k + 1));
								PCBList.set(k + 1, tempPCB);
							}
						}
					}
				}
			}
			if (PCBList.get(0).arriveTime <= time) {
				runPCB = 1;
				if (PCBList.get(0).roundTime == 0) {
					textSystemLogs.append("����" + PCBList.get(0).pid + "��ʼ����\n");
					PCBList.get(0).startTime = time;
					PCBList.get(0).roundTime = 1;
				}
				PCBList.get(0).cpuTime++;
				PCBList.get(0).dynamicRunning_PCB();
				if (PCBList.get(0).cpuTime == PCBList.get(0).serveTime) {
					textSystemLogs.append("����" + PCBList.get(0).pid + "���н���\n");
					PCBList.get(0).finishTime = time + 1;
					PCBList.get(0).turnaroundTime = PCBList.get(0).finishTime - PCBList.get(0).arriveTime;
					PCBList.get(0).weightedTurnaroundTime = (float) PCBList.get(0).turnaroundTime
							/ PCBList.get(0).serveTime;
					PCBList2.add(PCBList.get(0));
					PCBList.remove(0);
					runPCB = 0;
				} else {
					PCBList.get(0).dynamicPriority--;
				}
			}
			time++;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < PCBList2.size(); i++) {
			AverageTurnaroundTime += PCBList2.get(i).turnaroundTime;
			AverageWeightedTurnaroundTime += PCBList2.get(i).weightedTurnaroundTime;
		}
		AverageTurnaroundTime = AverageTurnaroundTime / PCBList2.size();
		AverageWeightedTurnaroundTime = AverageWeightedTurnaroundTime / PCBList2.size();
		textSystemLogs.append("ƽ����תʱ�䣺" + AverageTurnaroundTime + "\n");
		textSystemLogs.append("ƽ����Ȩ��תʱ�䣺" + AverageWeightedTurnaroundTime + "\n");
		textSystemLogs.append("��ϸ���������\n");
		return PCBList2;
	}

	public static LinkedList roundRobinAlgorithm(LinkedList<PCB> PCBList) {
		// ʱ��Ƭ��ת�����㷨
		int time = 0;
		int AverageTurnaroundTime = 0;
		int AverageWeightedTurnaroundTime = 0;
		LinkedList<PCB> PCBList2 = new LinkedList<PCB>();
		int removeTime = PCBList.get(0).arriveTime - 1;

		while (PCBList.size() != 0) {
			labelSystemTimeNum.setText(String.valueOf(time));
			textSystemLogs.append("Timer:" + time + "\n");
			int readyNum = 0;
			LinkedList<PCB> PCBList3 = new LinkedList<PCB>();
			for (int j = 0; j < PCBList.size(); j++) {
				if (PCBList.get(j).arriveTime <= time) {
					readyNum++;
					if (PCBList.get(j).roundTime == 0) {
						PCBList3.add(PCBList.get(j));
					}
				}
			}
			if (readyNum > 1) {
				if (time - 1 != removeTime) {
					PCB tempPCB = PCBList.remove(0);
					PCBList.add(readyNum - 1, tempPCB);
				}
				if (PCBList3 != null) {
					PCBList3.clear();
				}
			}
			if (PCBList.get(0).arriveTime <= time) {
				if (PCBList.get(0).roundTime == 0) {
					textSystemLogs.append("����" + PCBList.get(0).pid + "��ʼ����\n");
					PCBList.get(0).startTime = time;
					PCBList.get(0).roundTime = 1;
				}
				PCBList.get(0).cpuTime++;
				PCBList.get(0).remainTime--;
				PCBList.get(0).dynamicRunning_PCB();
				if (PCBList.get(0).dynamicPriority != 0) {
					PCBList.get(0).dynamicPriority--;
				}
				PCBList.get(0).alterTime = time;
				PCBList.get(0).running_PCB();
				if (PCBList.get(0).cpuTime == PCBList.get(0).serveTime) {
					textSystemLogs.append("����" + PCBList.get(0).pid + "���н���\n");
					PCBList.get(0).finishTime = time + 1;
					PCBList.get(0).turnaroundTime = PCBList.get(0).finishTime - PCBList.get(0).arriveTime;
					PCBList.get(0).weightedTurnaroundTime = (float) PCBList.get(0).turnaroundTime
							/ PCBList.get(0).serveTime;
					PCBList2.add(PCBList.get(0));
					PCBList.remove(0);
				}
			}
			time++;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < PCBList2.size(); i++) {
			AverageTurnaroundTime += PCBList2.get(i).turnaroundTime;
			AverageWeightedTurnaroundTime += PCBList2.get(i).weightedTurnaroundTime;
		}
		AverageTurnaroundTime = AverageTurnaroundTime / PCBList2.size();
		AverageWeightedTurnaroundTime = AverageWeightedTurnaroundTime / PCBList2.size();
		textSystemLogs.append("ƽ����תʱ�䣺" + AverageTurnaroundTime + "\n");
		textSystemLogs.append("ƽ����Ȩ��תʱ�䣺" + AverageWeightedTurnaroundTime + "\n");
		textSystemLogs.append("��ϸ���������\n");
		return PCBList2;
	}

	public static void Select(LinkedList<PCB> PCBList, int selection) {
		LinkedList PCBList1 = (LinkedList) PCBList.clone();
		int select = selection;
		switch (select) {
		case 1:
			LinkedList PCBList2 = (LinkedList) PCBList1.clone();
			sort_list(PCBList2);
			PCBList1 = staticPriorityAlgorithm(PCBList1);
			sort_list(PCBList1);
			break;
		case 2:
			LinkedList PCBList3 = (LinkedList) PCBList1.clone();
			sort_list(PCBList3);
			PCBList1 = dynamicPriorityAlgorithm(PCBList1);
			sort_list(PCBList1);
			break;
		case 3:
			LinkedList PCBList4 = (LinkedList) PCBList1.clone();
			sort_list(PCBList4);
			PCBList1 = roundRobinAlgorithm(PCBList1);
			sort_list(PCBList1);
			break;
		case 4:
			break;
		default:
			textSystemLogs.append("�����������������");
		}

	}

	public static void begin(int selection) {
		int PCBNum = Integer.parseInt(labelReadyProcessNum.getText());
		if (PCBNum == 0) {
			textSystemLogs.append("No Process\n");
		} else {
			LinkedList<PCB> PCBList = init_PCB(PCBNum);
			Select(PCBList, selection);
		}
	}

}

class PCB extends ControlMainWindow {
	int pid; // ���̱�ʶ��
	int priority; // �������ȼ�
	int arriveTime; // ���̵���ʱ��
	int serveTime; // ���̷���ʱ��
	int dynamicPriority = priority; // ��̬���ȼ�
	int startTime = 999; // ���̿�ʼʱ��
	int alterTime = 999; // �����޸�ʱ��
	int cpuTime = 0; // ��������ʱ��
	int remainTime = serveTime; // ����ʣ��ʱ��
	int finishTime = 999; // �������ʱ��
	int turnaroundTime = 999; // ��תʱ��
	double weightedTurnaroundTime = 999; // ��Ȩ��תʱ��
	int roundTime = 0; // �����Ƿ��һ�ν����ڴ�

	public PCB(int pid, int priority, int arriveTime, int serveTime) {
		this.pid = pid;
		this.priority = priority;
		this.arriveTime = arriveTime;
		this.serveTime = serveTime;
	}

	// ����ǰ��PCB
	public void initial_PCB() {
		pcb[pid].setNeedTime(serveTime);
		pcb[pid].setArriveTime(arriveTime);
		pcb[pid].setPriority(priority);
	}

	// �����е�PCB
	public void running_PCB() {
		pcb[pid].setNeedTime(serveTime);
		pcb[pid].setRunTime(cpuTime);
	}

	public void dynamicRunning_PCB() {
		pcb[pid].setPriority(dynamicPriority);
		pcb[pid].setNeedTime(serveTime);
		pcb[pid].setRunTime(cpuTime);
	}

	// ���к��PCB
	public void finish_PCB() {
		TableItem item = new TableItem(tableResult, SWT.NONE);
		item.setText(new String[] {
				String.valueOf(pid),String.valueOf(priority),String.valueOf(arriveTime),String.valueOf(serveTime),String.valueOf(cpuTime),String.valueOf(finishTime),String.valueOf(turnaroundTime),String.valueOf(weightedTurnaroundTime),
		});
	}
}
