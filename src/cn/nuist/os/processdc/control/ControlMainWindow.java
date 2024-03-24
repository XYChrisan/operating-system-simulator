package cn.nuist.os.processdc.control;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.nuist.os.cpusd.deadlock.DeadLockHandle;
import cn.nuist.os.cpusd.schedule.ProcessSchedule;
import cn.nuist.os.cpusd.schedule.WorkSchedule;
import cn.nuist.os.main.Main;
import cn.nuist.os.processdc.communication.CommunicationWindow;
import cn.nuist.os.processdc.sync.DeviceController;
import cn.nuist.os.storage.allorecy.AllocateRecycle;
import cn.nuist.os.storage.swap.Swap;
import cn.nuist.os.utility.AutoFreshThread;
import cn.nuist.os.utility.CheckTimeThread;
import cn.nuist.os.utility.InitLocation;
import cn.nuist.os.utility.RandomNeedDeviceThread;
import cn.nuist.os.utility.RandomRemoveNeedDevice;
import cn.nuist.os.utility.RandomStatusSwitchThread;
import cn.nuist.os.utility.ResourcesAllocateThread;
import cn.nuist.os.utility.SystemTimerThread;
import cn.nuist.os.utility.TimerThread;

import org.eclipse.swt.widgets.Menu;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ibm.icu.text.SimpleDateFormat;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ControlMainWindow extends Main {

	protected Shell shellControlMainWindow;
	public static Table tablePCB;
	public static Table tableReady;
	public static Table tableRunning;
	public static Table tableBlocked;
	public static Table tableSuspend;
	public static Label labelFreePCBNum;
	public static Label labelReadyProcessNum;
	public static Label labelRunningProcessNum;
	public static Label labelBlockedProcessNum;
	public static Label labelSuspendProcessNum;
	public static Label labelFreeMemoryNum;
	private Text textCommand;
	private Text textSendPID;
	public static Label labelSystemTimeNum;
	public static SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
	private Text textCommandHelp;
	public static Text textSystemLogs;
	public static Composite compositeHub1;
	public static Composite compositeHub2;
	public static Composite compositeHub3;
	protected final static int READY = 1;
	protected final static int RUNNING = 2;
	protected final static int BLOCKED = 3;
	protected final static int SUSPEND = 4;
	protected final static int CREATE = 5;
	protected final static int UNKNOWN = 0;
	public static Label labelStatus1;
	public static Label labelStatus2;
	public static Label labelStatus3;
	public static Label labelLED1;
	public static Label labelLED2;
	public static Label labelLED3;
	public static Table tableResult;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void start() {
		try {
			ControlMainWindow window = new ControlMainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shellControlMainWindow.open();
		shellControlMainWindow.layout();
		while (!shellControlMainWindow.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		DeviceController dev0=new DeviceController();
		dev0.setDeviceID(0);
		dev0.setDeviceInterface("USB_0:0");
		dev0.setDeviceName("HP LaserJet Pro P1106");
		dev0.setDeviceType("printer");
		dev0.setPower(false);
		dev0.setLock(false);
		dev[0]=dev0;
		
		DeviceController dev1=new DeviceController();
		dev1.setDeviceID(1);
		dev1.setDeviceInterface("USB_0:1");
		dev1.setDeviceName("HP LaserJet Pro M1213nf");
		dev1.setDeviceType("printer");
		dev1.setPower(false);
		dev1.setLock(false);
		dev[1]=dev1;
		
		DeviceController dev2=new DeviceController();
		dev2.setDeviceID(2);
		dev2.setDeviceInterface("SATA_0:1");
		dev2.setDeviceName("SONY ad-7260s");
		dev2.setDeviceType("burner");
		dev2.setPower(false);
		dev2.setLock(false);
		dev[2]=dev2;
		
		shellControlMainWindow = new Shell(SWT.MIN);
		shellControlMainWindow.setSize(1821, 794);
		shellControlMainWindow.setText("\u64CD\u4F5C\u7CFB\u7EDF\u6A21\u62DF\u5668");
		InitLocation.init(shellControlMainWindow);

		Label labelFreePCB = new Label(shellControlMainWindow, SWT.NONE);
		labelFreePCBNum = new Label(shellControlMainWindow, SWT.NONE);

		Menu menuFunction = new Menu(shellControlMainWindow, SWT.BAR);
		shellControlMainWindow.setMenuBar(menuFunction);

		MenuItem menuItemView = new MenuItem(menuFunction, SWT.CASCADE);
		menuItemView.setText("\u67E5\u770B(V)");

		Menu menuView = new Menu(menuItemView);
		menuItemView.setMenu(menuView);

		MenuItem menuItemFreshNow = new MenuItem(menuView, SWT.NONE);
		menuItemFreshNow.setText("\u7ACB\u5373\u5237\u65B0(R)");
		menuItemFreshNow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AutoFreshThread.refresh();
			}
		});

		MenuItem menuItemFreshSpeed = new MenuItem(menuView, SWT.CASCADE);
		menuItemFreshSpeed.setText("\u5237\u65B0\u901F\u5EA6(U)");

		Menu menuFreshSpeed = new Menu(menuItemFreshSpeed);
		menuItemFreshSpeed.setMenu(menuFreshSpeed);

		MenuItem menuItemHighSpeed = new MenuItem(menuFreshSpeed, SWT.NONE);
		menuItemHighSpeed.setText("\u9AD8(H)");
		menuItemHighSpeed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AutoFreshThread.setRefreshTime(1000);
			}
		});

		MenuItem menuItemNormalSpeed = new MenuItem(menuFreshSpeed, SWT.NONE);
		menuItemNormalSpeed.setText("\u6B63\u5E38(N)");
		menuItemNormalSpeed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AutoFreshThread.setRefreshTime(3000);
			}
		});

		MenuItem menuItemLowSpeed = new MenuItem(menuFreshSpeed, SWT.NONE);
		menuItemLowSpeed.setText("\u4F4E(L)");
		menuItemLowSpeed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AutoFreshThread.setRefreshTime(10000);
			}
		});

		MenuItem menuItemWorkSchedule = new MenuItem(menuFunction, SWT.CASCADE);
		menuItemWorkSchedule.setText("\u4F5C\u4E1A\u8C03\u5EA6(W)");

		Menu menuWorkSchedule = new Menu(menuItemWorkSchedule);
		menuItemWorkSchedule.setMenu(menuWorkSchedule);

		MenuItem menuItemFCFS = new MenuItem(menuWorkSchedule, SWT.NONE);
		menuItemFCFS.setText("\u5148\u6765\u5148\u670D\u52A1(FCFS)");
		menuItemFCFS.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (labelReadyProcessNum.getText() == "0") {
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No Process. The function is unavailable.\n");
				} else {
					WorkSchedule.FCFS();
				}
			}
		});

		MenuItem menuItemSJF = new MenuItem(menuWorkSchedule, SWT.NONE);
		menuItemSJF.setText("\u77ED\u4F5C\u4E1A\u4F18\u5148(SJF)");
		menuItemSJF.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (labelReadyProcessNum.getText() == "0") {
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No Process. The function is unavailable.\n");
				} else {
					WorkSchedule.SJF();
				}
			}
		});

		MenuItem menuItemHRRN = new MenuItem(menuWorkSchedule, SWT.NONE);
		menuItemHRRN.setText("\u9AD8\u54CD\u5E94\u6BD4\u4F18\u5148(HRRN)");
		menuItemHRRN.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (labelReadyProcessNum.getText() == "0") {
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No Process. The function is unavailable.\n");
				} else {
					WorkSchedule.HRRN();
				}
			}
		});

		MenuItem menuItemProcessSchedule = new MenuItem(menuFunction, SWT.CASCADE);
		menuItemProcessSchedule.setText("\u8FDB\u7A0B\u8C03\u5EA6(P)");

		Menu menu = new Menu(menuItemProcessSchedule);
		menuItemProcessSchedule.setMenu(menu);

		MenuItem menuItemStaticPriority = new MenuItem(menu, SWT.NONE);
		menuItemStaticPriority.setText("\u9759\u6001\u4F18\u5148\u7EA7\u8C03\u5EA6\u7B97\u6CD5");
		menuItemStaticPriority.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableResult.removeAll();
				ProcessSchedule.begin(1);
			}
		});

		MenuItem menuItemDynamicPriority = new MenuItem(menu, SWT.NONE);
		menuItemDynamicPriority.setText("\u52A8\u6001\u4F18\u5148\u7EA7\u8C03\u5EA6\u7B97\u6CD5");
		menuItemDynamicPriority.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableResult.removeAll();
				ProcessSchedule.begin(2);
			}
		});

		MenuItem menuItemRR = new MenuItem(menu, SWT.NONE);
		menuItemRR.setText("\u65F6\u95F4\u7247\u8F6E\u8F6C\u8C03\u5EA6\u7B97\u6CD5");
		menuItemRR.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableResult.removeAll();
				ProcessSchedule.begin(3);
			}
		});

		MenuItem menuItemDevices = new MenuItem(menuFunction, SWT.CASCADE);
		menuItemDevices.setText("\u8BBE\u5907(D)");

		Menu menuDevices = new Menu(menuItemDevices);
		menuItemDevices.setMenu(menuDevices);

		MenuItem menuItemPrinter = new MenuItem(menuDevices, SWT.CASCADE);
		menuItemPrinter.setText("\u6253\u5370\u673A(P)");

		Menu menuPrinter = new Menu(menuItemPrinter);
		menuItemPrinter.setMenu(menuPrinter);

		MenuItem menuItemUSB1 = new MenuItem(menuPrinter, SWT.CHECK);
		menuItemUSB1.setText("USB_0:0 (HP LaserJet Pro P1106)");
		menuItemUSB1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (menuItemUSB1.getSelection()) {
					dev[0].connect();
					compositeHub1.setVisible(true);
				} else {
					dev[0].disconnect();
					compositeHub1.setVisible(false);
				}
			}
		});

		MenuItem menuItemUSB2 = new MenuItem(menuPrinter, SWT.CHECK);
		menuItemUSB2.setText("USB_0:1 (HP LaserJet Pro M1213nf)");
		menuItemUSB2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (menuItemUSB2.getSelection()) {
					dev[1].connect();
					compositeHub2.setVisible(true);
				} else {
					dev[1].disconnect();
					compositeHub2.setVisible(false);
				}
			}
		});

		MenuItem menuItemBurner = new MenuItem(menuDevices, SWT.CASCADE);
		menuItemBurner.setText("\u523B\u5F55\u673A(B)");

		Menu menuBurner = new Menu(menuItemBurner);
		menuItemBurner.setMenu(menuBurner);

		MenuItem menuItemSATA1 = new MenuItem(menuBurner, SWT.CHECK);
		menuItemSATA1.setText("SATA_0:1 (SONY ad-7260s)");
		menuItemSATA1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (menuItemSATA1.getSelection()) {
					dev[2].connect();
					compositeHub3.setVisible(true);
				} else {
					dev[2].disconnect();
					compositeHub3.setVisible(false);
				}
			}
		});
		
		MenuItem menuItemMore = new MenuItem(menuFunction, SWT.CASCADE);
		menuItemMore.setText("\u66F4\u591A(M)");
		
		Menu menu_1 = new Menu(menuItemMore);
		menuItemMore.setMenu(menu_1);
		
		MenuItem menuItemDeadLockHandle = new MenuItem(menu_1, SWT.NONE);
		menuItemDeadLockHandle.setText("\u907F\u514D\u6B7B\u9501\uFF08\u94F6\u884C\u5BB6\u7B97\u6CD5\uFF09");
		menuItemDeadLockHandle.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DeadLockHandle.main();
			}
		});
		
		MenuItem menuItemAlloRecy = new MenuItem(menu_1, SWT.NONE);
		menuItemAlloRecy.setText("\u5206\u914D\u56DE\u6536");
		menuItemAlloRecy.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				AllocateRecycle.main();
			}
		});
		
		MenuItem menuItemSwap = new MenuItem(menu_1, SWT.NONE);
		menuItemSwap.setText("\u6362\u5165\u6362\u51FA");
		menuItemSwap.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Swap.main();
			}
		});
		
		MenuItem menuItemUtilities = new MenuItem(menuFunction, SWT.CASCADE);
		menuItemUtilities.setText("\u5B9E\u7528\u5DE5\u5177(U)");
		
		Menu menu_2 = new Menu(menuItemUtilities);
		menuItemUtilities.setMenu(menu_2);
		
		MenuItem menuItemOneKeyRun = new MenuItem(menu_2, SWT.NONE);
		menuItemOneKeyRun.setText("\u4E00\u952E\u8FD0\u884C\u6240\u6709\u5B9E\u4F8B");
		menuItemOneKeyRun.setEnabled(false);
		menuItemOneKeyRun.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				for(int i=0;i<PCB_MAX_CAPACITY;i++) {
					if(!CreateProcess.checkEmpty(pcb[i])&&pcb[i].getStatusNum()==READY) {
						pcb[i].switchToRunning();
					}
				}
			}
		});
		
		MenuItem menuItemOneKeyEnd = new MenuItem(menu_2, SWT.NONE);
		menuItemOneKeyEnd.setText("\u4E00\u952E\u7ED3\u675F\u6240\u6709\u5B9E\u4F8B");
		menuItemOneKeyEnd.setEnabled(false);
		menuItemOneKeyEnd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				for(int i=0;i<PCB_MAX_CAPACITY;i++) {
					if(!CreateProcess.checkEmpty(pcb[i])) {
						pcb[i].kill();
					}
				}
			}
		});
		
		MenuItem menuItemOnekeyCreate = new MenuItem(menu_2, SWT.NONE);
		menuItemOnekeyCreate.setText("\u4E00\u952E\u521B\u5EFA\u5B9E\u4F8B");
		menuItemOnekeyCreate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onekey();
				menuItemOneKeyRun.setEnabled(true);
				menuItemOneKeyEnd.setEnabled(true);
			}
		});
		
		
		
		MenuItem menuItemStartTimer = new MenuItem(menu_2, SWT.NONE);
		menuItemStartTimer.setText("\u542F\u52A8\u8BA1\u65F6\u5668");
		menuItemStartTimer.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SystemTimerThread.begin();
				menuItemStartTimer.setEnabled(false);
				String time = sdf.format(new Date());
				textSystemLogs.append(time + "System timer start!\n");
			}
		});
		
		MenuItem menuItemResetTime = new MenuItem(menu_2, SWT.NONE);
		menuItemResetTime.setText("\u91CD\u7F6E\u65F6\u95F4");
		menuItemResetTime.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				labelSystemTimeNum.setText("0");
				String time = sdf.format(new Date());
				textSystemLogs.append(time + "System timer reset!\n");
			}
		});
		
		MenuItem menuItemRandomIO = new MenuItem(menu_2, SWT.NONE);
		menuItemRandomIO.setText("\u968F\u673A\u7B49\u5F85I/O");
		menuItemRandomIO.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				RandomStatusSwitchThread.begin();
				menuItemRandomIO.setEnabled(false);
				String time = sdf.format(new Date());
				textSystemLogs.append(time + "Start random waiting for IO!\n");
			}
		});
		
		MenuItem menuItemRandomDevice = new MenuItem(menu_2, SWT.NONE);
		menuItemRandomDevice.setText("\u968F\u673A\u8BF7\u6C42\u8BBE\u5907");
		menuItemRandomDevice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				RandomNeedDeviceThread.begin();
				RandomRemoveNeedDevice.begin();
				menuItemRandomDevice.setEnabled(false);
				String time = sdf.format(new Date());
				textSystemLogs.append(time + "Start random request device!\n");
			}
		});

		MenuItem menuItemHelp = new MenuItem(menuFunction, SWT.CASCADE);
		menuItemHelp.setText("\u5E2E\u52A9(H)");

		Menu menuHelp = new Menu(menuItemHelp);
		menuItemHelp.setMenu(menuHelp);

		MenuItem menuItemAbout = new MenuItem(menuHelp, SWT.NONE);
		menuItemAbout.setText("\u5173\u4E8E(A)");
		menuItemAbout.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageBox mb = new MessageBox(shellControlMainWindow);
				mb.setText("关于");
				mb.setMessage("操作系统模拟器\n作者：吴恺诚、崔佳赟、楚义");
				mb.open();
			}
		});

		TabFolder tabFolderProcessView = new TabFolder(shellControlMainWindow, SWT.NONE);
		tabFolderProcessView.setBounds(10, 10, 1190, 426);

		TabItem tabItemPCB = new TabItem(tabFolderProcessView, SWT.NONE);
		tabItemPCB.setText("PCB");

		tablePCB = new Table(tabFolderProcessView, SWT.BORDER | SWT.FULL_SELECTION);
		tabItemPCB.setControl(tablePCB);
		tablePCB.setHeaderVisible(true);

		TableColumn tableColumnPCBPID = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBPID.setWidth(32);
		tableColumnPCBPID.setText("PID");

		TableColumn tableColumnPCBName = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBName.setWidth(71);
		tableColumnPCBName.setText("\u540D\u79F0");

		TableColumn tableColumnPCBStatus = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBStatus.setWidth(57);
		tableColumnPCBStatus.setText("\u72B6\u6001");

		TableColumn tableColumnPCBPriority = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBPriority.setWidth(56);
		tableColumnPCBPriority.setText("\u4F18\u5148\u7EA7");

		TableColumn tableColumnPCBUser = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBUser.setWidth(44);
		tableColumnPCBUser.setText("\u7528\u6237");

		TableColumn tableColumnPCBMemory = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBMemory.setWidth(65);
		tableColumnPCBMemory.setText("\u5185\u5B58");

		TableColumn tableColumnPCBErrorReason = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBErrorReason.setWidth(87);
		tableColumnPCBErrorReason.setText("\u5F02\u5E38\u539F\u56E0");

		TableColumn tableColumnPCBUsingDevice = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBUsingDevice.setWidth(100);
		tableColumnPCBUsingDevice.setText("\u6B63\u5728\u4F7F\u7528\u7684\u8BBE\u5907");

		TableColumn tableColumnPCBNeedTime = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBNeedTime.setWidth(91);
		tableColumnPCBNeedTime.setText("\u6240\u9700\u65F6\u95F4\uFF08\u79D2\uFF09");

		TableColumn tableColumnPCBRunningTime = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBRunningTime.setWidth(95);
		tableColumnPCBRunningTime.setText("\u5DF2\u8FD0\u884C\u65F6\u95F4\uFF08\u79D2\uFF09");

		TableColumn tableColumnPCBRestTime = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBRestTime.setWidth(83);
		tableColumnPCBRestTime.setText("\u5269\u4F59\u65F6\u95F4\uFF08\u79D2\uFF09");

		TableColumn tableColumnPCBArriveTime = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBArriveTime.setWidth(83);
		tableColumnPCBArriveTime.setText("\u5230\u8FBE\u65F6\u95F4\uFF08\u79D2\uFF09");

		TableColumn tableColumnPCBStartTime = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBStartTime.setWidth(77);
		tableColumnPCBStartTime.setText("\u5F00\u59CB\u65F6\u95F4\uFF08\u79D2\uFF09");

		TableColumn tableColumnPCBFinishTime = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBFinishTime.setWidth(91);
		tableColumnPCBFinishTime.setText("\u7ED3\u675F\u65F6\u95F4\uFF08\u79D2\uFF09");
		
		TableColumn tableColumnPCBMessage = new TableColumn(tablePCB, SWT.NONE);
		tableColumnPCBMessage.setWidth(100);
		tableColumnPCBMessage.setText("\u6D88\u606F");

		TabItem tabItemReady = new TabItem(tabFolderProcessView, SWT.NONE);
		tabItemReady.setText("\u5C31\u7EEA\u961F\u5217");

		tableReady = new Table(tabFolderProcessView, SWT.BORDER | SWT.FULL_SELECTION);
		tabItemReady.setControl(tableReady);
		tableReady.setHeaderVisible(true);

		TableColumn tableColumnReadyPID = new TableColumn(tableReady, SWT.NONE);
		tableColumnReadyPID.setWidth(100);
		tableColumnReadyPID.setText("PID");

		TableColumn tableColumnReadyName = new TableColumn(tableReady, SWT.NONE);
		tableColumnReadyName.setWidth(100);
		tableColumnReadyName.setText("\u540D\u79F0");

		TableColumn tableColumnReadyPriority = new TableColumn(tableReady, SWT.NONE);
		tableColumnReadyPriority.setWidth(100);
		tableColumnReadyPriority.setText("\u4F18\u5148\u7EA7");

		TableColumn tableColumnReadyRestNeedTime = new TableColumn(tableReady, SWT.NONE);
		tableColumnReadyRestNeedTime.setWidth(140);
		tableColumnReadyRestNeedTime.setText("\u5269\u4F59\u9700\u8981\u65F6\u95F4\uFF08\u79D2\uFF09");

		TabItem tabItemRunning = new TabItem(tabFolderProcessView, SWT.NONE);
		tabItemRunning.setText("\u6267\u884C\u961F\u5217");

		tableRunning = new Table(tabFolderProcessView, SWT.BORDER | SWT.FULL_SELECTION);
		tabItemRunning.setControl(tableRunning);
		tableRunning.setHeaderVisible(true);

		TableColumn tableColumnRunningPID = new TableColumn(tableRunning, SWT.NONE);
		tableColumnRunningPID.setWidth(100);
		tableColumnRunningPID.setText("PID");

		TableColumn tableColumnRunningName = new TableColumn(tableRunning, SWT.NONE);
		tableColumnRunningName.setWidth(100);
		tableColumnRunningName.setText("\u540D\u79F0");

		TableColumn tableColumnRunningPriority = new TableColumn(tableRunning, SWT.NONE);
		tableColumnRunningPriority.setWidth(100);
		tableColumnRunningPriority.setText("\u4F18\u5148\u7EA7");

		TableColumn tableColumnRunningRestTime = new TableColumn(tableRunning, SWT.NONE);
		tableColumnRunningRestTime.setWidth(100);
		tableColumnRunningRestTime.setText("\u5269\u4F59\u65F6\u95F4\uFF08\u79D2\uFF09");

		TabItem tabItemBlocked = new TabItem(tabFolderProcessView, SWT.NONE);
		tabItemBlocked.setText("\u963B\u585E\u961F\u5217");

		tableBlocked = new Table(tabFolderProcessView, SWT.BORDER | SWT.FULL_SELECTION);
		tabItemBlocked.setControl(tableBlocked);
		tableBlocked.setHeaderVisible(true);

		TableColumn tableColumnBlockedPID = new TableColumn(tableBlocked, SWT.NONE);
		tableColumnBlockedPID.setWidth(100);
		tableColumnBlockedPID.setText("PID");

		TableColumn tableColumnBlockedName = new TableColumn(tableBlocked, SWT.NONE);
		tableColumnBlockedName.setWidth(100);
		tableColumnBlockedName.setText("\u540D\u79F0");

		TableColumn tableColumnBlockedPriority = new TableColumn(tableBlocked, SWT.NONE);
		tableColumnBlockedPriority.setWidth(100);
		tableColumnBlockedPriority.setText("\u4F18\u5148\u7EA7");

		TabItem tabItemSuspend = new TabItem(tabFolderProcessView, SWT.NONE);
		tabItemSuspend.setText("\u6302\u8D77\u961F\u5217");

		tableSuspend = new Table(tabFolderProcessView, SWT.BORDER | SWT.FULL_SELECTION);
		tabItemSuspend.setControl(tableSuspend);
		tableSuspend.setHeaderVisible(true);

		TableColumn tableColumnSuspendPID = new TableColumn(tableSuspend, SWT.NONE);
		tableColumnSuspendPID.setWidth(100);
		tableColumnSuspendPID.setText("PID");

		TableColumn tableColumnSuspendName = new TableColumn(tableSuspend, SWT.NONE);
		tableColumnSuspendName.setWidth(100);
		tableColumnSuspendName.setText("\u540D\u79F0");

		TableColumn tableColumnSuspendPriority = new TableColumn(tableSuspend, SWT.NONE);
		tableColumnSuspendPriority.setWidth(100);
		tableColumnSuspendPriority.setText("\u4F18\u5148\u7EA7");

		TableColumn tableColumnSuspendRestTime = new TableColumn(tableSuspend, SWT.NONE);
		tableColumnSuspendRestTime.setWidth(100);
		tableColumnSuspendRestTime.setText("\u5269\u4F59\u65F6\u95F4\uFF08\u79D2\uFF09");
		
		TabItem tabItem = new TabItem(tabFolderProcessView, SWT.NONE);
		tabItem.setText("\u8FDB\u7A0B\u8C03\u5EA6\u8FD0\u884C\u7ED3\u679C");
		
		tableResult = new Table(tabFolderProcessView, SWT.BORDER | SWT.FULL_SELECTION);
		tabItem.setControl(tableResult);
		tableResult.setHeaderVisible(true);
		tableResult.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(tableResult, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("\u8FDB\u7A0B\u6807\u8BC6\u7B26");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(tableResult, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("\u8FDB\u7A0B\u4F18\u5148\u7EA7");
		
		TableColumn tblclmnNewColumn_2 = new TableColumn(tableResult, SWT.NONE);
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText("\u8FDB\u7A0B\u5230\u8FBE\u65F6\u95F4");
		
		TableColumn tblclmnNewColumn_3 = new TableColumn(tableResult, SWT.NONE);
		tblclmnNewColumn_3.setWidth(100);
		tblclmnNewColumn_3.setText("\u8FDB\u7A0B\u670D\u52A1\u65F6\u95F4");
		
		TableColumn tblclmnNewColumn_4 = new TableColumn(tableResult, SWT.NONE);
		tblclmnNewColumn_4.setWidth(100);
		tblclmnNewColumn_4.setText("\u8FDB\u7A0B\u5DF2\u8FD0\u884C\u65F6\u95F4");
		
		TableColumn tblclmnNewColumn_5 = new TableColumn(tableResult, SWT.NONE);
		tblclmnNewColumn_5.setWidth(100);
		tblclmnNewColumn_5.setText("\u8FDB\u7A0B\u5B8C\u6210\u65F6\u95F4");
		
		TableColumn tblclmnNewColumn_6 = new TableColumn(tableResult, SWT.NONE);
		tblclmnNewColumn_6.setWidth(100);
		tblclmnNewColumn_6.setText("\u5468\u8F6C\u65F6\u95F4");
		
		TableColumn tblclmnNewColumn_7 = new TableColumn(tableResult, SWT.NONE);
		tblclmnNewColumn_7.setWidth(100);
		tblclmnNewColumn_7.setText("\u5E26\u6743\u5468\u8F6C\u65F6\u95F4");

		Label labelSeparator = new Label(shellControlMainWindow, SWT.SEPARATOR | SWT.VERTICAL);
		labelSeparator.setBounds(1206, 26, 23, 488);

		Label labelControlPanel = new Label(shellControlMainWindow, SWT.NONE);
		labelControlPanel.setBounds(1235, 26, 61, 17);
		labelControlPanel.setText("\u63A7\u5236\u53F0");

		Button buttonCreateProcess = new Button(shellControlMainWindow, SWT.NONE);
		buttonCreateProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CreateProcessWindow.start();
			}
		});
		buttonCreateProcess.setBounds(1235, 49, 129, 27);
		buttonCreateProcess.setText("\u521B\u5EFA\u8FDB\u7A0B");

		Button buttonStartProcess = new Button(shellControlMainWindow, SWT.NONE);
		buttonStartProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					TableItem t = tablePCB.getItem(tablePCB.getSelectionIndex());
					pcb[Integer.parseInt(t.getText())].switchToRunning();
				} catch (IllegalArgumentException e1) {
					System.out.println("No Item Selected. Please retry.");
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No Item Selected. Please retry.\n");
				}
			}
		});
		buttonStartProcess.setBounds(1235, 82, 129, 27);
		buttonStartProcess.setText("\u5F00\u59CB\u8FDB\u7A0B");

		Button buttonKillProcess = new Button(shellControlMainWindow, SWT.NONE);
		buttonKillProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					TableItem t = tablePCB.getItem(tablePCB.getSelectionIndex());
					pcb[Integer.parseInt(t.getText())].kill();
				} catch (IllegalArgumentException e1) {
					System.out.println("No Item Selected. Please retry.");
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No Item Selected. Please retry.\n");
				}
			}
		});
		buttonKillProcess.setBounds(1235, 115, 129, 27);
		buttonKillProcess.setText("\u7ED3\u675F\u8FDB\u7A0B");

		Button buttonSuspendProcess = new Button(shellControlMainWindow, SWT.NONE);
		buttonSuspendProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					TableItem t = tablePCB.getItem(tablePCB.getSelectionIndex());
					pcb[Integer.parseInt(t.getText())].switchToSuspend();
				} catch (IllegalArgumentException e1) {
					System.out.println("No Item Selected. Please retry.");
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No Item Selected. Please retry.\n");
				}
			}
		});
		buttonSuspendProcess.setBounds(1235, 148, 129, 27);
		buttonSuspendProcess.setText("\u6302\u8D77\u8FDB\u7A0B");

		Button buttonActivateProcess = new Button(shellControlMainWindow, SWT.NONE);
		buttonActivateProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					TableItem t = tablePCB.getItem(tablePCB.getSelectionIndex());
					pcb[Integer.parseInt(t.getText())].switchToActivated();
				} catch (IllegalArgumentException e1) {
					System.out.println("No Item Selected. Please retry.");
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No Item Selected. Please retry.\n");
				}
			}
		});
		buttonActivateProcess.setBounds(1235, 181, 129, 27);
		buttonActivateProcess.setText("\u6FC0\u6D3B\u8FDB\u7A0B");

		Button buttonWakeUpProcess = new Button(shellControlMainWindow, SWT.NONE);
		buttonWakeUpProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					TableItem t = tablePCB.getItem(tablePCB.getSelectionIndex());
					pcb[Integer.parseInt(t.getText())].switchToRunning();
				} catch (IllegalArgumentException e1) {
					System.out.println("No Item Selected. Please retry.");
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No Item Selected. Please retry.\n");
				}
			}
		});
		buttonWakeUpProcess.setBounds(1235, 214, 129, 27);
		buttonWakeUpProcess.setText("\u5524\u9192\u8FDB\u7A0B");

		Label labelSystemLogs = new Label(shellControlMainWindow, SWT.NONE);
		labelSystemLogs.setBounds(1381, 26, 61, 17);
		labelSystemLogs.setText("\u7CFB\u7EDF\u65E5\u5FD7");

		textSystemLogs = new Text(shellControlMainWindow,
				SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textSystemLogs.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		textSystemLogs.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		textSystemLogs.setBounds(1381, 51, 411, 648);

		labelFreePCB.setBounds(25, 448, 83, 17);
		labelFreePCB.setText("\u7A7A\u95F2PCB\u6570\u91CF\uFF1A");

		labelFreePCBNum.setText("0");
		labelFreePCBNum.setBounds(114, 448, 61, 17);

		Label labelReadyProcess = new Label(shellControlMainWindow, SWT.NONE);
		labelReadyProcess = new Label(shellControlMainWindow, SWT.NONE);
		labelReadyProcess.setBounds(25, 476, 83, 17);
		labelReadyProcess.setText("\u5C31\u7EEA\u8FDB\u7A0B\u6570\uFF1A");

		labelReadyProcessNum = new Label(shellControlMainWindow, SWT.NONE);
		labelReadyProcessNum.setBounds(114, 476, 61, 17);
		labelReadyProcessNum.setText("0");

		Label labelRunningProcess = new Label(shellControlMainWindow, SWT.NONE);
		labelRunningProcess.setBounds(181, 448, 83, 17);
		labelRunningProcess.setText("\u6267\u884C\u8FDB\u7A0B\u6570\uFF1A");

		labelRunningProcessNum = new Label(shellControlMainWindow, SWT.NONE);
		labelRunningProcessNum.setBounds(270, 448, 61, 17);
		labelRunningProcessNum.setText("0");

		Label labelBlockedProcess = new Label(shellControlMainWindow, SWT.NONE);
		labelBlockedProcess.setBounds(181, 476, 83, 17);
		labelBlockedProcess.setText("\u963B\u585E\u8FDB\u7A0B\u6570\uFF1A");

		labelBlockedProcessNum = new Label(shellControlMainWindow, SWT.NONE);
		labelBlockedProcessNum.setBounds(270, 476, 61, 17);
		labelBlockedProcessNum.setText("0");

		Label labelSuspendProcess = new Label(shellControlMainWindow, SWT.NONE);
		labelSuspendProcess.setBounds(337, 448, 83, 17);
		labelSuspendProcess.setText("\u6302\u8D77\u8FDB\u7A0B\u6570\uFF1A");

		labelSuspendProcessNum = new Label(shellControlMainWindow, SWT.NONE);
		labelSuspendProcessNum.setBounds(426, 448, 61, 17);
		labelSuspendProcessNum.setText("0");

		Label labelTerminal = new Label(shellControlMainWindow, SWT.NONE);
		labelTerminal.setBounds(1235, 360, 61, 17);
		labelTerminal.setText("\u547D\u4EE4\u884C");

		textCommand = new Text(shellControlMainWindow, SWT.BORDER);
		textCommand.setBounds(1274, 388, 90, 23);

		Label labelCommand = new Label(shellControlMainWindow, SWT.NONE);
		labelCommand.setBounds(1235, 391, 24, 17);
		labelCommand.setText("\u547D\u4EE4");

		Label labelSendPID = new Label(shellControlMainWindow, SWT.NONE);
		labelSendPID.setBounds(1235, 420, 23, 17);
		labelSendPID.setText("PID");

		textSendPID = new Text(shellControlMainWindow, SWT.BORDER);
		textSendPID.setBounds(1274, 417, 90, 23);

		Button buttonSend = new Button(shellControlMainWindow, SWT.NONE);
		buttonSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String command = textCommand.getText();
					int pid = Integer.parseInt(textSendPID.getText());
					if (!CreateProcess.checkEmpty(pcb[pid])) {
						switch (command) {
						case "START":
							pcb[pid].switchToRunning();
							textCommand.setText("");
							textSendPID.setText("");
							break;
						case "KILL":
							pcb[pid].kill();
							textCommand.setText("");
							textSendPID.setText("");
							break;
						case "SUSPEND":
							pcb[pid].switchToSuspend();
							textCommand.setText("");
							textSendPID.setText("");
							break;
						case "ACTIVATE":
							pcb[pid].switchToActivated();
							textCommand.setText("");
							textSendPID.setText("");
							break;
						case "WAKE":
							pcb[pid].switchToRunning();
							textCommand.setText("");
							textSendPID.setText("");
							break;
						default:
							String time = sdf.format(new Date());
							textSystemLogs.append(time + "Unknown Command!\n");
							textCommand.setText("");
							textSendPID.setText("");
						}
					} else {
						String time = sdf.format(new Date());
						textSystemLogs.append(time + "PID not exist!\n");
						textCommand.setText("");
						textSendPID.setText("");
					}
				} catch (NumberFormatException e1) {
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No input, please retry!\n");
				}
			}
		});
		buttonSend.setBounds(1235, 466, 129, 27);
		buttonSend.setText("\u53D1\u9001\u547D\u4EE4");

		Button buttonClear = new Button(shellControlMainWindow, SWT.NONE);
		buttonClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textSystemLogs.setText("");
			}
		});
		buttonClear.setBounds(1737, 21, 55, 27);
		buttonClear.setText("\u6E05\u7A7A");

		Label labelSystemTime = new Label(shellControlMainWindow, SWT.NONE);
		labelSystemTime.setBounds(337, 476, 61, 17);
		labelSystemTime.setText("\u7CFB\u7EDF\u65F6\u95F4\uFF1A");

		labelSystemTimeNum = new Label(shellControlMainWindow, SWT.NONE);
		labelSystemTimeNum.setBounds(426, 476, 61, 17);
		labelSystemTimeNum.setText("0");

		Button buttonCommandHelp = new Button(shellControlMainWindow, SWT.TOGGLE);
		buttonCommandHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (buttonCommandHelp.getSelection()) {
					textCommandHelp.setVisible(true);
				} else {
					textCommandHelp.setVisible(false);
				}
			}
		});
		buttonCommandHelp.setImage(
				SWTResourceManager.getImage(ControlMainWindow.class, "/org/eclipse/jface/dialogs/images/help.png"));
		buttonCommandHelp.setBounds(1337, 355, 27, 27);

		Label labelSeparator2 = new Label(shellControlMainWindow, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelSeparator2.setBounds(25, 519, 1339, 8);

		Label labelFreeMemory = new Label(shellControlMainWindow, SWT.NONE);
		labelFreeMemory.setAlignment(SWT.RIGHT);
		labelFreeMemory.setBounds(493, 448, 110, 17);
		labelFreeMemory.setText("\u7CFB\u7EDF\u53EF\u7528\u5185\u5B58\u6570\uFF1A");

		labelFreeMemoryNum = new Label(shellControlMainWindow, SWT.NONE);
		labelFreeMemoryNum.setBounds(609, 448, 61, 17);
		labelFreeMemoryNum.setText("0");

		Label labelMemoryKB = new Label(shellControlMainWindow, SWT.NONE);
		labelMemoryKB.setBounds(676, 448, 23, 17);
		labelMemoryKB.setText("KB");

		textCommandHelp = new Text(shellControlMainWindow, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		textCommandHelp.setText(
				"START - \u5F00\u59CB\u8FDB\u7A0B\r\nKILL - \u7EC8\u6B62\u8FDB\u7A0B\r\nSUSPEND - \u6302\u8D77\u8FDB\u7A0B\r\nACTIVATE - \u6FC0\u6D3B\u8FDB\u7A0B\r\nWAKE - \u5524\u9192\u8FDB\u7A0B");
		textCommandHelp.setEditable(false);
		textCommandHelp.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		textCommandHelp.setBounds(1235, 263, 129, 91);
		textCommandHelp.setVisible(false);

		Label labelDeviceHubs = new Label(shellControlMainWindow, SWT.NONE);
		labelDeviceHubs.setBounds(25, 541, 61, 17);
		labelDeviceHubs.setText("\u8BBE\u5907\u69FD");

		compositeHub1 = new Composite(shellControlMainWindow, SWT.BORDER);
		compositeHub1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeHub1.setLocation(25, 564);
		compositeHub1.setSize(395, 135);
		compositeHub1.setVisible(false);

		Label labelDeviceID1 = new Label(compositeHub1, SWT.NONE);
		labelDeviceID1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		labelDeviceID1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceID1.setBounds(10, 10, 90, 17);
		labelDeviceID1.setText("\u8BBE\u5907ID\uFF1A0");

		Label labelDeviceName1 = new Label(compositeHub1, SWT.NONE);
		labelDeviceName1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		labelDeviceName1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceName1.setBounds(10, 33, 234, 17);
		labelDeviceName1.setText("\u8BBE\u5907\u540D\u79F0\uFF1AHP LaserJet Pro P1106");

		Label labelDeviceType1 = new Label(compositeHub1, SWT.NONE);
		labelDeviceType1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		labelDeviceType1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceType1.setBounds(10, 56, 108, 17);
		labelDeviceType1.setText("\u8BBE\u5907\u7C7B\u578B\uFF1A\u6253\u5370\u673A");

		Label labelDeviceStatus1 = new Label(compositeHub1, SWT.NONE);
		labelDeviceStatus1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		labelDeviceStatus1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceStatus1.setBounds(10, 79, 36, 17);
		labelDeviceStatus1.setText("\u72B6\u6001\uFF1A");
		
		Label labelPrinterIcon1 = new Label(compositeHub1, SWT.NONE);
		labelPrinterIcon1.setBackgroundImage(SWTResourceManager.getImage("E:\\PersonalFolder\\Pictures\\\u56FE\u72477.bmp"));
		labelPrinterIcon1.setBounds(286, 33, 61, 61);

		Label labelConnected1 = new Label(compositeHub1, SWT.NONE);
		labelConnected1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelConnected1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		labelConnected1.setBounds(10, 102, 67, 17);
		labelConnected1.setText("\u5DF2\u8FDE\u63A5");

		labelStatus1 = new Label(compositeHub1, SWT.NONE);
		labelStatus1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		labelStatus1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelStatus1.setBounds(52, 79, 228, 17);
		labelStatus1.setText("\u5C31\u7EEA");
		
		labelLED1 = new Label(compositeHub1, SWT.NONE);
		labelLED1.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/greenled.png"));
		labelLED1.setBounds(364, 10, 17, 17);

		Composite compositeHub1Empty = new Composite(shellControlMainWindow, SWT.BORDER);
		compositeHub1Empty.setBounds(25, 564, 395, 135);

		Label lblEmpty1 = new Label(compositeHub1Empty, SWT.NONE);
		lblEmpty1.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblEmpty1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 22, SWT.ITALIC));
		lblEmpty1.setAlignment(SWT.CENTER);
		lblEmpty1.setBounds(106, 54, 171, 51);
		lblEmpty1.setText("Empty");

		compositeHub2 = new Composite(shellControlMainWindow, SWT.BORDER);
		compositeHub2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeHub2.setLocation(497, 564);
		compositeHub2.setSize(395, 135);
		compositeHub2.setVisible(false);

		Label labelDeviceID2 = new Label(compositeHub2, SWT.NONE);
		labelDeviceID2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceID2.setText("\u8BBE\u5907ID\uFF1A1");
		labelDeviceID2.setBounds(10, 10, 90, 17);

		Label labelDeviceName2 = new Label(compositeHub2, SWT.NONE);
		labelDeviceName2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceName2.setText("\u8BBE\u5907\u540D\u79F0\uFF1AHP LaserJet Pro M1213nf");
		labelDeviceName2.setBounds(10, 33, 234, 17);

		Label labelDeviceType2 = new Label(compositeHub2, SWT.NONE);
		labelDeviceType2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceType2.setText("\u8BBE\u5907\u7C7B\u578B\uFF1A\u6253\u5370\u673A");
		labelDeviceType2.setBounds(10, 56, 108, 17);

		Label labelDeviceStatus2 = new Label(compositeHub2, SWT.NONE);
		labelDeviceStatus2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceStatus2.setText("\u72B6\u6001\uFF1A");
		labelDeviceStatus2.setBounds(10, 79, 36, 17);

		Label labelPrinterIcon2 = new Label(compositeHub2, SWT.NONE);
		labelPrinterIcon2.setBounds(286, 33, 61, 61);
		labelPrinterIcon2
				.setBackgroundImage(SWTResourceManager.getImage("E:\\PersonalFolder\\Pictures\\\u56FE\u72477.bmp"));

		Label labelConnected2 = new Label(compositeHub2, SWT.NONE);
		labelConnected2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelConnected2.setText("\u5DF2\u8FDE\u63A5");
		labelConnected2.setBounds(10, 102, 61, 17);

		labelStatus2 = new Label(compositeHub2, SWT.NONE);
		labelStatus2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelStatus2.setText("\u5C31\u7EEA");
		labelStatus2.setBounds(52, 79, 228, 17);
		
		labelLED2 = new Label(compositeHub2, SWT.NONE);
		labelLED2.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/greenled.png"));
		labelLED2.setBounds(364, 10, 17, 17);

		Composite compositeHub2Empty = new Composite(shellControlMainWindow, SWT.BORDER);
		compositeHub2Empty.setBounds(497, 564, 395, 135);

		Label lblEmpty2 = new Label(compositeHub2Empty, SWT.NONE);
		lblEmpty2.setText("Empty");
		lblEmpty2.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblEmpty2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 22, SWT.ITALIC));
		lblEmpty2.setAlignment(SWT.CENTER);
		lblEmpty2.setBounds(106, 54, 171, 51);

		compositeHub3 = new Composite(shellControlMainWindow, SWT.BORDER);
		compositeHub3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeHub3.setBounds(969, 564, 395, 135);
		compositeHub3.setVisible(false);

		Label labelDeviceID3 = new Label(compositeHub3, SWT.NONE);
		labelDeviceID3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceID3.setText("\u8BBE\u5907ID\uFF1A2");
		labelDeviceID3.setBounds(10, 10, 90, 17);

		Label labelDeviceName3 = new Label(compositeHub3, SWT.NONE);
		labelDeviceName3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceName3.setText("\u8BBE\u5907\u540D\u79F0\uFF1ASONY ad-7260s");
		labelDeviceName3.setBounds(10, 33, 234, 17);

		Label labelDeviceType3 = new Label(compositeHub3, SWT.NONE);
		labelDeviceType3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceType3.setText("\u8BBE\u5907\u7C7B\u578B\uFF1A\u523B\u5F55\u673A");
		labelDeviceType3.setBounds(10, 56, 108, 17);

		Label labelDeviceStatus3 = new Label(compositeHub3, SWT.NONE);
		labelDeviceStatus3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelDeviceStatus3.setText("\u72B6\u6001\uFF1A");
		labelDeviceStatus3.setBounds(10, 79, 36, 17);

		Label labelPrinterIcon3 = new Label(compositeHub3, SWT.NONE);
		labelPrinterIcon3
				.setBackgroundImage(SWTResourceManager.getImage("E:\\PersonalFolder\\Pictures\\\u56FE\u72478.bmp"));
		labelPrinterIcon3.setBounds(286, 33, 61, 61);

		Label labelConnected3 = new Label(compositeHub3, SWT.NONE);
		labelConnected3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelConnected3.setText("\u5DF2\u8FDE\u63A5");
		labelConnected3.setBounds(10, 102, 61, 17);

		labelStatus3 = new Label(compositeHub3, SWT.NONE);
		labelStatus3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelStatus3.setText("\u5C31\u7EEA");
		labelStatus3.setBounds(52, 79, 228, 17);
		
		labelLED3 = new Label(compositeHub3, SWT.NONE);
		labelLED3.setImage(SWTResourceManager.getImage(ControlMainWindow.class, "/cn/nuist/os/resources/greenled.png"));
		labelLED3.setBounds(364, 10, 17, 17);

		Composite compositeHub3Empty = new Composite(shellControlMainWindow, SWT.BORDER);
		compositeHub3Empty.setBounds(969, 564, 395, 135);

		Label lblEmpty3 = new Label(compositeHub3Empty, SWT.NONE);
		lblEmpty3.setText("Empty");
		lblEmpty3.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblEmpty3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 22, SWT.ITALIC));
		lblEmpty3.setAlignment(SWT.CENTER);
		lblEmpty3.setBounds(106, 54, 171, 51);
		
		Button buttonCommunity = new Button(shellControlMainWindow, SWT.NONE);
		buttonCommunity.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					TableItem t = tablePCB.getItem(tablePCB.getSelectionIndex());
					CommunicationWindow.start(pcb[Integer.parseInt(t.getText())].getPID());
				} catch (IllegalArgumentException e1) {
					System.out.println("No Item Selected. Please retry.");
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "No Item Selected. Please retry.\n");
				}
			}
		});
		buttonCommunity.setBounds(1235, 277, 129, 27);
		buttonCommunity.setText("\u8FDB\u7A0B\u901A\u4FE1");
		
		AutoFreshThread.begin();
		TimerThread.begin();
		CheckTimeThread.begin();
		ResourcesAllocateThread.begin();

	}
	
	public static void onekey() {
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessA", 8,
						"root", 40, 12, i,
						15, "打印机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessA created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessB", 6,
						"root", 30, 17, i,
						4, "打印机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessB created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessC", 7,
						"root", 50, 22, i,
						16, "");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessC created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessD", 10,
						"root", 37, 51, i,
						12, "刻录机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessD created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessE", 2,
						"root", 28, 5, i,
						16, "打印机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessE created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessF", 5,
						"root", 56, 41, i,
						186, "");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessF created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessG", 1,
						"root", 16, 4, i,
						27, "打印机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessG created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessH", 15,
						"root", 3, 21, i,
						62, "打印机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessH created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessI", 14,
						"root", 20, 65, i,
						36, "刻录机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessI created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessJ", 20,
						"root", 50, 2, i,
						174, "打印机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessJ created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessK", 3,
						"root", 22, 45, i,
						127, "");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessK created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessL", 16,
						"root", 40, 72, i,
						14, "打印机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessL created. PID=" + i + ".\n");
				}
				break;
			}
		}
		for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
			if (CreateProcess.checkEmpty(pcb[i])) {
				PCB process = new PCB();
				process = CreateProcess.oneKeyCreateProcess("ProcessM", 9,
						"root", 36, 51, i,
						245, "刻录机");
				if(process!=null) {
					pcb[i] = process;
					SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
					String time = sdf.format(new Date());
					textSystemLogs.append(
							time + "Process ProcessM created. PID=" + i + ".\n");
				}
				break;
			}
		}
	}
}
