package excecoes;

import entidade.*;

public class ColisaoException extends Exception {
    public ColisaoException(TipoEntidade tipo) {
        super("Colisão detectada com " + tipo.getNome());
    }
}
