package hr.java.musicshop.sucelja;

import hr.java.musicshop.controller.LoginController;
import hr.java.musicshop.controller.SignupController;
import hr.java.musicshop.iznimke.IncorrectPasswordException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static hr.java.musicshop.controller.HelloApplication.logger;

public sealed interface HashAndCheckPass permits LoginController, SignupController {

    default void checkPassword(String username, String password, Map<String, String> users){
        boolean uvjet = false;
        for(String userNames:users.keySet()){
            if(username.equals(userNames)){
                System.out.println("True");
                if (hashPassword(password).equals(users.get(userNames))) {
                    uvjet = true;
                    break;
                }
            }
        }
        if(!uvjet){
            throw new IncorrectPasswordException("Incorrect username or password.");
        }
    }

    default String hashPassword(String pass){
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            final byte[] hashbytes = digest.digest(pass.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashbytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
