#Sequence Generator
Personal Project Extra Credit for CMPE202

## Description:
This project creates a Parser which generates a Sequence Diagram from Java Source Code.

## Instructions for generating Sequence Diagrams
The program expects following arguments in the following format:
(Keyword) "(FullPath)" (ClassName) (MethodName) (OutputFileName)

1. Keyword:
  - One word string.
  - Enter keyword “seq” for generating the sequence diagram.

2. FullPath:
  - Full path of the folder which contains all the java source files. 
  -The program picks only the java files and ignores the rest.
  - Ex - "/Users/pavanteja/Downloads/Parser/Test Classes/sequence-diagram-test-1"

3. Class Name:
  - Class Name in which the method resides. Initiates the sequence diagram from this method.

4. Method Name:
  - Method Name from where the sequence diagram initiates.
  - Not necessary to include brackets after the function name.

5. Output file name:
  - Only one word(no spaces and no .png extensions.)
  - File will be created at the source files folder as given in second argument.
   
Example command:-
  java -jar jarname.jar seq "/Users/pavanteja/Downloads/Parser/Test Classes/sequence-diagram-test-1" Customer depositMoney seqdiagram
  
  
 Libraries and Tools:
I have used the JAVAPARSER library to parse the JAVA code into a logically understandable grammar 
https://github.com/javaparser/javaparser

The library provides various methods and classes that read the source code and provide access to each sub-unit of the code in terms of various methods and classes.

For generating the sequence diagram, I have used plantUML in the code
http://plantuml.com/


