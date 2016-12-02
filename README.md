MiniOozie Example
=================

Example of how to use MiniOozie, which is a JUnit-based test environment for Oozie job testing.  It sets up a Mini Hadoop Cluster and a Local Oozie instance that you can submit jobs to.

Run
---
````
mvn clean test
````

Known Issues
------------
* Due to [OOZIE-2751](https://issues.apache.org/jira/browse/OOZIE-2751), some methods in ````LocalOozieClient```` don't work correctly
* Due to [OOZIE-2273](https://issues.apache.org/jira/browse/OOZIE-2273), older versions of Oozie can't run MiniOozie without the workaround described there