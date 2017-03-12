package tat16.microFramework.command.impl;

import java.util.List;

import tat16.microFramework.CommandExecutor;
import tat16.microFramework.command.Command;
import tat16.microFramework.command.exception.FrameworkException;
import tat16.microFramework.command.exception.IncorrectParamsException;

public class CheckPageContainsCommand implements Command {
	private String text;

	@Override
	public boolean execute(List<String> params) throws FrameworkException {

		parseText(params);
		boolean isCommandAccept = false;

		String page = CommandExecutor.getPage();
		if (page != null) {
			if (page.contains(text)) {
				isCommandAccept = true;
			}
		}

		return isCommandAccept;
	}

	private void parseText(List<String> params) throws IncorrectParamsException {
		if (params.size() != 1) {
			throw new IncorrectParamsException(INCORRECT_PARAMS_COUNT_MESSAGE);
		}

		text = params.get(0);
	}

}
