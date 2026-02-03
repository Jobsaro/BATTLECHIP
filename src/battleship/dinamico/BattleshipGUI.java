/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleship.dinamico;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Administrator
 */

public class BattleshipGUI extends JFrame {
    private JButton[][] botonesTablero = new JButton[8][8];
    private Battleship logica = new Battleship();

    public BattleshipGUI() {
        setTitle("BATTLESHIP DINAMICO");
         //panel 8x8 
        setLayout(new GridLayout(8, 8));
        inicializarTablero();
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void inicializarTablero() {
        // Cargar imagenes
        // Para ataques fallidos "F" 
        ImageIcon iconoAgua = new ImageIcon(".png"); 
        // Para ataques acertados "X"
        ImageIcon iconoExplosion = new ImageIcon(".png"); 
        

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botonesTablero[i][j] = new JButton("~");
                int fila = i, col = j;
                
                botonesTablero[i][j].addActionListener(e -> {
                    // Accion al disparar 
                    boolean acierto = verificarDisparo(fila, col);
                    if (acierto) {
                        botonesTablero[fila][col].setIcon(iconoExplosion);
                        // Si le dio a un barco, el tablero cambia 
                        actualizarVistaTablero(); 
                    } else {
                        botonesTablero[fila][col].setIcon(iconoAgua);
                    }
                });
                add(botonesTablero[i][j]);
            }
        }
    }

    private boolean verificarDisparo(int f, int c) {
        // Llamada a la clase Battleship 
        return true;
    }

    private void actualizarVistaTablero() {
        // Redibuja los botones según la nueva posición aleatoria de la lógica [cite: 75]
    }

    public static void main(String[] args) {
        new BattleshipGUI().setVisible(true);
    }
}