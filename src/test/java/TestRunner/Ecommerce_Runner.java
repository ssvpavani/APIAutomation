package TestRunner;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features= {"src\\test\\resources\\Features\\ECommerce.feature"},
        //tags="@Regression",
        glue= "RSProcessStepDefinitions",
        dryRun=false,
        monochrome=true,

        //plugin={"pretty", "html:target/cucumber", "json:target/cucumber-report.json"}
        plugin = {"pretty","com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}

)
public class Ecommerce_Runner {
}
