import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class keyGenerator {
    public static void main(String[] args) {
        // 设置密钥的长度（以位为单位）
        int keyLength = 512;

        // 生成一个安全随机数生成器
        SecureRandom secureRandom = new SecureRandom();

        try {
            // 创建一个KeyGenerator实例，指定使用的算法（例如，HMACSHA256）
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HMACSHA256");

            // 使用安全随机数生成器初始化KeyGenerator
            keyGenerator.init(keyLength, secureRandom);

            // 生成密钥
            SecretKey secretKey = keyGenerator.generateKey();

            // 将密钥编码为Base64字符串以便存储和使用
            String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            // 打印生成的密钥
            System.out.println("Generated JWT Secret Key: " + base64Key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
