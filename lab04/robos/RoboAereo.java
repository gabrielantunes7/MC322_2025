package robos;

import ambiente.Ambiente;
import comunicavel.*;
import excecoes.RoboDesligadoException;

// Classe RoboAereo
// Somente robôs aéreos são comunicáveis
public class RoboAereo extends Robo implements Comunicavel {
    private int altitude;
    private int altitudeMaxima;
    private String mensagem; // Mensagem guardada na memória (vinda de outro comunicável)

    // Construtor do RoboAereo
    public RoboAereo(String nome, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima, TipoMaterial material) {
        super(nome, direcao, x, y, ambiente, material);
        this.altitude = 0;
        this.altitudeMaxima = altitudeMaxima;
    }

    // Método de movimento para cima
    public void subir(int metros) {
        if (altitude + metros <= altitudeMaxima) {
            altitude += metros;
        } else {
            System.out.println(nomeRobo + " atingiu a altitude máxima!");
        }
    }

    // Método de movimento para baixo
    public void descer(int metros) {
        if (altitude - metros >= 0) {
            altitude -= metros;
        } else {
            System.out.println(nomeRobo + " não pode descer abaixo do nível do solo!");
        }
    }

    // Sobrescrita do método de exibir posição (inclui altitude)
    @Override
    public void exibirPosicao() {
        System.out.println(nomeRobo + " está em: (" + xPosicao + ", " + yPosicao + ", " + altitude + ")");
    }

    @Override
    public int getZ() {
        return altitude;
    }

    @Override
    public String getDescricao() {
        return "RoboAereo: " + nomeRobo + ", Direção: " + direcao + ", Posição: (" + xPosicao + ", " + yPosicao + ", " + altitude + ")";
    }

    @Override
    public char getRepresentacao() {
        return 'A'; // Representação do RoboAereo
    }

    @Override
    public void enviarMensagem(Comunicavel receptor, String mensagem) throws RoboDesligadoException {
        if (!(this.isLigado())) {
            throw new RoboDesligadoException(this);
        }
        receptor.receberMensagem(mensagem);
    }

    @Override
    public void receberMensagem(String mensagem) throws RoboDesligadoException {
        if (!(this.isLigado())) {
            throw new RoboDesligadoException(this);
        }
        this.mensagem = mensagem;
        System.out.println(nomeRobo + " recebeu a mensagem: " + mensagem);
    }

    // Método para obter a mensagem guardada na memória do robô
    public String getMensagem() {
        return mensagem;
    }
}
