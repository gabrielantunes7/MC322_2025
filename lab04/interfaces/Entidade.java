package interfaces;

public interface Entidade {
    public int getX();
    public int getY();
    public int getZ();
    public TipoEntidade getTipo();
    public String getDescricao();
    public char getRepresentacao();
    void atualizarPosicao(int x, int y, int z);
}
