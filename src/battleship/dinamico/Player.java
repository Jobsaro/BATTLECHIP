/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleship.dinamico;

/**
 *
 * @author Administrator
 */
public class Player {
    private String username;
    private String password;
    private int puntos
    // Historial de ultimos 10 juegos 
    private String[] logs; 

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0; 
        this.logs = new String[10];
    }
   
}