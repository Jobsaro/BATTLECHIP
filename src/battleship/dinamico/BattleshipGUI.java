package battleship.dinamico;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BattleshipGUI extends JFrame {
    private JButton[][] botones = new JButton[8][8];
    private Battleship logicaJ1 = new Battleship();
    private Battleship logicaJ2 = new Battleship();
    private char[][] disparosJ1 = new char[8][8]; 
    private char[][] disparosJ2 = new char[8][8];
    private Battleship.Barco seleccionado = null;
    private boolean turnoJugador1 = true;
    private JLabel lblTurno;
    private String nombreJ1, nombreJ2;

    public BattleshipGUI(Player p1, Player p2) {
        this.nombreJ1 = p1.getUsername().toUpperCase();
        this.nombreJ2 = p2.getUsername().toUpperCase();

        setTitle("BATTLESHIP PRO - " + nombreJ1 + " VS " + nombreJ2);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLayout(new BorderLayout());

        lblTurno = new JLabel(nombreJ1 + ": PREPARA TU FLOTA", SwingConstants.CENTER);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 16));
        lblTurno.setPreferredSize(new Dimension(500, 40));
        add(lblTurno, BorderLayout.NORTH);

        JPanel panelTablero = new JPanel(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botones[i][j] = new JButton();
                botones[i][j].setPreferredSize(new Dimension(55, 55));
                int f = i, c = j;
                botones[i][j].addActionListener(e -> alHacerClic(f, c));
                panelTablero.add(botones[i][j]);
            }
        }
        add(panelTablero, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton btnListo = new JButton("LISTO / SIGUIENTE");
        JButton btnSalir = new JButton("ABANDONAR PARTIDA");
        
        btnSalir.setBackground(new Color(150, 50, 50));
        btnSalir.setForeground(Color.WHITE);

        btnListo.addActionListener(e -> pasarTurno(btnListo));
        btnSalir.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "¿Rendirse?") == 0) this.dispose();
        });

        panelBotones.add(btnListo); panelBotones.add(btnSalir);
        add(panelBotones, BorderLayout.SOUTH);

        refrescarTablero();
        setLocationRelativeTo(null);
    }

    private void alHacerClic(int f, int c) {
        Battleship actual = turnoJugador1 ? logicaJ1 : logicaJ2;
        Battleship oponente = turnoJugador1 ? logicaJ2 : logicaJ1;
        char[][] misDisparos = turnoJugador1 ? disparosJ1 : disparosJ2;

        if (logicaJ1.isFaseAtaque() && logicaJ2.isFaseAtaque()) {
            if (misDisparos[f][c] != 0) return; 
            Battleship.Barco b = oponente.getBarcoEn(f, c);
            if (b != null) {
                misDisparos[f][c] = 'X';
                refrescarTablero();
            } else {
                misDisparos[f][c] = 'F';
                refrescarTablero();
                JOptionPane.showMessageDialog(this, "Agua... Cambio de turno.");
                cambiarTurnoVisual();
            }
        } else {
            Battleship.Barco clicEn = actual.getBarcoEn(f, c);
            if (seleccionado == null) {
                if (clicEn != null) seleccionado = clicEn;
            } else {
                if (clicEn == seleccionado) actual.intentarRotar(seleccionado);
                else actual.moverBarco(seleccionado, f, c);
                seleccionado = null;
            }
            refrescarTablero();
        }
    }

    private void pasarTurno(JButton btn) {
        if (!logicaJ1.isFaseAtaque()) {
            logicaJ1.activarFaseAtaque();
            turnoJugador1 = false;
            lblTurno.setText(nombreJ2 + ": PREPARA TU FLOTA");
        } else if (!logicaJ2.isFaseAtaque()) {
            logicaJ2.activarFaseAtaque();
            turnoJugador1 = true;
            lblTurno.setText("ATAQUE: TURNO DE " + nombreJ1);
            btn.setEnabled(false);
        }
        refrescarTablero();
    }

    private void cambiarTurnoVisual() {
        turnoJugador1 = !turnoJugador1;
        lblTurno.setText("ATAQUE: TURNO DE " + (turnoJugador1 ? nombreJ1 : nombreJ2));
        refrescarTablero();
    }

    private void refrescarTablero() {
        Battleship actual = turnoJugador1 ? logicaJ1 : logicaJ2;
        // El oponente es el que recibe los disparos en fase de ataque
        Battleship oponente = turnoJugador1 ? logicaJ2 : logicaJ1; 
        char[][] misDisparos = turnoJugador1 ? disparosJ1 : disparosJ2;
        boolean faseAtaqueTotal = logicaJ1.isFaseAtaque() && logicaJ2.isFaseAtaque();

        Color colorAgua = new Color(30, 144, 255);
        Color colorBarco = Color.DARK_GRAY;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botones[i][j].setText("");
                botones[i][j].setBorder(UIManager.getBorder("Button.border"));

                if (faseAtaqueTotal) {
                    // --- FASE DE ATAQUE ---
                    if (misDisparos[i][j] == 'X') {
                        botones[i][j].setBackground(Color.RED);
                        botones[i][j].setText("X");
                    } else if (misDisparos[i][j] == 'F') {
                        botones[i][j].setBackground(new Color(150, 200, 255));
                        botones[i][j].setText("F");
                    } else {
                        // AQUÍ APLICAMOS LA REGLA DE VISIBILIDAD EN ATAQUE
                        Battleship.Barco bEnOponente = oponente.getBarcoEn(i, j);
                        if (bEnOponente != null && Battleship.modoJuego.equalsIgnoreCase("TUTORIAL")) {
                            botones[i][j].setBackground(colorBarco); // Visible en Tutorial
                        } else {
                            botones[i][j].setBackground(colorAgua); // Oculto en Arcade
                        }
                    }
                } else {
                    // --- FASE DE PREPARACIÓN (Selección y movimiento) ---
                    // En esta fase, los barcos SIEMPRE deben ser visibles para el jugador actual
                    // independientemente de si es Arcade o Tutorial, para poder acomodarlos.
                    Battleship.Barco b = actual.getBarcoEn(i, j);

                    if (b != null) {
                        botones[i][j].setBackground(colorBarco);

                        if (b == seleccionado) {
                            botones[i][j].setBorder(new LineBorder(Color.YELLOW, 3));
                        }
                    } else {
                        botones[i][j].setBackground(colorAgua);
                    }
                }
            }
        }
    }
}
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BattleshipGUI().setVisible(true));
    }
*/
