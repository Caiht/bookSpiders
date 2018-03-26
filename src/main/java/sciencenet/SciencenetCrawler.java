package sciencenet;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class SciencenetCrawler extends WebCrawler {


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
        String href = url.getURL();
        return href.startsWith("http://doc.sciencenet.cn/DocInfo.aspx?id=");
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
            String title =doc.select("#ct > div.hmleft > div.hmleft1 > h1").text();
            System.out.println("title: " + title);
            String author =doc.select("#ct > div.hmleft > div:nth-child(3)").text();



            author= StringUtils.substringBefore(author.substring(4), "期刊名称");
            System.out.println("author: " + author);

            String time =doc.select("#ct > div.hmleft > div.hmleft1").text();
            System.out.println("time: " + time);

            String publisher =doc.select("#ct > div.hmleft > div:nth-child(3) > br:nth-child(4)").text();
            System.out.println("publisher: " + publisher);

            String tag =doc.select("#ct > div.hmleft > div:nth-child(3) > br:nth-child(8)").text();
            System.out.println("tag: " + tag);

            String introduction =doc.select("#ct > div.hmleft > div.hmleft1 > p").text();
            System.out.println("introduction: " + introduction);

        }
    }
}
