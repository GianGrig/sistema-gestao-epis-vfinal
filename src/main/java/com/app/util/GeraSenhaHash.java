package com.app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeraSenhaHash {
    public static void main(String[] args) {
        String senhaPura = "Adm12@"; // coloque aqui a senha que deseja criptografar
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode(senhaPura);
        System.out.println("Hash: " + senhaCriptografada);
    }
}
