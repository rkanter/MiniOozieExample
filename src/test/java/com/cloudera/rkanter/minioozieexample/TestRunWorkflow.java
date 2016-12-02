package com.cloudera.rkanter.minioozieexample;

import org.apache.hadoop.fs.Path;
import org.apache.oozie.client.WorkflowJob;
import org.apache.oozie.example.SampleMapper;
import org.apache.oozie.test.MiniOozieTestCase;

import java.io.File;
import java.util.Properties;

public class TestRunWorkflow extends MiniOozieTestCase {

    @Override
    protected void setUp() throws Exception {
        setSystemProperty("oozie.test.user.test", System.getProperty("user.name"));
        super.setUp();
    }

    /**
     * Sets up and runs a basic Workflow using MiniOozie.
     *
     * @throws Exception
     */
    public void testRunWorkflow() throws Exception {
        // Find the Oozie examples jar (which has the Mapper and Reducer we're using) and upload to lib dir in HDFS
        Path localExamplesJAR = new Path(SampleMapper.class.getProtectionDomain().getCodeSource().getLocation().toString());
        Path libDir = new Path(getNameNodeUri(), "/lib");
        getFileSystem().mkdirs(libDir);
        getFileSystem().copyFromLocalFile(localExamplesJAR, new Path(libDir, localExamplesJAR.getName()));

        // Upload the workflow.xml to HDFS
        Path wfXML = new Path(getNameNodeUri(), "/workflow.xml");
        getFileSystem().copyFromLocalFile(getFile("workflow.xml"), wfXML);

        // Upload the input data to HDFS
        Path inputDir = new Path(getNameNodeUri(), "/input");
        Path dataTXT = new Path(inputDir, "/data.txt");
        getFileSystem().mkdirs(inputDir);
        getFileSystem().copyFromLocalFile(getFile("data.txt"), dataTXT);

        // Create the job properties
        Properties jobProps = new Properties();
        jobProps.put("nameNode", getNameNodeUri());
        jobProps.put("jobTracker", getJobTrackerUri());
        jobProps.put("oozie.wf.application.path", wfXML.toString());
        jobProps.put("inputDir", inputDir.toString());
        jobProps.put("outputDir", new Path(getNameNodeUri(), "/output").toString());
        jobProps.put("user.name", System.getProperty("user.name"));

        // Submit the job and wait for it to finish
        String jobId = getClient().run(jobProps);
        System.out.println("JobId: " + jobId);
        WorkflowJob.Status status = null;
        do {
            status = getClient().getJobInfo(jobId).getStatus();
            System.out.println("Status: " + status);
            Thread.sleep(3000);
        } while (!(status.equals(WorkflowJob.Status.SUCCEEDED) || status.equals(WorkflowJob.Status.FAILED) || status.equals(WorkflowJob.Status.KILLED)));
    }

    /**
     * Looks for a given fileName in the resources.
     *
     * @param fileName The fileName to look for
     * @return A Path to the file
     */
    private Path getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return new Path(file.toURI());
    }
}
