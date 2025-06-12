package com.ngcamargob.rackmaster.configuracion.security;

import org.springframework.stereotype.Component;

@Component
public class SimpleEncryption {

    private static final int SHIFT = 3; // Número de posiciones a desplazar

    // Método para cifrar la contraseña
    public static String encrypt(String plainText) {
        StringBuilder encrypted = new StringBuilder();

        for (char ch : plainText.toCharArray()) {
            char shiftedChar = (char) (ch + SHIFT); // Desplazamiento
            encrypted.append(shiftedChar);
        } return encrypted.toString(); // Retornar texto cifrado
    }

    // Método para descifrar la contraseña
    public static String decrypt(String encryptedText) {
        StringBuilder decrypted = new StringBuilder();

        for (char ch : encryptedText.toCharArray()) {
            char shiftedChar = (char) (ch - SHIFT); // Desplazamiento inverso
            decrypted.append(shiftedChar);
        } return decrypted.toString(); // Retornar texto descifrado
    }
}
