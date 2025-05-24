package robos;

import ambiente.Ambiente;
import sensores.SensorMovimentoCavalo;
import sensores.SensorStamina;
import sensores.SensorLimiteAmbiente;

// Classe CavaloRobo (herdada de RoboTerrestre)
// Representa um robô que se move em L, como um cavalo no xadrez
// O CavaloRobo tem stamina e pode se mover apenas se tiver energia
public class CavaloRobo extends RoboTerrestre {
    private int stamina;
    private int movimentosRealizados;
    private SensorMovimentoCavalo sensorMovimento;
    private SensorStamina sensorStamina;
    private SensorLimiteAmbiente sensorLimite;

    // Construtor do CavaloRobo
    public CavaloRobo(String nome, String direcao, int x, int y, int distanciaMaxima, int stamina,
                      Ambiente ambiente, TipoMaterial material) {
        super(nome, direcao, x, y, ambiente, distanciaMaxima, material);
        this.stamina = stamina;
        this.movimentosRealizados = 0;

        // Inicializa sensores
        this.sensorMovimento = new SensorMovimentoCavalo(10.0, this);
        this.sensorStamina = new SensorStamina(10.0, this);
        this.sensorLimite = new SensorLimiteAmbiente(10.0, this);
        

        this.adicionarSensor(sensorMovimento);
        this.adicionarSensor(sensorStamina);
        this.adicionarSensor(sensorLimite);
    }

    // Sobrescrita do método de movimento (em L, como no Xadrez)
    @Override
    public void mover(int deltaX, int deltaY) throws Exception {
        if (!sensorStamina.temStamina()) {
            System.out.println(nomeRobo + " está sem energia para se mover!");
            return;
        }

        if (!sensorMovimento.movimentoValido(deltaX, deltaY)) {
            System.out.println(nomeRobo + " só pode se mover em L como um cavalo no xadrez!");
            return;
        }

        // Se passou em todos os sensores, pode mover
        super.mover(deltaX, deltaY);
        movimentosRealizados++;
        System.out.println(nomeRobo + " moveu-se em L para: (" + xPosicao + ", " + yPosicao + ")");
    }

    // Método para restaurar stamina
    public void resetStamina() {
        movimentosRealizados = 0;
        System.out.println(nomeRobo + " recuperou sua energia!");
    }

    // Métodos auxiliares para sensores
    public int getStaminaMaxima() {
        return stamina;
    }

    public int getMovimentosRealizados() {
        return movimentosRealizados;
    }


    @Override
    public String getDescricao() {
        return "CavaloRobo: " + nomeRobo + ", Posição: (" + xPosicao + ", " + yPosicao + "), Stamina: " + stamina +
               ", Movimentos Realizados: " + movimentosRealizados;
    }

    @Override
    public char getRepresentacao() {
        return 'H'; // Representação do Cavalo no ambiente ("horse", o RoboCargueiro já é 'C')
    }
}
