
import ambiente.Ambiente;
import comunicacao.CentralComunicacao;
import comunicacao.Mensagem;
import excecoes.RoboDesligadoException;
import interfaces.Comunicavel;
import obstaculo.*;
import robos.*;
import sensores.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Ambiente ambiente;
    private static ArrayList<Robo> robos;
    private static CentralComunicacao central;

    public static void main(String[] args) {
        System.out.println("\n=== Sistema de Controle de Robôs ===\n");
        inicializarSistema();
        menuPrincipal();
    }

    private static void inicializarSistema() {
        // Inicialização do ambiente
        ambiente = new Ambiente(10, 10, 12);
        robos = new ArrayList<>();
        central = new CentralComunicacao();

        // Garantir que o mapa está inicializado
        ambiente.inicializarMapa();

        // Criação dos robôs
        robos.add(new RoboTerrestre("RT01", "Norte", 0, 0, ambiente, 3, TipoMaterial.METALICO));
        robos.add(new RoboAereo("RA01", "Leste", 2, 2, ambiente, 6, TipoMaterial.NAO_METALICO));
        robos.add(new RoboCargueiro("RC01", "Sul", 1, 1, ambiente, 5, 100, TipoMaterial.METALICO));
        robos.add(new RoboFurtivo("RF01", "Oeste", 3, 3, ambiente, 10, TipoMaterial.NAO_METALICO));
        robos.add(new CavaloRobo("CV01", "Norte", 4, 4, 3, 3, ambiente, TipoMaterial.METALICO));
        robos.add(new BispoRobo("BP01", "Leste", 5, 5, 3, 5, ambiente, TipoMaterial.NAO_METALICO));

        // Adicionando robôs ao ambiente
        for (Robo robo : robos) {
            ambiente.adicionarEntidade(robo);
            robo.ligar(); // Ligando todos os robôs inicialmente
        }

        // Adicionando sensores ao robô furtivo
        RoboFurtivo roboFurtivo = (RoboFurtivo) robos.stream()
                .filter(r -> r instanceof RoboFurtivo)
                .findFirst()
                .orElse(null);

        if (roboFurtivo != null) {
            roboFurtivo.adicionarSensor(new SensorUltrassonico(5, roboFurtivo));
            roboFurtivo.adicionarSensor(new SensorMagnetico(5, roboFurtivo, 1.5));
        }

        // Adicionando obstáculos iniciais
        ambiente.adicionarEntidade(new Obstaculo(8, 8, 9, 9, TipoObstaculo.ARVORE));
        ambiente.adicionarEntidade(new Obstaculo(6, 6, 7, 7, TipoObstaculo.PAREDE));

        System.out.println("Sistema inicializado com sucesso!");
    }

    private static void menuPrincipal() {
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Controlar Robôs");
            System.out.println("2. Gerenciar Ambiente");
            System.out.println("3. Sistema de Comunicação");
            System.out.println("4. Testes do Sistema");
            System.out.println("0. Sair");

            System.out.print("\nEscolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    menuRobos();
                    break;
                case 2:
                    menuAmbiente();
                    break;
                case 3:
                    menuComunicacao();
                    break;
                case 4:
                    executarTestes();
                    break;
                case 0:
                    System.out.println("\nEncerrando o sistema...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuRobos() {
        while (true) {
            System.out.println("\n=== Controle de Robôs ===");
            listarRobos();
            System.out.println("\n1. Selecionar Robô");
            System.out.println("2. Ligar/Desligar Robô");
            System.out.println("0. Voltar");

            System.out.print("\nEscolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    controlarRobo();
                    break;
                case 2:
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
            System.out.println("2. Adicionar Obstáculo");
            System.out.println("3. Verificar Colisões");
            System.out.println("0. Voltar");

            System.out.print("\nEscolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    ambiente.visualizarAmbiente();
                    break;
                case 2:
                    adicionarObstaculo();
                    break;
                case 3:
                    ambiente.detectarColisoes();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuComunicacao() {
        while (true) {
            System.out.println("\n=== Sistema de Comunicação ===");
            System.out.println("1. Enviar Mensagem");
            System.out.println("2. Verificar Status da Central");
            System.out.println("0. Voltar");

            System.out.print("\nEscolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    enviarMensagem();
                    break;
                case 2:
                    System.out.println(central);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void controlarRobo() {
        Robo robo = selecionarRobo();
        if (robo == null) return;

        while (true) {
            System.out.println("\n=== Controlando " + robo.getId() + " ===");
            System.out.println("1. Mover");
            System.out.println("2. Executar Tarefa");
            System.out.println("3. Status");
            System.out.println("0. Voltar");

            System.out.print("\nEscolha uma opção: ");
            int opcao = scanner.nextInt();

            try {
                switch (opcao) {
                    case 1:
                        System.out.print("Digite o deslocamento em X: ");
                        int dx = scanner.nextInt();
                        System.out.print("Digite o deslocamento em Y: ");
                        int dy = scanner.nextInt();
                        robo.mover(dx, dy);
                        break;
                    case 2:
                        robo.executarTarefa();
                        break;
                    case 3:
                        System.out.println(robo.getDescricao());
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void enviarMensagem() {
        try {
            System.out.println("\nSelecionando robô emissor:");
            Robo emissor = selecionarRobo();
            if (!(emissor instanceof Comunicavel)) {
                System.out.println("Este robô não possui capacidade de comunicação!");
                return;
            }

            System.out.println("\nSelecionando robô receptor:");
            Robo receptor = selecionarRobo();
            if (!(receptor instanceof Comunicavel)) {
                System.out.println("Este robô não possui capacidade de comunicação!");
                return;
            }

            System.out.print("\nDigite a mensagem: ");
            scanner.nextLine(); // Limpar buffer
            String conteudo = scanner.nextLine();

            Mensagem mensagem = new Mensagem((Comunicavel)emissor, conteudo, (Comunicavel)receptor);
            ((Comunicavel)emissor).enviarMensagem(mensagem, central);

        } catch (RoboDesligadoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro na comunicação: " + e.getMessage());
        }
    }

    private static void executarTestes() {
        System.out.println("\n=== Executando Testes do Sistema ===");

        // Teste de movimentação
        System.out.println("\nTeste de Movimentação:");
        try {
            Robo robo = robos.get(0);
            System.out.println("Posição inicial: " + robo.getDescricao());
            robo.mover(1, 1);
            System.out.println("Após movimento: " + robo.getDescricao());
        } catch (Exception e) {
            System.out.println("Erro no teste de movimentação: " + e.getMessage());
        }

        // Teste de sensores
        System.out.println("\nTeste de Sensores:");
        for (Robo robo : robos) {
            if (!robo.getSensores().isEmpty()) {
                System.out.println("Testando sensores de " + robo.getId());
                for (Sensor sensor : robo.getSensores()) {
                    try {
                        sensor.monitorar(robo.getX(), robo.getY(), ambiente);
                    } catch (Exception e) {
                        System.out.println("Erro no sensor: " + e.getMessage());
                    }
                }
            }
        }

        // Teste de colisões
        System.out.println("\nTeste de Colisões:");
        ambiente.detectarColisoes();

        System.out.println("\nTestes concluídos!");
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
        scanner.nextLine();
    }

    private static void listarRobos() {
        System.out.println("\nRobôs disponíveis:");
        for (int i = 0; i < robos.size(); i++) {
            Robo robo = robos.get(i);
            System.out.printf("%d. %s (%s) - %s%n",
                    i + 1,
                    robo.getId(),
                    robo.getClass().getSimpleName(),
                    robo.isLigado() ? "Ligado" : "Desligado");
        }
    }

    private static Robo selecionarRobo() {
        listarRobos();
        System.out.print("\nSelecione um robô (1-" + robos.size() + "): ");
        int escolha = scanner.nextInt() - 1;

        if (escolha >= 0 && escolha < robos.size()) {
            return robos.get(escolha);
        }

        System.out.println("Seleção inválida!");
        return null;
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

            System.out.println("\nTipos de obstáculo disponíveis:");
            TipoObstaculo[] tipos = TipoObstaculo.values();
            for (int i = 0; i < tipos.length; i++) {
                System.out.println(i + ". " + tipos[i]);
            }

            System.out.print("\nEscolha o tipo de obstáculo: ");
            int tipoEscolhido = scanner.nextInt();

            if (tipoEscolhido >= 0 && tipoEscolhido < tipos.length) {
                Obstaculo obstaculo = new Obstaculo(x1, y1, x2, y2, tipos[tipoEscolhido]);
                ambiente.adicionarEntidade(obstaculo);
                System.out.println("Obstáculo adicionado com sucesso!");
            } else {
                System.out.println("Tipo de obstáculo inválido!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao adicionar obstáculo: " + e.getMessage());
        }
    }
}