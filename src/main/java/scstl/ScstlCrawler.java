package scstl;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class ScstlCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile("\\d{4}/(\\d{1,2})/(\\d{1,2})");

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/douban_db?useUnicode=true&characterEncoding=utf-8";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "";

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
//        return href.startsWith("http://cnki.scstl.org/kcms/detail/detail.aspx?QueryID=1");
        return true;
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
            //标题
            String title = doc.select("#main > tbody > tr > td:nth-child(2)").text();
            System.out.println("title: " + title);
            //申请号
            String requestNumber = doc.select("body > table.mc_table1 > tbody > tr:nth-child(1) > td:nth-child(2)").text();
            System.out.println("requestNumber: " + requestNumber);
            //申请日
            String requestDate = doc.select("body > table.mc_table1 > tbody > tr:nth-child(1) > td:nth-child(4)").text();
            System.out.println("requestDate: " + requestDate);
            //公开号
            String publicationNumber = doc.select("body > table.mc_table1 > tbody > tr:nth-child(2) > td:nth-child(2)").text();
            System.out.println("publicationNumber: " + publicationNumber);
            //公开日
            String publicationDate = doc.select("body > table.mc_table1 > tbody > tr:nth-child(2) > td:nth-child(4)").text();
            System.out.println("publicationDate: " + publicationDate);
            //申请人
            String proposer = doc.select("body > table.mc_table1 > tbody > tr:nth-child(3) > td:nth-child(2)").text();
            System.out.println("proposer: " + proposer);
            //发明人
            String inventor = doc.select("body > table.mc_table1 > tbody > tr:nth-child(4) > td.checkItem").text();
            System.out.println("inventor: " + inventor);
            //摘要
            String introduction = doc.select("body > table.mc_table1 > tbody > tr:nth-child(7) > td.checkItem").text();
            System.out.println("introduction: " + introduction);
            //摘要
            String type = doc.select("body > table.mc_table1 > tbody > tr:nth-last-child(3) > td.checkItem").text();
            System.out.println("type: " + type);


            Connection conn = null;

            // 注册 JDBC 驱动
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date newDate1 = new Date(sDateFormat.parse(requestDate).getTime());
                Date newDate2 = new Date(sDateFormat.parse(publicationDate).getTime());
                String template = " INSERT INTO patents VALUES(?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = conn.prepareStatement(template);
                statement.setString(1,title);
                statement.setString(2,requestNumber);
                statement.setDate(3, newDate1);
                statement.setString(4,publicationNumber);
                statement.setDate(5, newDate2);
                statement.setString(6,proposer);
                statement.setString(7,inventor);
                statement.setString(8,introduction);
                statement.setString(9,type);
                statement.execute();
                System.out.println("成功插入数据");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
