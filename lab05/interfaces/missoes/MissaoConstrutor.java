package interfaces.missoes;

import robos.Robo;
import robos.RoboAutomatoConstrutor;

import ambiente.Ambiente;

public class MissaoConstrutor implements Missao {
    private int xAlvo;
    private int yAlvo;

    public MissaoConstrutor(int x, int y) {
        this.xAlvo = x;
        this.yAlvo = y;
    }

    @Override
    public String getDescricaoMissao() {
        return "Missão de Construção: Plantar árvore na posição (" + 
               xAlvo + "," + yAlvo + ")";
    }

    @Override
    public void executar(Robo r, Ambiente ambiente) throws Exception {
        if (!(r instanceof RoboAutomatoConstrutor)) {
            throw new Exception("Esta missão só pode ser executada por um RoboAutomatoConstrutor");
        }

        RoboAutomatoConstrutor construtor = (RoboAutomatoConstrutor) r;
        
        System.out.println("\n=== Iniciando " + getDescricaoMissao() + " ===");
        System.out.println("Robô: " + construtor.getId());
        System.out.println("Posição inicial: (" + construtor.getX() + "," + 
                          construtor.getY() + ")");
        
        // Define a posição alvo para o robô
        construtor.definirPosicaoAlvo(xAlvo, yAlvo);
        
        // Executa a missão do robô
        construtor.executarMissao();
    }
}