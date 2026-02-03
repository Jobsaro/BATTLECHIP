/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleship.dinamico;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleshipGUI extends JFrame {

    private JButton[][] botones = new JButton[8][8];
    private BattleshipLogica logica = new BattleshipLogica();
    private Barco seleccionado = null;

    public BattleshipGUI() {
        setTitle("BATTLESHIP - CONFIGURACIÓN DE FLOTA");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 750); // Ajustado para dar espacio a los botones inferiores
        setLocationRelativeTo(null);
        
        // Usamos BorderLayout para separar el tablero de los botones de control
        setLayout(new BorderLayout());

        // --- PANEL DEL TABLERO (CENTRO) ---
        JPanel panelTablero = new JPanel(new GridLayout(8, 8));
        inicializarTablero(panelTablero);
        add(panelTablero, BorderLayout.CENTER);

        // --- PANEL DE BOTONES (SUR) ---
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        
        // Botón Abandonar
        JButton btnAbandonar = new JButton("ABANDONAR");
        btnAbandonar.setBackground(new Color(200, 0, 0));
        btnAbandonar.setForeground(Color.WHITE);
        btnAbandonar.setFocusPainted(false);
        btnAbandonar.setFont(new Font("Arial", Font.BOLD, 13));
        btnAbandonar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres abandonar?", "Salir", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        // Botón Listo
        JButton btnListo = new JButton("LISTO");
        btnListo.setBackground(new Color(0, 150, 0));
        btnListo.setForeground(Color.WHITE);
        btnListo.setFocusPainted(false);
        btnListo.setFont(new Font("Arial", Font.BOLD, 13));
        btnListo.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "¡Flota Confirmada! Preparando tablero de batalla...");
        });

        panelControles.add(btnAbandonar);
        panelControles.add(btnListo);
        add(panelControles, BorderLayout.SOUTH);

        refrescarTablero();
    }

    private void inicializarTablero(JPanel panel) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botones[i][j] = new JButton();
                int f = i, c = j;

                botones[i][j].addActionListener(e -> {
                    Barco clicEn = logica.getBarcoEn(f, c);

                    if (seleccionado == null) {
                        if (clicEn != null) {
                            seleccionado = clicEn;
                        }
                    } else {
                        if (clicEn == seleccionado) {
                            logica.intentarRotar(seleccionado);
                        } else {
                            logica.moverBarco(seleccionado, f, c);
                        }
                        seleccionado = null;
                    }
                    refrescarTablero();
                });
                panel.add(botones[i][j]);
            }
        }
    }

    private void refrescarTablero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botones[i][j].setBackground(new Color(0, 100, 200));
                botones[i][j].setBorder(UIManager.getBorder("Button.border"));
                botones[i][j].setText("");
            }
        }

        for (Barco b : logica.getBarcos()) {
            for (int i = b.fila; i < b.fila + b.alto; i++) {
                for (int j = b.col; j < b.col + b.ancho; j++) {
                    botones[i][j].setBackground(Color.DARK_GRAY);
                    if (b == seleccionado) {
                        botones[i][j].setBorder(new LineBorder(Color.BLACK, 4));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BattleshipGUI().setVisible(true));
    }
}

// --- CLASES DE LÓGICA ---

class Barco {
    String nombre;
    int fila, col, alto, ancho;

    Barco(String nombre, int alto, int ancho) {
        this.nombre = nombre;
        this.alto = alto;
        this.ancho = ancho;
    }

    public void rotar() {
        int temp = alto;
        alto = ancho;
        ancho = temp;
    }
}

class BattleshipLogica {
    private List<Barco> barcos = new ArrayList<>();

    public BattleshipLogica() {
        barcos.add(new Barco("Portaaviones", 2, 3));
        barcos.add(new Barco("Acorazado", 4, 1));
        barcos.add(new Barco("Submarino", 3, 1));
        barcos.add(new Barco("Destructor", 2, 1));
        posicionarAleatoriamente();
    }

    private void posicionarAleatoriamente() {
        Random r = new Random();
        for (Barco b : barcos) {
            boolean exito = false;
            while (!exito) {
                int f = r.nextInt(8), c = r.nextInt(8);
                if (r.nextBoolean()) b.rotar();

                if (f + b.alto <= 8 && c + b.ancho <= 8) {
                    b.fila = f; b.col = c;
                    if (!hayColision(b)) exito = true;
                }
            }
        }
    }

    public boolean hayColision(Barco b1) {
        for (Barco b2 : barcos) {
            if (b1 == b2) continue;
            if (b1.fila < b2.fila + b2.alto && b1.fila + b1.alto > b2.fila &&
                b1.col < b2.col + b2.ancho && b1.col + b1.ancho > b2.col) return true;
        }
        return false;
    }

    public void moverBarco(Barco b, int nf, int nc) {
        int fOriginal = b.fila, cOriginal = b.col;
        if (nf + b.alto <= 8 && nc + b.ancho <= 8) {
            b.fila = nf; b.col = nc;
            if (hayColision(b)) {
                b.fila = fOriginal; b.col = cOriginal;
            }
        }
    }

    public void intentarRotar(Barco b) {
        b.rotar();
        if (b.fila + b.alto > 8 || b.col + b.ancho > 8 || hayColision(b)) {
            b.rotar();
        }
    }

    public Barco getBarcoEn(int f, int c) {
        for (Barco b : barcos) {
            if (f >= b.fila && f < b.fila + b.alto && c >= b.col && c < b.col + b.ancho) return b;
        }
        return null;
    }

    public List<Barco> getBarcos() { return barcos; }
}