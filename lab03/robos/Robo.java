package robos;
import java.util.ArrayList;

import ambiente.Ambiente;
import sensores.Sensor;

// Classe base Robo
public class Robo {
    protected String nomeRobo;
    protected String direcao;
    protected int xPosicao;
    protected int yPosicao;
    private TipoMaterial material; // material do qual é feito o robô (metalico/nao-metalico) 
    protected Ambiente ambiente;
    protected ArrayList<Sensor> sensores; // robôs podem ter sensores

    // Construtor da classe Robo
    public Robo(String nome, String direcaoRobo, int x, int y, Ambiente ambiente, TipoMaterial material) {
        this.nomeRobo = nome;
        this.direcao = direcaoRobo;
        this.xPosicao = x;
        this.yPosicao = y;
        this.ambiente = ambiente;
        this.sensores = new ArrayList<Sensor>();
        this.material = material;
    }

    // Método de movimento do Robo
    public void mover(int deltaX, int deltaY) {
        if (xPosicao + deltaX < 0 || xPosicao + deltaX > ambiente.getLargura() ||
            yPosicao + deltaY < 0 || yPosicao + deltaY > ambiente.getAltura()) {
            System.out.println(nomeRobo + " não pode se mover para fora do ambiente!");
            return;
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
}