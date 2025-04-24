import ambiente.Ambiente;
import obstaculo.*;
import robos.*;
import sensores.*;

public class Main {
    public static void main(String[] args) {
        final int x = 10, y = 10, z = 10; // valor imutável do tamanho do ambiente
        Ambiente ambiente = new Ambiente(x, y, z);

        RoboTerrestre roboTerra = new RoboTerrestre("Isaias", "Norte", 0, 0, ambiente, 3, TipoMaterial.METALICO);
        ambiente.adicionarRobo(roboTerra);

        RoboAereo roboAr = new RoboAereo("Gabriel", "Leste", 3, 7, ambiente, 6, TipoMaterial.NAO_METALICO);
        ambiente.adicionarRobo(roboAr);
        
        RoboCargueiro roboCarga = new RoboCargueiro("Carga", "Sul", 1, 2,ambiente, 5, 100, TipoMaterial.METALICO);
        ambiente.adicionarRobo(roboCarga);

        // vai servir somente para testar o sensor magnético
        RoboTerrestre roboTerra2 = new RoboTerrestre("RoboTerra2", "Norte", 10, 2, ambiente, 3, TipoMaterial.METALICO);
        ambiente.adicionarRobo(roboTerra2);

        RoboFurtivo roboFurtivo = new RoboFurtivo("Furtivo", "Oeste", 5, 5, ambiente, 10, TipoMaterial.NAO_METALICO);
        SensorUltrassonico ultra1 = new SensorUltrassonico(5, roboFurtivo);
        ambiente.adicionarRobo(roboFurtivo);
        roboFurtivo.adicionarSensor(ultra1);

        RoboFurtivo roboFurtivo2 = new RoboFurtivo("Furtivo2", "Oeste", 3, 2, ambiente, 10, TipoMaterial.NAO_METALICO);
        SensorUltrassonico ultra2 = new SensorUltrassonico(5, roboFurtivo2);
        SensorMagnetico magnetico = new SensorMagnetico(5, roboFurtivo2, 1.5);
        ambiente.adicionarRobo(roboFurtivo2);
        roboFurtivo2.adicionarSensor(ultra2);
        roboFurtivo2.adicionarSensor(magnetico);

        CavaloRobo roboCavalo = new CavaloRobo("Cavalo", "Norte", 2, 2, 3, 3, ambiente, TipoMaterial.METALICO);
        ambiente.adicionarRobo(roboCavalo);
        
        BispoRobo roboBispo = new BispoRobo("Bispo", "Leste", 3, 3, 3, 5, ambiente, TipoMaterial.NAO_METALICO);
        ambiente.adicionarRobo(roboBispo);

        Obstaculo arvore = new Obstaculo(8, 8, 9, 9, TipoObstaculo.ARVORE);
        ambiente.adicionarObstaculo(arvore);
        
        Obstaculo parede = new Obstaculo(6, 6, 10, 7, TipoObstaculo.PAREDE);
        ambiente.adicionarObstaculo(parede);

        // Testes de movimentação básica
        System.out.println("\n--- Testes de Movimentação Básica ---");
        roboTerra.exibirPosicao();
        roboTerra.mover(2, 2);
        roboTerra.exibirPosicao();

        roboAr.exibirPosicao();
        roboAr.mover(1, -1);
        roboAr.exibirPosicao();
        roboAr.subir(3);
        roboAr.exibirPosicao();
        roboAr.descer(2);
        roboAr.exibirPosicao();

        // Testes de falha esperada
        System.out.println("\n--- Testes de Falha Esperada de Movimentação ---");
        roboTerra.mover(10, 10); // Deve falhar, ultrapassa distância máxima
        roboAr.subir(10); // Deve falhar, ultrapassa altitude máxima
        roboAr.descer(10); // Deve falhar, altitude abaixo de 0
        roboFurtivo.mover(10, 10); // Deve falhar, ultrapassa limites do ambiente


        // Teste de stamina do CavaloRobo
        System.out.println("\n--- Testes de Stamina do CavaloRobo ---");
        roboCavalo.exibirPosicao();
        roboCavalo.mover(2, 1);
        roboCavalo.exibirPosicao();
        roboCavalo.mover(1, 2);
        roboCavalo.exibirPosicao();
        roboCavalo.mover(2, 1);
        roboCavalo.exibirPosicao();
        roboCavalo.mover(2, 1); // Deve falhar, pois atinge limite de stamina
        roboCavalo.resetStamina();
        roboCavalo.mover(1, 2);
        roboCavalo.exibirPosicao();

        // Teste da casa mais distante do BispoRobo
        System.out.println("\n--- Teste de Alcance Máximo do BispoRobo ---");
        roboBispo.exibirPosicao();
        roboBispo.mover(1,1);
        roboBispo.exibirPosicao();
        roboBispo.mover(3,3);
        roboBispo.exibirPosicao();
        roboBispo.mover(-2,2);
        roboBispo.exibirPosicao();
        roboBispo.mover(-2,1);
        roboBispo.exibirPosicao();
        int[] casaMaxima = roboBispo.casaMaisDistante();
        System.out.println(roboBispo.getNomeRobo() + " pode se mover até: (" + casaMaxima[0] + ", " + casaMaxima[1] + ")");

        // Teste de levar carga do RoboCargueiro
        System.out.println("\n--- Teste de Levar Carga do RoboCargueiro ---");
        roboCarga.exibirPosicao();
        roboCarga.levarCarga(50, 2, 2); // Deve funcionar
        roboCarga.exibirPosicao();
        roboCarga.levarCarga(150, 2, 2); // Deve falhar, carga acima da capacidade
        roboCarga.exibirPosicao();

        // Teste de ativação do modo furtivo
        System.out.println("\n--- Teste de Ativação do Modo Furtivo ---");
        roboFurtivo2.exibirPosicao();
        roboFurtivo2.alternarModoFurtivo(); // Ativa o modo furtivo

        // Teste de detecção de colisões
        System.out.println("\n--- Teste de Detecção de Colisões ---");
        roboTerra.mover(6, 7); // Colisão com o obstáculo arvore
        ambiente.detectarColisoes(); // Deve mostrar a colisão entre roboTerra e arvore

        // Teste do funcionamento do sensor ultrassônico (nesse caso, instalado no robô furtivo 2)
        System.out.println("\n--- Teste do Sensor Ultrassônico (no RoboFurtivo2) ---");
        roboFurtivo2.usarSensor(ultra1);
        roboFurtivo2.usarSensor(ultra2);

        // Teste do funcionamento do sensor magnético (nesse caso, instalado no robô furtivo 2)
        System.out.println("\n--- Teste do Sensor Magnético (no RoboFurtivo2) ---");
        roboFurtivo2.usarSensor(magnetico); // Deve detectar o roboCarga, que é metálico e deve detectar o roboTerra2, que também é metálico
        // Obs.: roboTerra2 só aparece para ele depois de detectar o roboCarga (amplia o alcance)
        // Se roboTerra2 estiver antes de roboCarga no ArrayList de robôs, o sensor magnético não o detecta, porque não ampliou o alcance ainda
    }
}
