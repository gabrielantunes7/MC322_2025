package ambiente;
import java.util.ArrayList;

import obstaculo.Obstaculo;
import robos.*;
import entidade.*;
import excecoes.*;

// Classe Ambiente
// Usada para representar o ambiente em que objetos da classe robô estão inseridos
public class Ambiente {
    private int largura;
    private int altura; 
    private int altitude;
    private ArrayList<Robo> robos;           // ArrayList utilizado para guardar os robôs que estão no ambiente
    private ArrayList<Obstaculo> obstaculos; // ArrayList utilizado para guardar os obstáculos que estão no ambiente
    private ArrayList<Entidade> entidades; // ArrayList utilizado para guardar as entidades que estão no ambiente
    private TipoEntidade[][][] mapa; // Mapa do ambiente, representando os tipos de entidades em cada posição

    // Inicializador
    // Cria um ambiente com dada largura, altura e altitude e cria um ArrayList para os robôs
    public Ambiente(int umaLargura, int umaAltura, int umaAltitude) {
        largura = umaLargura;
        altura = umaAltura;
        altitude = umaAltitude;
        this.robos = new ArrayList<Robo>();
        this.obstaculos = new ArrayList<Obstaculo>();
        this.entidades = new ArrayList<Entidade>();
        inicializarMapa();
    }

    // Adiciona um robô ao ambiente (coloca-o no ArrayList)
    public void adicionarRobo(Robo r) {
        robos.add(r);
    }

    // Remove um robô do ambiente (remove-o do ArrayList)
    public void removerRobo(Robo r) {
        robos.remove(r);
    }

    // Adiciona um obstàculo ao ambiente (coloca-o no ArrayList)
    public void adicionarObstaculo(Obstaculo o) {
        obstaculos.add(o);
    }

    // Remove um obstáculo do ambiente (remove-o do ArrayList)
    public void removerObstaculo(Obstaculo o) {
        obstaculos.remove(o);
    }

    // Adiciona uma entidade ao ambiente (coloca-a no ArrayList)
    // Essa entidade pode ser um robô ou um obstáculo
    // O método verifica o tipo da entidade e a adiciona ao ArrayList correspondente
    public void adicionarEntidade(Entidade e) {
        entidades.add(e);

        if (e.getTipo() == TipoEntidade.ROBO) 
            robos.add((Robo) e);
        else if (e.getTipo() == TipoEntidade.OBSTACULO)
            obstaculos.add((Obstaculo) e);
    }

    // Remove uma entidade do ambiente (remove-a do ArrayList)
    // O método verifica o tipo da entidade e a remove do ArrayList correspondente
    public void removerEntidade(Entidade e) {
        entidades.remove(e);

        if (e.getTipo() == TipoEntidade.ROBO) 
            robos.remove((Robo) e);
        else if (e.getTipo() == TipoEntidade.OBSTACULO)
            obstaculos.remove((Obstaculo) e);
    }
    
    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }
    
    public int getAltitude() {
        return altitude;
    }

    public ArrayList<Robo> getRobos() {
        return robos;
    }

    public ArrayList<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    public ArrayList<Entidade> getEntidades() {
        return entidades;
    }

    public boolean dentroDosLimites(int x, int y, int altitude) {
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

    public void inicializarMapa() {
        // Inicializa o mapa com o tipo de entidade padrão (vazio)
        mapa = new TipoEntidade[largura][altura][altitude];
        for (int i = 0; i < largura; i++) {
            for (int j = 0; j < altura; j++) {
                for (int k = 0; k < altitude; k++) {
                    mapa[i][j][k] = TipoEntidade.VAZIO;
                }
            }
        }
    }

    public boolean estaOcupado(int x, int y, int z) {
        return mapa[x][y][z] != TipoEntidade.VAZIO;
    }

    public TipoEntidade getTipoEntidade(int x, int y, int z) {
        return mapa[x][y][z];
    }

    public void moverEntidade(Entidade e, int novoX, int novoY, int novoZ) throws Exception {
        if (estaOcupado(novoX, novoY, novoZ))
            throw new ColisaoException(getTipoEntidade(novoX, novoY, novoZ));

        else if (e.getTipo() == TipoEntidade.OBSTACULO) {
            throw new MoverObstaculoException();
        }
        else {
            // Atualiza o mapa removendo a entidade da posição antiga
            mapa[e.getX()][e.getY()][e.getZ()] = TipoEntidade.VAZIO;

            // Atualiza o mapa colocando a entidade na nova posição
            mapa[novoX][novoY][novoZ] = e.getTipo();

            // Atualiza a posição da entidade (robô, porque não é possível mover um obstáculo)
            int deltaX = novoX - e.getX();
            int deltaY = novoY - e.getY();
            int deltaZ = novoZ - e.getZ();
            if (e instanceof RoboAereo) {
                if (deltaZ > 0) 
                    ((RoboAereo) e).subir(deltaZ);
                else if (deltaZ < 0)
                    ((RoboAereo) e).descer(deltaZ);
            }

            ((Robo) e).mover(deltaX, deltaY);
        }
    }
}