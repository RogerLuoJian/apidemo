package com.api.run;

import com.api.base.ExcelUtil;
import org.apache.log4j.Logger;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.nio.file.Paths;
import java.util.*;

public class TestCaseSuite {
    private static HashMap<String, HashMap<String, String>> testCaseSet = new LinkedHashMap<String, HashMap<String, String>>();
    private static final Logger logger = Logger.getLogger(TestCaseSuite.class.getName());

    public static XmlSuite buildSuite(String thread, String testerName) throws Exception {
        XmlSuite suite = new XmlSuite();
        suite.setName("Test");
        suite.setParallel(XmlSuite.ParallelMode.CLASSES);
        int threadCount = 1;//set default value
        if (thread !=null) {
            threadCount = Integer.valueOf(thread);
        }
        suite.setThreadCount(threadCount);
        setTestCaseSet(testerName);
        for (String key : testCaseSet.keySet()) {
            XmlTest xmlTest = new XmlTest(suite);
            xmlTest.setName(key);
            ArrayList<XmlClass> classes = new ArrayList<XmlClass>();
            XmlClass testClass = new XmlClass();
            testClass.setName("com.api.run.TestRunner");
            classes.add(testClass);
            xmlTest.setXmlClasses(classes);
            Map<String, String> testParams = new LinkedHashMap<String, String>();
            HashMap<String, String> caseParams = testCaseSet.get(key);
            caseParams.put("TestName", key);
            for (String paramsKey : caseParams.keySet()) {
                testParams.put(paramsKey, caseParams.get(paramsKey));
            }
            xmlTest.setParameters(testParams);
        }
        logger.debug(suite.toXml());
        return suite;
    }

    private static void setTestCaseSet(String sheetName) throws Exception {
        ExcelUtil.setExcelFile(Paths.get("API", "TestCaseSuite.xlsx").toString());
        int lastRow = ExcelUtil.getLastRowNums(sheetName);
        int lastColum = ExcelUtil.getLastColumNums(sheetName);

        for (int sRowNum = 1; sRowNum <= lastRow; sRowNum++) {
            if (ExcelUtil.getCellData(sRowNum, 5, sheetName).contains("Y")) {
                HashMap<String, String> testCase = new LinkedHashMap<>();
                for (int sColumNum = 1; sColumNum < lastColum; sColumNum++) {
                    String key = ExcelUtil.getCellData(0, sColumNum, sheetName);
                    String value = ExcelUtil.getCellData(sRowNum, sColumNum, sheetName);
                    //when column and row all not empty, add to testCase
                    if (!key.isEmpty() && !value.isEmpty())
                        testCase.put(key, value);
                }
                String caseName = ExcelUtil.getCellData(sRowNum, 0, sheetName);
                if (!caseName.isEmpty())
                    testCaseSet.put(caseName, testCase);
            }
        }
    }
}
