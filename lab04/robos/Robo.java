package robos;
import java.util.ArrayList;

import ambiente.Ambiente;
import sensores.Sensor;
import entidade.*;
import excecoes.*;
import sensoreavel.*;

// Classe base Robo
public class Robo implements Entidade, Sensoreavel {
    protected String nomeRobo;
    protected String direcao;
    protected int xPosicao;
    protected int yPosicao;
    private TipoMaterial material; // material do qual é feito o robô (metalico/nao-metalico) 
    protected Ambiente ambiente;
    protected ArrayList<Sensor> sensores; // robôs podem ter sensores
    private Status status; // status do robô (ligado/desligado)

    // Construtor da classe Robo
    public Robo(String nome, String direcaoRobo, int x, int y, Ambiente ambiente, TipoMaterial material) {
        this.nomeRobo = nome;
        this.direcao = direcaoRobo;
        this.xPosicao = x;
        this.yPosicao = y;
        this.ambiente = ambiente;
        this.sensores = new ArrayList<Sensor>();
        this.material = material;
        this.status = Status.DESLIGADO; // Inicialmente o robô está desligado
    }

    // Método de movimento do Robo
    public void mover(int deltaX, int deltaY) throws Exception {
        if (xPosicao + deltaX < 0 || xPosicao + deltaX > ambiente.getLargura() ||
            yPosicao + deltaY < 0 || yPosicao + deltaY > ambiente.getAltura()) {
            throw new ForaDosLimitesException(this);
        }

        xPosicao += deltaX;
        yPosicao += deltaY;
    }

    // Método que exibe a posição de dado robô na tela
    public void exibirPosicao() {
        System.out.println(nomeRobo + " está em: (" + xPosicao + ", " + yPosicao + ")");
    }

    public String getNomeRobo() {
        return nomeRobo;
    }

    public int getPosicaoX() {
        return xPosicao;
    }

    public int getPosicaoY() {
        return yPosicao;
    }

    public String getDirecao() {
        return direcao;
    }

    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    // Sensores podem ser adicionados e removidos dos robôs
    public void adicionarSensor(Sensor s) {
        sensores.add(s);
    }

    public void removerSensor(Sensor s) {
        sensores.remove(s);
    }

    public ArrayList<Sensor> getSensores() {
        return sensores;
    }

    public TipoMaterial getMaterial() {
        return material;
    }

    public void usarSensor(Sensor s) {
        if (sensores.contains(s)) {
            s.monitorar(xPosicao, yPosicao, ambiente);
        } else {
            System.out.println("Sensor não encontrado!");
        }
    }

    public void acionarSensores(Robo r) throws RoboDesligadoException {
        if (status == Status.DESLIGADO) {
            throw new RoboDesligadoException(r);
        }
        for (Sensor s: sensores) {
            s.monitorar(xPosicao, yPosicao, ambiente);
        }
    }

    public boolean isLigado() {
        return status == Status.LIGADO;
    }

    public void alternarStatus() {
        if (status == Status.LIGADO)
            status = Status.DESLIGADO;
        else
            status = Status.LIGADO;
    }

    @Override
    public int getX() {
        return xPosicao;
    }

    @Override
    public int getY() {
        return yPosicao;
    }

    @Override
    public int getZ() {
        return 0; // Z não é utilizado, mas implementado para satisfazer a interface
    }

    @Override
    public TipoEntidade getTipo() {
        return TipoEntidade.ROBO;
    }

    @Override
    public String getDescricao() {
        return "Robo: " + nomeRobo + ", Direção: " + direcao + ", Posição: (" + xPosicao + ", " + yPosicao + ")";
    }

    @Override
    public char getRepresentacao() {
        return 'R'; // Representação do robô no ambiente
    }
}