package interfaces;

import missoes.Missao;

public interface AgenteInteligente {
    public abstract void executarMissao(Missao missao) throws Exception;
}
