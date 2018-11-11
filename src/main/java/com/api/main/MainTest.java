package com.api.main;

import com.api.run.TestCaseSuite;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

public class MainTest {
    private static final Logger logger = Logger.getLogger(MainTest.class.getName());

    public static void main(String[] args) throws Exception {
        try {
            BasicConfigurator.configure();
            PropertyConfigurator.configure("log4j.properties");
            XmlSuite suite = TestCaseSuite.buildSuite("2", "Ben");
            TestNG testng = new TestNG();
            testng.setOutputDirectory("testOutput");
            testng.setCommandLineSuite(suite);
            testng.run();
        }catch (Exception e) {
            logger.error("Run failed. Please check below error message: " + e.toString());
        }

    }
}
