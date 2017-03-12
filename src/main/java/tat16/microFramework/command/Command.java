package tat16.microFramework.command;

import java.util.List;

import tat16.microFramework.command.exception.FrameworkException;

public interface Command {

	public static final String INCORRECT_PARAMS_COUNT_MESSAGE = "Неправильное кол-во параметров команды";

	public boolean execute(List<String> params) throws FrameworkException;
}
