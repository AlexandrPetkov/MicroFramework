package tat16.microFramework.command.impl;

import java.util.List;

import tat16.microFramework.CommandExecutor;
import tat16.microFramework.command.Command;
import tat16.microFramework.command.exception.FrameworkException;
import tat16.microFramework.command.exception.IncorrectParamsException;

public class CheckPageTitleCommand implements Command {
	private static final String START_TITLE_TEG = "<title>";
	private static final String END_TITLE_TEG = "</title>";

	String title;

	@Override
	public boolean execute(List<String> params) throws FrameworkException {
		parseHref(params);
		boolean isCommandAccept = false;

		String page = CommandExecutor.getPage();
		if (page != null) {
			if (page.contains(START_TITLE_TEG + title + END_TITLE_TEG)) {
				isCommandAccept = true;
			}
		}

		return isCommandAccept;
	}

	private void parseHref(List<String> params) throws IncorrectParamsException {
		if (params.size() != 1) {
			throw new IncorrectParamsException(INCORRECT_PARAMS_COUNT_MESSAGE);
		}

		title = params.get(0);
	}
}
