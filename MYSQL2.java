package 主程序;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;
public class MYSQL2 {
    public  void Shop(Scanner scanner) {
        // MySQL数据库连接参数
        String url = "jdbc:mysql://10.34.59.149:3306/销售系统";
        String username = "wen";
        String password = "123456";

        try {
            // 建立数据库连接
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("输入选择的功能：");
            int s=scanner.nextInt();
            if(s==1)
            {
                //开设新店
                insertData(connection,scanner);
            } else if (s==2) {
                //撤离店铺
                deleteData(connection,scanner);
            } else if(s==3) {
                //寻找店铺
                selectData(connection,scanner);
            }else if(s==4) {
                //店铺调整
                adjustData(connection,scanner);
            }

            //insertData(connection);
            //撤离店铺
            //deleteData(connection);
            //寻找店铺
            //selectData(connection);
            //店铺调整
            //adjustData(connection);

            // 关闭连接
            connection.close();
        } catch (SQLException e) {
            System.out.println("连接MySQL数据库时出错：" + e.getMessage());
            e.printStackTrace(); // 打印详细的异常信息
        }
    }


    // 向表中插入数据
    public static void insertData(Connection conn,Scanner scanner) {
        PreparedStatement pst = null;

        try {
            pst = conn.prepareStatement("INSERT INTO shop  VALUES (?,?,?)");

            // 插入数据
            System.out.println("请输入要开设的商店数量：");
            int num = scanner.nextInt();
            String[] shopNames = new String[num];
            String[] locations = new String[num];
            String[] addresses = new String[num];
            scanner.nextLine();//清除回车符
            for (int i = 0; i < num; i++) {
                System.out.println("请输入第" + (i + 1) + "个开设的商店的店名、地点、地址（用空格隔开）。");
                String input = scanner.nextLine();
                String[] temp = input.split(" ");
                shopNames[i] = temp[0];
                locations[i] = temp[1];
                addresses[i] = temp[2];

            }
            for (int i = 0; i < shopNames.length; i++) {
                pst.setString(1, shopNames[i]);
                pst.setString(2, locations[i]);
                pst.setString(3, addresses[i]);
                pst.executeUpdate(); // 执行单条插入
            }

            pst.close();
            System.out.println("新开设了"+shopNames.length+"家店！");
        } catch (SQLException e) {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            scanner.close(); // 关闭 Scanner
        }
    }


    //删除数据
    public static void deleteData(Connection conn,Scanner scanner) {
        PreparedStatement pstmt = null;
        try {
                System.out.println("请输入要撤离的店的名字：");
                String shopNameToDelete = scanner.next();
                pstmt = conn.prepareStatement("DELETE FROM shop WHERE 店名 = ?");
                pstmt.setString(1, shopNameToDelete);
                int rowCount = pstmt.executeUpdate();
                if(rowCount==1){
                    System.out.println(shopNameToDelete+"已经撤离");
                }else{
                    System.out.println("出错！");
                }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //查找
    public static void selectData(Connection connection,Scanner scanner) {
        try {
            // 创建PreparedStatement对象
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM shop WHERE 店名=?");

            System.out.println("请输入要寻找的店名：");
                String shopNameToSelect = scanner.next();
                // 设置查询参数
                statement.setString(1, shopNameToSelect);
                // 执行查询并获取结果
                ResultSet resultSet = statement.executeQuery();
                // 判断结果集是否为空
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("不存在该店！");
                } else {
                    while (resultSet.next()) {
                        // 获取每一行的数据并输出
                        String name = resultSet.getString("店名");
                        String location = resultSet.getString("地点");
                        String address = resultSet.getString("地址");
                        System.out.println("店名: " + name + ", 地点: " + location + ", 地址: " + address);
                    }
                }

            // 关闭资源
            statement.close();
        } catch (SQLException e) {
            System.out.println("查找数据出错：" + e.getMessage());
            e.printStackTrace();
        }
    }

    //更新数据
    public static void adjustData(Connection connection,Scanner scanner){
        try {


                System.out.println("要调整的店的店名为：");
                String shopNeedChange = scanner.next();
                // 检查店名是否存在
                PreparedStatement ifExistStatement = connection.prepareStatement("SELECT * FROM shop WHERE 店名=?");
                ifExistStatement.setString(1, shopNeedChange);
                ResultSet resultSet = ifExistStatement.executeQuery();
                if (!resultSet.next()) {
                    System.out.println("不存在该店!");
                    resultSet.close();
                    ifExistStatement.close();
                }
                resultSet.close();
                ifExistStatement.close();

                System.out.println("要调整的部分为：店名 地点 地址");
                String needToChange = scanner.next();
                System.out.println("新的 " + needToChange + " 为：");
                String changeTo = scanner.next();

                // 构造更新SQL语句
                String sql = "UPDATE shop SET " + needToChange + "=? WHERE 店名=?";
                PreparedStatement statement = connection.prepareStatement(sql);

                // 设置参数
                statement.setString(1, changeTo);
                statement.setString(2, shopNeedChange);

                // 执行更新操作
                int rowsAffected = statement.executeUpdate();
                System.out.println("更新了 " + rowsAffected + " 行。");

                // 关闭资源
                statement.close();

        } catch (SQLException e) {
            System.out.println("更新时出错：" + e.getMessage());
            e.printStackTrace();
        }
    }

}