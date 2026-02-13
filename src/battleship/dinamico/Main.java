package battleship.dinamico;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main extends JFrame {
    private JPanel panelContenedor;
    private CardLayout cardLayout;
    private JTextField txtUserLogin, txtUserReg;
    private JPasswordField txtPassLogin, txtPassReg;
    private JButton btnCancelarP2; 
    private boolean identificandoOponente = false;

    public Main() {
        setTitle("BATTLESHIP - SISTEMA");
        setSize(400, 650); // Aumenté un poco el alto para que quepan bien los botones
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // Agregando todos los paneles
        panelContenedor.add(crearPanelLogin(), "LOGIN");
        panelContenedor.add(crearPanelRegistro(), "REGISTRO");
        panelContenedor.add(crearPanelMenuPrincipal(), "MENU_PRINCIPAL");
        panelContenedor.add(crearPanelConfiguracion(), "CONFIGURACION");
        panelContenedor.add(crearPanelReportes(), "REPORTES");
        panelContenedor.add(crearPanelPerfil(), "PERFIL");

        add(panelContenedor);
        cardLayout.show(panelContenedor, "LOGIN");
    }

    // --- 1. PANEL DE LOGIN (MODIFICADO CON BOTÓN SALIR) ---
    private JPanel crearPanelLogin() {
        JPanel p = new JPanel(new GridLayout(11, 1, 10, 10)); // Aumenté a 11 filas
        p.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        p.setBackground(new Color(33, 37, 43));

        JLabel titulo = new JLabel("BATTLESHIP LOGIN", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        txtUserLogin = new JTextField();
        txtPassLogin = new JPasswordField();
        JButton btnEntrar = new JButton("INICIAR SESIÓN");
        JButton btnIrReg = new JButton("CREAR CUENTA");
        
        // Botón Cancelar (Para el Jugador 2)
        btnCancelarP2 = new JButton("CANCELAR (VOLVER)");
        btnCancelarP2.setBackground(new Color(150, 50, 50));
        btnCancelarP2.setForeground(Color.WHITE);
        btnCancelarP2.setVisible(false);

        // NUEVO BOTÓN: SALIR DEL JUEGO
        JButton btnSalirApp = new JButton("SALIR DEL JUEGO");
        btnSalirApp.setBackground(Color.BLACK);
        btnSalirApp.setForeground(Color.WHITE);
        btnSalirApp.setFocusPainted(false);

        p.add(titulo);
        p.add(new JLabel("Usuario:")).setForeground(Color.GRAY);
        p.add(txtUserLogin);
        p.add(new JLabel("Contraseña:")).setForeground(Color.GRAY);
        p.add(txtPassLogin);
        p.add(new JLabel()); // Espaciador
        p.add(btnEntrar); 
        p.add(btnIrReg); 
        p.add(btnCancelarP2);
        p.add(btnSalirApp); // Agregamos el botón salir al final

        // Lógica de Entrar
        btnEntrar.addActionListener(e -> {
            String u = txtUserLogin.getText().trim();
            String ps = new String(txtPassLogin.getPassword()).trim();
            
            Player player = Battleship.buscarPlayer(u);
            if (player != null && player.getPassword().equals(ps)) {
                if (!identificandoOponente) {
                    Battleship.currentUser = player;
                    cardLayout.show(panelContenedor, "MENU_PRINCIPAL");
                } else {
                    if (player == Battleship.currentUser) {
                        JOptionPane.showMessageDialog(this, "El oponente debe ser diferente.");
                        return;
                    }
                    identificandoOponente = false;
                    btnCancelarP2.setVisible(false);
                    new BattleshipGUI(Battleship.currentUser, player).setVisible(true);
                    cardLayout.show(panelContenedor, "MENU_PRINCIPAL");
                }
                limpiarLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Lógica Registro
        btnIrReg.addActionListener(e -> {
            limpiarRegistro();
            cardLayout.show(panelContenedor, "REGISTRO");
        });

        // Lógica Cancelar P2
        btnCancelarP2.addActionListener(e -> {
            identificandoOponente = false;
            btnCancelarP2.setVisible(false);
            limpiarLogin();
            cardLayout.show(panelContenedor, "MENU_PRINCIPAL");
        });

        // Lógica Salir del Programa
        btnSalirApp.addActionListener(e -> {
            System.exit(0); // Cierra toda la aplicación
        });

        return p;
    }

    private void limpiarLogin() {
        txtUserLogin.setText("");
        txtPassLogin.setText("");
    }

    private void limpiarRegistro() {
        txtUserReg.setText("");
        txtPassReg.setText("");
    }

    // --- 2. PANEL DE REGISTRO ---
    private JPanel crearPanelRegistro() {
        JPanel p = new JPanel(new GridLayout(8, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        p.setBackground(new Color(40, 44, 52));
        JLabel titulo = new JLabel("NUEVO PLAYER", SwingConstants.CENTER);
        titulo.setForeground(new Color(0, 190, 255));
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        txtUserReg = new JTextField();
        txtPassReg = new JPasswordField();
        JButton btnGuardar = new JButton("REGISTRAR");
        JButton btnVolver = new JButton("VOLVER");
        p.add(titulo);
        p.add(new JLabel("Username:")).setForeground(Color.GRAY);
        p.add(txtUserReg);
        p.add(new JLabel("Password:")).setForeground(Color.GRAY);
        p.add(txtPassReg);
        p.add(new JLabel());
        p.add(btnGuardar); p.add(btnVolver);
        btnGuardar.addActionListener(e -> {
            String u = txtUserReg.getText().trim();
            String ps = new String(txtPassReg.getPassword()).trim();
            
            if (u.isEmpty() || ps.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }
            if (Battleship.buscarPlayer(u) != null) {
                JOptionPane.showMessageDialog(this, "El usuario ya existe");
                return;
            }
            Battleship.listaPlayers.add(new Player(u, ps));
            JOptionPane.showMessageDialog(this, "Registrado con éxito");
            limpiarRegistro();
            cardLayout.show(panelContenedor, "LOGIN");
        });
        btnVolver.addActionListener(e -> cardLayout.show(panelContenedor, "LOGIN"));
        return p;
    }

    // --- 3. PANEL MENÚ PRINCIPAL ---
    private JPanel crearPanelMenuPrincipal() {
        JPanel p = new JPanel(new GridLayout(7, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        p.setBackground(new Color(25, 25, 30));

        JLabel titulo = new JLabel("MENÚ PRINCIPAL", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btnJugar = new JButton("1. Jugar Battleship");
        JButton btnConfig = new JButton("2. Configuración");
        JButton btnReportes = new JButton("3. Reportes");
        JButton btnPerfil = new JButton("4. Mi Perfil");
        JButton btnSalir = new JButton("5. Salir");

        estilizarBotonMenu(btnJugar);
        estilizarBotonMenu(btnConfig);
        estilizarBotonMenu(btnReportes);
        estilizarBotonMenu(btnPerfil);
        estilizarBotonMenu(btnSalir);

        p.add(titulo); p.add(btnJugar); p.add(btnConfig); p.add(btnReportes); p.add(btnPerfil); p.add(btnSalir);

        btnJugar.addActionListener(e -> {
            identificandoOponente = true;
            btnCancelarP2.setVisible(true);
            limpiarLogin();
            JOptionPane.showMessageDialog(this, "Se requiere autenticación del Jugador 2.");
            cardLayout.show(panelContenedor, "LOGIN");
        });

        btnConfig.addActionListener(e -> cardLayout.show(panelContenedor, "CONFIGURACION"));
        
        btnReportes.addActionListener(e -> cardLayout.show(panelContenedor, "REPORTES"));
        
        btnPerfil.addActionListener(e -> cardLayout.show(panelContenedor, "PERFIL"));

        btnSalir.addActionListener(e -> {
            Battleship.currentUser = null;
            limpiarLogin();
            cardLayout.show(panelContenedor, "LOGIN");
        });

        return p;
    }

    // --- 4. PANEL DE REPORTES ---
    private JPanel crearPanelReportes() {
        JPanel p = new JPanel(new GridLayout(7, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        p.setBackground(new Color(20, 30, 40)); 

        JLabel titulo = new JLabel("REPORTES", SwingConstants.CENTER);
        titulo.setForeground(new Color(0, 255, 150));
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btnUltimos = new JButton("a. Últimos juegos");
        JButton btnRanking = new JButton("b. Ranking de Jugadores");
        JButton btnVolver = new JButton("c. Regresar al Menú");

        estilizarBotonMenu(btnUltimos);
        estilizarBotonMenu(btnRanking);
        estilizarBotonMenu(btnVolver);

        btnUltimos.addActionListener(e -> {
            if (Battleship.currentUser != null) {
                StringBuilder sb = new StringBuilder("ÚLTIMOS 10 JUEGOS DE: " + Battleship.currentUser.getUsername() + "\n\n");
                ArrayList<String> historial = Battleship.currentUser.getHistorial();
                for (int i = 0; i < 10; i++) {
                    sb.append((i + 1)).append("- ");
                    if (i < historial.size()) sb.append(historial.get(i));
                    sb.append("\n");
                }
                mostrarReporte(sb.toString(), "Historial de Juegos");
            }
        });

        btnRanking.addActionListener(e -> {
            ArrayList<Player> ranking = new ArrayList<>(Battleship.listaPlayers);
            ranking.sort((p1, p2) -> Integer.compare(p2.getPuntos(), p1.getPuntos()));
            StringBuilder sb = new StringBuilder("RANKING GLOBAL DE JUGADORES\n(Ordenado por Puntos)\n\n");
            sb.append(String.format("%-20s %-10s\n", "JUGADOR", "PUNTOS"));
            sb.append("----------------------------------\n");
            for (Player player : ranking) {
                sb.append(String.format("%-20s %-10d\n", player.getUsername(), player.getPuntos()));
            }
            mostrarReporte(sb.toString(), "Ranking Global");
        });

        btnVolver.addActionListener(e -> cardLayout.show(panelContenedor, "MENU_PRINCIPAL"));
        p.add(titulo); p.add(btnUltimos); p.add(btnRanking); 
        p.add(new JLabel()); p.add(new JLabel()); p.add(new JLabel()); p.add(btnVolver);
        return p;
    }

    private void mostrarReporte(String texto, String titulo) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(350, 300));
        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    // --- 5. PANEL DE PERFIL ---
    private JPanel crearPanelPerfil() {
        JPanel p = new JPanel(new GridLayout(7, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        p.setBackground(new Color(25, 25, 30));

        JLabel titulo = new JLabel("MI PERFIL", SwingConstants.CENTER);
        titulo.setForeground(new Color(255, 215, 0)); // Color Dorado
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btnVerDatos = new JButton("a. Ver Mis Datos");
        JButton btnModificar = new JButton("b. Modificar Mis Datos");
        JButton btnEliminar = new JButton("c. Eliminar Cuenta");
        JButton btnRegresar = new JButton("d. Regresar al Menú");

        estilizarBotonMenu(btnVerDatos);
        estilizarBotonMenu(btnModificar);
        estilizarBotonMenu(btnEliminar);
        estilizarBotonMenu(btnRegresar);
        
        // Opción A: Ver Datos
        btnVerDatos.addActionListener(e -> {
            Player actual = Battleship.currentUser;
            if (actual != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("=========================================\n");
                sb.append("           DATOS DEL JUGADOR             \n");
                sb.append("=========================================\n\n");
                
                sb.append(String.format(" %-15s %s\n", "USUARIO:", actual.getUsername()));
                sb.append(String.format(" %-15s %s\n", "CONTRASEÑA:", actual.getPassword()));
                sb.append(String.format(" %-15s %d\n", "PUNTOS:", actual.getPuntos()));
                sb.append(String.format(" %-15s %d\n", "TOTAL JUEGOS:", actual.getHistorial().size()));
                
                sb.append("\n=========================================\n");
                sb.append("           HISTORIAL DE JUEGOS           \n");
                sb.append("=========================================\n");
                
                if (actual.getHistorial().isEmpty()) {
                    sb.append("\n (Sin registros aún)");
                } else {
                    for (String log : actual.getHistorial()) {
                        sb.append("\n - ").append(log);
                    }
                }
                mostrarReporte(sb.toString(), "Mis Datos Completos");
            }
        });

        // Opción B: Modificar Datos
        btnModificar.addActionListener(e -> {
            Player actual = Battleship.currentUser;
            String nuevoUser = JOptionPane.showInputDialog(this, "Ingrese nuevo Username:", actual.getUsername());
            
            if (nuevoUser != null && !nuevoUser.trim().isEmpty()) {
                if (!nuevoUser.equalsIgnoreCase(actual.getUsername()) && Battleship.buscarPlayer(nuevoUser) != null) {
                    JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String nuevoPass = JOptionPane.showInputDialog(this, "Ingrese nuevo Password:", actual.getPassword());
                    if (nuevoPass != null && !nuevoPass.trim().isEmpty()) {
                        actual.setUsername(nuevoUser); 
                        actual.setPassword(nuevoPass);
                        JOptionPane.showMessageDialog(this, "Datos modificados exitosamente.");
                    }
                }
            }
        });

        // Opción C: Eliminar Cuenta
        btnEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que deseas ELIMINAR tu cuenta?\nEsta acción es irreversible.", 
                "Eliminar Cuenta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                Player aBorrar = Battleship.currentUser;
                Battleship.listaPlayers.removeIf(p2 -> p2.getUsername().equals(aBorrar.getUsername()));
                
                Battleship.currentUser = null;
                JOptionPane.showMessageDialog(this, "Cuenta eliminada. Hasta luego.");
                limpiarLogin();
                cardLayout.show(panelContenedor, "LOGIN");
            }
        });

        btnRegresar.addActionListener(e -> cardLayout.show(panelContenedor, "MENU_PRINCIPAL"));

        p.add(titulo); p.add(btnVerDatos); p.add(btnModificar); p.add(btnEliminar); 
        p.add(new JLabel()); p.add(new JLabel()); p.add(btnRegresar);

        return p;
    }

    // --- 6. PANEL DE CONFIGURACIÓN ---
    private JPanel crearPanelConfiguracion() {
        JPanel p = new JPanel(new GridLayout(7, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        p.setBackground(new Color(25, 25, 30));
        JLabel titulo = new JLabel("CONFIGURACIÓN", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE); titulo.setFont(new Font("Arial", Font.BOLD, 20));
        JButton btnDificultad = new JButton("A. Dificultad");
        JButton btnModoJuego = new JButton("B. Modo de Juego");
        JButton btnRegresar = new JButton("C. Regresar");
        estilizarBotonMenu(btnDificultad); estilizarBotonMenu(btnModoJuego); estilizarBotonMenu(btnRegresar);
        
        btnDificultad.addActionListener(e -> {
            String[] niveles = {"EASY (5 barcos)", "NORMAL (4 barcos)", "EXPERT (2 barcos)", "GENIUS (1 barco)"};
            String res = (String) JOptionPane.showInputDialog(this, "Dificultad:", "Config", 
                    JOptionPane.PLAIN_MESSAGE, null, niveles, niveles[1]);
            if(res != null) {
                if(res.contains("EASY")) Battleship.cantidadBarcosDificultad = 5;
                else if(res.contains("NORMAL")) Battleship.cantidadBarcosDificultad = 4;
                else if(res.contains("EXPERT")) Battleship.cantidadBarcosDificultad = 2;
                else if(res.contains("GENIUS")) Battleship.cantidadBarcosDificultad = 1;
                JOptionPane.showMessageDialog(this, "Dificultad actualizada.");
            }
        });

        btnModoJuego.addActionListener(e -> {
            String[] modos = {"ARCADE", "TUTORIAL"};
            String res = (String) JOptionPane.showInputDialog(this, "Modo de Juego:", "Config", 
                    JOptionPane.PLAIN_MESSAGE, null, modos, Battleship.modoJuego);
            if(res != null) {
                Battleship.modoJuego = res;
                JOptionPane.showMessageDialog(this, "Modo de juego: " + res);
            }
        });

        btnRegresar.addActionListener(e -> cardLayout.show(panelContenedor, "MENU_PRINCIPAL"));
        p.add(titulo); p.add(btnDificultad); p.add(btnModoJuego); p.add(new JLabel()); p.add(new JLabel()); p.add(new JLabel()); p.add(btnRegresar);
        return p;
    }

    private void estilizarBotonMenu(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(50, 50, 60));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}