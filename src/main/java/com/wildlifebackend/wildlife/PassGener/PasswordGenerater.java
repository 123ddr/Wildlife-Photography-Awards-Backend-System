package com.wildlifebackend.wildlife.PassGener;

import java.security.SecureRandom;

public class PasswordGenerater {

    private static final String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM=new SecureRandom();

     public static String generatePassword(int length){
         StringBuilder password=new StringBuilder();

         for(int i=0;i<length;i++){
             password.append(characters.charAt(RANDOM.nextInt(characters.length())));
         }

         return password.toString();
     }

}
