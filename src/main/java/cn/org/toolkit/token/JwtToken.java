package cn.org.toolkit.token;

import cn.org.zax.date.JzDate;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

/**
 * @Package: cn.org.basic.bcryption.token
 * @ClassName: JwtToken
 * @Description:
 * @Author: mac-pro
 * @CreateDate: 2019/2/20 下午9:04
 * @Version: 1.0
 */
@Slf4j
public class JwtToken {
    /**
     * 解析 jwt token
     * @param jsonWebToken
     * @param base64Security
     * @return
     */
    public static Claims parsedJWT(String jsonWebToken, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 创建 jwt token
     * @param name
     * @param loginPassword
     * @param role
     * @param audience
     * @param issuer
     * @param TTLMillis
     * @param base64Security
     * @descript jwt包含了使用.分隔的三部分： Header 头部 Payload 负载 Signature 签名
     * @herder : header中通常包含了两部分：token类型和采用的加密算法。{ "alg": "HS256", "typ": "JWT"}
     * 接下来对这部分内容使用 Base64Url 编码组成了JWT结构的第一部分。
     * @payload: Token的第二部分是负载，它包含了claim， Claim是一些实体（通常指的用户）的状态和额外的元数据，
     * 有三种类型的claim：reserved, public 和 private.Reserved claims: 这些claim是JWT预先定义的，
     * 在JWT中并不会强制使用它们，而是推荐使用，常用的有 iss（签发者）,exp（过期时间戳）, sub（面向的用户）, aud（接收方）, iat（签发时间）。
     * Public claims：根据需要定义自己的字段，注意应该避免冲突 Private claims：这些是自定义的字段，可以用来在双方之间交换信息
     * 负载使用的例子：{ "sub": "1234567890", "name": "John Doe", "admin": true}
     * 上述的负载需要经过Base64Url编码后作为JWT结构的第二部分。
     * @signature 创建签名需要使用编码后的header和payload以及一个秘钥，
     * 使用header中指定签名算法进行签名。例如如果希望使用HMAC SHA256算法，
     * 那么签名应该使用下列方式创建： HMACSHA256( base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
     * 签名用于验证消息的发送者以及消息是没有经过篡改的。 完整的JWT 完整的JWT格式的输出是以. 分隔的三段Base64编码，
     * 与SAML等基于XML的标准相比，JWT在HTTP和HTML环境中更容易传递。 下列的JWT展示了一个完整的JWT格式，它拼接了之前的Header，
     * Payload以及秘钥签名例子:
     * {header头: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.
     * payload包体:
     * eyJyb2xlIjoiU1lTX0FETUlOIiwidW5pcXVlX25hbWUiOiJsb2dpblBhc3N3b3JkIiwidXNlcmlkIjoiYWRtaW4iL
     * CJpc3MiOiJlbnRpdHlOYW1lIiwiYXVkIjoiMTIzIiwiZXhwIjoxNTI0NTQ3MTY4LCJuYmYiOjE1MjQ1NDY1NjR9.
     * signature签名:
     * K4L562MxP5E0QbKPhjNUTFP-i6RBYg45zLCgl1DMPtQ}
     * @return
     */
    public static String createJWT(String loginPassword, String name, String role,
                                   String audience, String issuer, long TTLMillis, String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成过期时间
        long nowMillis = System.currentTimeMillis();
        log.info(String.format("当前类{%s}生成jwt时间参数:{%s}",
                Thread.currentThread().getStackTrace()[1].getClassName(), JzDate.toString(nowMillis)));
        //生成签名密钥 base64二进制编码
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        log.info("当前类[{}]生成的签名秘钥参数:{{}}",
                Thread.currentThread().getStackTrace()[1].getClassName(), JSONObject.toJSONString(signatureAlgorithm));
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("role", role)
                .claim("unique_name", loginPassword)
                .claim("name", name)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            log.info("过期时间是:{{}}",JzDate.toString(expMillis));
            builder.setExpiration(JzDate.toDate(expMillis)).setNotBefore(JzDate.toDate(nowMillis));
        }
        //生成JWT
        return builder.compact();
    }
}
