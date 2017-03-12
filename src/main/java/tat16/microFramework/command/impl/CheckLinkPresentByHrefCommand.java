package tat16.microFramework.command.impl;

import java.util.List;

import tat16.microFramework.CommandExecutor;
import tat16.microFramework.command.Command;
import tat16.microFramework.command.exception.FrameworkException;
import tat16.microFramework.command.exception.IncorrectParamsException;

public class CheckLinkPresentByHrefCommand implements Command {
	private static final String HREF = "href=";
	private static final String QUOTE = "'";
	private static final String DOUBLE_QUOTE = "\"";

	private String href;

	@Override
	public boolean execute(List<String> params) throws FrameworkException {

		parseHref(params);

		boolean isCommandAccept = false;

		String page = CommandExecutor.getPage();
		if (page != null) {
			if (page.contains(HREF + QUOTE + href + QUOTE) || page.contains(HREF + DOUBLE_QUOTE + href + DOUBLE_QUOTE)) {
				isCommandAccept = true;
			}
		}

		return isCommandAccept;
	}

	private void parseHref(List<String> params) throws IncorrectParamsException {
		if (params.size() != 1) {
			throw new IncorrectParamsException(INCORRECT_PARAMS_COUNT_MESSAGE);
		}

		href = params.get(0);
	}
}
