package cn.nuist.os.storage.allorecy;

import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.io.IOException;

public class AllocateRecycle {
	public static void main() {
		Console console = System.console();
        if(console == null && !GraphicsEnvironment.isHeadless()){
            try {
				Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"allorecy.jar\""});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
