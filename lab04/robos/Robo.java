package robos;

import java.util.ArrayList;

import ambiente.Ambiente;
import excecoes.*;
import interfaces.Entidade;
import interfaces.Sensoreavel;
import interfaces.TipoEntidade;
import sensores.*;


public abstract class Robo implements Entidade, Sensoreavel {
    protected String id;
    protected EstadoRobo estado;
    protected TipoEntidade tipoEntidade;
    protected int xPosicao;
    protected int yPosicao;
    protected int zPosicao;
    protected String direcao; // 🔥 -> Direção adicionada corretamente

    protected Ambiente ambiente;
    protected ArrayList<Sensor> sensores;
    protected TipoMaterial material;

    // ✅ Construtor
    public Robo(String id, String direcao, int x, int y, Ambiente ambiente, TipoMaterial material) {
        this.id = id;
        this.direcao = direcao; // 🔥 Direção sendo atribuída corretamente
        this.estado = EstadoRobo.DESLIGADO;
        this.tipoEntidade = TipoEntidade.ROBO;
        this.xPosicao = x;
        this.yPosicao = y;
        this.zPosicao = 0;

        this.ambiente = ambiente;
        this.sensores = new ArrayList<>();
        this.material = material;
    }

    // ✅ Metodo exigido: moverPara
    public void moverPara(int x, int y, int z) throws Exception {
        if (!ambiente.dentroDosLimites(x, y, z)) {
            throw new ForaDosLimitesException(this);
        }
        this.xPosicao = x;
        this.yPosicao = y;
        this.zPosicao = z;
    }

    // ✅ Método auxiliar de movimento (XY apenas, como antes)
    public void mover(int deltaX, int deltaY) throws Exception {
        moverPara(xPosicao + deltaX, yPosicao + deltaY, zPosicao);
    }

    // ✅ Estado do robo
    public void ligar() {
        estado = EstadoRobo.LIGADO;
    }

    public void desligar() {
        estado = EstadoRobo.DESLIGADO;
    }

    public boolean isLigado() {
        return estado == EstadoRobo.LIGADO;
    }

    // ✅ Metodo abstrato exigido
    public abstract void executarTarefa();

    // ✅ Sensores
    public void adicionarSensor(Sensor s) {
        sensores.add(s);
    }

    public void removerSensor(Sensor s) {
        sensores.remove(s);
    }

    public ArrayList<Sensor> getSensores() {
        return sensores;
    }

    public void acionarSensores(Robo r) throws RoboDesligadoException {
        if (!isLigado()) {
            throw new RoboDesligadoException(r);
        }
        for (Sensor s : sensores) {
            s.monitorar(xPosicao, yPosicao, ambiente);
        }
    }

    // ✅ Implementação de Entidade
    @Override
    public void atualizarPosicao(int x, int y, int z) {
        this.xPosicao = x;
        this.yPosicao = y;
        this.zPosicao = z;
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
        return zPosicao;
    }

    @Override
    public TipoEntidade getTipo() {
        return tipoEntidade;
    }

    @Override
    public String getDescricao() {
        return "Robo: " + id + ", Estado: " + estado + ", Direção: " + direcao + ", Posição: (" + xPosicao + ", " + yPosicao + ", " + zPosicao + ")";
    }

    @Override
    public char getRepresentacao() {
        return 'R';
    }

    // ✅ Getters padrão
    public String getId() {
        return id;
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

    public TipoMaterial getMaterial() {
        return material;
    }
}
