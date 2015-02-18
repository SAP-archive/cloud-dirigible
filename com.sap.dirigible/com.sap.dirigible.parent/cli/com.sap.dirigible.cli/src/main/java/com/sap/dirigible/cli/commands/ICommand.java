package com.sap.dirigible.cli.commands;

import java.util.Properties;

public interface ICommand {

	public void execute(Properties properties) throws Exception;
}
