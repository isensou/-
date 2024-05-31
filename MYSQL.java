package 主程序;

//2200310630韦善壮
import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;
//2200310631温盛祥
public class MYSQL {
    public String url = "jdbc:mysql://10.34.59.149:3306/销售系统";
    public String user = "wen";
    public String password = "123456";
    public void Select(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(url, user, password))
              {
            // 创建 PreparedStatement 对象来执行带参数的 SQL 查询
            String sql;
            System.out.println("选择功能：");
            System.out.println("1.查看所有");
            System.out.println("2.按照商品编号查找指定信息");
            System.out.println("3.按照商品名称查找指定信息");
            int x = Integer.parseInt(scanner.next());

            if (x == 1) {
                sql = "SELECT * FROM `commodity`";
            } else if (x == 2) {
                System.out.println("请输入商品编号：");
                int id = scanner.nextInt();
                sql = "SELECT * FROM `commodity` WHERE 商品编号=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, id);
                    executeAndPrintResults(preparedStatement);
                }
                return;
            } else if (x == 3) {
                System.out.println("请输入商品名称：");
                String name = scanner.next();
                sql = "SELECT * FROM `commodity` WHERE 名称=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, name);
                    executeAndPrintResults(preparedStatement);
                }
                return;
            } else {
                System.out.println("输入选项无效，请重新选择。");
                return;
            }

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);
                // 打印查询结果
                while (resultSet.next()) {
                    int id = resultSet.getInt("商品编号");
                    BigDecimal price = resultSet.getBigDecimal("单价");
                    String name = resultSet.getString("名称");
                    System.out.println("商品编号: " + id +", 单价：" + price + ", 名称: " + name);
                    System.out.print("");
                }
            }
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    private void executeAndPrintResults(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            // 打印查询结果
            while (resultSet.next()) {
                int id = resultSet.getInt("商品编号");
                BigDecimal price = resultSet.getBigDecimal("单价");
                String name = resultSet.getString("名称");
                System.out.println("商品编号: " + id +", 单价：" + price + ", 名称: " + name);
                System.out.print("");
            }
        }
    }

    public void insert(int id,BigDecimal price, String name) {
        try {
            // 连接到数据库
            Connection connection = DriverManager.getConnection(url, user, password);

            // 创建 PreparedStatement 对象来执行 SQL 插入语句
            String sql;
            sql = "INSERT INTO `commodity` (商品编号, 单价,名称) VALUES (?, ? , ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setBigDecimal(2,price);
            preparedStatement.setString(3, name);

            // 执行插入操作
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("插入成功");
            } else {
                System.out.println("插入失败");
            }

            // 关闭连接
            preparedStatement.close();
            connection.close();
        }
        catch (SQLIntegrityConstraintViolationException e) {
            // 捕获到重复键异常时的处理逻辑
            insert( id+1, price,name);
            // 进行异常处理的其他逻辑
        }
        catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }


    }
    public void update(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(url, user, password))
        {
            System.out.println("请选择更新方式：");
            System.out.println("1. 通过商品编号更新");
            System.out.println("2. 通过商品名称更新");
            int option = scanner.nextInt();

            if (option == 1) {
                System.out.println("请输入商品编号：");
                int id = scanner.nextInt();
                System.out.println("请输入新的单价：");
                BigDecimal price = scanner.nextBigDecimal();
                updateById(id, price);
            } else if (option == 2) {
                System.out.println("请输入商品名称：");
                String name = scanner.next();
                System.out.println("请输入新的单价：");
                BigDecimal price = scanner.nextBigDecimal();
                updateByName(name, price);
            } else {
                System.out.println("无效选项");
            }
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    private void updateById(int id, BigDecimal price) throws SQLException {
        String sql = "UPDATE `commodity` SET 单价 = ? WHERE 商品编号 = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, price);
            preparedStatement.setInt(2, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("更新成功");
            } else {
                System.out.println("更新失败");
            }
        }
    }

    private void updateByName(String name, BigDecimal price) throws SQLException {
        String sql = "UPDATE `commodity` SET 单价 = ? WHERE 名称 = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, price);
            preparedStatement.setString(2, name);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("更新成功");
            } else {
                System.out.println("更新失败");
            }
        }
    }

    public void delete(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             ) {
            System.out.println("请选择删除方式：");
            System.out.println("1. 通过商品编号删除");
            System.out.println("2. 通过商品名称删除");
            int option = scanner.nextInt();

            if (option == 1) {
                System.out.println("请输入商品编号：");
                int id = scanner.nextInt();
                deleteById(id);
            } else if (option == 2) {
                System.out.println("请输入商品名称：");
                String name = scanner.next();
                deleteByName(name);
            } else {
                System.out.println("无效选项");
            }
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    private void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM `commodity` WHERE 商品编号 = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("删除成功");
            } else {
                System.out.println("删除失败");
            }
        }
    }

    private void deleteByName(String name) throws SQLException {
        String sql = "DELETE FROM `commodity` WHERE 名称 = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("删除成功");
            } else {
                System.out.println("删除失败");
            }
        }
    }

}
