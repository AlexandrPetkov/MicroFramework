package tat16.microFramework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				totalTestCount--;
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

		return results;
	}

	private boolean open(String urlString, String timeout) {
		URL url = null;
		URLConnection connection = null;
		boolean isCommandAccept = false;
		long startTime;
		Map<String, List<String>> headers = new HashMap<>();
		InputStream stream;

		try {
			url = new URL(urlString);
			connection = url.openConnection();
			connection.setConnectTimeout(3000);

			connection.connect();

			for (Map.Entry<String, List<String>> pair : connection.getHeaderFields().entrySet()) {
				System.out.print(pair.getKey() + " : ");
				for (int i = 0; i < pair.getValue().size(); i++) {
					System.out.print(pair.getValue().get(i) + ", ");
				}
				System.out.println();
			}

			startTime = new Date().getTime();
			stream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

			StringBuilder builder = new StringBuilder();

			while (reader.ready()) {
				builder.append(reader.readLine() + NEWLINE);
			}

			page = builder.toString();
		} catch (MalformedURLException e) {
			// logger
			e.printStackTrace();
			return isCommandAccept;
		} catch (IOException e) {
			// logger
			e.printStackTrace();
			return isCommandAccept;
		}

		isCommandAccept = true;

		// System.out.println(page);
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
