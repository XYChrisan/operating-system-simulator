package cn.nuist.os.processdc.control;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.SimpleDateFormat;

import cn.nuist.os.utility.InitLocation;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CreateProcessWindow extends ControlMainWindow {
	protected Shell shellCreateProcess;
	private Text textProcessNameInput;
	private Text textNeedTimeInput;
	private Text textArriveTimeInput;
	private Text textNeedMemoryInput;
	private Text textProcessPriorityInput;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void start() {
		try {
			CreateProcessWindow window = new CreateProcessWindow();
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
		shellCreateProcess.open();
		shellCreateProcess.layout();
		while (!shellCreateProcess.isDisposed()) {
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
		shellCreateProcess = new Shell(SWT.MIN);
		shellCreateProcess.setSize(319, 377);
		shellCreateProcess.setText("\u521B\u5EFA\u8FDB\u7A0B");
		InitLocation.init(shellCreateProcess);

		Label labelProcessName = new Label(shellCreateProcess, SWT.RIGHT);
		labelProcessName.setBounds(10, 35, 89, 17);
		labelProcessName.setText("\u8FDB\u7A0B\u540D\u79F0");

		textProcessNameInput = new Text(shellCreateProcess, SWT.BORDER);
		textProcessNameInput.setBounds(118, 32, 157, 23);

		Label textProcessPriority = new Label(shellCreateProcess, SWT.RIGHT);
		textProcessPriority.setBounds(10, 70, 89, 17);
		textProcessPriority.setText("\u8FDB\u7A0B\u4F18\u5148\u7EA7");

		textProcessPriorityInput = new Text(shellCreateProcess, SWT.BORDER);
		textProcessPriorityInput.setBounds(118, 67, 157, 23);

		Label labelProcessUser = new Label(shellCreateProcess, SWT.RIGHT);
		labelProcessUser.setBounds(10, 105, 89, 17);
		labelProcessUser.setText("\u521B\u5EFA\u7528\u6237");

		Combo comboProcessUserSelection = new Combo(shellCreateProcess, SWT.NONE);
		comboProcessUserSelection.setBounds(118, 102, 157, 25);
		comboProcessUserSelection.add("root");

		Combo comboRequestDevType = new Combo(shellCreateProcess, SWT.NONE);
		comboRequestDevType.setBounds(118, 242, 157, 25);
		comboRequestDevType.add("´òÓ¡»ú");
		comboRequestDevType.add("¿ÌÂ¼»ú");

		Label labelProcessNeedTime = new Label(shellCreateProcess, SWT.RIGHT);
		labelProcessNeedTime.setBounds(12, 140, 87, 17);
		labelProcessNeedTime.setText("\u9700\u8981\u65F6\u95F4");

		textNeedTimeInput = new Text(shellCreateProcess, SWT.BORDER);
		textNeedTimeInput.setBounds(118, 137, 157, 23);

		Button buttonCreateProcess = new Button(shellCreateProcess, SWT.NONE);
		buttonCreateProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (int i = 0; i < PCB_MAX_CAPACITY; i++) {
					if (CreateProcess.checkEmpty(pcb[i])) {
						PCB process = new PCB();
						process = CreateProcess.createProcess(textProcessNameInput, textProcessPriorityInput,
								comboProcessUserSelection, textNeedTimeInput, textArriveTimeInput, i,
								textNeedMemoryInput, comboRequestDevType);
						if(process!=null) {
							pcb[i] = process;
							SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
							String time = sdf.format(new Date());
							textSystemLogs.append(
									time + "Process " + textProcessNameInput.getText() + " created. PID=" + i + ".\n");
						}
						shellCreateProcess.dispose();
						break;
					}
				}
			}
		});
		buttonCreateProcess.setBounds(109, 284, 80, 27);
		buttonCreateProcess.setText("\u521B\u5EFA");

		Button buttonCancel = new Button(shellCreateProcess, SWT.NONE);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shellCreateProcess.dispose();
			}
		});
		buttonCancel.setBounds(195, 284, 80, 27);
		buttonCancel.setText("\u53D6\u6D88");

		Label labelArriveTime = new Label(shellCreateProcess, SWT.NONE);
		labelArriveTime.setAlignment(SWT.RIGHT);
		labelArriveTime.setBounds(10, 175, 89, 17);
		labelArriveTime.setText("\u5230\u8FBE\u65F6\u95F4");

		textArriveTimeInput = new Text(shellCreateProcess, SWT.BORDER);
		textArriveTimeInput.setBounds(118, 172, 157, 23);

		Label labelNeedMemory = new Label(shellCreateProcess, SWT.NONE);
		labelNeedMemory.setAlignment(SWT.RIGHT);
		labelNeedMemory.setBounds(38, 210, 61, 17);
		labelNeedMemory.setText("\u6240\u9700\u5185\u5B58");

		textNeedMemoryInput = new Text(shellCreateProcess, SWT.BORDER);
		textNeedMemoryInput.setBounds(118, 207, 135, 23);

		Label labelMemoryKB = new Label(shellCreateProcess, SWT.NONE);
		labelMemoryKB.setAlignment(SWT.RIGHT);
		labelMemoryKB.setBounds(255, 210, 20, 17);
		labelMemoryKB.setText("KB");

		Label labelNeedDevice = new Label(shellCreateProcess, SWT.NONE);
		labelNeedDevice.setAlignment(SWT.RIGHT);
		labelNeedDevice.setBounds(10, 245, 89, 17);
		labelNeedDevice.setText("\u8BF7\u6C42\u8BBE\u5907\u7C7B\u578B");

	}
}
