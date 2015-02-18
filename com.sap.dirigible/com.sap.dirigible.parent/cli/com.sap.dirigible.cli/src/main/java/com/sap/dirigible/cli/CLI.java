package com.sap.dirigible.cli;

import java.util.Properties;

import com.sap.dirigible.cli.commands.ICommand;
import com.sap.dirigible.cli.commands.ImportProjectCommand;
import com.sap.dirigible.cli.utils.CommonProperties;
import com.sap.dirigible.cli.utils.Utils;

public class CLI implements CommonProperties.CLI {

	public static void main(String[] args) throws Exception {
		Properties properties = Utils.loadCLIProperties(args);
		ICommand command = getCommand(properties);
		run(command, properties);
	}

	private static ICommand getCommand(Properties properties) {
		ICommand command = null;
		String commandName = properties.getProperty(COMMAND);
		if (commandName.equals(COMMAND_IMPORT)) {
			command = new ImportProjectCommand();
		}
		return command;
	}

	private static void run(ICommand command, Properties properties) throws Exception {
		if(command != null) {
			command.execute(properties);
		} else {
			String message = String.format("Command \"%s\" does not exsist", properties.getProperty(COMMAND));
			throw new IllegalArgumentException(message);
		}
	}
}
