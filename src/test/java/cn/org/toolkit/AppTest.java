package cn.org.toolkit;


import cn.org.toolkit.token.JwtToken;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void test() {
        String pw = BCrypt.hashpw("12333", BCrypt.gensalt());
        System.out.println(pw);
        boolean checkpw = BCrypt.checkpw("123", pw);
        boolean checkpw2 = BCrypt.checkpw("12333", pw);
        System.out.println(checkpw);
        System.out.println(checkpw2);
    }
    @Test
    public void test2(){
        String resultMsg;
        String salt = "123456";
        String name = "admin";
        String role = "SYS_ADMIN";
        String clientID = "123";
        String entityName = "entityName";
        String loginName = "loginAdmin";
        String loginPassword = "loginPassword";

        //拼装accessToken
        String accessToken = JwtToken.createJWT(loginPassword, String.valueOf(name),
                role, clientID, entityName,
                604800, "mySecret");
        System.out.println(accessToken);
    }
}
