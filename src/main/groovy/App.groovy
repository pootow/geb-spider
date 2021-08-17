import geb.Browser
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver

class CloseableBrowser implements Closeable {

    @Override
    void close() throws IOException {
        this.quit()
    }
}

class Item {
    boolean isAd
    String name
}

class App {
    static void main(String[] args) {

//        def browser = new Browser(driver: new RemoteWebDriver(new URL('http://localhost:4445/wd/hub'), new ChromeOptions()))
        def browser = new Browser()
//        browser.metaClass.mixin CloseableBrowser
//        (browser as Closeable).withCloseable {
            browser.with {
                go('https://www.taobao.com')

                waitFor(60*1000) {
                    $('.site-nav-login-info-nick').text() == 'tb734832977165'
                }

                def searchForm = $('#J_TSearchForm')
                searchForm.q = '宠物洁齿粉'
                assert searchForm.q == '宠物洁齿粉'

                withNewWindow{ searchForm.$('.btn-search').click() } {
                    def itemDivs = $('div.items>div.item')


                }



            }
//        }
    }
}
