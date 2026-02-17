package battleship.dinamico;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battleship {
    public static ArrayList<Player> listaPlayers = new ArrayList<>();
    public static Player currentUser = null; 
    
    public static int cantidadBarcosDificultad = 4;
    public static String modoJuego = "TUTORIAL";

    private List<Barco> barcos = new ArrayList<>();
    private boolean faseAtaque = false;

    public Battleship() {
        configurarFlota();
        posicionarTodaLaFlota();
    }
    
    private void configurarFlota() {
        barcos.clear();
        if (cantidadBarcosDificultad >= 1) barcos.add(new Barco("Portaaviones", 2, 3, 5));
        if (cantidadBarcosDificultad >= 2) barcos.add(new Barco("Acorazado", 4, 1, 4));
        if (cantidadBarcosDificultad >= 3) barcos.add(new Barco("Submarino", 3, 1, 3));
        if (cantidadBarcosDificultad >= 4) barcos.add(new Barco("Destructor", 2, 1, 2));
        if (cantidadBarcosDificultad >= 5) barcos.add(new Barco("Fragata", 1, 2, 1));
    }

    public static Player buscarPlayer(String user) {
        for (Player p : listaPlayers) {
            if (p.getUsername().equalsIgnoreCase(user)) return p;
        }
        return null;
    }

    private void posicionarTodaLaFlota() {
        reubicarTodaLaFlota();
    }

    public void reubicarTodaLaFlota() {
        Random rand = new Random();
        for (Barco b : barcos) {
            boolean colocado = false;
            while (!colocado) {
                int nf = rand.nextInt(8);
                int nc = rand.nextInt(8);
                if (rand.nextBoolean()) b.rotar();

                int oldF = b.fila; int oldC = b.col;
                b.fila = nf; b.col = nc;

                if (nf + b.alto <= 8 && nc + b.ancho <= 8) {
                    if (!hayColision(b)) {
                        colocado = true;
                    }
                }
                
                if (!colocado) {
                    b.fila = oldF; b.col = oldC;
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

    // metodo para gestionar lista
    public List<Barco> getBarcosList() {
        return barcos;
    }

    public void eliminarBarco(Barco b) {
        barcos.remove(b);
    }

    public void activarFaseAtaque() { this.faseAtaque = true; }
    public boolean isFaseAtaque() { return faseAtaque; }

    // clase barco
    public class Barco {
        public String nombre;
        public int fila, col, alto, ancho;
        public int vidas;

        Barco(String n, int al, int an, int v) { 
            nombre = n; alto = al; ancho = an; vidas = v;
        }
        
        public void rotar() { int t = alto; alto = ancho; ancho = t; }

        public boolean estaHundido(char[][] disparosOponente) {
            boolean tocado = false;
            for (int i = fila; i < fila + alto; i++) {
                for (int j = col; j < col + ancho; j++) {
                    if (disparosOponente[i][j] == 'X') {
                        tocado = true;
                        break;
                    }
                }
            }

            if (tocado) {
                if (vidas > 1) {
                    vidas--; 
                    Battleship.this.reubicarTodaLaFlota();
                    // Limpiamos disparos porque se movieron
                    limpiarDisparos(disparosOponente);
                    return false; 
                } else {
                    vidas = 0;
                    return true; // HUNDIDO DEFINITIVAMENTE
                }
            }
            return false;
        }

        private void limpiarDisparos(char[][] disparos) {
            for(int x = 0; x < 8; x++) {
                for(int y = 0; y < 8; y++) {
                    if (disparos[x][y] == 'X') disparos[x][y] = 0;
                }
            }
        }
    }
}