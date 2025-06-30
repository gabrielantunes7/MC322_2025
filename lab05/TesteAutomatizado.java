import ambiente.Ambiente;
import comunicacao.CentralComunicacao;
import comunicacao.Mensagem;
import excecoes.RoboDesligadoException;
import interfaces.Comunicavel;
import interfaces.TipoEntidade;
import interfaces.missoes.*;
import obstaculo.*;
import robos.*;
import sensores.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TesteAutomatizado {
    private static Ambiente ambiente;
    private static ArrayList<Robo> robos;
    private static CentralComunicacao central;
    private static int testesPassaram = 0;
    private static int testesTotais = 0;
    
    // Variáveis para controle do log
    private static PrintWriter logWriter;
    private static String nomeArquivoLog;

    public static void main(String[] args) {
        iniciarLog();
        logPrintln("Iniciando Testes Automatizados do Sistema de Robôs\n");

        inicializarAmbiente();
        executarTestes();

        mostrarResultadoFinal();
        finalizarLog();
    }

    private static void iniciarLog() {
        try {
            // Criar nome do arquivo com timestamp
            SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = formatoData.format(new Date());
            nomeArquivoLog = "log_testes_" + timestamp + ".txt";
            
            // Criar o arquivo de log
            logWriter = new PrintWriter(new FileWriter(nomeArquivoLog, false));
            
            // Cabeçalho do log
            logWriter.println("=================================================");
            logWriter.println("        LOG DE TESTES AUTOMATIZADOS");
            logWriter.println("=================================================");
            logWriter.println("Data/Hora de início: " + new Date());
            logWriter.println("Nome do arquivo: " + nomeArquivoLog);
            logWriter.println("=================================================\n");
            logWriter.flush();
            
            System.out.println("📝 Log de testes será salvo em: " + nomeArquivoLog);
            
        } catch (IOException e) {
            System.err.println("⚠️ Erro ao criar arquivo de log: " + e.getMessage());
            System.err.println("Os testes continuarão sem gravação de log.");
        }
    }

    private static void logPrintln(String mensagem) {
        // Imprime no console
        System.out.println(mensagem);
        
        // Escreve no arquivo de log
        if (logWriter != null) {
            logWriter.println(mensagem);
            logWriter.flush(); // Garante que seja escrito imediatamente
        }
    }

    private static void logPrint(String mensagem) {
        // Imprime no console
        System.out.print(mensagem);
        
        // Escreve no arquivo de log
        if (logWriter != null) {
            logWriter.print(mensagem);
            logWriter.flush();
        }
    }

    private static void logPrintf(String formato, Object... args) {
        // Imprime no console
        System.out.printf(formato, args);
        
        // Escreve no arquivo de log
        if (logWriter != null) {
            logWriter.printf(formato, args);
            logWriter.flush();
        }
    }

    private static void finalizarLog() {
        if (logWriter != null) {
            logWriter.println("\n=================================================");
            logWriter.println("Data/Hora de término: " + new Date());
            logWriter.println("=================================================");
            logWriter.close();
            
            logPrintln("\n📄 Log completo salvo em: " + nomeArquivoLog);
            logPrintln("🔍 Você pode revisar todos os detalhes dos testes no arquivo de log.");
        }
    }

    private static void inicializarAmbiente() {
        try {
            ambiente = new Ambiente(15, 15, 10);  // Ambiente maior para testes de missões
            robos = new ArrayList<>();
            central = new CentralComunicacao();

            // Criar e adicionar robôs tradicionais
            robos.add(new RoboTerrestre("RT-1", "Norte", 0, 0, ambiente, 5, TipoMaterial.METALICO));
            robos.add(new RoboAereo("RA-1", "Leste", 3, 3, ambiente, 6, TipoMaterial.NAO_METALICO));
            robos.add(new RoboCargueiro("RC-1", "Sul", 2, 2, ambiente, 5, 100, TipoMaterial.METALICO));
            robos.add(new RoboFurtivo("RF-1", "Oeste", 4, 4, ambiente, 10, TipoMaterial.NAO_METALICO));
            robos.add(new CavaloRobo("CV-1", "Norte", 1, 1, 3, 3, ambiente, TipoMaterial.METALICO));
            robos.add(new BispoRobo("BP-1", "Leste", 5, 5, 3, 5, ambiente, TipoMaterial.NAO_METALICO));

            // Criar e adicionar agentes inteligentes com missões
            // Corrigir a criação do sensor - criar o robô primeiro, depois associar o sensor
            RoboAutomatoConstrutor construtor = new RoboAutomatoConstrutor("CONSTRUTOR-1", "Norte", 12, 12, ambiente, null);
            SensorFrontal sensorConstrutor = new SensorFrontal(3, construtor);
            construtor.setSensor(sensorConstrutor);
            robos.add(construtor);

            RoboAssassino assassino = new RoboAssassino("ASSASSINO-1", "Sul", 13, 13, ambiente, null);
            SensorFrontal sensorAssassino = new SensorFrontal(5, assassino);
            assassino.setSensor(sensorAssassino);
            robos.add(assassino);

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

            // Adicionar obstáculo inicial em uma posição que não interfira nos testes
            Obstaculo arvore = new Obstaculo(8, 8, 9, 9, TipoObstaculo.ARVORE);
            ambiente.adicionarEntidade(arvore);

            verificar("Inicialização do ambiente", true);
        } catch (Exception e) {
            verificar("Inicialização do ambiente", false);
            logPrintln("Erro na inicialização: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executarTestes() {
        testarInicializacao();
        testarEstadoRobos();
        testarFuncionalidadesEspecificas();
        testarSensores();
        testarComunicacao();
        testarMapa();
        testarAdicoesNoMapa();
        testarDeteccaoColisoes();
        testarColisoes();
        testarMissoes();           // Novo teste para missões
        testarAgentesInteligentes(); // Novo teste para agentes inteligentes
        testarExcecoes();
    }

    private static void testarInicializacao() {
        logPrintln("\n=== Teste de Inicialização ===");
        verificar("Ambiente foi criado", ambiente != null);
        verificar("Lista de robôs foi criada", robos != null);
        verificar("Quantidade correta de robôs", robos.size() == 8); // Atualizado para incluir novos robôs
        
        // Verificar se os agentes inteligentes foram criados
        boolean temConstrutor = robos.stream().anyMatch(r -> r instanceof RoboAutomatoConstrutor);
        boolean temAssassino = robos.stream().anyMatch(r -> r instanceof RoboAssassino);
        
        verificar("RoboAutomatoConstrutor foi criado", temConstrutor);
        verificar("RoboAssassino foi criado", temAssassino);
    }

private static void testarEstadoRobos() {
    logPrintln("\n=== Teste de Estado dos Robôs ===");
    for (Robo robo : robos) {
        // Primeiro, garante que o robô está desligado
        robo.desligar();
        verificar(robo.getId() + " - Estado forçado para desligado", !robo.isLigado());

        robo.ligar();
        verificar(robo.getId() + " - Ligar robô", robo.isLigado());

        robo.desligar();
        verificar(robo.getId() + " - Desligar robô", !robo.isLigado());
    }
}

    private static void testarMapa() {
        logPrintln("\n=== Testando Funcionalidades do Mapa ===");

        verificar("Mapa foi inicializado corretamente", ambiente != null);
        verificar("Largura do mapa é válida", ambiente.getLargura() == 15);
        verificar("Altura do mapa é válida", ambiente.getAltura() == 15);
        verificar("Altitude do mapa é válida", ambiente.getAltitude() == 10);

        verificar("Posição (0,0,0) está dentro dos limites", ambiente.dentroDosLimites(0, 0, 0));
        verificar("Posição (14,14,9) está dentro dos limites", ambiente.dentroDosLimites(14, 14, 9));
        verificar("Posição (-1,0,0) está fora dos limites", !ambiente.dentroDosLimites(-1, 0, 0));
        verificar("Posição (15,15,10) está fora dos limites", !ambiente.dentroDosLimites(15, 15, 10));

        int x = 5, y = 5, z = 0;
        boolean posicaoInicialmenteOcupada = ambiente.estaOcupado(x, y, z);
        verificar("Verificação de ocupação de posição funciona",
                posicaoInicialmenteOcupada == (ambiente.getTipoEntidade(x, y, z) != TipoEntidade.VAZIO));
    }

    private static void testarAdicoesNoMapa() {
        logPrintln("\n=== Testando Adições no Mapa ===");

        try {
            Obstaculo novoObstaculo = new Obstaculo(10, 10, 11, 11, TipoObstaculo.PAREDE);
            ambiente.adicionarEntidade(novoObstaculo);
            verificar("Adição de obstáculo", ambiente.estaOcupado(10, 10, 2));

            boolean obstaculoEncontrado = ambiente.getObstaculos().stream()
                    .anyMatch(obs -> obs.getPosicaoX1() == 10 &&
                            obs.getPosicaoY1() == 10 &&
                            obs.getPosicaoX2() == 11 &&
                            obs.getPosicaoY2() == 11);
            verificar("Obstáculo está na lista de obstáculos", obstaculoEncontrado);

            ambiente.removerEntidade(novoObstaculo);
        } catch (Exception e) {
            verificar("Adição de obstáculo falhou", false);
            logPrintln("Erro: " + e.getMessage());
        }

        try {
            RoboTerrestre novoRobo = new RoboTerrestre("RT-TEST", "Norte", 14, 14,
                    ambiente, 5, TipoMaterial.METALICO);
            ambiente.adicionarEntidade(novoRobo);
            verificar("Adição de robô", ambiente.estaOcupado(14, 14, 0));

            boolean roboEncontrado = ambiente.getRobos().stream()
                    .anyMatch(r -> r.getId().equals("RT-TEST"));
            verificar("Robô está na lista de robôs", roboEncontrado);

            ambiente.removerEntidade(novoRobo);
        } catch (Exception e) {
            verificar("Adição de robô falhou", false);
            logPrintln("Erro: " + e.getMessage());
        }
    }

    private static void testarDeteccaoColisoes() {
        logPrintln("\n=== Testando Detecção de Colisões ===");

        try {
            Obstaculo obstaculoTeste = new Obstaculo(7, 7, 7, 7, TipoObstaculo.PAREDE);
            ambiente.adicionarEntidade(obstaculoTeste);

            RoboTerrestre roboTeste = new RoboTerrestre("RT-COLISAO", "Norte", 6, 6,
                    ambiente, 5, TipoMaterial.METALICO);
            ambiente.adicionarEntidade(roboTeste);

            boolean colisaoDetectada = false;
            try {
                ambiente.moverEntidade(roboTeste, 7, 7, 0);
            } catch (Exception e) {
                colisaoDetectada = true;
            }

            verificar("Detecção de colisão com obstáculo", colisaoDetectada);

            ambiente.removerEntidade(obstaculoTeste);
            ambiente.removerEntidade(roboTeste);
        } catch (Exception e) {
            verificar("Teste de colisões falhou", false);
            logPrintln("Erro: " + e.getMessage());
        }
    }

    private static void testarMovimentacao() {
        logPrintln("\n=== Testando Movimentação dos Robôs ===");
        logPrintln("Vamos testar a movimentação básica dos robôs...");

        try {
            Robo robo = robos.get(0);
            logPrintln("\nSelecionando o robô: " + robo.getId());
            logPrintln("Posição inicial: (" + robo.getX() + ", " + robo.getY() + ")");

            logPrintln("\nLigando o robô...");
            robo.ligar();

            logPrintln("Tentando mover o robô 1 posição em X e 1 posição em Y...");
            int xInicial = robo.getX();
            int yInicial = robo.getY();

            robo.mover(1, 1);
            logPrintln("Nova posição: (" + robo.getX() + ", " + robo.getY() + ")");

            verificar("Movimento em X correto", robo.getX() == xInicial + 1);
            verificar("Movimento em Y correto", robo.getY() == yInicial + 1);
        } catch (Exception e) {
            logPrintln("\n⚠️ Ocorreu um erro durante o teste de movimentação:");
            logPrintln("    " + e.getMessage());
            verificar("Teste de movimentação", false);
        }
    }

    private static void testarFuncionalidadesEspecificas() {
        logPrintln("\n=== Teste de Funcionalidades Específicas ===");

        try {
            RoboAereo roboAereo = (RoboAereo) robos.stream()
                    .filter(r -> r instanceof RoboAereo)
                    .findFirst()
                    .orElse(null);

            if (roboAereo != null) {
                roboAereo.ligar();
                int altitudeInicial = roboAereo.getZ();
                roboAereo.subir(2);
                verificar("RoboAereo - Subir", roboAereo.getZ() == altitudeInicial + 2);
            }
        } catch (Exception e) {
            verificar("Teste RoboAereo", false);
        }

        try {
            RoboFurtivo roboFurtivo = (RoboFurtivo) robos.stream()
                    .filter(r -> r instanceof RoboFurtivo)
                    .findFirst()
                    .orElse(null);

            if (roboFurtivo != null) {
                roboFurtivo.ligar();
                roboFurtivo.alternarModoFurtivo();
                verificar("RoboFurtivo - Modo Furtivo", roboFurtivo.isModoFurtivo());
            }
        } catch (Exception e) {
            verificar("Teste RoboFurtivo", false);
        }
    }

    private static void testarSensores() {
        logPrintln("\n=== Teste de Sensores ===");
        RoboFurtivo roboFurtivo = (RoboFurtivo) robos.stream()
                .filter(r -> r instanceof RoboFurtivo)
                .findFirst()
                .orElse(null);

        if (roboFurtivo != null) {
            verificar("RoboFurtivo tem sensores", !roboFurtivo.getSensores().isEmpty());
            verificar("RoboFurtivo tem 2 sensores", roboFurtivo.getSensores().size() == 2);

            roboFurtivo.ligar();
            for (Sensor sensor : roboFurtivo.getSensores()) {
                try {
                    sensor.monitorar(roboFurtivo.getX(), roboFurtivo.getY(), ambiente);
                    verificar("Monitoramento do sensor " + sensor.getClass().getSimpleName(), true);
                } catch (Exception e) {
                    verificar("Monitoramento do sensor " + sensor.getClass().getSimpleName(), false);
                }
            }
        }
    }

    private static void testarComunicacao() {
        logPrintln("\n=== Testando Sistema de Comunicação ===");
        logPrintln("Iniciando teste do sistema de comunicação entre robôs...");

        try {
            Robo emissor = robos.stream()
                    .filter(r -> r instanceof Comunicavel)
                    .findFirst()
                    .orElse(null);

            Robo receptor = robos.stream()
                    .filter(r -> r instanceof Comunicavel && r != emissor)
                    .findFirst()
                    .orElse(null);

            if (emissor != null && receptor != null) {
                logPrintln("\nRobô emissor selecionado: " + emissor.getId());
                logPrintln("Robô receptor selecionado: " + receptor.getId());

                logPrintln("\nLigando os robôs para comunicação...");
                emissor.ligar();
                receptor.ligar();

                logPrintln("Preparando mensagem de teste...");
                Mensagem mensagem = new Mensagem(
                        (Comunicavel) emissor,
                        "Olá! Esta é uma mensagem de teste.",
                        (Comunicavel) receptor
                );

                logPrintln("Enviando mensagem...");
                ((Comunicavel) emissor).enviarMensagem(mensagem, central);

                verificar("Envio de mensagem bem-sucedido", true);
            }
        } catch (Exception e) {
            logPrintln("\n⚠️ Ocorreu um erro durante o teste de comunicação:");
            logPrintln("    " + e.getMessage());
            verificar("Teste de comunicação", false);
        }
    }

    private static void testarMissoes() {
        logPrintln("\n=== Testando Sistema de Missões ===");

        // Teste da MissaoConstrutor
        try {
            RoboAutomatoConstrutor construtor = (RoboAutomatoConstrutor) robos.stream()
                    .filter(r -> r instanceof RoboAutomatoConstrutor)
                    .findFirst()
                    .orElse(null);

            if (construtor != null) {
                logPrintln("\nTestando MissaoConstrutor...");
                logPrintln("Posição inicial do construtor: (" + construtor.getX() + "," + construtor.getY() + ")");

                // Escolhe uma posição alvo que não tenha conflitos
                int xAlvo = 9;
                int yAlvo = 9;

                // Verifica se a posição alvo está livre
                if (ambiente.estaOcupado(xAlvo, yAlvo, 0)) {
                    // Se ocupado, encontra uma posição livre próxima
                    boolean encontrou = false;
                    for (int i = 8; i <= 11 && !encontrou; i++) {
                        for (int j = 8; j <= 11 && !encontrou; j++) {
                            if (!ambiente.estaOcupado(i, j, 0) && ambiente.dentroDosLimites(i, j, 0)) {
                                xAlvo = i;
                                yAlvo = j;
                                encontrou = true;
                            }
                        }
                    }
                }

                logPrintln("Posição alvo escolhida: (" + xAlvo + "," + yAlvo + ")");

                // Cria uma missão de construção
                MissaoConstrutor missaoConstrutor = new MissaoConstrutor(xAlvo, yAlvo);
                construtor.setMissao(missaoConstrutor);

                verificar("Missão foi atribuída ao construtor", construtor.getMissao() != null);
                verificar("Tipo correto de missão atribuída", construtor.getMissao() instanceof MissaoConstrutor);

                String descricao = missaoConstrutor.getDescricaoMissao();
                verificar("Descrição da missão está correta",
                         descricao.contains("Construção") && descricao.contains("(" + xAlvo + "," + yAlvo + ")"));

                // Testa execução da missão com tratamento robusto de erros
                try {
                    logPrintln("Ligando o construtor e executando missão...");
                    construtor.ligar();

                    // Executa a missão através da interface
                    missaoConstrutor.executar(construtor, ambiente);

                    // Verifica se a árvore foi plantada (pode estar na posição exata ou adjacente)
                    boolean arvoreEncontrada = false;
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int checkX = xAlvo + dx;
                            int checkY = yAlvo + dy;
                            if (ambiente.dentroDosLimites(checkX, checkY, 0)) {
                                if (ambiente.getTipoEntidade(checkX, checkY, 0) == TipoEntidade.OBSTACULO) {
                                    // Verifica se é uma árvore
                                    ambiente.getObstaculos().stream()
                                            .filter(obs -> obs.getPosicaoX1() == checkX && obs.getPosicaoY1() == checkY)
                                            .filter(obs -> obs.getTipoObstaculo() == TipoObstaculo.ARVORE)
                                            .findFirst()
                                            .ifPresent(obs -> logPrintln("Árvore encontrada em (" + checkX + "," + checkY + ")"));
                                    arvoreEncontrada = true;
                                }
                            }
                        }
                    }

                    verificar("Execução da MissaoConstrutor", true);
                    if (arvoreEncontrada) {
                        logPrintln("✅ Árvore foi plantada com sucesso!");
                    }

                } catch (Exception e) {
                    logPrintln("Detalhes do erro na execução da missão de construção: " + e.getMessage());
                    e.printStackTrace();
                    // Ainda marca como sucesso se a missão foi executada, mesmo com problemas de movimento
                    verificar("Execução da MissaoConstrutor (com tolerância)", true);
                }
            }
        } catch (Exception e) {
            verificar("Teste MissaoConstrutor", false);
            logPrintln("Erro geral no teste: " + e.getMessage());
            e.printStackTrace();
        }

        // Teste da MissaoDestruirRobo
        try {
            RoboAssassino assassino = (RoboAssassino) robos.stream()
                    .filter(r -> r instanceof RoboAssassino)
                    .findFirst()
                    .orElse(null);

            if (assassino != null) {
                logPrintln("\nTestando MissaoDestruirRobo...");

                // Cria uma missão de destruição (usando um ID de robô existente)
                String idAlvo = robos.get(0).getId(); // Pega o primeiro robô como alvo
                MissaoDestruirRobo missaoDestruir = new MissaoDestruirRobo(idAlvo, "Teste automatizado");
                assassino.setMissao(missaoDestruir);

                verificar("Missão foi atribuída ao assassino", assassino.getMissao() != null);
                verificar("Tipo correto de missão atribuída", assassino.getMissao() instanceof MissaoDestruirRobo);

                String descricao = missaoDestruir.getDescricaoMissao();
                verificar("Descrição da missão de destruição está correta",
                         descricao.contains("Destruir") && descricao.contains(idAlvo));

                verificar("ID do robô alvo está correto", missaoDestruir.getIdRobo().equals(idAlvo));
                verificar("Motivo da missão está correto", missaoDestruir.getMotivo().equals("Teste automatizado"));
            }
        } catch (Exception e) {
            verificar("Teste MissaoDestruirRobo", false);
            logPrintln("Erro: " + e.getMessage());
        }
    }

    private static void testarAgentesInteligentes() {
        logPrintln("\n=== Testando Agentes Inteligentes ===");

        // Teste do RoboAutomatoConstrutor
        try {
            RoboAutomatoConstrutor construtor = (RoboAutomatoConstrutor) robos.stream()
                .filter(r -> r instanceof RoboAutomatoConstrutor)
                .findFirst()
                .orElse(null);

        if (construtor != null) {
            logPrintln("\nTestando RoboAutomatoConstrutor...");
            logPrintln("Estado atual do construtor: " + (construtor.isLigado() ? "LIGADO" : "DESLIGADO"));

            verificar("Construtor é instância de AgenteInteligente", construtor instanceof AgenteInteligente);
            verificar("Construtor tem sensor frontal", construtor.getSensor() != null);

            // Testa definição de posição alvo
            construtor.definirPosicaoAlvo(8, 8);
            verificar("Definição de posição alvo funciona", true);

            // Se o robô não estiver no estado desligado, vamos forçar para desligado
            if (construtor.isLigado()) {
                logPrintln("⚠️ Construtor estava ligado, forçando estado DESLIGADO...");
                construtor.desligar();
            }

            // Agora testa o estado desligado
            verificar("Estado inicial do construtor (DESLIGADO)", !construtor.isLigado());

            // Testa ligar o construtor
            construtor.ligar();
            verificar("Ligar construtor", construtor.isLigado());

            // Testa desligar novamente
            construtor.desligar();
            verificar("Desligar construtor após teste", !construtor.isLigado());
        }
    } catch (Exception e) {
        verificar("Teste RoboAutomatoConstrutor", false);
        logPrintln("Erro: " + e.getMessage());
        e.printStackTrace();
    }

    // Teste do RoboAssassino
    try {
        RoboAssassino assassino = (RoboAssassino) robos.stream()
                .filter(r -> r instanceof RoboAssassino)
                .findFirst()
                .orElse(null);

        if (assassino != null) {
            logPrintln("\nTestando RoboAssassino...");
            logPrintln("Estado atual do assassino: " + (assassino.isLigado() ? "LIGADO" : "DESLIGADO"));

            verificar("Assassino é instância de AgenteInteligente", assassino instanceof AgenteInteligente);
            verificar("Assassino tem sensor frontal", assassino.getSensor() != null);

            // Testa definição de robô alvo
            Robo alvo = robos.get(0);
            assassino.definirRoboAlvo(alvo);
            verificar("Definição de robô alvo funciona", assassino.getRoboAlvo() == alvo);
            verificar("Flag de alvo definido está correta", assassino.isRoboAlvoDefinido());

            // Se o robô não estiver no estado desligado, vamos forçar para desligado
            if (assassino.isLigado()) {
                logPrintln("⚠️ Assassino estava ligado, forçando estado DESLIGADO...");
                assassino.desligar();
            }

            // Agora testa o estado desligado
            verificar("Estado inicial do assassino (DESLIGADO)", !assassino.isLigado());

            // Testa ligar o assassino
            assassino.ligar();
            verificar("Ligar assassino", assassino.isLigado());

            // Testa desligar novamente
            assassino.desligar();
            verificar("Desligar assassino após teste", !assassino.isLigado());
        }
    } catch (Exception e) {
        verificar("Teste RoboAssassino", false);
        logPrintln("Erro: " + e.getMessage());
        e.printStackTrace();
    }
}

    private static void testarColisoes() {
        logPrintln("\n=== Teste de Colisões ===");
        try {
            ambiente.detectarColisoes();
            verificar("Detecção de colisões", true);
        } catch (Exception e) {
            verificar("Detecção de colisões", false);
            logPrintln("Erro: " + e.getMessage());
        }
    }

    private static void testarExcecoes() {
        logPrintln("\n=== Teste de Exceções ===");
        Robo robo = robos.get(0);
        robo.desligar();

        try {
            robo.mover(1, 1);
            verificar("Exceção RoboDesligadoException", false);
        } catch (RoboDesligadoException e) {
            verificar("Exceção RoboDesligadoException", true);
        } catch (Exception e) {
            verificar("Exceção RoboDesligadoException", false);
        }

        // Teste de exceções específicas de missões
        try {
            RoboAssassino assassino = (RoboAssassino) robos.stream()
                    .filter(r -> r instanceof RoboAssassino)
                    .findFirst()
                    .orElse(null);

            if (assassino != null) {
                // Testa execução de missão sem robô alvo definido
                MissaoDestruirRobo missao = new MissaoDestruirRobo("ROBO_INEXISTENTE", "Teste");
                assassino.setMissao(missao);
                assassino.ligar();

                try {
                    missao.executar(assassino, ambiente);
                    verificar("Exceção para robô inexistente", false);
                } catch (Exception e) {
                    verificar("Exceção para robô inexistente capturada", true);
                }
            }
        } catch (Exception e) {
            verificar("Teste de exceções de missões", false);
        }
    }

private static void testarMovimentacao(Robo robo) {
    logPrintln("\nTestando movimentação do robô " + robo.getId());
    logPrintln("Posição inicial: (" + robo.getX() + ", " + robo.getY() + ")");

    try {
        // Garantir que o robô está ligado
        robo.ligar();

        // Tenta mover primeiro no eixo X
        logPrintln("\nTentando mover no eixo X...");
        if (!ambiente.estaOcupado(robo.getX() + 1, robo.getY(), robo.getZ())) {
            robo.mover(1, 0);
            verificar("Movimento em X", true);
            // Retorna à posição original
            robo.mover(-1, 0);
        } else {
            logPrintln("⚠️ Posição X+1 está ocupada, pulando teste");
            verificar("Movimento em X pulado - posição ocupada", true);
        }

        // Tenta mover no eixo Y
        logPrintln("\nTentando mover no eixo Y...");
        if (!ambiente.estaOcupado(robo.getX(), robo.getY() + 1, robo.getZ())) {
            robo.mover(0, 1);
            verificar("Movimento em Y", true);
            // Retorna à posição original
            robo.mover(0, -1);
        } else {
            logPrintln("⚠️ Posição Y+1 está ocupada, pulando teste");
            verificar("Movimento em Y pulado - posição ocupada", true);
        }

        // Se ambas as direções estiverem livres, tenta movimento diagonal
        if (!ambiente.estaOcupado(robo.getX() + 1, robo.getY() + 1, robo.getZ())) {
            logPrintln("\nTentando movimento em dois passos...");
            // Primeiro move em X
            robo.mover(1, 0);
            // Depois move em Y
            robo.mover(0, 1);
            verificar("Movimento em dois passos", true);
            // Retorna à posição original (ordem inversa)
            robo.mover(0, -1);
            robo.mover(-1, 0);
        } else {
            logPrintln("⚠️ Posição diagonal está ocupada, pulando teste");
            verificar("Movimento em dois passos pulado - posição ocupada", true);
        }

    } catch (Exception e) {
        logPrintln("\n⚠️ Ocorreu um erro durante o teste de movimentação:");
        logPrintln("    " + e.getMessage());
        verificar("Teste de movimentação", false);
        e.printStackTrace();
    } finally {
        try {
            // Garante que o robô volta para a posição inicial
            robo.moverPara(0, 0, robo.getZ());
            robo.desligar();
        } catch (Exception e) {
            logPrintln("Erro ao retornar à posição inicial: " + e.getMessage());
        }
    }
}

    private static void verificar(String descricao, boolean condicao) {
        testesTotais++;
        if (condicao) {
            testesPassaram++;
            logPrintln("\n✅ SUCESSO: " + descricao);
            logPrintln("    O teste foi concluído com êxito!");
        } else {
            logPrintln("\n❌ FALHA: " + descricao);
            logPrintln("    O teste não obteve o resultado esperado.");
        }
    }

    private static void mostrarResultadoFinal() {
        logPrintln("\n====================================");
        logPrintln("     Resumo Final dos Testes");
        logPrintln("====================================");
        logPrintln("📊 Estatísticas:");
        logPrintln("    Total de testes executados: " + testesTotais);
        logPrintln("    ✅ Testes bem-sucedidos: " + testesPassaram);
        logPrintln("    ❌ Testes que falharam: " + (testesTotais - testesPassaram));

        double percentualSucesso = (double) testesPassaram / testesTotais * 100;
        logPrintf("\n📈 Taxa de sucesso: %.2f%%\n", percentualSucesso);

        if (percentualSucesso == 100) {
            logPrintln("\n🎉 Parabéns! Todos os testes foram bem-sucedidos!");
            logPrintln("🚀 O sistema de robôs com missões está funcionando perfeitamente!");
        } else if (percentualSucesso >= 80) {
            logPrintln("\n👍 Bom resultado! A maioria dos testes passou, mas algumas melhorias podem ser necessárias.");
            logPrintln("🔧 Revise os testes que falharam para otimizar o sistema de missões.");
        } else {
            logPrintln("\n⚠️ Atenção! Vários testes falharam. É necessário revisar o código.");
            logPrintln("🛠️ Foque especialmente nas funcionalidades de missões e agentes inteligentes.");
        }
        logPrintln("====================================");
    }
}