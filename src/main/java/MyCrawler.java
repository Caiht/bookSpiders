import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {



    private final static Pattern FILTERS = Pattern.compile("^\\d{7}.*$");

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

        return href.startsWith("http://www.bookschina.com/")&&FILTERS.matcher(href.replace("http://www.bookschina.com/","")).matches();
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
            String title =doc.select("div.content > div:nth-child(3) > div > div.bookDetaiWrap > div.bookInfo > div.padLeft10 > h1").text();
            System.out.println("title: " + title);
            String author =doc.select("div.content > div:nth-child(3) > div > div.bookDetaiWrap > div.bookInfo > div.padLeft10 > div.author > a").text();
            System.out.println("author: " + author);

            String price =doc.select(" div.content > div:nth-child(3) > div > div.bookDetaiWrap > div.bookInfo > div.priceWrap > span.sellPrice").text();
            System.out.println("price: " + price);

            String time =doc.select("div.content > div:nth-child(3) > div > div.bookDetaiWrap > div.bookInfo > div.padLeft10 > div.publisher > i").text();
            System.out.println("time: " + time);

            String publisher =doc.select("div.content > div:nth-child(3) > div > div.bookDetaiWrap > div.bookInfo > div.padLeft10 > div.publisher > span:nth-child(1)").text();
            System.out.println("publisher: " + publisher);

            String tag =doc.select("#copyrightInfor > ul > li.kind > div:nth-child(2) > a").text();
            System.out.println("tag: " + tag);

            String introduction =doc.select("#brief > p").text();
            System.out.println("introduction: " + introduction);

            String isbn =doc.select("#copyrightInfor > ul > li:nth-child(1)").text();
            System.out.println("isbn: " + isbn);
        }
    }
}