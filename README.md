# Inventory-Management-System

This project performs unit testing on the Inventory Management System written in PHP and Javascript. The system has many SQL and Cross-Site Scripting vulnerabilities but for the scope of the course, only XSS vulnerabilities have been tested. After identifying the vulnerabilities with Pixy tool, a class has been created to test the specific vulnerability with Selenium Web Driver.

Most of the test cases are individual, but as there are vulnerabilities which are tightly connected or extremely similar, I have also grouped some of them in order to simplify the testing process. Usually test cases consist of injecting HTML as parameters and then checking the presence of these parameters in the application page. This is the least invasive method available, but it is not applicable to all the cases, therefore some test cases make use of JavaScript code – namely alert statements which are easy to spot and detect. In cases where alert statements are used, the presence or number of alerts present is being checked against the expected number of alerts.

Each test extends a BaseTest class which contains all the necessary methods used for the tests such as application actions (e.g. adding, removing items), writing to the database or preparing the drivers. The tests are independent, and they perform all the necessary actions such as populating the database with required data and clean-up after the test has ended. To simulate the user behavior, test clean-up is also done through Selenium, even though it would have been easier to directly clear the database. For this reason, it is recommended to run each test individually, as the failure of a certain test (e.g. because of error reasons such as driver error or execution too slow for Selenium to find some elements) might leave erroneous data behind (e.g. alerts) which might make the following tests fail as well. Nevertheless, all the tests have been run at once without error both for the patched and original code (see below).
If a test passes it means the vulnerability is present. If a test fails without any errors, it means there is no vulnerability. Errors might appear occasionally if the computer is too slow or something non-related to the test case went wrong. If this happens, delete the remaining attack data from the application (if present) and re-run the test.

![Image description](https://i.imgur.com/aWR1k9O.png)


