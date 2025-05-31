package comunicacao;

import interfaces.Comunicavel;

// Classe Mensagem
// Representa uma mensagem trocada entre robôs aéreos
// Contém remetente, conteúdo e receptor
public class Mensagem {
    private Comunicavel remetente;
    private String conteudo;
    private Comunicavel receptor;

    public Mensagem(Comunicavel remetente, String conteudo, Comunicavel receptor) {
        this.remetente = remetente;
        this.conteudo = conteudo;
        this.receptor = receptor;
    }

    public Comunicavel getRemetente() {
        return remetente;
    }

    public String getConteudo() {
        return conteudo;
    }

    public Comunicavel getReceptor() {
        return receptor;
    }
}