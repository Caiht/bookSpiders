package cnki;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.http.message.BasicHeader;

import java.util.HashSet;

public class CnkiController {

    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "/home/IdeaProjects/Crawler4jDemo/data";
        int numberOfCrawlers = 7;
        CrawlConfig config = new CrawlConfig();
        //是否尊重网络重定向
//        config.setFollowRedirects(false);
        //抓取深度
        config.setMaxDepthOfCrawling(1);
        config.setCrawlStorageFolder(crawlStorageFolder);

        HashSet<BasicHeader> collections = new HashSet<BasicHeader>();
        collections.add(new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"));
        collections.add(new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
        collections.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
        collections.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.9"));
//        collections.add(new BasicHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8"));
//        collections.add(new BasicHeader("Connection", "keep-alive"));
        collections.add(new BasicHeader("Cookie", "ASP.NET_SessionId=us5y53rfqjy02r45v2czwj3p;LID=;RsPerPage=20;FileNameS=cnki%3A;KNS_DisplayModel=custommode"));
        config.setDefaultHeaders(collections);
        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);



        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        String string="http://222.197.165.84/kns55/brief/brief.aspx?curpage=";
        String str ="&RecordsPerPage=20&QueryID=2&ID=&turnpage=1&tpagemode=L&dbPrefix=CMFD&Fields=&DisplayMode=custommode&PageName=ASP.brief_result_aspx&sKuaKuID=2";
        for(int i=0;i<=83773;i++){

            controller.addSeed(string.concat(String.valueOf(i)).concat(str));
            System.out.println(i);
        }


        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(CnkiCrawler.class, numberOfCrawlers);
    }
}
