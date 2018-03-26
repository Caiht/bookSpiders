package transform;

import java.sql.*;
import java.text.SimpleDateFormat;

public class DateTransform {

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/douban_db";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "";

     public static void main(String[] args) throws Exception {
         Connection conn = null;
         Statement stmt = null;
         int i=0;
         try{
             // 注册 JDBC 驱动
             Class.forName("com.mysql.jdbc.Driver");

             // 打开链接
             System.out.println("连接数据库...");
             conn = DriverManager.getConnection(DB_URL,USER,PASS);

             // 执行查询
             System.out.println(" 实例化Statement对象...");
             stmt = conn.createStatement();
             String sql;
             sql = "SELECT * FROM allbooks";
             ResultSet rs = stmt.executeQuery(sql);

             // 展开结果集数据库
             while(rs.next()){
                 // 通过字段检索
                 String title = rs.getString("title");
                 String author = rs.getString("author");
                 String isbn= rs.getString("isbn");
                 String time=rs.getString("time");
                 String[] nums = time.split("\\D+");
                 if(nums.length>=3){
                     time=nums[1]+"-"+nums[2];
                 }else{
                     time=nums[1]+"-1";
                 }
                 SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM");
                 Date newDate =new Date(sDateFormat.parse(time).getTime());
                 String template = " UPDATE allbooks SET new_date=? WHERE isbn=? ";
                 PreparedStatement statement = conn.prepareStatement (template);
                 statement.setDate(1,newDate);
                 statement.setString(2,isbn);
                 statement.execute();
                 // 输出数据
//                 System.out.print("题目: " + title);
//                 System.out.print(", 作者: " + author);
//                 System.out.print("\n");
                 i++;
             }
             // 完成后关闭
             rs.close();
             stmt.close();
             conn.close();
         }catch(SQLException se){
             // 处理 JDBC 错误
             se.printStackTrace();
         }catch(Exception e){
             // 处理 Class.forName 错误
             e.printStackTrace();
         }finally{
             System.out.println("数据总条数"+i);
             // 关闭资源
             try{
                 if(stmt!=null) stmt.close();
             }catch(SQLException se2){
             }// 什么都不做
             try{
                 if(conn!=null) conn.close();
             }catch(SQLException se){
                 se.printStackTrace();
             }
         }
         System.out.println("Goodbye!");
     }

}
