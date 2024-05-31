package 主程序;

import java.sql.*;
import java.util.Scanner;
//2200310621梁光朝
public class SupplyManagement {

    public  void Supply() {
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
                System.out.println("请输入操作类型 (1: 添加供应记录, 2: 删除供应记录, 3: 修改供应记录, 4: 查询供应记录，-1:退出): ");
                int operation = Integer.parseInt(scanner.nextLine());

                if (operation == -1) {
                    break;
                }
                switch (operation) {
                    case 1:
                        addSupply(statement, scanner, connection);
                        break;
                    case 2:
                        deleteSupply(statement, scanner);
                        break;
                    case 3:
                        changeSupply(statement, scanner);
                        break;
                    case 4:
                        querySupply(statement, scanner);
                        break;
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

    private static void addSupply(Statement statement, Scanner scanner, Connection connection) throws SQLException {
        // 实现增加功能
        // 设置自动提交为false，以便进行事务处理

        System.out.println("请输入店名: ");
        String storeName = scanner.nextLine();

        System.out.println("请输入商品编号: ");
        int productId = Integer.parseInt(scanner.nextLine());

        System.out.println("请输入供应商编号: ");
        int supplierId = Integer.parseInt(scanner.nextLine());

        // 检查这些外键是否在相关表中存在
        boolean foreignKeysExist = checkForeignKeysExist(connection, storeName, productId, supplierId);

        if (foreignKeysExist) {
            System.out.println("开始插入供应记录...");

            System.out.println("请输入供应数量: ");
            int supplyQuantity = Integer.parseInt(scanner.nextLine());

            System.out.println("请输入供应日期: ");
            String supplyDate = scanner.nextLine();

            // 执行插入操作
            String insertQuery = "INSERT INTO `supply` (店名, 商品编号, 供应商编号, 供应数量, 供应日期) VALUES ('" + storeName + "', " + productId + ", " + supplierId + ", " + supplyQuantity + ", '" + supplyDate + "')";
            statement.executeUpdate(insertQuery);

            // 提交事务


            System.out.println("插入成功！");
        } else {
            System.out.println("外键在相关表中不存在，无法插入供应记录。");
        }
    }

    private static void deleteSupply(Statement statement, Scanner scanner) throws SQLException {
        // 实现删除功能
        System.out.println("请输入店名: ");
        String storeName = scanner.nextLine();

        System.out.println("请输入商品编号: ");
        int productId = Integer.parseInt(scanner.nextLine());

        System.out.println("请输入供应商编号: ");
        int supplierId = Integer.parseInt(scanner.nextLine());

        // 执行删除操作
        String deleteQuery = "DELETE FROM `supply` WHERE 店名 = '" + storeName + "' AND 商品编号 = " + productId + " AND 供应商编号 = " + supplierId;
        int rowsAffected = statement.executeUpdate(deleteQuery);

        if (rowsAffected > 0) {
            System.out.println("删除成功！");

        } else {
            System.out.println("未找到符合条件的供应记录，无法删除.");
        }
    }

    private static void changeSupply(Statement statement, Scanner scanner) throws SQLException {
        System.out.println("请输入需要修改的供应记录的店名、商品编号和供应商编号（用空格分隔）: ");
        String[] inputs = scanner.nextLine().split(" ");
        String storeName = inputs[0];
        int productId = Integer.parseInt(inputs[1]);
        int supplierId = Integer.parseInt(inputs[2]);

        // 先检查是否存在该供应记录
        String checkQuery = "SELECT COUNT(*) as count FROM `supply` WHERE 店名 = '" + storeName + "' AND 商品编号 = " + productId + " AND 供应商编号 = " + supplierId;
        ResultSet rs = statement.executeQuery(checkQuery);
        rs.next();
        int count = rs.getInt("count");

        if (count == 0) {
            System.out.println("该供应记录不存在，无法修改。");
        } else {
            System.out.println("请输入需要修改的属性 (供应数量或供应日期): ");
            String property = scanner.nextLine();
            String updateQuery = "";

            if (property.equals("供应数量")) {
                System.out.println("请输入新的供应数量: ");
                int newSupplyQuantity = Integer.parseInt(scanner.nextLine());
                updateQuery = "UPDATE `supply` SET 供应数量 = " + newSupplyQuantity + " WHERE 店名 = '" + storeName + "' AND 商品编号 = " + productId + " AND 供应商编号 = " + supplierId;
            } else if (property.equals("供应日期")) {
                System.out.println("请输入新的供应日期: ");
                String newSupplyDate = scanner.nextLine();
                updateQuery = "UPDATE `supply` SET 供应日期 = '" + newSupplyDate + "' WHERE 店名 = '" + storeName + "' AND 商品编号 = " + productId + " AND 供应商编号 = " + supplierId;
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
    private static void querySupply(Statement statement, Scanner scanner) throws SQLException {
        // 实现查询功能
        System.out.println("请输入店名（输入0表示无参）: ");
        String storeName = scanner.nextLine();

        System.out.println("请输入商品编号（输入0表示无参）: ");
        int productId = Integer.parseInt(scanner.nextLine());

        System.out.println("请输入供应商编号（输入0表示无参）: ");
        int supplierId = Integer.parseInt(scanner.nextLine());

        String query = "SELECT * FROM `supply` WHERE 1=1";

        if (!"0".equals(storeName)) {
            query += " AND 店名 = '" + storeName + "'";
        }

        if (productId!= 0) {
            query += " AND 商品编号 = " + productId;
        }

        if (supplierId!= 0) {
            query += " AND 供应商编号 = " + supplierId;
        }

        // 执行查询
        ResultSet resultSet = statement.executeQuery(query);

        // 遍历结果集
        while (resultSet.next()) {
            // 处理每一行数据
            supplierId = resultSet.getInt("供应商编号");
            int supplyQuantity = resultSet.getInt("供应数量");
            String supplyDate = resultSet.getString("供应日期");
            System.out.println("供应商编号: " + supplierId + "\t商品编号: " + productId + "\t店名: " + storeName + "\t供应数量: " + supplyQuantity + "\t供应日期: " + supplyDate);
        }
    }

    private static boolean checkForeignKeysExist(Connection connection, String storeName, int productId, int supplierId) throws SQLException {
        Statement statement = connection.createStatement();
        String checkShopQuery = "SELECT COUNT(*) as count FROM `shop` WHERE 店名 = '" + storeName + "'";
        java.sql.ResultSet shopRs = statement.executeQuery(checkShopQuery);
        shopRs.next();
        int shopCount = shopRs.getInt("count");

        String checkCommodityQuery = "SELECT COUNT(*) as count FROM `commodity` WHERE 商品编号 = " + productId;
        java.sql.ResultSet commodityRs = statement.executeQuery(checkCommodityQuery);
        commodityRs.next();
        int commodityCount = commodityRs.getInt("count");

        String checkSupplierQuery = "SELECT COUNT(*) as count FROM `supplier` WHERE 供应商编号 = " + supplierId;
        java.sql.ResultSet supplierRs = statement.executeQuery(checkSupplierQuery);
        supplierRs.next();
        int supplierCount = supplierRs.getInt("count");

        // 关闭结果集和语句
        shopRs.close();
        commodityRs.close();
        supplierRs.close();
        statement.close();

        // 返回这些外键是否在相关表中存在的布尔值
        return shopCount > 0 && commodityCount > 0 && supplierCount > 0;
    }
}
