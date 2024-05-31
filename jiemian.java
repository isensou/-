package 主程序;
import java.math.BigDecimal;
import java.util.*;
//2200310630韦善壮
public class jiemian {

    Scanner scanner = new Scanner(System.in);
    public void SetFunction2()
    {
        System.out.println("1,开设新店");
        System.out.println("2,撤离店铺");
        System.out.println("3,寻找店铺");
        System.out.println("4,店铺调整");
    }

    public void SetFunction1() {
        System.out.println("1,搜索商品");
        System.out.println("2,新增商品");
        System.out.println("3,修改商品信息");
        System.out.println("4,商品下架");

    }
    public void SetFunction3()
    {
        System.out.println("1,店铺管理");
        System.out.println("2,商品管理");
        System.out.println("3,供应信息管理");
        System.out.println("4,店员信息管理");
        System.out.println("5,商品销售管理");
        System.out.println("6,关闭系统");
    }
    public void FunctionSelection1(Scanner scanner) {
        MYSQL commodity = new MYSQL();
        System.out.print("选择功能:");
        if (scanner.hasNext()) {
            int a = scanner.nextInt();

            if (a==1) {
                commodity.Select(scanner);


            } else if (a==2) {
                System.out.print("商品编号：");
                int id = scanner.nextInt();
                System.out.print("单价：");
                BigDecimal price = scanner.nextBigDecimal();
                System.out.print("名称：");
                String name = scanner.next();
                commodity.insert(id, price, name);
            } else if (a==3) {
                commodity.update(scanner);
            } else if (a==4) {
                commodity.delete(scanner);
            } else {
                System.out.print("更多功能尚在开发中，请尽情期待！");

            }
            scanner.nextLine();
        }
    }
}

