package interfaces.missoes;

import robos.Robo;
import ambiente.Ambiente;

public interface Missao {
    public String getDescricaoMissao();
    public void executar(Robo r, Ambiente ambiente) throws Exception;
}
