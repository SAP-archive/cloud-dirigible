var systemLib = require('system');

systemLib.println("Hello World!");

response.getWriter().println("Hello World!");

response.setContentType("text/html");
response.getWriter().flush();
response.getWriter().close();