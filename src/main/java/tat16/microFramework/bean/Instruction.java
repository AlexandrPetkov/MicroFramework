package tat16.microFramework.bean;

import java.util.List;

public class Instruction {
	private String command;
	private List<String> parameters;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instruction other = (Instruction) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "[" + command + " " + parametersToString() + "]";
	}

	private String parametersToString() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < parameters.size(); i++) {
			builder.append("\"" + parameters.get(i) + "\" ");
		}

		builder.deleteCharAt(builder.length() - 1);

		return builder.toString();
	}

}
