package ambiente;

import java.util.ArrayList;
import entidade.*;
import excecoes.*;
import obstaculo.Obstaculo;
import robos.*;
import sensores.Sensor;

public class Ambiente {
    private int largura;
    private int altura;
    private int altitude;
    private ArrayList<Entidade> entidades;
    private TipoEntidade[][][] mapa;

    // Construtor
    public Ambiente(int umaLargura, int umaAltura, int umaAltitude) {
        this.largura = umaLargura;
        this.altura = umaAltura;
        this.altitude = umaAltitude;
        this.entidades = new ArrayList<>();
        inicializarMapa();
    }

    // Adiciona uma entidade
    public void adicionarEntidade(Entidade e) {
        entidades.add(e);
    }

    // Remove uma entidade
    public void removerEntidade(Entidade e) {
        entidades.remove(e);
    }

    // Getters
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
    public int getAltitude() { return altitude; }
    public ArrayList<Entidade> getEntidades() { return entidades; }

    public ArrayList<Robo> getRobos() {
        ArrayList<Robo> lista = new ArrayList<>();
        for (Entidade e : entidades) {
            if (e instanceof Robo) {
                lista.add((Robo) e);
            }
        }
        return lista;
    }

    public ArrayList<Obstaculo> getObstaculos() {
        ArrayList<Obstaculo> lista = new ArrayList<>();
        for (Entidade e : entidades) {
            if (e instanceof Obstaculo) {
                lista.add((Obstaculo) e);
            }
        }
        return lista;
    }

    // Verifica se uma posição está dentro dos limites
    public boolean dentroDosLimites(int x, int y, int z) {
        return (x >= 0 && x < largura &&
                y >= 0 && y < altura &&
                z >= 0 && z < altitude);
    }

    // ⚠️ Nova versão robusta - lança exceção se fora dos limites
    public void verificarLimites(int x, int y, int z, Robo robo) throws ForaDosLimitesException {
        if (!dentroDosLimites(x, y, z)) {
            throw new ForaDosLimitesException(robo);
        }
    }

    // Inicializa o mapa
    public void inicializarMapa() {
        mapa = new TipoEntidade[largura][altura][altitude];
        for (int i = 0; i < largura; i++) {
            for (int j = 0; j < altura; j++) {
                for (int k = 0; k < altitude; k++) {
                    mapa[i][j][k] = TipoEntidade.VAZIO;
                }
            }
        }
    }

    // Verifica se uma posição está ocupada
    public boolean estaOcupado(int x, int y, int z) {
        return mapa[x][y][z] != TipoEntidade.VAZIO;
    }

    public TipoEntidade getTipoEntidade(int x, int y, int z) {
        return mapa[x][y][z];
    }

    public void executarSensores() {
        for (Entidade e : entidades) {
            if (e instanceof Robo) {
                Robo robo = (Robo) e;
                if (robo.isLigado()) {
                    System.out.println("\nExecutando sensores do robô " + robo.getId() + ":");
                    for (Sensor sensor : robo.getSensores()) {
                        sensor.monitorar(robo.getX(), robo.getY(), this);
                    }
                } else {
                    System.out.println("\nO robô " + robo.getId() + " está desligado e não executará sensores.");
                }
            }
        }
    }

    public void detectarColisoes() {
        for (Robo r : getRobos()) {
            for (Obstaculo o : getObstaculos()) {
                if (r.getX() >= o.getPosicaoX1() && r.getX() <= o.getPosicaoX2() &&
                    r.getY() >= o.getPosicaoY1() && r.getY() <= o.getPosicaoY2() &&
                    r.getZ() == o.getZ()) {
                    System.out.println("Colisão detectada entre " + r.getId() +
                            " e um obstáculo do tipo " + o.getTipoObstaculo() + "!");
                }
            }
        }
    }

    // Move uma entidade (apenas robôs)
    public void moverEntidade(Entidade e, int novoX, int novoY, int novoZ)
            throws Exception {

        verificarLimites(novoX, novoY, novoZ, (Robo) e);

        if (e.getTipo() == TipoEntidade.OBSTACULO) {
            throw new MoverObstaculoException();
        }

        if (estaOcupado(novoX, novoY, novoZ)) {
            throw new ColisaoException(getTipoEntidade(novoX, novoY, novoZ));
        }

        mapa[e.getX()][e.getY()][e.getZ()] = TipoEntidade.VAZIO;
        mapa[novoX][novoY][novoZ] = e.getTipo();

        int deltaX = novoX - e.getX();
        int deltaY = novoY - e.getY();
        int deltaZ = novoZ - e.getZ();

        if (e instanceof RoboAereo) {
            if (deltaZ > 0) {
                ((RoboAereo) e).subir(deltaZ);
            } else if (deltaZ < 0) {
                ((RoboAereo) e).descer(-deltaZ);
            }
        }

        ((Robo) e).mover(deltaX, deltaY);
        e.atualizarPosicao(novoX, novoY, novoZ);
    }

    // ✅ Novo método - Visualiza o ambiente no plano XY (visão de cima)
    public void visualizarAmbiente() {
        System.out.println("Visualização do ambiente (plano XY, Z=0):");
        for (int y = altura - 1; y >= 0; y--) {
            for (int x = 0; x < largura; x++) {
                boolean encontrado = false;
                for (int z = 0; z < altitude; z++) {
                    if (mapa[x][y][z] != TipoEntidade.VAZIO) {
                        System.out.print(mapa[x][y][z].name().charAt(0) + " ");
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}