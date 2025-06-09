
import ambiente.Ambiente;
import comunicacao.CentralComunicacao;
import comunicacao.Mensagem;
import excecoes.RoboDesligadoException;
import interfaces.Comunicavel;
import interfaces.TipoEntidade;
import obstaculo.*;
import robos.*;
import sensores.*;

import java.util.ArrayList;

public class TesteAutomatizado {
    private static Ambiente ambiente;
    private static ArrayList<Robo> robos;
    private static CentralComunicacao central;
    private static int testesPassaram = 0;
    private static int testesTotais = 0;

    public static void main(String[] args) {
        System.out.println("Iniciando Testes Automatizados do Sistema de Robôs\n");

        inicializarAmbiente();
        executarTestes();

        mostrarResultadoFinal();
    }

    private static void inicializarAmbiente() {
        try {
            ambiente = new Ambiente(10, 10, 10);
            robos = new ArrayList<>();
            central = new CentralComunicacao();

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

            verificar("Inicialização do ambiente", true);
        } catch (Exception e) {
            verificar("Inicialização do ambiente", false);
            System.out.println("Erro na inicialização: " + e.getMessage());
        }
    }

    private static void executarTestes() {
        testarInicializacao();
        testarEstadoRobos();
        testarMovimentacao();
        testarFuncionalidadesEspecificas();
        testarSensores();
        testarComunicacao();
        testarMapa();             // Novo teste
        testarAdicoesNoMapa();    // Novo teste
        testarDeteccaoColisoes(); // Novo teste
        testarColisoes();
        testarExcecoes();
    }


    private static void testarInicializacao() {
        System.out.println("\n=== Teste de Inicialização ===");
        verificar("Ambiente foi criado", ambiente != null);
        verificar("Lista de robôs foi criada", robos != null);
        verificar("Quantidade correta de robôs", robos.size() == 6);
    }

    private static void testarEstadoRobos() {
        System.out.println("\n=== Teste de Estado dos Robôs ===");
        for (Robo robo : robos) {
            robo.desligar();
            verificar(robo.getId() + " - Estado inicial desligado", !robo.isLigado());

            robo.ligar();
            verificar(robo.getId() + " - Ligar robô", robo.isLigado());

            robo.desligar();
            verificar(robo.getId() + " - Desligar robô", !robo.isLigado());
        }
    }
    private static void testarMapa() {
        System.out.println("\n=== Testando Funcionalidades do Mapa ===");

        // Teste de inicialização do mapa
        verificar("Mapa foi inicializado corretamente", ambiente != null);

        // Teste das dimensões do mapa
        verificar("Largura do mapa é válida", ambiente.getLargura() == 10);
        verificar("Altura do mapa é válida", ambiente.getAltura() == 10);
        verificar("Altitude do mapa é válida", ambiente.getAltitude() == 10);

        // Teste de posições dentro dos limites
        verificar("Posição (0,0,0) está dentro dos limites", ambiente.dentroDosLimites(0, 0, 0));
        verificar("Posição (9,9,9) está dentro dos limites", ambiente.dentroDosLimites(9, 9, 9));
        verificar("Posição (-1,0,0) está fora dos limites", !ambiente.dentroDosLimites(-1, 0, 0));
        verificar("Posição (10,10,10) está fora dos limites", !ambiente.dentroDosLimites(10, 10, 10));

        // Teste de ocupação de posições
        int x = 5, y = 5, z = 0;
        boolean posicaoInicialmenteOcupada = ambiente.estaOcupado(x, y, z);
        verificar("Verificação de ocupação de posição funciona",
                posicaoInicialmenteOcupada == (ambiente.getTipoEntidade(x, y, z) != TipoEntidade.VAZIO));
    }

