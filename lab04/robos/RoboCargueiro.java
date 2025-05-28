package robos;

import ambiente.Ambiente;
import excecoes.CargaExcessivaException;

// Classe RoboCargueiro (herda de RoboAereo)
public class RoboCargueiro extends RoboAereo {
    private int capacidadeCarga; // Capacidade máxima de carga (kg)

    // Construtor
    public RoboCargueiro(String id, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima, int capacidade, TipoMaterial material) {
        super(id, direcao, x, y, ambiente, altitudeMaxima, material);
        this.capacidadeCarga = capacidade;
    }

    // Método para levar uma carga até uma nova posição
    public void levarCarga(int pesoCarga, int deltaX, int deltaY) throws Exception {
        if (pesoCarga > capacidadeCarga) {
            throw new CargaExcessivaException(this, pesoCarga, capacidadeCarga);
        }

        int novoX = this.xPosicao + deltaX;
        int novoY = this.yPosicao + deltaY;

        if (!ambiente.dentroDosLimites(novoX, novoY, zPosicao)) {
            System.out.println(getId() + " não pode se mover para fora dos limites do ambiente!");
            return;
        }

        super.mover(deltaX, deltaY);
        System.out.println(getId() + " levou uma carga de " + pesoCarga + " kg para a posição (" + novoX + ", " + novoY + ", " + zPosicao + ")!");
    }

    // 🔥 Implementação obrigatória do lab
    @Override
    public void executarTarefa() {
        try {
            // Exemplo simples de tarefa: levar carga de 10 kg para 2 unidades à frente
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
}
