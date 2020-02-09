# client-transactions-report
Client Transactions Flat File Data Reader and Daily Summary Report Generator

# ClientTransactionsReports
This is a SpringBoot Maven based java application. Parse an input fixed width file and create an output.csv file based on business rules.

## Scope of this application?
1. The java application parse an input text (.txt) file from resources folder directory.
2. Read the input text file and generate a output.csv file in the project root Output.csv file in directory
3. Application activities can be monitored from the generated log file (logs/client-transactions-report.log)

## What is the minimum requirement to run this application?
1. JDK1.8 (minimum) and JDK path is set correctly
2. Install Maven
3. Any command line application (CMD/ power shell/ terminal (for Mac))

### Which Operating systems are supported?
Windows 64/ Linux / Mac OS

## What are the steps to execute the application?
1. Do a git clone of this project by executing the following command:
    - $ git clone https://github.com/paspuletibalakrishna/client-transactions-report.git
   
2. $ cd client-transactions-report
3. In command line application / or Terminal run the following commands:

    - $ mvn spring-boot:run
  
This command will download the required dependencies, build and run the Test cases and generate a test output file.
  
This command will run the application and generate Output.csv file in the project directory.

Logs can be verified in console/client-transactions-report.log

#Libraries used
FlatPack to read the Fixed width text file and external config file to read the column data
Used extensively Java 8 Stream APIs, CSVWriter APIs and Spring Dependency Injection features
Used Logback-spring logging framework to generate the application logs.

## How this project is Organised?

![alt tag] https://github.com/paspuletibalakrishna/client-transactions-report/blob/master/ProjectFilesStructure.png

## Troubleshooting
Tested on both windows and mac machines and it worked out of the box. So make sure to configure the JDK path correctly to before running the mvn commands.
