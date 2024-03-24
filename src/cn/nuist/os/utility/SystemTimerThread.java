package cn.nuist.os.utility;

import cn.nuist.os.processdc.control.ControlMainWindow;

public class SystemTimerThread extends ControlMainWindow {
	static Thread t=new Thread() {
		public void run() {
			while (true) {
				addTime();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	public static void begin() {
		// 启动系统计时器线程
		t.start();
	}
	
	// 系统计时器函数
	public static void addTime() {
		labelSystemTimeNum.getDisplay().asyncExec(new Runnable() {
			public void run() {
				labelSystemTimeNum.setText(String.valueOf(Integer.parseInt(labelSystemTimeNum.getText()) + 1));
			}
		});
	}
}
