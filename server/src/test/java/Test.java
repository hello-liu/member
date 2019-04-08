import com.moss.common.model.BackModel;

public class Test {


    public void testSwitch(){
        String method = "busi.integralToMoney";

        long i = 0 ;
        while(true) {
            switch (method) {

                //user
                case "user.login":
                    ;
                case "user.logout":
                     ;
                case "user.add":
                     ;
                case "user.del":
                     ;
                case "user.update":
                     ;
                case "user.updateFlag":
                     ;
                case "user.setMerchant":
                     ;
                case "user.get":
                     ;
                case "user.list":
                     ;

                //merchant
                case "merchant.add":
                     ;
                case "merchant.del":
                     ;
                case "merchant.update":
                     ;
                case "merchant.list":
                     ;
                case "merchant.updateFlag":
                     ;
                case "merchant.get":
                     ;

                //member
                case "member.add":
                     ;
                case "member.del":
                     ;
                case "member.update":
                     ;
                case "member.list":
                     ;
                case "member.get":
                     ;
                case "member.updateFlag":
                     ;

                //busi
                case "busi.charge":
                     ;
                case "busi.consume":
                     ;
                case "busi.refund":
                     ;
                case "busi.integralToMoney":
                     ;


                default:
            }
            i++;
            if(i%1000000000==0){
                System.out.println(i);
            }
        }
    }
}
