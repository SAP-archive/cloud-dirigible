dirigible [![Build Status](https://travis-ci.org/SAP/cloud-dirigible.svg)](https://travis-ci.org/SAP/cloud-dirigible)
=========

Introduction
------------

### Overview ###

Dirigible is an open source project that provides Integrated Development Environment as a Service (IDEaaS) as well as the runtime engines integration for the running applications.
The applications created with Dirigible comply with the Dynamic Applications concepts and structure.
The environment itself runs directly in the browser, therefore does not require additional downloads and installations.
It packs all the needed components, which makes it self-contained and well integrated software bundle that can be deployed on any Java based Web Server such as Tomcat, Jetty, JBoss connected via JDBC to the RDBMS of your choice (currently supported versions for RDBMS are HANA 1.x and MaxDB, experimental - Sybase ASE).

### Background ###

The Dirigible project came out of an internal SAP initiative to address the extension and adaptation use-cases around SOA and Enterprise Services.
On one hand in this project were implied the lessons learned from the standard tools and approaches so far and on the other hand, there were added features aligned with the most recent technologies and architectural patterns related to Web 2.0 and HTML5, which made it complete enough to be used as the only tool and environment needed for building and running on demand application in the cloud.

### Try ###

Dirigible project is deployed and tested against the [HANA Cloud Platform](https://account.hana.ondemand.com/).

You can start by creating your own unlimited free trial account at [https://account.hanatrial.ondemand.com/](https://account.hanatrial.ondemand.com/) with HANA database underneath.

Sandbox instance with restricted functionality is available at: [http://trial.dirigible.io](http://trial.dirigible.io).


Get Started
-----

##### Download #####
The "fast-track" - you can download the WAR files produced by the latest release from:
[https://github.com/SAP/dirigible/releases/](https://github.com/SAP/dirigible/releases/)
and skip the build section.

Nevertheless, we highly recommend to build the WAR files from the sources to be able to consume also the experimental features not available yet in the releases.

##### Build #####

1. Get the [Maven](http://maven.apache.org/) build tool version 3.0.x
2. Clone the repository <https://github.com/SAP/cloud-dirigible.git> or [download the latest release](https://github.com/SAP/dirigible/archive/master.zip).
3. Go to the folder *com.sap.dirigible.parent*
4. Build the project executing:

        mvn clean install

5. The build should pass successfully
6. The two produced WAR files dirigible-ide\*.war and dirigible-runtime\*.war are ready to be deployed


##### Deploy #####
###### HANA Cloud Platform ######

1. Deploy on [HANA Cloud Platform](https://account.hana.ondemand.com/) with the [Cloud SDK](https://tools.hana.ondemand.com/#cloud).
2. Get the SDK from <https://tools.hana.ondemand.com/#cloud>
3. Go to *neo-java-web-sdk-1.xxx/tools* folder
4. Deploy with command:

        neo deploy --account <your_account> --application <application_name> --user <your_user> --host <target_landscape_host> --source <source_directory> --password <your_password>

6. Start with command:

        neo start --account <your_account> --application <application_name> --user <your_user> --host <target_landscape_host> --password <your_password> -y


7. Go to https://account.hanatrial.ondemand.com/cockpit at Authorizations section. Add both roles - Developer and Operator - to your user to have full access to all the features.

###### Tomcat ######

1. The same WAR files can be deployed on [Tomcat](http://tomcat.apache.org/) Web Container. In this case the built-in Derby database will be used.

More information about how to deploy on Tomcat can be found [here](http://tomcat.apache.org/tomcat-7.0-doc/appdev/deployment.html)

2. For simplicity rename the WAR respectively *dirigible-ide.war* for IDE and *dirigible.war* for Runtime
3. Configure Users store:

        <tomcat-users>
                <role rolename="Developer"/>
                <role rolename="Operator"/>
                <role rolename="Everyone"/>
                <user username="tomcat" password="tomcat" roles="Developer,Operator,Everyone"/>
        </tomcat-users>

4. Open a Web Browser and go to location:

        http://localhost:8080/dirigible-ide (IDE)
        http://localhost:8080/dirigible     (Runtime)

        and login with tomcat/tomcat

###### Eclipse ######

1. The IDE part can be run directly via the Eclipse. This is useful for easy testing of new features during development
2. Import the project as existing Maven project into your local Eclipse environment
3. Go to project *com.sap.dirigible.ide.target* and open the file with the same name with the Target Editor
4. Click on the *Set as Target Platform* link and wait until the required bundles get synchronized
5. Use *dirigible-local.launch* file to *Run As* configuration
6. Open a Web Browser and go to location

        http://localhost:8080/dirigible


Additional Information
----------------------

### License ###

This project is copyrighted by [SAP AG](http://www.sap.com/) and made available under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). Please also confer to the text files "LICENSE" and "NOTICE" included with the project sources.


### Contributors ###

File an [issue](https://github.com/SAP/dirigible/issues) or send us a [pull request](https://github.com/SAP/dirigible/pulls).


### References ###


- Project Home
[http://www.dirigible.io](http://www.dirigible.io)

- Help Portal
[http://help.dirigible.io](http://help.dirigible.io) or PDF https://github.com/SAP/dirigible/raw/master/docs/dirigible_help.pdf

- Simple Samples
[http://samples.dirigible.io](http://samples.dirigible.io)

- Sandbox Instance
[http://sandbox.dirigible.io](http://sandbox.dirigible.io)

- Forum
[http://forum.dirigible.io](http://forum.dirigible.io)

