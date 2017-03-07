package tat16.HT1;

import java.io.IOException;

import tat16.microFramework.MicroFramework;

public class App {
	public static void main(String[] args) throws IOException {
		MicroFramework framework = new MicroFramework();

		framework.initFramework("INPUT.TXT", "OUTPUT.TXT");
		framework.startTesting();
	}
}
