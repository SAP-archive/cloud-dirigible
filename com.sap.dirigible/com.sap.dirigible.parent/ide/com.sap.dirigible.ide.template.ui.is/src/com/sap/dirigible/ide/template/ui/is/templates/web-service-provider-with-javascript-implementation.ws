{
  "address":"/${endpointAddress}",
  "wsdl":"/${projectName}/${fileNameNoExtension}.wsdl",
  "serviceName":"{http://${fileNameNoExtension}.dirigible.sap.com/}${fileNameNoExtensionTitle}Service",
  "processorType":"CAMEL",
  "processor":"direct:${routeId}",
  "format":"JSON"
}
