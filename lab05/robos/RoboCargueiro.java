package robos;

import ambiente.Ambiente;
import excecoes.CargaExcessivaException;
import excecoes.RoboDesligadoException;
import interfaces.Carregavel;
import missoes.Missao;
import missoes.MissaoLevarCarga;
import interfaces.AgenteInteligente;
import excecoes.ForaDosLimitesException;

public class RoboCargueiro extends RoboAereo implements Carregavel, AgenteInteligente {
    private int capacidadeCarga;
    private MissaoLevarCarga missao;

    public RoboCargueiro(String id, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima, int capacidade, TipoMaterial material) {
        super(id, direcao, x, y, ambiente, altitudeMaxima, material);
        this.capacidadeCarga = capacidade;
        this.missao = null; // Inicialmente sem missão
    }

    @Override
    public void levarCarga(int pesoCarga, int deltaX, int deltaY) throws Exception {
        if (!isLigado()) {
            throw new RoboDesligadoException(this);
        }

        if (pesoCarga > capacidadeCarga) {
            throw new CargaExcessivaException(this, pesoCarga, capacidadeCarga);
        }

        int novoX = this.xPosicao + deltaX;
        int novoY = this.yPosicao + deltaY;

        if (!ambiente.dentroDosLimites(novoX, novoY, zPosicao)) {
            throw new ForaDosLimitesException(this);
        }

        super.mover(deltaX, deltaY);
        System.out.println(getId() + " levou uma carga de " + pesoCarga + " kg para a posição (" + novoX + ", " + novoY + ", " + zPosicao + ")!");
    }

    @Override
    public int getCapacidadeCarga() {
        return capacidadeCarga;
    }

    @Override
    public void executarTarefa() {
        try {
            levarCarga(10, 2, 0);
            System.out.println(getId() + " executou sua tarefa padrão de transporte de carga.");
        } catch (Exception e) {
            System.out.println(getId() + " não pôde executar a tarefa: " + e.getMessage());
        }
    }

    @Override
    public String getDescricao() {
        return "RoboCargueiro: " + getId() + ", Direção: " + direcao + ", Posição: (" + xPosicao + ", " + yPosicao + ", " + getZ() + "), Capacidade de Carga: " + capacidadeCarga + " kg";
    }

    @Override
    public char getRepresentacao() {
        return 'C'; // 'C' de Cargueiro
    }

    public void receberMissao(MissaoLevarCarga missao) {
        this.missao = missao;
        System.out.println(getId() + " recebeu a missão de levar carga: " + missao.getCarga() + " kg para (" + missao.getXChegada() + ", " + missao.getYChegada() + ", " + missao.getZChegada() + ")");
    }

    public MissaoLevarCarga getMissao() {
        return missao;
    }

    @Override
    public void executarMissao(Missao missao) throws Exception {
        missao.executar(this, ambiente);
    }
}
