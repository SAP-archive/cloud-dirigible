var systemLib = require('system');
var ioLib = require('io');

// read the input stream
var input = ioLib.read(request.getReader());
// instantiate an Object
var inputMessage = JSON.parse(input);

// define the target namespace depending on your WSDL and locate its prefix 
var namespace = "http://${fileNameNoExtension}.dirigible.sap.com/";
var namespacePrefix = "";
// take the namespace prefix for the target namespace
for (var fieldName in inputMessage["soapenv:Envelope"]) {
  if (inputMessage["soapenv:Envelope"][fieldName] === namespace) {
      namespacePrefix = fieldName;
      break;
  }
}
namespacePrefix = namespacePrefix.substring(7); // "@xmlns:"

// getting the input parameters from the input message 
var param1 = inputMessage["soapenv:Envelope"]["soapenv:Body"][namespacePrefix + ":${fileNameNoExtension}Request"].param1;
var param2 = inputMessage["soapenv:Envelope"]["soapenv:Body"][namespacePrefix + ":${fileNameNoExtension}Request"].param2;

// ACTUAL BUSINESS LOGIC STARTS HERE

// simple echo implementation
var result1 = "echo: " + param1 + " and " + param2;

// ACTUAL BUSINESS LOGIC ENDS HERE

// build the output message
var outputMessage = 
  {"soapenv:Envelope": 
    {"@xmlns:soapenv": "http://schemas.xmlsoap.org/soap/envelope/",
     "@xmlns:${fileNameNoExtension}": "http://${fileNameNoExtension}.dirigible.sap.com/",
     "soapenv:Header": null,
     "soapenv:Body": 
      { "${fileNameNoExtension}:${fileNameNoExtension}Response":  
        {"${fileNameNoExtension}:result1": result1} 
      }
    }
  };

response.getWriter().println(JSON.stringify(outputMessage));
response.getWriter().flush();
