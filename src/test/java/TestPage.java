import org.apache.poi.ss.formula.functions.T;
import org.testng.annotations.Test;
import pom.pageobjects.HomePage;
import pom.pageobjects.LoginPage;
import pom.pageobjects.Page;

public class TestPage extends FatherTest {
    @Test
    public void testPage() {
        HomePage homePage = Page.getInstance(androidDriverManager.getDriver(), HomePage.class);

        homePage.goTo();

        homePage.verifyPageTitle();

        homePage.scrollTo();

        LoginPage loginPage = Page.getInstance(androidDriverManager.getDriver(), LoginPage.class);


    }
}
