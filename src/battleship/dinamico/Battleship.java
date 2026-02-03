/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleship.dinamico;

import java.util.Random;

/**
 *
 * @author Administrator
 */

public class Battleship {
    // Matriz de 8x8 [cite: 4]
    private String[][] tableroJugador1 = new String[8][8]; 
    private String[][] tableroJugador2 = new String[8][8];
    private String dificultad = "NORMAL"; 

    public void regenerarTablero(String[][] tablero, int cantidadBarcos) {
        // Limpiar tablero actual
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) tablero[i][j] = null;
        }
        
        // Reposicionar barcos aleatoriamente 
        Random rand = new Random();
        int colocados = 0;
        while (colocados < cantidadBarcos) {
            int f = rand.nextInt(8);
            int c = rand.nextInt(8);
            if (tablero[f][c] == null) {
                // Aqui usaria los codigos PA, AZ, blabla. 
                tablero[f][c] = "BARCO"; 
                colocados++;
            }
        }
    }
}