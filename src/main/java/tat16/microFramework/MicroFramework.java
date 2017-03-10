package tat16.microFramework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tat16.microFramework.bean.Instruction;

public class MicroFramework {

	private static final String SPACE = " ";
	private static final String QUOTE = "\"";

	private File outputFile;
	private List<Instruction> instructions = new ArrayList<>();

	public MicroFramework() {
	}

	public MicroFramework(String inputFile, String outputFile) {

		initFramework(new File(inputFile), new File(outputFile));
	}

	public void initFramework(File inputFile, File outputFile) {

		try (BufferedReader input = new BufferedReader(new FileReader(inputFile))) {
			initInstructions(input);

		} catch (FileNotFoundException e) {
			System.out.println("Указанный вами файл с командами не найден.");
			System.exit(0);
		} catch (IOException e1) {
			System.out.println("Возникла ошибка ввода/вывода при попытке закрыть поток чтения из файла с командами");
		}

		this.outputFile = outputFile;
	}

	public void initFramework(String inputFile, String outputFile) {

		initFramework(new File(inputFile), new File(outputFile));
	}

	public void startTesting() {

		if (instructions != null) {
			List<String> results = null;
			CommandExecutor executor = new CommandExecutor();

			results = executor.execute(instructions);

			outputResults(results);
		}
	}

	private void outputResults(List<String> results) {

		try (FileWriter writer = new FileWriter(outputFile)) {
			for (int i = 0; i < results.size(); i++) {
				writer.write(results.get(i) + "\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println("Возникла ошибка ввода/вывода при попытке записи в файл");
		}
	}

	private void initInstructions(BufferedReader reader) {
		Instruction instruction;
		String line;

		try {
			while (reader.ready()) {
				line = reader.readLine().trim();
				instruction = convertStringToInstruction(line);

				instructions.add(instruction);
			}
		} catch (IOException e) {
			System.out.println("Возникла ошибка ввода/вывода при попытке чтения команды из файла");
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
