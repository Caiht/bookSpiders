import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.util.Random;

public class Controller {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "/data/crawl/root";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();

        config.setMaxDepthOfCrawling(4);
        config.setCrawlStorageFolder(crawlStorageFolder);
        Random random = new Random();
        //设置请求的频率
        config.setPolitenessDelay(random.nextInt(2000%1001)+1000);
        /*
         * 这里可以设置代理
         * config.setProxyHost("proxyserver.example.com");  // 代理地址
         * config.setProxyPort(8080); // 代理端口
         *
         * 如果使用代理，也可以设置身份认证  用户名和密码
         * config.setProxyUsername(username); config.getProxyPassword(password);
         */

        /*
         * 这个配置假如设置成true，当一个爬虫突然终止或者奔溃，我们可以恢复；
         * 默认配置是false；推荐用默认配置，假如设置成true，性能会大打折扣；
         */
        config.setResumableCrawling(false);
        /*
         * 是否爬取二进制文件，比如图片，PDF文档，视频之类的东西 这里设置false 不爬取
         * 默认值true，爬取
         */
        config.setIncludeBinaryContentInCrawling(false);
        /*
         * 实例化爬虫控制器
         */
        PageFetcher pageFetcher = new PageFetcher(config);// 实例化页面获取器
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();// 实例化爬虫机器人配置 比如可以设置 user-agent
        robotstxtConfig.setUserAgentName("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * 配置爬虫种子页面，就是规定的从哪里开始爬，可以配置多个种子页面
         */
        controller.addSeed("http://www.china-pub.com/browse/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
    }
}