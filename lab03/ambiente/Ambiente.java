package ambiente;
import java.util.ArrayList;

import obstaculo.Obstaculo;
import robos.*;

// Classe Ambiente
// Usada para representar o ambiente em que objetos da classe robô estão inseridos
public class Ambiente{
    private int largura;
    private int altura; 
    private int altitude;
    private ArrayList<Robo> robos;           // ArrayList utilizado para guardar os robôs que estão no ambiente
    private ArrayList<Obstaculo> obstaculos; // ArrayList utilizado para guardar os obstáculos que estão no ambiente

    // Inicializador
    // Cria um ambiente com dada largura, altura e altitude e cria um ArrayList para os robôs
    public Ambiente(int umaLargura, int umaAltura, int umaAltitude){
        largura = umaLargura;
        altura = umaAltura;
        altitude = umaAltitude;
        this.robos = new ArrayList<Robo>();
        this.obstaculos = new ArrayList<Obstaculo>();
    }

    // Adiciona um robô ao ambiente (coloca-o no ArrayList)
    public void adicionarRobo(Robo r){
        robos.add(r);
    }

    // Remove um robô do ambiente (remove-o do ArrayList)
    public void removerRobo(Robo r){
        robos.remove(r);
    }

    // Adiciona um obstàculo ao ambiente (coloca-o no ArrayList)
    public void adicionarObstaculo(Obstaculo o){
        obstaculos.add(o);
    }

    // Remove um obstáculo do ambiente (remove-o do ArrayList)
    public void removerObstaculo(Obstaculo o){
        obstaculos.remove(o);
    }
    
    public int getLargura(){
        return largura;
    }

    public int getAltura(){
        return altura;
    }
    
    public int getAltitude(){
        return altitude;
    }

    public ArrayList<Robo> getRobos() {
        return robos;
    }

    public ArrayList<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    public boolean dentroDosLimites(int x, int y, int altitude){
        // Verifica se a posição (x, y) está dentro dos limites do ambiente
        if (x >= 0 && x < largura && y >= 0 && y < altura && altitude >= 0 && altitude <= this.altitude)
            return true;
        else
            return false;
    }

    // Verifica se há colisões entre robôs e obstáculos
    public void detectarColisoes(){
        for (Robo r: robos)
            for (Obstaculo o: obstaculos){
                if (r.getPosicaoX() >= o.getPosicaoX1() && r.getPosicaoX() <= o.getPosicaoX2() &&
                    r.getPosicaoY() >= o.getPosicaoY1() && r.getPosicaoY() <= o.getPosicaoY2()){
                    System.out.println("Colisão detectada entre " + r.getNomeRobo() + " e um obstáculo do tipo " + o.getTipo() + "!");
                }
            }
    }
}