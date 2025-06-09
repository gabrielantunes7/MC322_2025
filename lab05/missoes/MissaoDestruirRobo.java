package missoes;

import robos.RoboFurtivo;
import robos.Robo;
import ambiente.Ambiente;
import excecoes.RoboIncompativelException;
import excecoes.RoboNaoEncontradoException;

public class MissaoDestruirRobo extends Missao {
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
    public void executar(Robo r, Ambiente ambiente) throws Exception {
        if (!(r instanceof RoboFurtivo)) 
            throw new RoboIncompativelException("Furtivo");

        RoboFurtivo furtivo = (RoboFurtivo) r;
        boolean roboEncontrado = ambiente.getRobos().stream()
            .anyMatch(robo -> robo.getId().equals(idRobo));
        if(!roboEncontrado) 
            throw new RoboNaoEncontradoException("idRobo");
        
        Robo roboAlvo = ambiente.getRobos().stream()
            .filter(robo -> robo.getId().equals(idRobo))
            .findFirst()
            .orElseThrow(() -> new RoboNaoEncontradoException(idRobo));

        if (!(furtivo.isModoFurtivo()))
            furtivo.alternarModoFurtivo();
        furtivo.moverPara(roboAlvo.getX(), roboAlvo.getY(), roboAlvo.getZ());
        roboAlvo.destruirRobo();
    }
}
