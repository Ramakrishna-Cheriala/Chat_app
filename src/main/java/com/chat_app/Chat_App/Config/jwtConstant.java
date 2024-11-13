package com.chat_app.Chat_App.Config;

public class jwtConstant {
    public static String JWT_HEADER = "Authorization";
    public static String SECRET_KEY = "9gieruv98qhgvuqp9v98huvbwsiup9qp73i4brov98pqubfwn9v81ourbvvb1oi3ubev983qp4viu19q8ervu";

//    public jwtConstant() {
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGenerator.generateKey();
//            SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//            System.out.println("secret ley:" + SECRET_KEY);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
