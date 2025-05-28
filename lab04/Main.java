import ambiente.Ambiente;
import obstaculo.*;
import robos.*;
import sensores.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n=== Iniciando Testes do Sistema de Robôs ===\n");
        
        // Inicialização do ambiente e robôs
        Ambiente ambiente = new Ambiente(10, 10, 10);
        
        // Criação dos robôs
        RoboTerrestre roboTerrestre = new RoboTerrestre("TesteTerrestre", "Norte", 0, 0, ambiente, 3, TipoMaterial.METALICO);
        RoboAereo roboAereo = new RoboAereo("TesteAereo", "Leste", 3, 3, ambiente, 6, TipoMaterial.NAO_METALICO);
        RoboCargueiro roboCargueiro = new RoboCargueiro("TesteCargueiro", "Sul", 1, 1, ambiente, 5, 100, TipoMaterial.METALICO);
        RoboFurtivo roboFurtivo = new RoboFurtivo("TesteFurtivo", "Oeste", 2, 2, ambiente, 10, TipoMaterial.NAO_METALICO);
        CavaloRobo roboCavalo = new CavaloRobo("TesteCavalo", "Norte", 2, 2, 3, 3, ambiente, TipoMaterial.METALICO);
        BispoRobo roboBispo = new BispoRobo("TesteBispo", "Leste", 3, 3, 3, 5, ambiente, TipoMaterial.NAO_METALICO);

        try {
            // Teste 1: Movimentação Básica do Robô Terrestre
            System.out.println("\n=== Teste 1: Movimentação Básica do Robô Terrestre ===");
            System.out.println("Posição inicial: (" + roboTerrestre.getX() + "," + roboTerrestre.getY() + ")");
            roboTerrestre.mover(2, 2);
            System.out.println("Posição após movimento (2,2): (" + roboTerrestre.getX() + "," + roboTerrestre.getY() + ")");
            roboTerrestre.mover(10, 10); // Deve falhar - movimento muito grande
            System.out.println("Posição após tentativa de movimento inválido: (" + roboTerrestre.getX() + "," + roboTerrestre.getY() + ")");

            // Teste 2: Movimentação do Robô Aéreo
            System.out.println("\n=== Teste 2: Movimentação do Robô Aéreo ===");
            System.out.println("Altitude inicial: " + roboAereo.getZ());
            roboAereo.subir(2);
            System.out.println("Altitude após subir 2 unidades: " + roboAereo.getZ());
            roboAereo.descer(1);
            System.out.println("Altitude após descer 1 unidade: " + roboAereo.getZ());
            roboAereo.subir(10); // Deve falhar - muito alto
            System.out.println("Altitude após tentativa de subir demais: " + roboAereo.getZ());

            // Teste 3: Capacidade de Carga do Robô Cargueiro
            System.out.println("\n=== Teste 3: Teste do Robô Cargueiro ===");
            System.out.println("Tentando carregar 50 unidades (dentro do limite):");
            roboCargueiro.levarCarga(50, 2, 2);
            System.out.println("Tentando carregar 150 unidades (acima do limite):");
            roboCargueiro.levarCarga(150, 2, 2);

            // Teste 4: Modo Furtivo
            System.out.println("\n=== Teste 4: Teste do Robô Furtivo ===");
            System.out.println("Estado inicial do modo furtivo");
            roboFurtivo.alternarModoFurtivo();
            System.out.println("Estado após alternar modo furtivo");
            roboFurtivo.alternarModoFurtivo();
            System.out.println("Estado após alternar novamente");

            // Teste 5: Movimento em L do Cavalo
            System.out.println("\n=== Teste 5: Movimento do Robô Cavalo ===");
            System.out.println("Posição inicial: (" + roboCavalo.getX() + "," + roboCavalo.getY() + ")");
            roboCavalo.mover(2, 1); // Movimento em L válido
            System.out.println("Posição após movimento em L: (" + roboCavalo.getX() + "," + roboCavalo.getY() + ")");
            roboCavalo.mover(1, 1); // Movimento inválido
            System.out.println("Posição após tentativa de movimento inválido: (" + roboCavalo.getX() + "," + roboCavalo.getY() + ")");

            // Teste 6: Movimento Diagonal do Bispo
            System.out.println("\n=== Teste 6: Movimento do Robô Bispo ===");
            System.out.println("Posição inicial: (" + roboBispo.getX() + "," + roboBispo.getY() + ")");
            roboBispo.mover(1, 1); // Movimento diagonal válido
            System.out.println("Posição após movimento diagonal: (" + roboBispo.getX() + "," + roboBispo.getY() + ")");
            roboBispo.mover(1, 0); // Movimento não diagonal
            System.out.println("Posição após tentativa de movimento não diagonal: (" + roboBispo.getX() + "," + roboBispo.getY() + ")");

            // Teste 7: Colisões
            System.out.println("\n=== Teste 7: Teste de Colisões ===");
            Obstaculo obstaculo = new Obstaculo(5, 5, 6, 6, TipoObstaculo.ARVORE);
            ambiente.adicionarEntidade(obstaculo);
            roboTerrestre.mover(5, 5); // Deve detectar colisão
            ambiente.detectarColisoes();

            // Teste 8: Sensores
            System.out.println("\n=== Teste 8: Teste de Sensores ===");
            SensorUltrassonico sensorUltra = new SensorUltrassonico(5, roboFurtivo);
            roboFurtivo.adicionarSensor(sensorUltra);
            System.out.println("Número de sensores no robô furtivo: " + roboFurtivo.getSensores().size());
            roboFurtivo.ligar();
            try {
                roboFurtivo.acionarSensores(roboFurtivo);
            } catch (Exception e) {
                System.out.println("Erro ao acionar sensores: " + e.getMessage());
            }

            // Visualização final do ambiente
            System.out.println("\n=== Estado Final do Ambiente ===");
            ambiente.visualizarAmbiente();

        } catch (Exception e) {
            System.out.println("Erro durante os testes: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== Testes Concluídos ===");
    }
}