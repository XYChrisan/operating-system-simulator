package cn.nuist.os.processdc.communication;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.SimpleDateFormat;

import cn.nuist.os.processdc.control.ControlMainWindow;
import cn.nuist.os.utility.InitLocation;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CommunicationWindow extends ControlMainWindow{

	protected Shell shellCommunication;
	private Text textPID;
	private Text textContent;
	public static int pidthis;
	public static SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void start(int pid) {
		try {
			pidthis=pid;
			CommunicationWindow window = new CommunicationWindow();
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
		shellCommunication.open();
		shellCommunication.layout();
		while (!shellCommunication.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shellCommunication = new Shell();
		shellCommunication.setSize(345, 194);
		shellCommunication.setText("\u8FDB\u7A0B\u901A\u4FE1-\u53D1\u9001\u4FE1\u606F");
		InitLocation.init(shellCommunication);
		
		Label lblpid = new Label(shellCommunication, SWT.NONE);
		lblpid.setAlignment(SWT.RIGHT);
		lblpid.setBounds(28, 33, 61, 17);
		lblpid.setText("\u76EE\u6807PID\uFF1A");
		
		textPID = new Text(shellCommunication, SWT.BORDER);
		textPID.setBounds(95, 30, 200, 23);
		
		Label label = new Label(shellCommunication, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setBounds(28, 69, 61, 17);
		label.setText("\u6D88\u606F\u5185\u5BB9\uFF1A");
		
		textContent = new Text(shellCommunication, SWT.BORDER);
		textContent.setBounds(95, 66, 200, 23);
		
		Button buttonSend = new Button(shellCommunication, SWT.NONE);
		buttonSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					int toPID=Integer.parseInt(textPID.getText());
					String message=textContent.getText();
					pcb[toPID].setMessage("½ø³Ì"+pidthis+"£º"+message);
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "Process "+pidthis+" send a message to Process "+toPID+", content: "+message+"\n");
				}catch(NullPointerException e1) {
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "Unknown PID, please retry.\n");
				}catch(NumberFormatException e2) {
					String time = sdf.format(new Date());
					textSystemLogs.append(time + "Unknown PID, please retry.\n");
				}
				shellCommunication.dispose();
			}
		});
		buttonSend.setBounds(214, 108, 80, 27);
		buttonSend.setText("\u53D1\u9001");

	}
}
