package tat16.microFramework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tat16.microFramework.bean.Instruction;
import tat16.microFramework.command.Command;
import tat16.microFramework.command.exception.FrameworkException;
import tat16.microFramework.command.impl.CheckLinkPresentByHrefCommand;
import tat16.microFramework.command.impl.CheckLinkPresentByNameCommand;
import tat16.microFramework.command.impl.CheckPageContainsCommand;
import tat16.microFramework.command.impl.CheckPageTitleCommand;
import tat16.microFramework.command.impl.OpenCommand;

public class CommandExecutor {
	private static String page = null;

	public static String getPage() {
		return page;
	}

	public static void setPage(String page) {
		CommandExecutor.page = page;
	}

	public List<String> execute(List<Instruction> instructions) {
		int totalTestCount = 0;
		int passedTestCount = 0;
		int failedTestCount = 0;
		float totalTime = 0f;
		String cause = "";
		float commandExecutingTime;
		long commandStartTime;
		boolean isSuccess = false;
		List<String> results = new ArrayList<>();
		Command command = null;

		String commandName;
		List<String> parameters;

		for (int i = 0; i < instructions.size(); i++) {
			commandName = instructions.get(i).getCommand().toLowerCase();
			parameters = instructions.get(i).getParameters();

			command = chooseCommand(commandName);
			commandStartTime = new Date().getTime();

			try {
				if (command != null) {
					isSuccess = command.execute(parameters);
					cause = "";
				} else {
					cause = "Command not found";
					isSuccess = false;
				}
			} catch (FrameworkException e) {
				cause = " " + e.getMessage();
				isSuccess = false;
			}

			commandExecutingTime = (new Date().getTime() - commandStartTime) / 1000f;
			totalTime += commandExecutingTime;
			totalTestCount++;

			if (isSuccess) {
				passedTestCount++;
			} else
				failedTestCount++;

			results.add((isSuccess ? "+ " : "! ") + instructions.get(i).toString() + " " + commandExecutingTime);
			System.out.println(results.get(i) + cause);

			isSuccess = false;
		}

		// formation of final results
		results.add("Total tests: " + totalTestCount);
		results.add("Passed/Failed: " + passedTestCount + "/" + failedTestCount);
		results.add("Total time: " + totalTime);
		results.add("Average time: " + Math.round((totalTime / totalTestCount) * 1000) / 1000f);

		return results;
	}

	private Command chooseCommand(String commandName) {
		Command command = null;

		switch (commandName) {

		case "open":
			command = new OpenCommand();
			break;
		case "checklinkpresentbyhref":
			command = new CheckLinkPresentByHrefCommand();
			break;
		case "checklinkpresentbyname":
			command = new CheckLinkPresentByNameCommand();
			break;
		case "checkpagetitle":
			command = new CheckPageTitleCommand();
			break;
		case "checkpagecontains":
			command = new CheckPageContainsCommand();
			break;
		}

		return command;
	}
}
