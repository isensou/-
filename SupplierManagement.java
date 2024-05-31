package 主程序;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;
//2200310621梁光朝
public class SupplierManagement {
    public  void Supplier() {
        String url = "jdbc:mysql://10.34.59.149:3306/销售系统";
        String user = "wen";
        String password = "123456";

        try {
            // 连接到数据库
            Connection connection = DriverManager.getConnection(url, user, password);

            // 创建 Statement 对象来执行 SQL 操作
            Statement statement = connection.createStatement();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("请输入操作类型 (1: 添加供应商, 2: 删除供应商, 3: 修改供应商, 4: 查询供应商,5:退出供应商信息管理): ");
                int operation = Integer.parseInt(scanner.nextLine());

                switch (operation) {
                    case 1:
                        addSupplier(statement, scanner);
                        break;
                    case 2:
                        deleteSupplier(statement, scanner);
                        break;
                    case 3:
                        changeSupplier(statement, scanner);
                        break;
                    case 4:
                        querySupplier(statement, scanner);
                    case 5:
                        return;
                    default:
                        System.out.println("输入错误，请重新输入！");
                        break;
                }
            }

            // 关闭连接
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    //添加供应商
    private static void addSupplier(Statement statement, Scanner scanner) throws SQLException {

        System.out.println("请输入供应商编号: ");
        int id = Integer.parseInt(scanner.nextLine());

// 先检查是否已存在相同id的供应商
        String checkQuery = "SELECT COUNT(*) as count FROM `supplier` WHERE 供应商编号 = " + id;
        java.sql.ResultSet rs = statement.executeQuery(checkQuery);
        rs.next();
        int count = rs.getInt("count");

        if (count > 0) {
            System.out.println("该供应商编号已存在，无法插入新记录。");
        } else {
            System.out.println("请输入供应商名称: ");
            String supplierName = scanner.nextLine();

            System.out.println("请输入联系电话: ");
            String telephoneNumber = scanner.nextLine();

// 执行插入操作
            String insertQuery = "INSERT INTO `supplier` (供应商编号, 名称, 联系电话) VALUES (" + id + ", '" + supplierName + "', '" + telephoneNumber + "')";
            statement.executeUpdate(insertQuery);
            System.out.println("插入成功！");
        }

    }

    private static void deleteSupplier(Statement statement, Scanner scanner) throws SQLException {
        System.out.println("请输入需要删除的供应商编号: ");
        int id = Integer.parseInt(scanner.nextLine());

// 先检查是否存在该供应商记录
        String checkQuery = "SELECT COUNT(*) as count FROM `supplier` WHERE 供应商编号 = " + id;
        java.sql.ResultSet rs = statement.executeQuery(checkQuery);
        rs.next();
        int count = rs.getInt("count");

        if (count == 0) {
            System.out.println("该供应商编号不存在，无法删除。");
        } else {
// 执行删除操作
            String deleteQuery = "DELETE FROM `supplier` WHERE 供应商编号 = " + id;
            statement.executeUpdate(deleteQuery);
            System.out.println("删除成功！");
        }
    }

    private static void changeSupplier(Statement statement, Scanner scanner) throws SQLException {
        System.out.println("请输入需要修改的供应商编号: ");
        int id = Integer.parseInt(scanner.nextLine());

// 先检查是否存在该供应商记录
        String checkQuery = "SELECT COUNT(*) as count FROM `supplier` WHERE 供应商编号 = " + id;
        java.sql.ResultSet rs = statement.executeQuery(checkQuery);
        rs.next();
        int count = rs.getInt("count");

        if (count == 0) {
            System.out.println("该供应商编号不存在，无法修改。");
        } else {
            System.out.println("请输入需要修改的属性 (名称或联系电话): ");
            String property = scanner.nextLine();
            String updateQuery = "";

            if (property.equals("名称")) {
                System.out.println("请输入新的供应商名称: ");
                String newName = scanner.nextLine();
                updateQuery = "UPDATE `supplier` SET 名称 = '" + newName + "' WHERE 供应商编号 = " + id;
            } else if (property.equals("联系电话")) {
                System.out.println("请输入新的联系电话: ");
                String newTelephone = scanner.nextLine();
                updateQuery = "UPDATE `supplier` SET 联系电话 = '" + newTelephone + "' WHERE 供应商编号 = " + id;
            } else {
                System.out.println("输入的属性不正确，修改失败。");
            }

            if (!updateQuery.isEmpty()) {
// 执行修改操作
                statement.executeUpdate(updateQuery);
                System.out.println("修改成功！");
            }
        }
    }

    private static void querySupplier(Statement statement, Scanner scanner) throws SQLException {
        System.out.println("请输入需要查询的供应商编号或名称: ");
        String Get = scanner.nextLine();

        String selectQuery = "SELECT * FROM `supplier`";
        ResultSet rs = statement.executeQuery(selectQuery);
        while (rs.next()) {
            int id = rs.getInt("供应商编号");
            String suppliername = rs.getString("名称");
            String telephonenumber = rs.getString("联系电话");


            try {
                int getid = Integer.parseInt(Get);
// 判断是否为数字，如果是数字，则进行相应处理
                if (id == getid) {
                    System.out.println("供应商编号: " + id + "\t" + "名称: " + suppliername + "\t" + "联系电话：" + telephonenumber);
                }
            } catch (NumberFormatException e) {
// 如果不是数字，则直接作为字符串处理
                if (Objects.equals(suppliername, Get)) {
                    System.out.println("供应商编号: " + id + "\t" + "名称: " + suppliername + "\t" + "联系电话：" + telephonenumber);
                }
            }
        }
    }
}