import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import ambiente.Ambiente;
import robos.*;
import sensores.*;
import obstaculo.*;
import interfaces.missoes.*;
import interfaces.Comunicavel;  // Adicionar esta importação
import comunicacao.*;
import excecoes.RoboDesligadoException;  // Adicionar esta importação
import excecoes.RoboNaoEncontradoException;
import excecoes.ColisaoException;
import robos.RoboCargueiro;
import interfaces.missoes.MissaoConstrutor;


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Ambiente ambiente;
    private static ArrayList<Robo> robos;
    private static CentralComunicacao central;
    
    // Variáveis para controle do log das missões automáticas
    private static PrintWriter logWriter;
    private static String nomeArquivoLog;

    public static void main(String[] args) {
        System.out.println("\n=== Sistema de Controle de Robôs ===\n");
        inicializarSistema();
        menuPrincipal();
    }

    // Métodos para controle do log das missões automáticas
    private static void iniciarLogMissoes() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            nomeArquivoLog = "missoes_automaticas_" + sdf.format(new Date()) + ".log";
            
            logWriter = new PrintWriter(new FileWriter(nomeArquivoLog, true));
            
            logPrintln("=".repeat(80));
            logPrintln("LOG DE EXECUÇÃO DE MISSÕES AUTOMÁTICAS");
            logPrintln("Data/Hora de Início: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            logPrintln("=".repeat(80));
            
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo de log: " + e.getMessage());
            logWriter = null;
        }
    }

    private static void logPrintln(String mensagem) {
        // Imprime no terminal
        System.out.println(mensagem);
        
        // Escreve no arquivo de log se disponível
        if (logWriter != null) {
            logWriter.println(mensagem);
            logWriter.flush(); // Força a escrita imediata
        }
    }

    private static void logPrint(String mensagem) {
        // Imprime no terminal
        System.out.print(mensagem);
        
        // Escreve no arquivo de log se disponível
        if (logWriter != null) {
            logWriter.print(mensagem);
            logWriter.flush();
        }
    }

    private static void logPrintf(String formato, Object... args) {
        String mensagem = String.format(formato, args);
        logPrint(mensagem);
    }

    private static void finalizarLogMissoes() {
        if (logWriter != null) {
            logPrintln("\n" + "=".repeat(80));
            logPrintln("Data/Hora de Finalização: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            logPrintln("=".repeat(80));
            
            logWriter.close();
            System.out.println("\nLog salvo em: " + nomeArquivoLog);
        }
    }

    private static void inicializarSistema() {
        // Inicialização do ambiente
        ambiente = new Ambiente(10, 10, 12);
        robos = new ArrayList<>();
        central = new CentralComunicacao();

        // Garantir que o mapa está inicializado
        ambiente.inicializarMapa();

        // Criação dos robôs básicos
        robos.add(new RoboTerrestre("RT01", "Norte", 0, 0, ambiente, 3, TipoMaterial.METALICO));
        robos.add(new RoboAereo("RA01", "Leste", 2, 2, ambiente, 6, TipoMaterial.NAO_METALICO));
        robos.add(new RoboCargueiro("RC01", "Sul", 1, 1, ambiente, 5, 100, TipoMaterial.METALICO));
        robos.add(new RoboFurtivo("RF01", "Oeste", 3, 3, ambiente, 10, TipoMaterial.NAO_METALICO));
        robos.add(new CavaloRobo("CV01", "Norte", 4, 4, 3, 3, ambiente, TipoMaterial.METALICO));
        robos.add(new BispoRobo("BP01", "Leste", 5, 5, 3, 5, ambiente, TipoMaterial.NAO_METALICO));

        // Criação dos robôs agentes inteligentes
        // RoboAutomatoConstrutor
        RoboAutomatoConstrutor roboConstrutor = new RoboAutomatoConstrutor(
            "AC01", "Norte", 0, 0, ambiente, null  // Inicialmente sem sensor
        );

        // Criar o sensor depois que o robô existe
        SensorFrontal sensorConstrutor = new SensorFrontal(5.0, roboConstrutor);

        // Configurar o sensor no robô
        roboConstrutor.setSensor(sensorConstrutor);

        // Configurar missão inicial para o robô construtor
        MissaoConstrutor missaoConstrutor = new MissaoConstrutor(7, 3);
        roboConstrutor.setMissao(missaoConstrutor);

        // Adicionar o robô construtor à lista de robôs
        robos.add(roboConstrutor);

        // RoboAssassino
        MissaoDestruirRobo missaoDestruir = new MissaoDestruirRobo("RF01", "Robô considerado hostil");

        RoboAssassino roboAssassino = new RoboAssassino(
            "AS01", "Norte", 1, 1, ambiente, null);

        // Criar o sensor depois que o robô existe
        SensorFrontal sensorAssassino = new SensorFrontal(8.0, roboAssassino);

        // Configurar o sensor no robô assassino
        roboAssassino.setSensor(sensorAssassino);

        // *** DEFINIR A MISSÃO ***
        roboAssassino.setMissao(missaoDestruir);

        // *** ENCONTRAR E DEFINIR O ROBÔ ALVO ***
        // Busca o robô furtivo que foi criado anteriormente
        Robo roboAlvo = robos.stream()
                .filter(r -> r.getId().equals("RF01"))
                .findFirst()
                .orElse(null);

        if (roboAlvo != null) {
            roboAssassino.definirRoboAlvo(roboAlvo);
        } else {
            System.out.println("Aviso: Robô alvo RF01 não encontrado!");
        }

        // Adicionar o robô assassino à lista de robôs
        robos.add(roboAssassino);

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

        // Adicionando obstáculos iniciais em diferentes níveis
        ambiente.adicionarEntidade(new Obstaculo(8, 8, 9, 9, TipoObstaculo.ARVORE));
        ambiente.adicionarEntidade(new Obstaculo(6, 6, 7, 7, TipoObstaculo.PAREDE));
        ambiente.adicionarEntidade(new Obstaculo(3, 3, 5, 5, TipoObstaculo.PREDIO));
        ambiente.adicionarEntidade(new Obstaculo(8, 8, 8, 8, TipoObstaculo.PAREDE)); // nível 0
        ambiente.adicionarEntidade(new Obstaculo(6, 6, 6, 6, TipoObstaculo.PAREDE)); // nível 1
        ambiente.adicionarEntidade(new Obstaculo(4, 4, 4, 4, TipoObstaculo.PAREDE)); // nível 2

        System.out.println("Sistema inicializado com sucesso!");
    }

    private static void menuPrincipal() {
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Controlar Robôs");
            System.out.println("2. Gerenciar Ambiente");
            System.out.println("3. Sistema de Comunicação");
            System.out.println("4. Executar Missões Automáticas");
            System.out.println("5. Visualizar Ambiente");
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
                    executarMissoesAutomaticas();
                    break;
                case 5:
                    ambiente.visualizarAmbiente();
                    break;
                case 0:
                    System.out.println("\nEncerrando o sistema...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void executarMissoesAutomaticas() {
        // Iniciar o sistema de log
        iniciarLogMissoes();
        
        logPrintln("\n=== Execução de Missões Automáticas ===");
        logPrintln("1. Executar Missão de Construção");
        logPrintln("2. Executar Missão de Destruir Robô");
        logPrintln("3. Executar Todas as Missões");
        logPrintln("4. Listar Sensores das Missões");  // Nova opção
        logPrintln("0. Voltar");

        logPrint("\nEscolha uma opção: ");
        int opcao = scanner.nextInt();

        try {
            switch (opcao) {
                case 1:
                    executarMissoesConstrucao();
                    break;
                case 2:
                    executarMissoesDestruicao();
                    break;
                case 3:
                    executarTodasMissoes();
                    break;
                case 4:
                    listarSensoresMissoes();  // Chamar a nova função
                    break;
                case 0:
                    logPrintln("Voltando ao menu principal...");
                    break;
                default:
                    logPrintln("Opção inválida!");
            }
        } catch (Exception e) {
            logPrintln("Erro ao executar missão: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Finalizar o log
            finalizarLogMissoes();
        }
    }

    private static void listarSensoresMissoes() {
        logPrintln("\n" + "=".repeat(60));
        logPrintln("SENSORES UTILIZADOS NAS MISSÕES AUTOMÁTICAS");
        logPrintln("=".repeat(60));
        
        boolean encontrouRobos = false;
        
        // Analisar sensores do RoboAutomatoConstrutor
        for (Robo robo : robos) {
            if (robo instanceof RoboAutomatoConstrutor) {
                encontrouRobos = true;
                logPrintln("\n>>> MISSÃO DE CONSTRUÇÃO");
                logPrintln("Robô: " + robo.getId() + " (" + robo.getClass().getSimpleName() + ")");
            
                if (!robo.getSensores().isEmpty()) {
                    for (Sensor sensor : robo.getSensores()) {
                        logPrintln("├─ Sensor: " + sensor.getClass().getSimpleName());
                        logPrintln("├─ Alcance: " + sensor.getAlcanceMaximo() + " metros");
                        logPrintln("└─ Função: Detectar obstáculos/entidades durante navegação para posição alvo");
                    }
                } else {
                    logPrintln("└─ Nenhum sensor configurado!");
                }
            }
        }
    
        // Analisar sensores do RoboAssassino
        for (Robo robo : robos) {
            if (robo instanceof RoboAssassino) {
                encontrouRobos = true;
                logPrintln("\n>>> MISSÃO DE DESTRUIÇÃO");
                logPrintln("Robô: " + robo.getId() + " (" + robo.getClass().getSimpleName() + ")");
            
                if (!robo.getSensores().isEmpty()) {
                    for (Sensor sensor : robo.getSensores()) {
                        logPrintln("├─ Sensor: " + sensor.getClass().getSimpleName());
                        logPrintln("├─ Alcance: " + sensor.getAlcanceMaximo() + " metros");
                        logPrintln("└─ Função: Detectar e rastrear o robô alvo durante perseguição");
                    }
                } else {
                    logPrintln("└─ Nenhum sensor configurado!");
                }
            }
        }
    
        if (!encontrouRobos) {
            logPrintln("⚠ Nenhum robô de missão automática encontrado no sistema!");
        }
    
        // Resumo comparativo
        logPrintln("\n" + "-".repeat(40));
        logPrintln("RESUMO COMPARATIVO DOS SENSORES");
        logPrintln("-".repeat(40));
        logPrintf("%-20s | %-15s | %-10s | %-20s%n", "MISSÃO", "SENSOR", "ALCANCE", "ESPECIALIZAÇÃO");
        logPrintln("-".repeat(40));
        logPrintf("%-20s | %-15s | %-10s | %-20s%n", "Construção", "SensorFrontal", "5.0m", "Navegação segura");
        logPrintf("%-20s | %-15s | %-10s | %-20s%n", "Destruição", "SensorFrontal", "8.0m", "Rastreamento alvo");
        logPrintln("-".repeat(40));
    }

    private static void executarMissoesConstrucao() {
        logPrintln("\n" + "=".repeat(60));
        logPrintln("EXECUTANDO MISSÕES DE CONSTRUÇÃO");
        logPrintln("=".repeat(60));
        
        boolean encontrouRobo = false;
        for (Robo robo : robos) {
            if (robo instanceof RoboAutomatoConstrutor) {
                encontrouRobo = true;
                logPrintln("\n>>> Iniciando missão de construção com " + robo.getId());
                logPrintln("Status inicial do robô: " + robo.getDescricao());
                
                try {
                    ((RoboAutomatoConstrutor) robo).executarTarefa();
                    logPrintln("✓ Missão de construção executada com sucesso!");
                    logPrintln("Status final do robô: " + robo.getDescricao());
                } catch (Exception e) {
                    logPrintln("✗ Erro na execução da missão: " + e.getMessage());
                }
            }
        }
        
        if (!encontrouRobo) {
            logPrintln("⚠ Nenhum robô construtor encontrado no sistema!");
        }
    }

    private static void executarMissoesDestruicao() {
        logPrintln("\n" + "=".repeat(60));
        logPrintln("EXECUTANDO MISSÕES DE DESTRUIÇÃO");
        logPrintln("=".repeat(60));
        
        boolean encontrouRobo = false;
        for (Robo robo : robos) {
            if (robo instanceof RoboAssassino) {
                encontrouRobo = true;
                logPrintln("\n>>> Iniciando missão de destruição com " + robo.getId());
                logPrintln("Status inicial do robô: " + robo.getDescricao());
                
                try {
                    ((RoboAssassino) robo).executarTarefa();
                    logPrintln("✓ Missão de destruição executada com sucesso!");
                    logPrintln("Status final do robô: " + robo.getDescricao());
                } catch (Exception e) {
                    logPrintln("✗ Erro na execução da missão: " + e.getMessage());
                }
            }
        }
        
        if (!encontrouRobo) {
            logPrintln("⚠ Nenhum robô assassino encontrado no sistema!");
        }
    }

    private static void executarTodasMissoes() {
        logPrintln("\n" + "=".repeat(60));
        logPrintln("EXECUTANDO TODAS AS MISSÕES AUTOMÁTICAS");
        logPrintln("=".repeat(60));
        
        // Executar missões de construção
        executarMissoesConstrucao();
        
        // Executar missões de destruição
        executarMissoesDestruicao();
        
        logPrintln("\n" + "=".repeat(60));
        logPrintln("EXECUÇÃO DE TODAS AS MISSÕES CONCLUÍDA");
        logPrintln("=".repeat(60));
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
            System.out.println("4. Executar Sensores");
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
                case 4:
                    ambiente.executarSensores();
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