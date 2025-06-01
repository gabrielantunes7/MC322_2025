package sensores;

import robos.CavaloRobo;  // Adicione esta linha de importação

import ambiente.Ambiente;

public class SensorStamina extends Sensor {
    private CavaloRobo cavalo;

    public SensorStamina(double alcanceMaximo, CavaloRobo cavalo) {
        super(alcanceMaximo, cavalo);
        this.cavalo = cavalo;
    }

    // Verifica se o cavalo ainda pode se mover
    public boolean temStamina() {
        return cavalo.getMovimentosRealizados() < cavalo.getStaminaMaxima();
    }

    @Override
    public void monitorar(int x, int y, Ambiente ambiente) {
        if (temStamina()) {
            System.out.println("[SensorStamina] O robô tem stamina para continuar se movendo.");
        } else {
            System.out.println("[SensorStamina] Stamina esgotada! O robô não pode mais se mover.");
        }
    }

}