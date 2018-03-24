import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {



    private final static Pattern FILTERS = Pattern.compile("^\\d+?.*$");

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();

        return href.startsWith("http://product.china-pub.com/")&&href.replace("http://product.china-pub.com/","").startsWith("[0-9]");
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            Document doc = Jsoup.parse(html);
            String title =doc.select("#right > div.pro_book > div.pro_book > h1").text();
            System.out.println("title: " + title);
            String author =doc.select("#con_a_1 > div:nth-child(1) > ul > li:nth-child(1) > a > strong").text();
            System.out.println("author: " + author);

            String price =doc.select("#right > div.pro_book > div.pro_buy_intr > ul > li:nth-child(1) > span").text();
            System.out.println("price: " + price);

            String time =doc.select("#con_a_1 > div:nth-child(1) > ul > li:nth-child(4)").text();
            System.out.println("time: " + time);

            String publisher =doc.select("#con_a_1 > div:nth-child(1) > ul > li:nth-child(2) > a").text();
            System.out.println("publisher: " + publisher);

            String tag =doc.select("#con_a_1 > div:nth-child(1) > ul > li:nth-child(10) > span > a:nth-child(2)").text();
            System.out.println("tag: " + tag);

            String introduction =doc.select("#con_a_1 > div:nth-child(2) > div").text();
            System.out.println("introduction: " + introduction);

            String isbn =doc.select("#con_a_1 > div:nth-child(1) > ul > li:nth-child(4) > strong").text();
            System.out.println("isbn: " + isbn);
        }
    }
}