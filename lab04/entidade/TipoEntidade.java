package entidade;

public enum TipoEntidade {
    ROBO("robô"),
    OBSTACULO("obstáculo"),
    VAZIO("desconhecido");

    private String nome;

    TipoEntidade(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}