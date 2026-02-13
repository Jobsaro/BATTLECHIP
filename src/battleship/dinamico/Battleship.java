package battleship.dinamico;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battleship {
    public static ArrayList<Player> listaPlayers = new ArrayList<>();
    public static Player currentUser = null; 
    
    // REQUISITOS DE CONFIGURACIÓN
    public static int cantidadBarcosDificultad = 4; // Default NORMAL
    public static String modoJuego = "TUTORIAL";    // Default TUTORIAL

    private List<Barco> barcos = new ArrayList<>();
    private boolean faseAtaque = false;

    public Battleship() {
        configurarFlota();
        posicionarTodaLaFlota();
    }
    
    private void configurarFlota() {
        barcos.clear();
        // EASY: 5, NORMAL: 4, EXPERT: 2, GENIUS: 1
        if (cantidadBarcosDificultad >= 1) barcos.add(new Barco("Portaaviones", 2, 3));
        if (cantidadBarcosDificultad >= 2) barcos.add(new Barco("Acorazado", 4, 1));
        if (cantidadBarcosDificultad >= 3) barcos.add(new Barco("Submarino", 3, 1));
        if (cantidadBarcosDificultad >= 4) barcos.add(new Barco("Destructor", 2, 1));
        if (cantidadBarcosDificultad >= 5) barcos.add(new Barco("Fragata", 1, 2));
    }

    public static Player buscarPlayer(String user) {
        for (Player p : listaPlayers) {
            if (p.getUsername().equalsIgnoreCase(user)) return p;
        }
        return null;
    }

    private void posicionarTodaLaFlota() {
        Random rand = new Random();
        for (Barco b : barcos) {
            boolean colocado = false;
            while (!colocado) {
                int nf = rand.nextInt(8), nc = rand.nextInt(8);
                if (rand.nextBoolean()) b.rotar();
                if (nf + b.alto <= 8 && nc + b.ancho <= 8) {
                    b.fila = nf; b.col = nc;
                    if (!hayColision(b)) colocado = true;
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
        if (faseAtaque) return;
        int af = b.fila, ac = b.col;
        b.fila = nf; b.col = nc;
        if (hayColision(b) || nf + b.alto > 8 || nc + b.ancho > 8) {
            b.fila = af; b.col = ac;
        }
    }

    public void intentarRotar(Barco b) {
        if (faseAtaque) return;
        b.rotar();
        if (b.fila + b.alto > 8 || b.col + b.ancho > 8 || hayColision(b)) b.rotar();
    }

    public Barco getBarcoEn(int f, int c) {
        for (Barco b : barcos) {
            if (f >= b.fila && f < b.fila + b.alto && c >= b.col && c < b.col + b.ancho) return b;
        }
        return null;
    }

    public void activarFaseAtaque() { this.faseAtaque = true; }
    public boolean isFaseAtaque() { return faseAtaque; }

    public static class Barco {
        String nombre;
        int fila, col, alto, ancho;
        Barco(String n, int al, int an) { nombre = n; alto = al; ancho = an; }
        public void rotar() { int t = alto; alto = ancho; ancho = t; }
    }
}