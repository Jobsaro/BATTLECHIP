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
        setLayout(new GridLayout(8, 8));
        inicializarTablero();
        refrescarTablero();
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarTablero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botones[i][j] = new JButton();
                int f = i, c = j;
                
                botones[i][j].addActionListener(e -> {
                    Barco clicEn = logica.getBarcoEn(f, c);

                    if (seleccionado == null) {
                        // Paso 1: Seleccionar
                        if (clicEn != null) seleccionado = clicEn;
                    } else {
                        // Paso 2: Si es el mismo, rotar. Si es otro lado, mover.
                        if (clicEn == seleccionado) {
                            logica.intentarRotar(seleccionado);
                        } else {
                            logica.moverBarco(seleccionado, f, c);
                        }
                        seleccionado = null; // Soltar selección
                    }
                    refrescarTablero();
                });
                add(botones[i][j]);
            }
        }
    }

    private void refrescarTablero() {
        // Dibujar el mar
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botones[i][j].setBackground(new Color(0, 100, 200));
                botones[i][j].setBorder(UIManager.getBorder("Button.border"));
            }
        }

        // Dibujar los barcos sobre el mar
        for (Barco b : logica.getBarcos()) {
            for (int i = b.fila; i < b.fila + b.alto; i++) {
                for (int j = b.col; j < b.col + b.ancho; j++) {
                    botones[i][j].setBackground(Color.DARK_GRAY);
                    // Aplicar borde negro si está seleccionado
                    if (b == seleccionado) {
                        botones[i][j].setBorder(new LineBorder(Color.BLACK, 3));
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
        // Dimensiones solicitadas
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
            // Verifica si los rectángulos se interceptan
            if (b1.fila < b2.fila + b2.alto && b1.fila + b1.alto > b2.fila &&
                b1.col < b2.col + b2.ancho && b1.col + b1.ancho > b2.col) return true;
        }
        return false;
    }

    public void moverBarco(Barco b, int nf, int nc) {
        int fOriginal = b.fila, cOriginal = b.col;
        if (nf + b.alto <= 8 && nc + b.ancho <= 8) {
            b.fila = nf; b.col = nc;
            if (hayColision(b)) { b.fila = fOriginal; b.col = cOriginal; }
        }
    }

    public void intentarRotar(Barco b) {
        b.rotar();
        // Si al rotar choca o se sale, se deshace
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