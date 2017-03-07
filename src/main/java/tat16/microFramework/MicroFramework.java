package tat16.microFramework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tat16.microFramework.bean.Instruction;

public class MicroFramework {

	private static final String SPACE = " ";
	private static final String QUOTE = "\"";

	private BufferedReader input;
	private FileWriter output;
	private List<Instruction> instructions = new ArrayList<>();

	public MicroFramework() {
	}

	public MicroFramework(String inputFile, String outputFile) throws IOException {

		initFramework(new File(inputFile), new File(outputFile));

	}

	public void initFramework(File inputFile, File outputFile) throws IOException {

		if (input != null) {
			input.close();
		}

		if (output != null) {
			output.close();
		}

		input = new BufferedReader(new FileReader(inputFile));
		output = new FileWriter(outputFile);

		initInstructions();

	}

	public void initFramework(String inputFile, String outputFile) throws IOException {

		initFramework(new File(inputFile), new File(outputFile));

		initInstructions();
	}

	public void startTesting() throws IOException {

		if (instructions != null) {
			List<String> results = null;
			CommandExecutor executor = new CommandExecutor();

			results = executor.execute(instructions);

			outputResults(results);
		}
	}

	private void outputResults(List<String> results) throws IOException {

		for (int i = 0; i < results.size(); i++) {
			output.write(results.get(i) + "\n");
		}

		output.flush();
		output.close();

	}

	private void initInstructions() throws IOException {
		Instruction instruction;
		String line;

		if (input != null) {
			while (input.ready()) {
				line = input.readLine().trim();
				instruction = convertStringToInstruction(line);

				instructions.add(instruction);
			}
		}
	}

	private Instruction convertStringToInstruction(String line) {
		List<String> parameters = new ArrayList<>();
		String[] strings = line.split(SPACE);
		StringBuilder argument;
		Instruction instruction = null;

		if (strings.length == 0) {
			return null;
		}

		instruction = new Instruction();
		instruction.setCommand(strings[0]);

		for (int i = 1; i < strings.length; i++) {

			if (strings[i].startsWith(QUOTE)) {

				argument = new StringBuilder();

				while (!strings[i].endsWith(QUOTE)) {
					argument.append(strings[i] + SPACE);
					i++;
				}
				argument.append(strings[i]);

				argument.deleteCharAt(0);
				argument.deleteCharAt(argument.length() - 1);
				parameters.add(argument.toString());

			} else
				parameters.add(strings[i]);
		}

		instruction.setParameters(parameters);

		return instruction;
	}
}
