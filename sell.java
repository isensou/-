package 主程序;
import java.util.Scanner;
//2200310617黄天天
public class sell {

    public  void sells() {
        Mysql3 b=new Mysql3();

        while(true)
        {
            System.out.println("请输入对销售表进行的操作:\n1-查询数据\t2-插入数据\t3-更改数据\t4-删除数据\t0-退出");
            Scanner scanner = new Scanner(System.in);
            int cs=scanner.nextInt();
            switch (cs)
            {
                case 0:
                    break;
                case 1:
                    b.select();
                    break;
                case 2:
                    b.insert();
                    break;
                case 3:
                    b.update();
                    break;
                case 4:
                    b.delete();
                    break;
                default:
                    System.out.println("非法输入！");
            }
            if (cs==0){
                System.out.println("感谢使用!");
                break;
            }
        }

    }

}
