package tat16.microFramework.command.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tat16.microFramework.CommandExecutor;
import tat16.microFramework.command.Command;
import tat16.microFramework.command.exception.FrameworkException;
import tat16.microFramework.command.exception.IncorrectParamsException;

public class CheckLinkPresentByNameCommand implements Command {

	private String linkName;

	@Override
	public boolean execute(List<String> params) throws FrameworkException {
		parseLinkName(params);

		boolean isCommandAccept = false;
		String page = CommandExecutor.getPage();
		List<String> links = new ArrayList<>();
		Pattern pattern = Pattern.compile("<.+href=[\"'].+[\"'].+>" + linkName + "</");
		Matcher matcher = pattern.matcher(page);

		while (matcher.find()) {
			if (page.contains(linkName)) {
				return true;
			}
		}

		return isCommandAccept;
	}

	private void parseLinkName(List<String> params) throws IncorrectParamsException {
		if (params.size() != 1) {
			throw new IncorrectParamsException(INCORRECT_PARAMS_COUNT_MESSAGE);
		}

		linkName = params.get(0);
	}
}
