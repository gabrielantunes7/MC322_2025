import java.util.ArrayList;
import java.util.Scanner;
import ambiente.Ambiente;
import obstaculo.*;
import robos.*;
import sensores.*;
import entidade.*;
import excecoes.*;
import comunicavel.Comunicavel;

public class TesteInterativo {
    private static Scanner scanner = new Scanner(System.in);
    private static Ambiente ambiente;
    private static ArrayList<Robo> robos = new ArrayList<>();

    public static void main(String[] args) {
        try {
            inicializarAmbiente();
            exibirMenu();
        } catch (Exception e) {
            System.out.println("Erro fatal: " + e.getMessage());
        }
    }

    private static void exibirMenu() {
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Gerenciar Robôs");
            System.out.println("2. Gerenciar Ambiente");
            System.out.println("3. Testar Sensores");
            System.out.println("4. Comunicação entre Robôs");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = scanner.nextInt();
                switch (opcao) {
                    case 1 -> menuRobos();
                    case 2 -> menuAmbiente();
                    case 3 -> testarSensores();
                    case 4 -> menuComunicacao();
                    case 0 -> {
                        System.out.println("Encerrando o programa...");
                        return;
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                scanner.nextLine(); // Limpar buffer
            }
        }
    }

    private static void menuRobos() throws Exception {
        while (true) {
            System.out.println("\n=== Gerenciamento de Robôs ===");
            System.out.println("1. Listar Robôs");
            System.out.println("2. Controlar Robô");
            System.out.println("3. Ligar/Desligar Robô");
            System.out.println("0. Voltar");

            int opcao = scanner.nextInt();
            switch (opcao) {
                case 1:
                    listarRobos();
                    break;
                case 2:
                    controlarRobo(selecionarRobo());
                    break;
                case 3:
                    alternarEstadoRobo();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuAmbiente() {
        while (true) {
            System.out.println("\n=== Gerenciamento do Ambiente ===");
            System.out.println("1. Visualizar Ambiente");
            System.out.println("2. Verificar Colisões");
            System.out.println("3. Adicionar Obstáculo");
            System.out.println("0. Voltar");

            try {
                int opcao = scanner.nextInt();
                switch (opcao) {
                    case 1:
                        ambiente.visualizarAmbiente();
                        break;
                    case 2:
                        ambiente.detectarColisoes();
                        break;
                    case 3:
                        adicionarObstaculo();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void menuComunicacao() {
        System.out.println("\n=== Comunicação entre Robôs ===");
        try {
            Robo emissor = selecionarRobo();
            if (!(emissor instanceof Comunicavel)) {
                System.out.println("Este robô não possui capacidade de comunicação!");
                return;
            }

            Robo receptor = selecionarRobo();
            if (!(receptor instanceof Comunicavel)) {
                System.out.println("O robô receptor não possui capacidade de comunicação!");
                return;
            }

            System.out.print("Digite a mensagem: ");
            scanner.nextLine(); // Limpar buffer
            String mensagem = scanner.nextLine();

            ((Comunicavel)emissor).enviarMensagem((Comunicavel)receptor, mensagem);

        } catch (Exception e) {
            System.out.println("Erro na comunicação: " + e.getMessage());
        }
    }

    private static void inicializarAmbiente() {
        ambiente = new Ambiente(10, 10, 10);

        // Criar e adicionar robôs
        robos.add(new RoboTerrestre("RT-1", "Norte", 0, 0, ambiente, 5, TipoMaterial.METALICO));
        robos.add(new RoboAereo("RA-1", "Leste", 3, 3, ambiente, 6, TipoMaterial.NAO_METALICO));
        robos.add(new RoboCargueiro("RC-1", "Sul", 2, 2, ambiente, 5, 100, TipoMaterial.METALICO));
        robos.add(new RoboFurtivo("RF-1", "Oeste", 4, 4, ambiente, 10, TipoMaterial.NAO_METALICO));
        robos.add(new CavaloRobo("CV-1", "Norte", 1, 1, 3, 3, ambiente, TipoMaterial.METALICO));
        robos.add(new BispoRobo("BP-1", "Leste", 5, 5, 3, 5, ambiente, TipoMaterial.NAO_METALICO));

        // Adicionar robôs ao ambiente
        robos.forEach(ambiente::adicionarEntidade);

        // Adicionar sensores ao robô furtivo
        RoboFurtivo roboFurtivo = (RoboFurtivo) robos.stream()
                .filter(r -> r instanceof RoboFurtivo)
                .findFirst()
                .orElse(null);

        if (roboFurtivo != null) {
            roboFurtivo.adicionarSensor(new SensorUltrassonico(5, roboFurtivo));
            roboFurtivo.adicionarSensor(new SensorMagnetico(5, roboFurtivo, 1.5));
        }

        // Adicionar obstáculo inicial
        Obstaculo arvore = new Obstaculo(8, 8, 9, 9, TipoObstaculo.ARVORE);
        ambiente.adicionarEntidade(arvore);
    }

    private static void controlarRobo(Robo robo) throws Exception {
        if (robo == null) return;

        while (true) {
            System.out.println("\n=== Controlando " + robo.getId() + " ===");
            System.out.println("1. Mover");
            System.out.println("2. Exibir Informações");

            // Opções específicas por tipo de robô
            if (robo instanceof RoboAereo) {
                System.out.println("3. Subir");
                System.out.println("4. Descer");
            } else if (robo instanceof RoboCargueiro) {
                System.out.println("3. Transportar Carga");
            } else if (robo instanceof RoboFurtivo) {
                System.out.println("3. Alternar Modo Furtivo");
            } else if (robo instanceof CavaloRobo) {
                System.out.println("3. Resetar Stamina");
            } else if (robo instanceof BispoRobo) {
                System.out.println("3. Ver Casa Mais Distante");
            }

            System.out.println("0. Voltar");

            int opcao = scanner.nextInt();
            if (opcao == 0) break;

            try {
                switch (opcao) {
                    case 1 -> moverRobo(robo);
                    case 2 -> System.out.println(robo.getDescricao());
                    case 3 -> executarAcaoEspecifica(robo);
                    case 4 -> {
                        if (robo instanceof RoboAereo) {
                            System.out.print("Digite a altura para descer: ");
                            ((RoboAereo) robo).descer(scanner.nextInt());
                        }
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void executarAcaoEspecifica(Robo robo) throws Exception {
        if (robo instanceof RoboAereo) {
            System.out.print("Digite a altura para subir: ");
            ((RoboAereo) robo).subir(scanner.nextInt());
        } else if (robo instanceof RoboCargueiro) {
            System.out.print("Digite o peso da carga: ");
            int peso = scanner.nextInt();
            System.out.print("Digite X destino: ");
            int x = scanner.nextInt();
            System.out.print("Digite Y destino: ");
            int y = scanner.nextInt();
            ((RoboCargueiro) robo).levarCarga(peso, x, y);
        } else if (robo instanceof RoboFurtivo) {
            ((RoboFurtivo) robo).alternarModoFurtivo();
            System.out.println("Modo furtivo alterado!");
        } else if (robo instanceof CavaloRobo) {
            ((CavaloRobo) robo).resetStamina();
            System.out.println("Stamina resetada!");
        } else if (robo instanceof BispoRobo) {
            int[] casa = ((BispoRobo) robo).casaMaisDistante();
            System.out.println("Casa mais distante: (" + casa[0] + ", " + casa[1] + ")");
        }
    }

    private static void moverRobo(Robo robo) throws Exception {
        System.out.print("Digite o deslocamento em X: ");
        int dx = scanner.nextInt();
        System.out.print("Digite o deslocamento em Y: ");
        int dy = scanner.nextInt();
        robo.mover(dx, dy);
    }

    private static Robo selecionarRobo() {
        listarRobos();
        System.out.print("Selecione um robô (1-" + robos.size() + "): ");
        int escolha = scanner.nextInt() - 1;
        if (escolha >= 0 && escolha < robos.size()) {
            return robos.get(escolha);
        }
        System.out.println("Seleção inválida!");
        return null;
    }

    private static void listarRobos() {
        System.out.println("\n=== Robôs Disponíveis ===");
        for (int i = 0; i < robos.size(); i++) {
            Robo robo = robos.get(i);
            System.out.printf("%d. %s (%s) - %s%n",
                    i + 1,
                    robo.getId(),
                    robo.getClass().getSimpleName(),
                    robo.isLigado() ? "Ligado" : "Desligado");
        }
    }

    private static void alternarEstadoRobo() {
        Robo robo = selecionarRobo();
        if (robo != null) {
            if (robo.isLigado()) {
                robo.desligar();
                System.out.println(robo.getId() + " foi desligado.");
            } else {
                robo.ligar();
                System.out.println(robo.getId() + " foi ligado.");
            }
        }
    }

    private static void adicionarObstaculo() {
        try {
            System.out.print("Digite X inicial: ");
            int x1 = scanner.nextInt();
            System.out.print("Digite Y inicial: ");
            int y1 = scanner.nextInt();
            System.out.print("Digite X final: ");
            int x2 = scanner.nextInt();
            System.out.print("Digite Y final: ");
            int y2 = scanner.nextInt();

            System.out.println("Tipos de obstáculo disponíveis:");
            for (TipoObstaculo tipo : TipoObstaculo.values()) {
                System.out.println(tipo.ordinal() + ". " + tipo);
            }

            System.out.print("Escolha o tipo de obstáculo: ");
            int tipoEscolhido = scanner.nextInt();
            TipoObstaculo tipo = TipoObstaculo.values()[tipoEscolhido];

            Obstaculo obstaculo = new Obstaculo(x1, y1, x2, y2, tipo);
            ambiente.adicionarEntidade(obstaculo);
            System.out.println("Obstáculo adicionado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao adicionar obstáculo: " + e.getMessage());
        }
    }

    private static void testarSensores() {
        System.out.println("\n=== Teste de Sensores ===");
        for (Robo robo : robos) {
            if (!robo.getSensores().isEmpty()) {
                System.out.println("\nSensores do robô " + robo.getId() + ":");
                for (Sensor sensor : robo.getSensores()) {
                    try {
                        System.out.println("- " + sensor.getClass().getSimpleName() + ":");
                        System.out.println("  Leitura atual: ");
                        sensor.monitorar(robo.getX(), robo.getY(), ambiente);
                        
                        if (sensor instanceof SensorMagnetico) {
                            System.out.println("  Intensidade magnética: " + 
                                ((SensorMagnetico) sensor).getIntensidadeMagnetica());
                        }
                    } catch (Exception e) {
                        System.out.println("  Erro na leitura: " + e.getMessage());
                    }
                }
            }
        }
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine(); // Limpar buffer
        scanner.nextLine(); // Aguardar ENTER
    }
}