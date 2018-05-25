package cnki;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CnkiCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    private final static Pattern pattern = Pattern.compile("ReplaceChar1\\(ReplaceChar\\('.*'\\)");

    private final static Pattern pattern2 = Pattern.compile("ReplaceChar1\\(ReplaceChar\\(ReplaceFont\\('','.*'\\)");

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
        return !FILTERS.matcher(href).matches();
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
            System.out.println(html);
            Matcher matcher = pattern.matcher(html);
            Matcher matcher2 = pattern2.matcher(html);

//            MatchResult matchResult=matcher.toMatchResult();
            Document doc = Jsoup.parse(html);
            String title = "";
            String content = "";
            String author = "";
            String teacher = "";
            String university = "";
            String date = "";
            String type = "";
            String introduction = "";
            Date newDate;
            Connection conn = null;
            try {

                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                conn.setAutoCommit(false);
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM");
                String template = " INSERT INTO cnki_articles VALUES(?,?,?,?,?,?,?)";
                PreparedStatement statement = conn.prepareStatement(template);
                int i = 1;
                while (matcher.find() && matcher2.find()) {
                    title = matcher.group();
                    title = StringUtils.substringBetween(title, "ReplaceChar1(ReplaceChar('", "')");
                    System.out.println("title:" + title);
                    author = doc.select("#ctl00 > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td > div > ul > li:nth-child(" + i + ") > div > div.GridRightColumn > div.GridContentDiv > a:nth-child(2)").text();
                    System.out.println("author:" + author);
                    teacher = doc.select("#ctl00 > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td > div > ul > li:nth-child(" + i + ") > div > div.GridRightColumn > div.GridContentDiv > a:nth-child(3)").text();
                    System.out.println("teacher:" + teacher);
                    content = doc.select("#ctl00 > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td > div > ul > li:nth-child(" + i + ") > div > div.GridRightColumn").text();
                    university =StringUtils.substringBetween(content, "【作者基本信息】", "，");
                    System.out.println("university:" + university);
                    date = StringUtils.substringBetween(content, "【发表年期】", "期");
                    System.out.println("date:" + date);
                    type = StringUtils.substringBetween(content, university + "，", "，");
                    System.out.println("type:" + type);
                    introduction = StringUtils.substringBetween(matcher2.group(), "ReplaceChar1(ReplaceChar(ReplaceFont('','", "')");
                    System.out.println("introduction:" + introduction);
                    // 注册 JDBC 驱动
                    newDate = new Date(sDateFormat.parse(date).getTime());
                    statement.setString(1, title);
                    statement.setString(2, author);
                    statement.setString(3, teacher);
                    statement.setString(4, university);
                    statement.setDate(5, newDate);
                    statement.setString(6, type);
                    statement.setString(7, introduction);
                    statement.addBatch();
                    System.out.println("成功插入数据");
                    i++;
                }
                statement.executeBatch();
                conn.commit();
                statement.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
