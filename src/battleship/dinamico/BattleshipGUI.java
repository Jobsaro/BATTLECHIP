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
    
    // Guardamos los objetos de Player para actualizar sus datos al final
    private Player player1, player2;
    private String nombreJ1, nombreJ2;

    public BattleshipGUI(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
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
            if(JOptionPane.showConfirmDialog(this, "Rendirse?") == 0) this.dispose();
        });

        panelBotones.add(btnListo); panelBotones.add(btnSalir);
        add(panelBotones, BorderLayout.SOUTH);

        refrescarTablero();
        setLocationRelativeTo(null);
    }

    private void alHacerClic(int f, int c) {
        Battleship oponente = turnoJugador1 ? logicaJ2 : logicaJ1;
        char[][] misDisparos = turnoJugador1 ? disparosJ1 : disparosJ2;

        if (logicaJ1.isFaseAtaque() && logicaJ2.isFaseAtaque()) {
            if (misDisparos[f][c] != 0) return; 
            Battleship.Barco b = oponente.getBarcoEn(f, c);
            if (b != null) {
                misDisparos[f][c] = 'X';
                refrescarTablero();
                
                if (verificarVictoria(misDisparos, oponente)) {
                    completarDatosYFinalizar();
                }
            } else {
                misDisparos[f][c] = 'F';
                refrescarTablero();
                JOptionPane.showMessageDialog(this, "Agua... Cambio de turno.");
                cambiarTurnoVisual();
            }
        } else {
            Battleship actual = turnoJugador1 ? logicaJ1 : logicaJ2;
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

    private boolean verificarVictoria(char[][] misDisparos, Battleship oponente) {
        int impactosNecesarios = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (oponente.getBarcoEn(i, j) != null) impactosNecesarios++;
            }
        }

        int impactosLogrados = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (misDisparos[i][j] == 'X') impactosLogrados++;
            }
        }
        return impactosLogrados == impactosNecesarios;
    }

    //se completan los datos de las demas clases
    private void completarDatosYFinalizar() {
        Player ganador = turnoJugador1 ? player1 : player2;
        Player perdedor = turnoJugador1 ? player2 : player1;

        // Sumar puntos al ganador (3 puntos por victoria)
        ganador.addPuntos(3);

        //Registrar en el historial de ambos
        ganador.registrarJuego("Victoria contra " + perdedor.getUsername());
        perdedor.registrarJuego("Derrota contra " + ganador.getUsername());

        // Pantalla de victoria
        JPanel panelVic = new JPanel(new BorderLayout());
        JLabel txt = new JLabel("<html><center>¡VICTORIA!<br><br><b>" + ganador.getUsername() + "</b> ha ganado.<br>+3 Puntos obtenidos.</center></html>", SwingConstants.CENTER);
        panelVic.add(txt);

        JOptionPane.showMessageDialog(this, panelVic, "Juego Terminado", JOptionPane.PLAIN_MESSAGE);
        
        this.dispose();
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
        Battleship oponente = turnoJugador1 ? logicaJ2 : logicaJ1; 
        char[][] misDisparos = turnoJugador1 ? disparosJ1 : disparosJ2;
        boolean faseAtaqueTotal = logicaJ1.isFaseAtaque() && logicaJ2.isFaseAtaque();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botones[i][j].setText("");
                botones[i][j].setBorder(UIManager.getBorder("Button.border"));

                if (faseAtaqueTotal) {
                    if (misDisparos[i][j] == 'X') {
                        botones[i][j].setBackground(Color.RED);
                        botones[i][j].setText("X");
                        Battleship.Barco b = oponente.getBarcoEn(i, j);
                        if (b != null && b.estaHundido(misDisparos)) {
                            botones[i][j].setBorder(new LineBorder(Color.YELLOW, 3));
                        }
                    } else if (misDisparos[i][j] == 'F') {
                        botones[i][j].setBackground(new Color(150, 200, 255));
                        botones[i][j].setText("F");
                    } else {
                        if (oponente.getBarcoEn(i, j) != null && Battleship.modoJuego.equalsIgnoreCase("TUTORIAL")) {
                            botones[i][j].setBackground(Color.DARK_GRAY);
                        } else {
                            botones[i][j].setBackground(new Color(30, 144, 255));
                        }
                    }
                } else {
                    Battleship.Barco b = actual.getBarcoEn(i, j);
                    if (b != null) {
                        botones[i][j].setBackground(Color.DARK_GRAY);
                        if (b == seleccionado) botones[i][j].setBorder(new LineBorder(Color.YELLOW, 3));
                    } else {
                        botones[i][j].setBackground(new Color(30, 144, 255));
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
