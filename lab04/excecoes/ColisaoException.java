package excecoes;

import interfaces.TipoEntidade;

public class ColisaoException extends Exception {
    public ColisaoException(TipoEntidade tipo) {
        super("Colisão detectada com " + tipo.getNome());
    }
}
