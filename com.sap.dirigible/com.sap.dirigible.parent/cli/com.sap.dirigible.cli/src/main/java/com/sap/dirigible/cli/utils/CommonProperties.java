package com.sap.dirigible.cli.utils;

public interface CommonProperties {

	public interface CLI {
		public static final String COMMAND_IMPORT = "import";
		public static final String COMMAND = "command";
	}

	public interface RemoteCommand {
		public static final String PROPERTY_PROXY_HOST = "proxyHost";
		public static final String PROPERTY_PROXY_PORT = "proxyPort";
		public static final String PROPERTY_PROXY_SCHEME = "proxyScheme";
	}

	public interface ImportProjectCommand extends RemoteCommand {
		public static final String PROPERTY_URL = "url";
		public static final String PROPERTY_ARCHIVE = "archive";
	}
}
