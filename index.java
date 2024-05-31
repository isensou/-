package 主程序;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;

public class index {
    public  void Index() {
        String url = "jdbc:mysql://10.34.59.149:3306/销售系统";
        String username = "wen";
        String password = "123456";
        Scanner scanner = new Scanner(System.in);
        // MySQL数据库连接参数
        try {
            // 建立数据库连接
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("请输入对销售表进行的操作:\n1-插入数据\t2-删除数据\t3-查找数据\t4-更新数据\t0-退出");
            int s=scanner.nextInt();
            switch (s) {
                case 1:
                    insertData(connection, scanner);
                    break;
                case 2:
                     deleteData(scanner);
                     break;
                case 3:
                      selectEmployee(scanner);
                      break;
                case 4:
                      updateEmployee(connection);
                      break;
                case 5:return;
                default:
                    System.out.print("更多功能尚在开发中，请尽情期待！");
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("连接MySQL数据库时出错：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 向表中插入数据
    public static void insertData(Connection conn,Scanner scanner) {
        PreparedStatement pst = null;

        try {
            pst = conn.prepareStatement("INSERT INTO employee VALUES (?,?,?,?,?,?)");

            // 插入数据
            System.out.println("请输入新店员的数量：");
            int num = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            for (int i = 0; i < num; i++) {
                System.out.println("请输入第" + (i + 1) + "店员的信息（工号、姓名、联系电话、工作店名、工资、聘用时间）");
                System.out.print("工号: ");
                long eno = scanner.nextLong();
                System.out.print("姓名: ");
                String name = scanner.next();
                System.out.print("联系电话: ");
                String phoneNumber= scanner.next();
                System.out.print("店名: ");
                String shopName = scanner.next();
                System.out.print("工资: ");
                Double salary = scanner.nextDouble(); // 将工资字段的数据类型改为 double
                System.out.print("聘用时间（格式：yyyy-MM-dd）: ");
                String time = scanner.next();

                pst.setLong(1, eno);
                pst.setString(2, name);
                pst.setString(3, phoneNumber);
                pst.setString(4, shopName);
                pst.setDouble(5, salary);
                pst.setString(6, time);

                pst.executeUpdate();
            }

            System.out.println("成功插入了" + num + "条数据！");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //删除店员
    public static void deleteData(Scanner scanner) {
        String url = "jdbc:mysql://10.34.59.149:3306/销售系统";
        String username = "wen";
        String password = "123456";
        System.out.println("要删除的店员工号为：");
        int attribute=scanner.nextInt();

        String sql = "DELETE FROM employee WHERE 工号 = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, attribute);
            int rowsAffected = pst.executeUpdate();
            System.out.println("成功删除了" + rowsAffected + "条记录。");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查找表中的的数据
    public  static void selectEmployee(Scanner scanner) {
        String url = "jdbc:mysql://10.34.59.149:3306/销售系统";
        String username = "wen";
        String password = "123456";
        try (Connection connection = DriverManager.getConnection(url, username, password))
        {
            // 创建 PreparedStatement 对象来执行带参数的 SQL 查询
            String sql;
            System.out.println("请选择功能：");
            System.out.println("1.已知店员工号查询该店员信息");
            System.out.println("2.已知店员联系电话查询该店员信息");
            System.out.println("3.已知店员姓名查询该店员信息");
            System.out.println("4.已知店员聘用时间查询该店员信息");
            System.out.println("5.查询在某个商店下所有的店员");
            int x = Integer.parseInt(scanner.next());
            if (x == 1) {
                System.out.println("请输入要查询的店员工号：");
                int eno = scanner.nextInt();
                sql = "SELECT * FROM `employee` WHERE 工号=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, eno);
                    executeAndPrintResults(preparedStatement);
                }
                return;

            } else if (x == 2) {
                System.out.println("请输入要查询的店员的联系电话：");
                String phoneNumber= scanner.next();
                sql = "SELECT * FROM `employee` WHERE 联系电话=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, phoneNumber);
                    executeAndPrintResults(preparedStatement);
                }
                return;

            } else if (x == 3) {
                System.out.println("请输入要查询的店员的姓名：");
                String name = scanner.next();
                sql = "SELECT * FROM `employee` WHERE 姓名=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, name);
                    executeAndPrintResults(preparedStatement);
                }
                return;

            }else if (x == 4) {
                System.out.println("请输入要查询的店员的聘用时间：");
                String time = scanner.next();
                sql = "SELECT * FROM `employee` WHERE 联系电话=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, time);
                    executeAndPrintResults(preparedStatement);
                }
                return;
            }else if (x == 5) {
                System.out.println("请输入商店：");
                String shopName = scanner.next();
                sql = "SELECT * FROM `employee` WHERE 店名=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, shopName);
                    executeAndPrintResults(preparedStatement);
                }
                return;
            }else {
                System.out.println("输入选项无效，请重新选择。");
                return;
            }
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
    }
    private static void executeAndPrintResults(PreparedStatement preparedStatement) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //更新店员信息
    public static void updateEmployee(Connection conn) {
        PreparedStatement pst = null;
        Scanner scanner = new Scanner(System.in);
        try {
            pst = conn.prepareStatement("UPDATE employee SET 姓名=?, 联系电话=?, 店名=?, 工资=?,聘用时间=?  WHERE 工号=?");
            System.out.println("您想更新几条员工记录？");
            int num = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            for (int i = 0; i < num; i++) {
                System.out.println("请输入要更新员工的工号：");
                System.out.println("请输入第" + (i + 1) + "名店员的更新后的信息（工号、姓名、联系电话、工作店名、工资、聘用时间）");
                System.out.print("工号： ");
                int eno = scanner.nextInt();
                System.out.print("姓名： ");
                String name = scanner.next();
                System.out.print("联系电话： ");
                String phoneNumber= scanner.next();
                System.out.print("店名： ");
                String shopName = scanner.next();
                System.out.print("工资： ");
                Double salary = scanner.nextDouble();
                System.out.print("聘用时间（格式：yyyy-MM-dd）： ");
                String time = scanner.next();

                pst.setLong(6, eno);
                pst.setString(1, name);
                pst.setString(2, phoneNumber);
                pst.setString(3, shopName);
                pst.setDouble(4, salary);
                pst.setString(5, time);

                pst.executeUpdate();
            }

            System.out.println("成功更新了" + num + "条数据！");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            scanner.close(); // 关闭 Scanner
        }
    }

}






