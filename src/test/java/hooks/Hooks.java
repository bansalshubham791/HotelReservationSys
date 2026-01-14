package hooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

        private static ExtentReports extent = ExtentReportManager.getExtentReports();
        private static ThreadLocal<ExtentTest> scenarioTest = new ThreadLocal<>();

        @Before
        public void beforeScenario(Scenario scenario) {
            ExtentTest test = extent.createTest(scenario.getName());
            scenarioTest.set(test);
        }

        @AfterStep
        public void afterStep(Scenario scenario) {
            if (scenario.isFailed()) {
                scenarioTest.get().fail("Step failed");
            } else {
                scenarioTest.get().pass("Step passed");
            }
        }

        @After
        public void afterScenario() {
            extent.flush();
        }
    }

