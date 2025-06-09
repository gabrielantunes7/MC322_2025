package missoes;

import robos.Robo;
import ambiente.Ambiente;

public abstract class Missao {
    public abstract void executar(Robo r, Ambiente ambiente) throws Exception;
}
