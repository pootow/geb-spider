import geb.Browser
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver

class CloseableBrowser implements Closeable {

    @Override
    void close() throws IOException {
        this.quit()
    }
}

class App {
    static void main(String[] args) {
        def browser = new Browser(driver: new RemoteWebDriver(new URL('http://localhost:4445/wd/hub'), new ChromeOptions()))
        browser.metaClass.mixin CloseableBrowser
        (browser as Closeable).withCloseable {
            browser.with {
                go('https://www.baidu.com')
                print $('#s-top-left a').collect {it.text() }.join()
                throw new Exception()
            }
        }
    }
}
