package interfaces.missoes;

import robos.RoboAssassino;
import robos.Robo;
import ambiente.Ambiente;
import excecoes.RoboIncompativelException;
import excecoes.RoboNaoEncontradoException;

// Missão para destruir um robô alvo
// Somente pode ser executada por um RoboAssassino
public class MissaoDestruirRobo implements Missao {
    private String idRobo;
    private String motivo;

    public MissaoDestruirRobo(String idRobo, String motivo) {
        this.idRobo = idRobo;
        this.motivo = motivo;
    }

    public String getIdRobo() {
        return idRobo;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String getDescricaoMissao() {
        return "Destruir o robô com ID: " + idRobo + " por motivo: " + motivo;
    }

    @Override
    public void executar(Robo r, Ambiente ambiente) throws Exception {
        if (!(r instanceof RoboAssassino)) 
            throw new RoboIncompativelException("Assassino");

        RoboAssassino assassino = (RoboAssassino) r;
        boolean roboEncontrado = ambiente.getRobos().stream()
            .anyMatch(robo -> robo.getId().equals(idRobo));
        if(!roboEncontrado) 
            throw new RoboNaoEncontradoException("idRobo");
        
        Robo roboAlvo = ambiente.getRobos().stream()
            .filter(robo -> robo.getId().equals(idRobo))
            .findFirst()
            .orElseThrow(() -> new RoboNaoEncontradoException(idRobo));

        assassino.moverPara(roboAlvo.getX(), roboAlvo.getY(), roboAlvo.getZ());
        roboAlvo.destruirRobo();
    }
}