    private static void testarAdicoesNoMapa() {
        System.out.println("\n=== Testando Adições no Mapa ===");

        // Teste de adição de obstáculo
        try {
            Obstaculo novoObstaculo = new Obstaculo(2, 7, 3, 9, TipoObstaculo.PAREDE);
            ambiente.adicionarEntidade(novoObstaculo);
            verificar("Adição de obstáculo", ambiente.estaOcupado(2, 7, 2));

            // Verifica se o obstáculo está na lista de obstáculos
            boolean obstaculoEncontrado = ambiente.getObstaculos().stream()
                    .anyMatch(obs -> obs.getPosicaoX1() == 2 &&
                            obs.getPosicaoY1() == 7 &&
                            obs.getPosicaoX2() == 3 &&
                            obs.getPosicaoY2() == 9);
            verificar("Obstáculo está na lista de obstáculos", obstaculoEncontrado);

            // Remove o obstáculo para testes futuros
            ambiente.removerEntidade(novoObstaculo);
        } catch (Exception e) {
            verificar("Adição de obstáculo falhou", false);
            System.out.println("Erro: " + e.getMessage());
        }

        // Teste de adição de robô
        try {
            RoboTerrestre novoRobo = new RoboTerrestre("RT-TEST", "Norte", 6, 6,
                    ambiente, 5, TipoMaterial.METALICO);
            ambiente.adicionarEntidade(novoRobo);
            verificar("Adição de robô", ambiente.estaOcupado(6, 6, 0));

            // Verifica se o robô está na lista de robôs
            boolean roboEncontrado = ambiente.getRobos().stream()
                    .anyMatch(r -> r.getId().equals("RT-TEST"));
            verificar("Robô está na lista de robôs", roboEncontrado);

            // Remove o robô para testes futuros
            ambiente.removerEntidade(novoRobo);
        } catch (Exception e) {
            verificar("Adição de robô falhou", false);
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void testarDeteccaoColisoes() {
        System.out.println("\n=== Testando Detecção de Colisões ===");

        try {
            // Cria um obstáculo em uma posição específica
            Obstaculo obstaculoTeste = new Obstaculo(5, 5, 5, 5, TipoObstaculo.PAREDE);
            ambiente.adicionarEntidade(obstaculoTeste);

            // Tenta mover um robô para a posição do obstáculo
            RoboTerrestre roboTeste = new RoboTerrestre("RT-COLISAO", "Norte", 4, 4,
                    ambiente, 5, TipoMaterial.METALICO);
            ambiente.adicionarEntidade(roboTeste);

            boolean colisaoDetectada = false;
            try {
                ambiente.moverEntidade(roboTeste, 5, 5, 0);
            } catch (Exception e) {
                colisaoDetectada = true;
            }

            verificar("Detecção de colisão com obstáculo", colisaoDetectada);

            // Limpa as entidades de teste
            ambiente.removerEntidade(obstaculoTeste);
            ambiente.removerEntidade(roboTeste);
        } catch (Exception e) {
            verificar("Teste de colisões falhou", false);
            System.out.println("Erro: " + e.getMessage());
        }
    }


    private static void testarMovimentacao() {
        System.out.println("\n=== Testando Movimentação dos Robôs ===");
        System.out.println("Vamos testar a movimentação básica dos robôs...");

        try {
            Robo robo = robos.get(0);
            System.out.println("\nSelecionando o robô: " + robo.getId());
            System.out.println("Posição inicial: (" + robo.getX() + ", " + robo.getY() + ")");

            System.out.println("\nLigando o robô...");
            robo.ligar();

            System.out.println("Tentando mover o robô 1 posição em X e 1 posição em Y...");
            int xInicial = robo.getX();
            int yInicial = robo.getY();

            robo.mover(1, 1);
            System.out.println("Nova posição: (" + robo.getX() + ", " + robo.getY() + ")");

            verificar("Movimento em X correto", robo.getX() == xInicial + 1);
            verificar("Movimento em Y correto", robo.getY() == yInicial + 1);
        } catch (Exception e) {
            System.out.println("\n⚠️ Ocorreu um erro durante o teste de movimentação:");
            System.out.println("    " + e.getMessage());
            verificar("Teste de movimentação", false);
        }
    }


    private static void testarFuncionalidadesEspecificas() {
        System.out.println("\n=== Teste de Funcionalidades Específicas ===");

        // Teste RoboAereo
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

        // Teste RoboFurtivo
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
        System.out.println("\n=== Teste de Sensores ===");
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
        System.out.println("\n=== Testando Sistema de Comunicação ===");
        System.out.println("Iniciando teste do sistema de comunicação entre robôs...");

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
                System.out.println("\nRobô emissor selecionado: " + emissor.getId());
                System.out.println("Robô receptor selecionado: " + receptor.getId());

                System.out.println("\nLigando os robôs para comunicação...");
                emissor.ligar();
                receptor.ligar();

                System.out.println("Preparando mensagem de teste...");
                Mensagem mensagem = new Mensagem(
                        (Comunicavel) emissor,
                        "Olá! Esta é uma mensagem de teste.",
                        (Comunicavel) receptor
                );

                System.out.println("Enviando mensagem...");
                ((Comunicavel) emissor).enviarMensagem(mensagem, central);

                verificar("Envio de mensagem bem-sucedido", true);
            }
        } catch (Exception e) {
            System.out.println("\n⚠️ Ocorreu um erro durante o teste de comunicação:");
            System.out.println("    " + e.getMessage());
            verificar("Teste de comunicação", false);
        }
    }


    private static void testarColisoes() {
        System.out.println("\n=== Teste de Colisões ===");
        try {
            ambiente.detectarColisoes();
            verificar("Detecção de colisões", true);
        } catch (Exception e) {
            verificar("Detecção de colisões", false);
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void testarExcecoes() {
        System.out.println("\n=== Teste de Exceções ===");
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
    }

    private static void verificar(String descricao, boolean condicao) {
        testesTotais++;
        if (condicao) {
            testesPassaram++;
            System.out.println("\n✅ SUCESSO: " + descricao);
            System.out.println("    O teste foi concluído com êxito!");
        } else {
            System.out.println("\n❌ FALHA: " + descricao);
            System.out.println("    O teste não obteve o resultado esperado.");
        }
    }


    private static void mostrarResultadoFinal() {
        System.out.println("\n====================================");
        System.out.println("     Resumo Final dos Testes");
        System.out.println("====================================");
        System.out.println("📊 Estatísticas:");
        System.out.println("    Total de testes executados: " + testesTotais);
        System.out.println("    ✅ Testes bem-sucedidos: " + testesPassaram);
        System.out.println("    ❌ Testes que falharam: " + (testesTotais - testesPassaram));

        double percentualSucesso = (double) testesPassaram / testesTotais * 100;
        System.out.printf("\n📈 Taxa de sucesso: %.2f%%\n", percentualSucesso);

        if (percentualSucesso == 100) {
            System.out.println("\n🎉 Parabéns! Todos os testes foram bem-sucedidos!");
        } else if (percentualSucesso >= 80) {
            System.out.println("\n👍 Bom resultado! A maioria dos testes passou, mas algumas melhorias podem ser necessárias.");
        } else {
            System.out.println("\n⚠️ Atenção! Vários testes falharam. É necessário revisar o código.");
        }
        System.out.println("====================================");
    }
}