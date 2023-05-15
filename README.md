# JCShield Java class decompilation protection


Before presenting JCShield just a few considerations. 
There is currently no real method to fully protect java classes from reverse engineering. 
The  Bytecode Obfuscation is a fantastic protection that modifies Java bytecode so that it is much harder to read and understand. This technique can only make decompilation harder, not avoid it.
Even encryption does not provide an effective protection. The JVM requires the original bytecode to run the classes.  This means that if you can run the code you can get the bytecode too, and if you can get  the bytecode  you can always decompile it.

JCShield is a solution to protect java classes from reverse engineering using AES encryption, leaving the decryption process to the native code and to complicate the reverse engineering process.

JCShield is  composed of 3 parts:

1. A maven plugin to encrypt the classes during the build process
2. A native shared library, coded in C++, to decrypt the classes at runtime
3. A Java agent to pass to the JVM using the -javaagent argument


The Java agent uses JNA to load the native library to decrypt the encrypted classes.
The decryption key is hard-coded into the C++ code, so you need to set it before compiling editing the crypto.cpp file.

## Prerequisite

[Visual Studio Build Tools] for Windows systems
GCC for Linux systems

## Compilation and usage

Step 1. Compile the maven plugin. 

	cd jcshield-mvn-plugin
	mvn clean install

Step 2. To use the plugin add the folling code in the build section of your project pom file:

	
	<build>
	........

	<plugins>
		<plugin>
			<groupId>cryptoclass.plugin</groupId>
			<artifactId>cryptoclass-mvn-plugin</artifactId>
		</plugin>			
	</plugins>


	<pluginManagement>
		<plugins>

			....
			<plugin>
				<groupId>cryptoclass.plugin</groupId>
				<artifactId>cryptoclass-mvn-plugin</artifactId>
				<version>1.0-SNAPSHOT</version>
					<configuration>				    	
						<classdir>${project.build.outputDirectory}</classdir>
						<classes>
						<param></param>
					</classes>
					<secretkey>000102030405060708090A0B0C0D0E</secretkey>
				</configuration>	     
				<executions>
					<execution>
						<id>default-encryption</id>
						<phase>compile</phase>
						<goals>
							<goal>encrypt</goal>
						</goals>
					</execution>
				</executions>
			</plugin>

			.....

		</plugins>
	</pluginManagement>

	</build>

The secretkey attribute contains the 128 bits AES key in the format of 16 hexadecimal numbers.

Step 3. Compile the native lib 
	
For Windows:

	cshield\windows_x64
	build.bat

Compiling generates the ** crypto.dll ** file. This file must be added to the **PATH** environment variable to be linked by the Java agent.

For Linux:

	cshield\linux_x64
	build.sh

Compiling generates the **crypto.so** file. This file must be added to the **LD_LIBRARY_PATH** environment variable to be linked by the Java agent.

Step 4. Compile the Java Agent

	cd jcshield-agent
	mvn clean install

Rename the generated file** crypto-agent-1.0-jar-with-dependencies.jar** to the simpler name **crypto-agent.jar**

Now you have all the pieces to run the demo.

## Trying the demo

jcshield-demo is a maven project with a single java class. Build the project with the command 

	mvn clean install
	
If you try to execute the generated .jar file  with the following command 

	java -jar jcshield-demo-1.0.jar 
	
it raises the following error:

	Error: LinkageError occurred while loading main class jcshield.Demo
        java.lang.ClassFormatError: Incompatible magic value 2005639721 in class file jcshield/Demo

The main java class Demo.class is encrypted. To execute the code we need to pass the agent to the JVM and the path to the shared library.

Copy the agent jar and the shared library in the same folder of the  jcshield-demo-1.0.jar  file. 

If you are using a windows machine type the following command:

	java -javaagent:crypto-agent.jar -jar jcshield-demo-1.0.jar

If you are using a linux system set the environment variable LD_LIBRARY_PATH to point to the path of the shared library crypto.so file.

## Notes
This project is still ongoing

## Copyrights

Copyright (c) 2016-2021, Paolo Mascia.

This project is licensed under the [MIT license].

 [MIT license]: https://opensource.org/licenses/MIT "MIT license"
 [Visual Studio Build Tools]: https://visualstudio.microsoft.com/it/downloads/ "VS Build Tools"
