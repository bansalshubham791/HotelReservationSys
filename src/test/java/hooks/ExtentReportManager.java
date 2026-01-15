package hooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static ExtentReports getExtentReports() {

        if (extent == null) {
            ExtentSparkReporter spark =
                    new ExtentSparkReporter("target/extent-report/ExtentReport.html");

            spark.config().setDocumentTitle("Hotel Reservation API Test Report");
            spark.config().setReportName("Automation Test Results");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "Hotel Reservation System");
            extent.setSystemInfo("Framework", "Cucumber + TestNG + RestAssured");
            extent.setSystemInfo("Tester", "Shubham");
        }
        return extent;
    }
}
