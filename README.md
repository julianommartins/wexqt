# wexqt
IBM Watson Explorer Query tool - WEX QT

What it is?
It's a tool that allows any user to perform searches at Watson Explorer (WEX) using a simple language like SQL or Natural Language Query.

How can you use it?
Just configure your servers and collections and start it.
It runs as a service in a configurable port. 
User enter query selecting options and voilà!

Benefits
Support and Development team can use the tool to debug or solve problems.
Applications/Services can use it by performing  REST calls, language/technology independent.
POC and prototyping became EASY.
Code can be reusable by other projects.

API Mode
You can add the project as a dependence in any Java Project, this will allow quick access to WEX. No WEX specific development required. Write ONE line of code and talk to WEX!

---

Requirements:
✔ Java 8
✔ Maven 2

Checkout, build and run
1. Checkout as a Maven project
2. After checkout you can go to the project folder and run:
1. mvn clean
This will clean any compiled code, garbage, etc
2. mvn spring-boot:run
This will build the project and launch it. You can access at: http://localhost:8080/queryToHtml

If you need to run in another port, use the syntax (where 8009 its some example):
mvn spring-boot:run -Dserver.port=8009

Using as a project dependency
In the project that you want to use WEX-WS, you can add it at your Java Build project at
Project dependency Tab.
After this done, you can invoke wexwsApi class and start to play.
See the example inside API package.

For support or questions, mail juliano.jmm@gmail.com
