package cn.nuist.os.utility;

import org.eclipse.swt.widgets.Shell;

public class InitLocation {
	// 初始化swt窗口界面位置
	public static void init(Shell shell) {
		int width = shell.getDisplay().getBounds().width;
		int height = shell.getDisplay().getBounds().height;
		int x = (width - shell.getBounds().width) / 2;
		int y = (height - shell.getBounds().height) / 2;
		shell.setLocation(x, y);
	}
}
