package tat16.microFramework.command.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import tat16.microFramework.CommandExecutor;
import tat16.microFramework.command.Command;
import tat16.microFramework.command.exception.CommandException;
import tat16.microFramework.command.exception.FrameworkException;
import tat16.microFramework.command.exception.IncorrectParamsException;

public class OpenCommand implements Command {

	private static final String NEWLINE = "\n";
	private static final String TIMEOUT_MATCHER = "(\\d){1,4}([.](\\d){1,2})?";
	private static final String READER_EXCEPTION_MESSAGE = "Возникли проблемы при чтении страницы с урлом: ";
	private static final String INCORRECT_TIMEOUT_MESSAGE = "некорректный timeout. Timeout может быть только положительным числом от 0 до 9999. Допускается дробное число до 2-х знаков в дробной части. Дробная часть должна отделяться только знаком '.' ";

	private String urlString;
	private float timeout;

	@Override
	public boolean execute(List<String> params) throws FrameworkException {
		long startTime = new Date().getTime();
		boolean isCommandAccept = false;
		URL url = null;
		float realTimeout = 0;

		parseParameters(params);

		try {
			url = new URL(urlString);
		} catch (MalformedURLException e1) {
			return isCommandAccept;
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
			StringBuilder builder = new StringBuilder();

			while (reader.ready()) {
				builder.append(reader.readLine() + NEWLINE);
			}
			CommandExecutor.setPage(builder.toString());
		} catch (IOException e) {
			throw new CommandException(READER_EXCEPTION_MESSAGE + url.toString());
		}

		realTimeout = new Date().getTime() - startTime;

		if (realTimeout <= timeout * 1000) {
			isCommandAccept = true;
		}

		return isCommandAccept;
	}

	private void parseParameters(List<String> parameters) throws IncorrectParamsException {
		urlString = null;
		timeout = 0f;

		if (parameters.size() != 2) {
			throw new IncorrectParamsException(INCORRECT_PARAMS_COUNT_MESSAGE);
		}

		if (!parameters.get(1).matches(TIMEOUT_MATCHER)) {
			throw new IncorrectParamsException(INCORRECT_TIMEOUT_MESSAGE);
		}

		urlString = parameters.get(0);
		timeout = Float.parseFloat(parameters.get(1));
	}
}
