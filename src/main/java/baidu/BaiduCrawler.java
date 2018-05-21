package baidu;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BaiduCrawler extends WebCrawler {

//    private final static Pattern FILTERS = Pattern.compile("\\d{4}/(\\d{1,2})/(\\d{1,2})");

    // JDBC 驱动名及数据库 URL
//    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    static final String DB_URL = "jdbc:mysql://localhost:3306/douban_db?useUnicode=true&characterEncoding=utf-8";
//
//    // 数据库的用户名与密码，需要根据自己的设置
//    static final String USER = "root";
//    static final String PASS = "";

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
        return href.startsWith("http://xueshu.baidu.com/s?wd=paperuri");
//        return  true;
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
            String title = doc.select("#dtl_l > div:nth-child(1) > h3 > a").text();
            System.out.println("title: " + title);

            String author = doc.select("#dtl_l > div:nth-child(1) > div.c_content.content_hidden > div.author_wr > p.author_text > a").text();
            System.out.println("author: " + author);

            String time = doc.select("#dtl_l > div:nth-child(1) > div.c_content.content_hidden > div.publish_wr > p.publish_text > span:nth-child(n+2)").text();
            System.out.println("time: " + time);

            String publisher = doc.select("#dtl_l > div:nth-child(1) > div.c_content.content_hidden > div.publish_wr > p.publish_text > a").text();
            System.out.println("publisher: " + publisher);

            String tag = doc.select("#dtl_r > div > div > a > i").text();
            System.out.println("tag: " + tag);

            String introduction = doc.select("#dtl_l > div:nth-child(1) > div.c_content.content_hidden > div.abstract_wr > p.abstract").text();
            System.out.println("introduction: " + introduction);
//            Connection conn = null;
//            Statement stmt = null;
//
//            // 注册 JDBC 驱动
//            try {
//                Class.forName("com.mysql.jdbc.Driver");
//                conn = DriverManager.getConnection(DB_URL, USER, PASS);
//                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//                Date newDate = new Date(sDateFormat.parse(time).getTime());
//                String template = " INSERT INTO articles VALUES(?,?,?,?,?,?)";
//                PreparedStatement statement = conn.prepareStatement(template);
//                statement.setString(1,title);
//                statement.setString(2,author);
//                statement.setDate(3, newDate);
//                statement.setString(4,publisher);
//                statement.setString(5,tag);
//                statement.setString(6,introduction);
//                statement.execute();
//                System.out.println("成功插入数据");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


        }
    }
}
