import geb.Browser
import geb.navigator.Navigator
import groovy.json.JsonOutput

class CloseableBrowser implements Closeable {

    @Override
    void close() throws IOException {
        this.quit()
    }
}

enum Market {
    TMall,
    Taobao,
}

class Item {
    boolean isAd
    Market market
    String name
    String nid
    String dealCnt
    String shopName
    String url
    String sellCnt
}

class App {
    static void main(String[] args) {

//        def browser = new Browser(driver: new RemoteWebDriver(new URL('http://localhost:4445/wd/hub'), new ChromeOptions()))
        def browser = new Browser()
//        browser.metaClass.mixin CloseableBrowser
//        (browser as Closeable).withCloseable {
        browser.with {
            go('https://www.taobao.com')

            waitFor(60 * 1000) {
                def loginInfo = $('.site-nav-login-info-nick')
                loginInfo && loginInfo.text() != ''
            }

            def searchForm = $('#J_TSearchForm')
            searchForm.q = '宠物洁齿粉'
            assert searchForm.q == '宠物洁齿粉'

            withWindow { searchForm.$('.btn-search').click() } {
                def itemDivs = $('div.items > div.item')

                itemDivs.take(2).each { itemDiv ->

                    def item = new Item()


                    def link = itemDiv.$('a.pic-link')
                    item.nid = link.getAttribute('data-nid')
                    item.dealCnt = itemDiv.$('div.deal-cnt').text()[0..-4]
                    item.shopName = itemDiv.$('.shop > a > span:nth-child(2)').text()
                    item.isAd = itemDiv.classes().contains('item-ad')
                    item.url = link.getAttribute("href")

                    switch (item.url.toLowerCase().split(/\//)[2].split(/\./)[-2]) {
                        case 'tmall':
                            item.market = Market.TMall
                            break
                        case 'taobao':
                            item.market = Market.Taobao
                            break
                        default:
                            println '[[ERROR]]  Unsupported url: ' + item.url
                            break
                    }

                    withNewWindow { itemDiv.$('div.title > a').click() } {
                        switch (item.market) {
                            case Market.TMall:
                                extractTmall($(), item)
                                break
                            case Market.Taobao:
                                extractTaobao($(), item)
                                break
                            default:
                                assert false
                                break
                        }
                    }

                    println JsonOutput.toJson(item)

                }

            }


        }
//        }
    }


    static void extractTaobao(Navigator navigator, Item item) {
        item.sellCnt = navigator.$('.tm-ind-sellCount .tm-count').text()
    }

    static void extractTmall(Navigator navigator, Item item) {
        item.sellCnt = navigator.$('#J_SellCounter').text()

    }
}
