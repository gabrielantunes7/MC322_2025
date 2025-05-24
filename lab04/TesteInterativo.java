import java.util.ArrayList;
import java.util.Scanner;
import ambiente.Ambiente;
import obstaculo.*;
import robos.*;
import sensores.*;
import entidade.*;

public class TesteInterativo {
    private static Scanner scanner = new Scanner(System.in);
    private static Ambiente ambiente;
    private static ArrayList<Robo> robos = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        inicializarAmbiente();

        while (true) {
            exibirMenuPrincipal();
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    escolherRobo();
                    break;
                case 2:
                    testarSensores();
                    break;
                case 3:
                    visualizarAmbiente();
                    break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void inicializarAmbiente() {
        ambiente = new Ambiente(10, 10, 10);

        // Inicializar robôs
        RoboTerrestre roboTerra = new RoboTerrestre("Terrestre-1", "Norte", 0, 0, ambiente, 3, TipoMaterial.METALICO);
        RoboAereo roboAr = new RoboAereo("Aereo-1", "Leste", 3, 7, ambiente, 6, TipoMaterial.NAO_METALICO);
        RoboFurtivo roboFurtivo = new RoboFurtivo("Furtivo-1", "Oeste", 5, 5, ambiente, 10, TipoMaterial.NAO_METALICO);
        CavaloRobo roboCavalo = new CavaloRobo("Cavalo-1", "Norte", 2, 2, 3, 3, ambiente, TipoMaterial.METALICO);
        BispoRobo roboBispo = new BispoRobo("Bispo-1", "Leste", 3, 3, 3, 5, ambiente, TipoMaterial.NAO_METALICO);

        // Adicionar sensores aos robôs
        SensorUltrassonico ultra = new SensorUltrassonico(5, roboFurtivo);
        SensorMagnetico magnetico = new SensorMagnetico(5, roboFurtivo, 1.5);
        roboFurtivo.adicionarSensor(ultra);
        roboFurtivo.adicionarSensor(magnetico);

        // Adicionar robôs ao ambiente e à lista
        ambiente.adicionarEntidade((Entidade) roboTerra);
        ambiente.adicionarEntidade((Entidade) roboAr);
        ambiente.adicionarEntidade((Entidade) roboFurtivo);
        ambiente.adicionarEntidade((Entidade) roboCavalo);
        ambiente.adicionarEntidade((Entidade) roboBispo);

        robos.add(roboTerra);
        robos.add(roboAr);
        robos.add(roboFurtivo);
        robos.add(roboCavalo);
        robos.add(roboBispo);

        // Adicionar alguns obstáculos
        Obstaculo arvore = new Obstaculo(8, 8, 9, 9, TipoObstaculo.ARVORE);
        ambiente.adicionarObstaculo(arvore);
    }

    private static void controlarRobo(Robo robo) throws Exception {
        while (true) {
            System.out.println("\n=== Controlando " + robo.getNomeRobo() + " ===");
            System.out.println("1. Mover");
            System.out.println("2. Exibir posição");
            
            // Adicionar opções específicas para cada tipo de robô
            if (robo instanceof CavaloRobo) {
                System.out.println("3. Reset Stamina");
            } else if (robo instanceof BispoRobo) {
                System.out.println("3. Ver casa mais distante");
            }
            
            System.out.println("4. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o deslocamento em X: ");
                    int dx = scanner.nextInt();
                    System.out.print("Digite o deslocamento em Y: ");
                    int dy = scanner.nextInt();
                    robo.mover(dx, dy);
                    break;
                case 2:
                    robo.exibirPosicao();
                    break;
                case 3:
                    if (robo instanceof CavaloRobo) {
                        ((CavaloRobo) robo).resetStamina();
                    } else if (robo instanceof BispoRobo) {
                        int[] casaMaxima = ((BispoRobo) robo).casaMaisDistante();
                        System.out.println("Casa mais distante possível: (" + 
                            casaMaxima[0] + ", " + casaMaxima[1] + ")");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Escolher e controlar robô");
        System.out.println("2. Testar sensores");
        System.out.println("3. Visualizar ambiente");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void escolherRobo() throws Exception {
        System.out.println("\n=== Robôs Disponíveis ===");
        for (int i = 0; i < robos.size(); i++) {
            System.out.println(i + 1 + ". " + robos.get(i).getNomeRobo());
        }

        System.out.print("Escolha um robô (1-" + robos.size() + "): ");
        int escolha = scanner.nextInt() - 1;

        if (escolha >= 0 && escolha < robos.size()) {
            controlarRobo(robos.get(escolha));
        } else {
            System.out.println("Escolha inválida!");
        }
    }

    private static void testarSensores() {
        System.out.println("\n=== Teste de Sensores ===");
        for (Robo robo : robos) {
            if (!robo.getSensores().isEmpty()) {
                System.out.println("\nSensores do robô " + robo.getNomeRobo() + ":");
                for (Sensor sensor : robo.getSensores()) {
                    System.out.println("Ativando sensor: " + sensor.getClass().getSimpleName());
                    robo.usarSensor(sensor);
                }
            }
        }
    }

    private static void visualizarAmbiente() {
        System.out.println("\n=== Estado do Ambiente ===");
        System.out.println("Dimensões: 10x10x10");
        System.out.println("\nRobôs no ambiente:");
        for (Robo robo : robos) {
            robo.exibirPosicao();
        }
        ambiente.detectarColisoes();
    }
}