MiniOozie Example
=================

Example of how to use MiniOozie, which is a JUnit-based test environment for Oozie job testing.  It sets up a Mini Hadoop Cluster and a Local Oozie instance that you can submit jobs to.

Run
---
````
mvn clean test [-Doozie.version=<version>]
````
or from your IDE.

The ``oozie.version`` can be any Oozie version (CDH or Apache).  It defaults to CDH 5.10.0.

Known Issues
------------
* Due to [OOZIE-2273](https://issues.apache.org/jira/browse/OOZIE-2273), older versions of Oozie can't run MiniOozie without the workaround described there
  * This is fixed in Oozie 4.3.0 and CDH 5.10.0
* Due to [OOZIE-2751](https://issues.apache.org/jira/browse/OOZIE-2751), some methods in ````LocalOozieClient```` don't work correctly
  * This should be fixed in Oozie 5.0.0 and CDH 6.0.0
