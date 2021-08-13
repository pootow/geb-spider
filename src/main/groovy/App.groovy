import geb.Browser
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

class App {
    static void main(String[] args) {
        def browser = new Browser(driver: new RemoteWebDriver(new URL('http://localhost:4445/wd/hub'), new ChromeOptions()))
        browser.with {
            go('https://www.baidu.com')
            print $('#s-top-left a').collect {it.text() }.join()
            quit()
        }


    }
}
