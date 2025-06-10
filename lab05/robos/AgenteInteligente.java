package robos;

import ambiente.Ambiente;
import interfaces.missoes.Missao;
import sensores.SensorFrontal;
import excecoes.ForaDosLimitesException;
import excecoes.RoboDesligadoException;

public abstract class AgenteInteligente extends Robo {
    private Missao missao;
    private SensorFrontal sensor;

    public AgenteInteligente(String id, String direcao, int x, int y, Ambiente ambiente, TipoMaterial material, SensorFrontal sensor) {
        super(id, direcao, x, y, ambiente, material);
        this.missao = null; // Inicialmente sem missão
        this.sensor = sensor; // Sensor para detecção de obstáculos
    }

    public Missao getMissao() {
        return missao;
    }

    public void setMissao(Missao missao) {
        this.missao = missao;
    }

    public SensorFrontal getSensor() {
        return sensor;
    }

    // movimentação autônoma do Agente Inteligente
    @Override
    public void moverPara(int x, int y, int z) throws Exception {
        if (!ambiente.dentroDosLimites(x, y, z)) {
            throw new ForaDosLimitesException(this);
        }
        if (!isLigado()) {
            throw new RoboDesligadoException(this);
        }

        // define a direção inicial do robô com base na posição atual e na posição de destino
        if (y - this.yPosicao > 0)
            this.direcao = "NORTE";
        else
            this.direcao = "SUL";

        while (this.xPosicao != x || this.yPosicao != y || this.zPosicao != z) {
            switch(this.direcao) {
                case "NORTE":
                    if (!ambiente.dentroDosLimites(this.xPosicao, this.yPosicao + 1, this.zPosicao)) {
                        throw new ForaDosLimitesException(this);
                    }
                    if (sensor.encontrouObstaculo(this.xPosicao, this.yPosicao, ambiente)) {
                        if (x - this.xPosicao > 0) 
                            this.direcao = "LESTE";
                        else
                            this.direcao = "OESTE";

                        continue; // Tenta mover na nova direção
                    }
                    this.yPosicao++;
                    break;
                case "SUL":
                    if (!ambiente.dentroDosLimites(this.xPosicao, this.yPosicao - 1, this.zPosicao)) {
                        throw new ForaDosLimitesException(this);
                    }
                    if (sensor.encontrouObstaculo(this.xPosicao, this.yPosicao, ambiente)) {
                        if (x - this.xPosicao > 0) 
                            this.direcao = "LESTE";
                        else
                            this.direcao = "OESTE";

                        continue; // Tenta mover na nova direção
                    }
                    this.yPosicao--;
                    break;
                case "LESTE":
                    if (!ambiente.dentroDosLimites(this.xPosicao + 1, this.yPosicao, this.zPosicao)) {
                        throw new ForaDosLimitesException(this);
                    }
                    if (sensor.encontrouObstaculo(this.xPosicao, this.yPosicao, ambiente)) {
                        if (y - this.yPosicao > 0) 
                            this.direcao = "NORTE";
                        else
                            this.direcao = "SUL";

                        continue; // Tenta mover na nova direção
                    }
                    this.xPosicao++;
                    break;
                case "OESTE":
                    if (!ambiente.dentroDosLimites(this.xPosicao - 1, this.yPosicao, this.zPosicao)) {
                        throw new ForaDosLimitesException(this);
                    }
                    if (sensor.encontrouObstaculo(this.xPosicao, this.yPosicao, ambiente)) {
                        if (y - this.yPosicao > 0) 
                            this.direcao = "NORTE";
                        else
                            this.direcao = "SUL";

                        continue; // Tenta mover na nova direção
                    }
                    this.xPosicao--;
                    break;
                default:
                    break;
            }
        }
        // se conseguiu chegar ao destino, atualiza a posição no ambiente
        ambiente.moverEntidade(this, x, y, z);
    }

    public abstract void executarMissao() throws Exception;
}
