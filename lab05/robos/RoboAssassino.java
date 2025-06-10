package robos;

import interfaces.missoes.MissaoDestruirRobo;
import ambiente.Ambiente;
import sensores.SensorFrontal;

public class RoboAssassino extends AgenteInteligente {
    MissaoDestruirRobo missao;

    public RoboAssassino(String id, String direcao, int x, int y, Ambiente ambiente, TipoMaterial material, SensorFrontal sensor, MissaoDestruirRobo missao) {
        super(id, direcao, x, y, ambiente, material, sensor);
        this.missao = missao;
    }

    @Override
    public void executarTarefa() throws Exception{
        executarMissao();
        System.out.println(getId() + " executou sua tarefa de assassinar um robô inimigo.");
    }

    @Override
    public void executarMissao() throws Exception{
       missao.executar(this, ambiente);
    }
}
