package 主程序;
import java.util.Objects;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.math.*;
//2200310617黄天天
public class Mysql3 {
    String url = "jdbc:mysql://10.34.59.149:3306/销售系统";
    String user = "wen";
    String password = "123456";
    public void select()
    {
        try {
            // 连接到数据库
            Connection connection = DriverManager.getConnection(url, user, password);

            // 创建Statement对象
            Statement statement = connection.createStatement();

            // 查询数据
            ResultSet resultSet = statement.executeQuery("SELECT * FROM sell ORDER BY 商品编号");
            System.out.println("商品编号\t工号\t\t店名\t\t销售数量\t 销售时间");
            // 遍历结果集
            while (resultSet.next()) {
                // 获取每行数据的字段值
                int commodity_id = resultSet.getInt("商品编号");
                String name = resultSet.getString("店名");
                int id = resultSet.getInt("工号");
                int num=resultSet.getInt("销售数量");
                String date =resultSet.getString("销售时间");
                // 输出到控制台
                System.out.println(commodity_id+"\t\t"+id+"\t"+name+"\t "+num+"\t\t " +
                        ""+date);
            }

            // 关闭ResultSet、Statement和Connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    public void insert() {
        int cs=0; //用作确认其他关联表中是否存在相关插入信息
        // 使用try-with-resources语句自动关闭连接
        Scanner scanner = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            String tips = "请输入商品编号，工号，店名，销售数量，销售时间：";
            System.out.println(tips);
            int commodity_id =scanner.nextInt();
            int id=scanner.nextInt();
            String name=scanner.next();
            int num=scanner.nextInt();
            String date = scanner.next();

            // 创建Statement对象
            Statement statement = connection.createStatement();
            // 查询数据
            ResultSet resultSet = statement.executeQuery("SELECT  c.商品编号, e.工号, s.店名  \n" +
                    "                    FROM  shop s\n" +
                    "                    JOIN employee e  ON S.店名=e.店名\n" +
                    "                    CROSS JOIN  commodity c \n" +
                    "                    LEFT JOIN sell se ON c.商品编号 = se.商品编号 AND e.工号 = se.工号 AND s.店名 = se.店名  \n" +
                    "                    WHERE se.商品编号 IS NULL;");//进行三表的连接，确保插入时数据真实存在且合理

            while(resultSet.next() )
            {
                //resultSet1.next();
                int temp=resultSet.getInt("商品编号");
                int temp1=resultSet.getInt("工号");
                String temp2 = resultSet.getString("店名");

                if (temp == commodity_id && temp1==id && temp2.equals(name))
                {

                    String insertQuery1 = "INSERT INTO sell (商品编号,工号,店名,销售数量,销售时间) VALUES (?,?,?,?,?)";

                    // 创建 PreparedStatement 对象
                    try (PreparedStatement preparedStatement1 = connection.prepareStatement(insertQuery1)) {
                        // 设置插入参数

                        preparedStatement1.setInt(1, commodity_id); // 设置商品编号
                        preparedStatement1.setInt(2,id ); // 设置工号
                        preparedStatement1.setString(3, name);//设置店名
                        preparedStatement1.setInt(4, num);//设置销售数量
                        preparedStatement1.setString(5, date);//设置销售时间

                        // 执行插入操作
                        preparedStatement1.executeUpdate();
                        System.out.println("插入成功!");
                        preparedStatement1.close();
                    } catch (SQLException e) {
                        // 处理 PreparedStatement 异常
                        e.printStackTrace();
                    }
                    cs=1;
                    break;
                }
            }
            if (cs==0)
            {
                System.out.println("输入的信息不存在或重复输入！");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // 处理连接异常
            e.printStackTrace();
        }
    }

    public void update()
    {

        try {
            // 连接到数据库
            Connection connection = DriverManager.getConnection(url, user, password);

            // 创建Statement对象
            Statement statement = connection.createStatement();

            Scanner scanner = new Scanner(System.in);
            String tips = "请输入需要更改数据的商品编号，工号，店名：";
            System.out.println(tips);
            int commodity_id =scanner.nextInt();
            int id=scanner.nextInt();
            String name=scanner.next();

            // 查询数据
            ResultSet resultSet = statement.executeQuery("select 商品编号,工号,店名 from sell");
            while(resultSet.next())
            {
                if(resultSet.getInt("商品编号")==commodity_id && resultSet.getInt("工号")
                        == id && Objects.equals(resultSet.getString("店名"), name))
                {
                    System.out.println("请输入更改序号：\n1-更改商品编号\t2-更改工号\t3-更改店名\t" +
                            "4-更改销售数量\t5-更改销售时间");
                    int choice=scanner.nextInt();
                    switch (choice)
                    {
                        case 1:
                            System.out.println("请输入更改后的商品编号：");
                            int new_cid =scanner.nextInt();
                            if(judge(new_cid,id,name))
                            {
                                String updateQuery = "UPDATE sell SET 商品编号 =" + new_cid+" where 商品编号="+
                                        commodity_id+" and 工号= "+id+" and 店名= '"+name+"';";
                                // 执行更新操作
                                statement.executeUpdate(updateQuery);
                                System.out.println("更新成功！");

                            }else {
                                System.out.println("输入信息不存在或重复录入信息!");
                            }
                            break;
                        case 2:
                            System.out.println("请输入更改后的工号：");
                            int new_id =scanner.nextInt();
                            if(judge(commodity_id,new_id,name))
                            {
                                String updateQuery = "UPDATE sell SET 工号 =" + new_id+" where 商品编号="+
                                        commodity_id+" and 工号= "+id+" and 店名= '"+name+"';";
                                // 执行更新操作
                                statement.executeUpdate(updateQuery);
                                System.out.println("更新成功！");
                            }else {
                                System.out.println("输入信息不存在或重复录入信息!");
                            }
                            break;
                        case 3:
                            System.out.println("请输入更改后的店名：");
                            String new_name =scanner.next();
                            if(judge(commodity_id,id,new_name))
                            {
                                String updateQuery = "UPDATE sell SET 店名 =" + new_name+" where 商品编号="+
                                        commodity_id+" and 工号= "+id+" and 店名= '"+name+"';";
                                // 执行更新操作
                                statement.executeUpdate(updateQuery);
                                System.out.println("更新成功！");
                            }else {
                                System.out.println("输入信息不存在或重复录入信息!");
                            }
                            break;
                        case 4:
                            System.out.println("请输入更改后的销售数量：");
                            int new_num =scanner.nextInt();
                            String updateQuery = "UPDATE sell SET 销售数量 =" + new_num+" where 商品编号="+
                                    commodity_id+" and 工号= "+id+" and 店名= '"+name+"';";
                            // 执行更新操作
                            statement.executeUpdate(updateQuery);
                            System.out.println("更新成功！");
                            break;
                        case 5:
                            System.out.println("请输入更改后的销售日期：");
                            String new_date =scanner.next();
                            String updateQuery1 = "UPDATE sell SET 销售日期 =" + new_date+" where 商品编号="+
                                    commodity_id+" and 工号= "+id+" and 店名= '"+name+"';";
                            // 执行更新操作
                            statement.executeUpdate(updateQuery1);
                            System.out.println("更新成功！");
                            break;
                        default:
                            System.out.println("非法输入！");

                    }
                    break;
                }
            }



            statement.close();
            connection.close();
        }catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    public  void delete()
    {

        try {
            int cs=0;
            // 连接到数据库
            Connection connection = DriverManager.getConnection(url, user, password);

            // 创建Statement对象
            Statement statement = connection.createStatement();

            Scanner scanner = new Scanner(System.in);
            String tips = "请输入需要删除数据的商品编号，工号，店名：";
            System.out.println(tips);
            int commodity_id = scanner.nextInt();
            int id = scanner.nextInt();
            String name = scanner.next();

            // 查询数据
            ResultSet resultSet = statement.executeQuery("select 商品编号,工号,店名 from sell");
            while(resultSet.next())
            {
                int temp=resultSet.getInt("商品编号");
                int temp1=resultSet.getInt("工号");
                String temp2 = resultSet.getString("店名");

                if (temp == commodity_id && temp1==id && temp2.equals(name))
                {
                    // 执行删除操作

                    String deleteQuery = "DELETE FROM sell WHERE 商品编号 = "+commodity_id+" AND 工号 = "+id+" AND 店名 = '"+name+"';";
                    statement.executeUpdate(deleteQuery);
                    System.out.println("删除成功！");
                    cs=1;
                    break;

                }
            }
            if(cs!=1)
            {
                System.out.println("输入的信息不存在！");
            }




            // 关闭Statement和Connection
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    public boolean judge(int new_cid,int new_id,String new_name)//判断函数，用于判断update新数据后是否存在于employee与shop表中
    {
        try {
            // 连接到数据库
            Connection connection = DriverManager.getConnection(url, user, password);

            // 创建Statement对象
            Statement statement = connection.createStatement();

            // 查询数据
            ResultSet resultSet = statement.executeQuery("SELECT  c.商品编号, e.工号, s.店名  \n" +
                    "                    FROM  shop s\n" +
                    "                    JOIN employee e  ON S.店名=e.店名\n" +
                    "                    CROSS JOIN  commodity c \n" +
                    "                    LEFT JOIN sell se ON c.商品编号 = se.商品编号 AND e.工号 = se.工号 AND s.店名 = se.店名  \n" +
                    "                    WHERE se.商品编号 IS NULL;" );
            // 遍历结果集
            while (resultSet.next()) {
                // 获取每行数据的字段值
                int commodity_id = resultSet.getInt("商品编号");
                String name = resultSet.getString("店名");
                int id = resultSet.getInt("工号");
                if(Objects.equals(new_name, name) && new_id ==id && new_cid ==commodity_id)
                {
                    return true;
                }

            }

            // 关闭ResultSet、Statement和Connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
        return false;
    }



}
