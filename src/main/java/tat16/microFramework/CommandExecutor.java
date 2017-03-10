package tat16.microFramework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tat16.microFramework.bean.Instruction;

public class CommandExecutor {
	private static final String START_TITLE_TEG = "<title>";
	private static final String END_TITLE_TEG = "</title>";
	private static final String HREF = "href=";
	private static final String QUOTE = "\"";
	private static final String NEWLINE = "\n";

	private String page;

	public List<String> execute(List<Instruction> instructions) {
		int totalTestCount = 0;
		int passedTestCount = 0;
		int failedTestCount = 0;
		float totalTime = 0f;

		float commandExecutingTime;
		long commandStartTime;
		boolean isSuccess = false;
		List<String> results = new ArrayList<>();

		String command;
		List<String> parameters;

		for (int i = 0; i < instructions.size(); i++) {
			command = instructions.get(i).getCommand().toLowerCase();
			parameters = instructions.get(i).getParameters();

			commandStartTime = new Date().getTime();

			switch (command) {
			case "open":
				isSuccess = open(parameters.get(0), parameters.get(1));
				break;
			case "checklinkpresentbyhref":
				isSuccess = checkLinkPresentByHref(parameters.get(0));
				break;
			case "checklinkpresentbyname":
				isSuccess = checkLinkPresentByName(parameters.get(0));
				break;
			case "checkpagetitle":
				isSuccess = checkPageTitle(parameters.get(0));
				break;
			case "checkpagecontains":
				isSuccess = checkPageContains(parameters.get(0));
				break;
			default:
				isSuccess = false;
				break;
			}

			commandExecutingTime = (new Date().getTime() - commandStartTime) / 1000f;
			totalTime += commandExecutingTime;
			totalTestCount++;
			if (isSuccess) {
				passedTestCount++;
			} else
				failedTestCount++;

			results.add((isSuccess ? "+ " : "! ") + instructions.get(i).toString() + " " + commandExecutingTime);

			isSuccess = false;
		}

		results.add("Total tests: " + totalTestCount);
		results.add("Passed/Failed: " + passedTestCount + "/" + failedTestCount);
		results.add("Total time: " + totalTime);
		results.add("Average time: " + Math.round((totalTime / totalTestCount) * 1000) / 1000f);

		for (int i = 0; i < results.size(); i++) {
			System.out.println(results.get(i));
		}

		return results;
	}

	private boolean open(String urlString, String timeout) {
		long startTime = new Date().getTime();
		boolean isCommandAccept = false;
		URL url = null;
		float realTimeout = 0;

		try {
			url = new URL(urlString);
		} catch (MalformedURLException e1) {
			return isCommandAccept;
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
			StringBuilder builder = new StringBuilder();
			while (reader.ready()) {
				builder.append(reader.readLine() + NEWLINE);
			}
			page = builder.toString();
		} catch (IOException e) {
			System.out.println("Возникли проблемы при чтении страницы с урлом: " + url.toString());
			isCommandAccept = false;
		}

		realTimeout = new Date().getTime() - startTime;
		if (realTimeout * 1000 <= Float.parseFloat(timeout)) {
			isCommandAccept = true;
		}

		return isCommandAccept;
	}

	private boolean checkLinkPresentByHref(String href) {
		boolean isCommandAccept = false;

		if (page != null) {
			if (page.contains(HREF + QUOTE + href + QUOTE)) {
				isCommandAccept = true;
			}
		}

		return isCommandAccept;
	}

	private boolean checkLinkPresentByName(String linkName) {
		boolean isCommandAccept = false;

		return isCommandAccept;
	}

	private boolean checkPageTitle(String title) {
		boolean isCommandAccept = false;

		if (page != null) {
			if (page.contains(START_TITLE_TEG + title + END_TITLE_TEG)) {
				isCommandAccept = true;
			}
		}

		return isCommandAccept;
	}

	private boolean checkPageContains(String text) {
		boolean isCommandAccept = false;

		if (page != null) {
			if (page.contains(text)) {
				isCommandAccept = true;
			}
		}

		return isCommandAccept;
	}
}
