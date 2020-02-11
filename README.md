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

    - $ mvn -e package
  
This command will download the required dependencies, build and run the Test cases and generate a test output file.
 
1. java -jar target/client-transactions-report-1.0.jar  --input.file=./Input.txt --config.file=future-transactions-fixedlength.pzmap.xml --transaction.date=20100820

   This command takes input file and config file for data mapping and transaction dates as arguments and generate the report in the project directory Output.csv file for the given transaction date

2. java -jar target/client-transactions-report-1.0.jar  --input.file=./Input.txt --config.file=future-transactions-fixedlength.pzmap.xml --transaction.date=20100819 

     This command takes input file and config file for data mapping and transaction dates as arguments and generate the report in the project directory Output.csv file for the given transaction date
     
3. java -jar target/client-transactions-report-1.0.jar  --input.file=./Input.txt --config.file=future-transactions-fixedlength.pzmap.xml --transaction.date=20100816 

     This command takes input file and config file for data mapping and transaction dates as arguments and give the error message as the there are no transactions on the given date

4. java -jar target/client-transactions-report-1.0.jar  --input.file=./Input.txt --config.file=future-transactions-fixedlength.pzmap.xml

     This command takes input file and config file for data mapping as arguments and generate the report in the project directory Output.csv file for all the transaction dates in the input file.

## Logs can be verified in terminal or logs/client-transactions-report.log


## Libraries used
1. FlatPack to read the Fixed width text file and external config file to read the column data
2. Used extensively Java 8 Stream APIs, CSVWriter APIs and Spring Dependency Injection features
3. Used Logback-spring logging framework to generate the application logs.

## How this project is Organised?

https://github.com/paspuletibalakrishna/client-transactions-report/blob/master/ProjectFilesStructure.png

## Troubleshooting
Tested on both windows and mac machines and it worked out of the box. So make sure to configure the JDK path correctly to before running the mvn commands.
