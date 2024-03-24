package cn.nuist.os.processdc.control;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.nuist.os.utility.InitLocation;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CreateStatus {

	protected static Shell shellCreateStatus;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void start(boolean ifSuccess) {
		try {
			CreateStatus window = new CreateStatus();
			window.open(ifSuccess);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open(boolean ifSuccess) {
		Display display = Display.getDefault();
		createContents(ifSuccess);
		shellCreateStatus.open();
		shellCreateStatus.layout();
		while (!shellCreateStatus.isDisposed()) {
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
	protected void createContents(boolean ifSuccess) {
		shellCreateStatus = new Shell(SWT.MIN);
		shellCreateStatus.setSize(271, 180);
		shellCreateStatus.setText("\u63D0\u793A");
		InitLocation.init(shellCreateStatus);

		Label labelState = new Label(shellCreateStatus, SWT.NONE);
		labelState.setAlignment(SWT.CENTER);
		if (ifSuccess) {
			labelState.setText("创建成功！");
		} else {
			labelState.setText("创建失败！信息不完整！");
		}

		labelState.setBounds(10, 22, 238, 52);

		Button buttonOK = new Button(shellCreateStatus, SWT.NONE);
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shellCreateStatus.dispose();
			}
		});
		buttonOK.setBounds(88, 98, 80, 27);
		buttonOK.setText("\u786E\u5B9A");
	}

	public static void closeWindow() {
		shellCreateStatus.dispose();
	}

}
