package battleship.dinamico;

import java.util.ArrayList;

public class Player {
    private String username;
    private String password;
    private int puntos;
    private ArrayList<String> historial; 

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.historial = new ArrayList<>();
    }

    // --- MÉTODOS GETTERS (YA LOS TENÍAS) ---
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getPuntos() { return puntos; }
    public ArrayList<String> getHistorial() { return historial; }

    // --- MÉTODOS SETTERS (ESTOS SON LOS QUE TE FALTABAN) ---
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // --- MÉTODOS DE LÓGICA ---
    public void addPuntos(int p) { 
        this.puntos += p; 
    }

    public void registrarJuego(String resultado) {
        historial.add(0, resultado);
        if (historial.size() > 10) {
            historial.remove(10);
        }
    }
}