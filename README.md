# 🤖 Simulador de Robôs Inteligentes

&#x20;

Simulador 3D de robôs autônomos com diferentes capacidades, desenvolvido em Java para a disciplina **MC322 – Programação Orientada a Objetos** (UNICAMP).

---

## 🚀 Objetivos

* Exercitar **interfaces** e **polimorfismo** em Java
* Implementar **herança múltipla** via interfaces
* Implementar **Agentes Inteligentes**
* Gerar relatórios LOGS
* Criar e lançar **exceções personalizadas**
* Integrar **sensores** e definir **tarefas específicas**
* Desenvolver um **menu interativo** para controle e monitoramento de robôs

---

## 🛠️ Tecnologias & IDEs

* **Linguagem:** Java SE
* **IDE:** IntelliJ IDEA, Visual Studio Code

---

## 📁 Estrutura do Projeto

```
lab04/
├── Main.java               # Entrada, menu interativo
├── TesteAutomatizado.java  # Testes JUnit de movimentação e exceções
├── ambiente/               # Gerenciamento do mapa 3D (classe Ambiente)
├── comunicacao/            # CentralComunicacao e histórico de mensagens
├── excecoes/               # Exceções personalizadas
├── interfaces/             # Interfaces (Entidade, Comunicavel, etc.)
├── obstaculo/              # Definições de TipoObstaculo e suas características
├── robos/                  # Implementação de robôs (RoboTerrestre, etc.)
└── sensores/               # Sensores e GerenciadorSensores
```

---

## 🧠 Interfaces Implementadas

| Interface       | Descrição                              | Implementação                  |
| -------------   | -------------------------------------- | ------------------------------ |
| `Entidade`    | Contrato base para objetos no ambiente   | Todos robôs e obstáculos       |
| `Comunicavel` | Envia e recebe mensagens                 | `RoboFurtivo`, `RoboCargueiro` |
| `Sensoreavel` | Detecção e notificação de sensores       | Robôs com sensores             |
| `Carregavel`  | Capacidade de transportar carga          | `RoboCargueiro`                |
| `IBispoRobo`  | Movimento diagonal no plano XY           | `BispoRobo`                    |
| `ICavaloRobo`        | Movimento em L                    | `CavaloRobo`                   |
| `MissaoConstrutor`   | Guia nosso robo construtor        | `RoboAutomatoConstrutor`       |
| `MIssaoDestruirRobo` | Guia nosso robo assasino          | `RoboAssassino `               |
| `Missao`             | "pai" de todas as MIssoes         | `E herdada pelas outras`       |


---

## 💥 Exceções Personalizadas

| Exceção                        | Finalidade                                   |
| ------------------------------ | -------------------------------------------- |
| `RoboDesligadoException`       | Ação inválida ao operar robô desligado       |
| `ColisaoException`             | Tentativa de mover-se para célula ocupada    |
| `ForaDosLimitesException`      | Movimento fora dos limites do ambiente       |
| `MoverObstaculoException`      | Tentativa de deslocar obstáculo              |
| `EnergiaInsuficienteException` | Energia insuficiente para ação               |
| `RoboIncomunicavelException`   | Comunicação com robô sem suporte             |
| `CargaExcessivaException`      | Excesso de carga acima da capacidade do robô |
| `RoboTerrestreNaoVoaException` | Tentativa de voo com robô terrestre          |

---

## 🤖 Robôs & Tarefas (executarTarefa)

| Robô                    | Especialidade          | Tarefa                                   |
| ---------------         | ---------------------- | ---------------------------------------- |
| `RoboTerrestre`         | Plano XY               | Patrulhar área terrestre                 |
| `RoboAereo`             | Movimento em 3D        | Sobrevoar e mapear áreas elevadas        |
| `RoboFurtivo`           | Comunicação discreta   | Espionagem e envio de relatórios         |
| `RoboCargueiro`         | Transporte de recursos | Carregar e entregar cargas               |
| `BispoRobo`             | Movimento diagonal     | Cobertura tática com sensores            |
| `CavaloRobo`            | Movimento em L         | Avanço estratégico em terrenos complexos |
| `RoboAutomatoConstrutor`| Planta arvores         | Adiciona obstáculos no caminho           |
| `RoboAssassino`         | Destroi outros robos   | Robo autonomo que destroi outros         |
| `AgenteInteligente`     | Movimento automatico   | Pai dos Robos que recebem missoes        |



---

## 🌍 Ambiente & Obstáculos

* **Ambiente:** Grade 3D (`TipoEntidade[][][]`) gerenciada por `Ambiente`.
* **Obstáculos** (`TipoObstaculo`): `PAREDE`, `ARVORE`, `PREDIO`, `BURACO`, `OUTRO`.

  * Cada tipo define altura, bloqueio de passagem e símbolo de mapa.

---

## 👁️ Sensores

Implementações em `sensores/`:

* `SensorMagnetico`, `SensorUltrassonico`
* `SensorMovimentoCavalo`, `SensorMovimentoDiagonal`
* `SensorStamina`, `SensorLimiteAmbiente`, `SensorAlcanceDiagonal`

`GerenciadorSensores` centraliza leituras e notifica atuadores.

---

## 💬 Comunicação

* Robôs com `Comunicavel` trocam mensagens via `CentralComunicacao`.
* Histórico global disponível no menu interativo.

---

## 🕹️ Menu Interativo

Opções em `Main.java`:

1. Listar robôs (ID, tipo, estado, posição)
2. Movimentação manual (X, Y, Z)
3. Atribuir/executar missão
4. Ativar/desligar robô
5. Exibir ambiente 2D (plano XY)
6. Monitorar sensores
7. Enviar/ler mensagens
8. Exibir logs no terminal
9. Sair

---

## 🧪 Testes Automatizados

`TesteAutomatizado.java`:

* Movimentações válidas e inválidas
* Comunicação e restrições de interfaces
* Lançamento de exceções personalizadas

---

## 📐 Diagrama UML

Confira `diagrama_lab5.png` para visão completa de classes, interfaces e exceções.

---

## ▶️ Compilação & Execução

### Terminal

```bash
# Compilar
javac -d out Main.java TesteAutomatizado.java */*.java

# Executar simulador
java -cp out Main

# Executar testes
java -cp out TesteAutomatizado
```

---

## 👥 Autores

* **Gabriel Mattias Antunes**
* **Isaias Junio Jarcem**

UNICAMP – Engenharia de Computação | MC322
