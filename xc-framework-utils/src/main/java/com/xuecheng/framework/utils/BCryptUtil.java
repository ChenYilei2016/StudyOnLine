package com.xuecheng.framework.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *       <dependency>
 *             <groupId>org.springframework.security</groupId>
 *             <artifactId>spring-security-crypto</artifactId>
 *             <version>4.2.3.RELEASE</version>
 *       </dependency>
 */
public class BCryptUtil {
    public static String encode(String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashPass = passwordEncoder.encode(password);
        return hashPass;
    }
    public static boolean matches(String password,String hashPass){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean f = passwordEncoder.matches(password, hashPass);
        return f;
    }
}
