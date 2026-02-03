/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleship.dinamico;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Battleship {
    private List<Barco> barcos = new ArrayList<>();
    private Random rand = new Random();

    public Battleship() {
        // Definimos la flota con las formas que pediste
        // Nota: alto y ancho se pasan según la forma inicial
        barcos.add(new Barco("Portaaviones", 2, 3));
        barcos.add(new Barco("Acorazado", 4, 1));
        barcos.add(new Barco("Submarino", 3, 1));
        barcos.add(new Barco("Destructor", 2, 1));
        
        // Al iniciar, los esparcimos por el tablero
        posicionarTodaLaFlota();
    }

    private void posicionarTodaLaFlota() {
        for (Barco b : barcos) {
            boolean colocado = false;
            while (!colocado) {
                // Generar coordenadas al azar entre 0 y 7
                int nuevaF = rand.nextInt(8);
                int nuevaC = rand.nextInt(8);
                
                // Intentar rotar aleatoriamente al inicio (opcional)
                if (rand.nextBoolean()) b.rotar();

                // Verificar si cabe en los bordes y no choca con otros
                if (nf_dentro(nuevaF, b.alto) && nc_dentro(nuevaC, b.ancho)) {
                    b.fila = nuevaF;
                    b.col = nuevaC;
                    
                    if (!hayColision(b)) {
                        colocado = true; // Éxito, pasamos al siguiente barco
                    }
                }
            }
        }
    }

    // Auxiliares para no salir de los bordes (8x8)
    private boolean nf_dentro(int f, int alto) { return f + alto <= 8; }
    private boolean nc_dentro(int c, int ancho) { return c + ancho <= 8; }

    public boolean hayColision(Barco b1) {
        for (Barco b2 : barcos) {
            if (b1 == b2 || b2.fila == -1) continue; // No comparar consigo mismo o barcos no colocados
            if (b1.fila < b2.fila + b2.alto && b1.fila + b1.alto > b2.fila &&
                b1.col < b2.col + b2.ancho && b1.col + b1.ancho > b2.col) {
                return true;
            }
        }
        return false;
    }

    // El resto de los métodos (moverBarco, getBarcoEn, etc.) se mantienen igual...
    public List<Barco> getBarcos() { return barcos; }
    
    public Barco getBarcoEn(int f, int c) {
        for (Barco b : barcos) {
            if (f >= b.fila && f < b.fila + b.alto && c >= b.col && c < b.col + b.ancho) return b;
        }
        return null;
    }

    public boolean moverBarco(Barco b, int nf, int nc) {
        int af = b.fila, ac = b.col;
        if (nf + b.alto > 8 || nc + b.ancho > 8) return false;
        b.fila = nf; b.col = nc;
        if (hayColision(b)) {
            b.fila = af; b.col = ac;
            return false;
        }
        return true;
    }

    public class Barco {
        String nombre;
        int fila = -1, col = -1, alto, ancho;

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
}