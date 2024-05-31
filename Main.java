package 主程序;
import java.math.BigDecimal;
import java.util.*;
//2200310630韦善壮
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MYSQL2 w=new MYSQL2();
        sell w2=new sell();
        SupplierManagement w3=new SupplierManagement();
        SupplyManagement w4=new SupplyManagement();
        index w5=new index();
        jiemian n = new jiemian();
        int s;
        int s2;
        while (true) {
            System.out.println("欢迎使用商品销售管理系统！");
            n.SetFunction3();
            System.out.print("输入数字选择功能:");
            s=scanner.nextInt();
            if(s==1)
            {
                n.SetFunction2();
                w.Shop(scanner);
            } else if (s==2) {
                n.SetFunction1();
                n.FunctionSelection1(scanner);
            }

            else if (s==3) {
                System.out.println("1，供应商管理");
                System.out.println("2，供应信息管理");
                s2= scanner.nextInt();
            switch (s2){
                case 1:
                    w3.Supplier();
                    break;
                case 2:
                    w4.Supply();
                    break;
                default:
                    System.out.print("更多功能尚在开发中，请尽情期待！");
            }
        }
        else if (s==4) {
            w5.Index();
        }
        else if (s==5) {
            w2.sells();
        } else if (s==6) {
                return;
            }

        }
}}


