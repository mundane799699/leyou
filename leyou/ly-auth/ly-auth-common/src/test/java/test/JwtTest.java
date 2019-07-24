package test;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.common.utils.RsaUtils;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.Before;
import org.junit.Test;

public class JwtTest {

    private static final String pubKeyPath = "/Users/mundane/develop/temp/rsa/rsa.pub";

    private static final String priKeyPath = "/Users/mundane/develop/temp/rsa/rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        //token = eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU2MzYwMjI2NX0.f8JAZu8OTJGotPJuNuLSOufH6acwMlBQahkbGKnuz7evR5f6AYvT8O2JJPCCrrXf4wUhA5PVUKFhbywZrEhw3ihnNGphAB34P1LPuqUs9fm5sgSfOfu1hT5RVqTr1yR2b835Xgj8FeEIq6npEJzuwkDe4p_nc3v09o7pT3F2ZTQ

        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU2MzYwMjI2NX0.f8JAZu8OTJGotPJuNuLSOufH6acwMlBQahkbGKnuz7evR5f6AYvT8O2JJPCCrrXf4wUhA5PVUKFhbywZrEhw3ihnNGphAB34P1LPuqUs9fm5sgSfOfu1hT5RVqTr1yR2b835Xgj8FeEIq6npEJzuwkDe4p_nc3v09o7pT3F2ZTQ";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
