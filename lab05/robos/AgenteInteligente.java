package robos;

import ambiente.Ambiente;
import interfaces.missoes.Missao;
import sensores.SensorFrontal;
import excecoes.ForaDosLimitesException;
import excecoes.RoboDesligadoException;
import sensores.SensorLimiteAmbiente;

public abstract class AgenteInteligente extends Robo {
    private Missao missao;
    private SensorFrontal sensor;
    protected SensorLimiteAmbiente sensorLimite;

    public AgenteInteligente(String id, String direcao, int x, int y, Ambiente ambiente, TipoMaterial material, SensorFrontal sensor) {
        super(id, direcao, x, y, ambiente, material);
        this.missao = null;
        this.sensor = sensor;
        this.sensorLimite = new SensorLimiteAmbiente(Double.MAX_VALUE, this);
        this.adicionarSensor(sensorLimite);
    }

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

    private void atualizarDirecao(int deltaX, int deltaY) {
        if (deltaX > 0) {
            this.direcao = "LESTE";
        } else if (deltaX < 0) {
            this.direcao = "OESTE";
        } else if (deltaY > 0) {
            this.direcao = "NORTE";
        } else {
            this.direcao = "SUL";
        }
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

    public void setSensor(SensorFrontal sensor) {
        this.sensor = sensor;
    }

    protected void printarMovimento(String direcao, int x, int y, int z) {
        System.out.println("\n=== " + getId() + " - Movimento " + direcao + " ===");
        System.out.println("Posição atual: (" + x + "," + y + "," + z + ")");
        System.out.println("Direção: " + getDirecao());
        System.out.println(ambiente.toStringNivelZ(z));
        System.out.println("=====================================\n");
        
        // Adiciona uma pequena pausa para melhor visualização
        try {
            Thread.sleep(500); // Pausa de 500ms entre cada movimento
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public abstract void executarMissao() throws Exception;
}