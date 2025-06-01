# 🤖 Simulador de Robôs Inteligentes — Lab 04

Este projeto é uma simulação de um ambiente tridimensional com robôs inteligentes, cada um com capacidades distintas, implementado em Java como parte do Laboratório 4 da disciplina MC322.

---

## 📌 Objetivos do Lab

- Aplicar **interfaces e polimorfismo**
- Utilizar **herança múltipla via interfaces**
- Criar e lançar **exceções personalizadas**
- Integrar **sensores e tarefas robóticas específicas**
- Desenvolver um **menu interativo** para controlar os robôs

---

## 📁 Estrutura do Projeto

```
lab04/
├── Main.java
├── TesteAutomatizado.java
├── ambiente/                 # Classe Ambiente (mapa 3D, controle de entidades)
├── comunicacao/              # Central de comunicação entre robôs
├── excecoes/                 # Exceções personalizadas
├── interfaces/               # Interfaces comportamentais e estruturais
├── obstaculo/                # Tipos de obstáculos e suas características
├── robos/                    # Classes dos robôs com especializações
├── sensores/                # Sensores específicos usados por robôs
```

---

## 🧠 Interfaces Implementadas

| Interface       | Função                                         | Onde é implementada                     |
|----------------|------------------------------------------------|------------------------------------------|
| `Entidade`      | Contrato base para qualquer objeto do ambiente | Todos os robôs e obstáculos              |
| `Comunicavel`   | Envio e recebimento de mensagens               | Robôs como `RoboFurtivo` e `Cargueiro`   |
| `Sensoreavel`   | Comportamento sensorial                        | Todos os robôs que usam sensores         |
| `Carregavel`    | Capacidade de carga                            | `RoboCargueiro`                          |
| `IBispoRobo`    | Movimento em diagonal                          | `BispoRobo`                              |
| `ICavaloRobo`   | Movimento em L                                 | `CavaloRobo`                             |

---

## 💥 Exceções Personalizadas

| Exceção                        | Finalidade                                                              |
|--------------------------------|-------------------------------------------------------------------------|
| `RoboDesligadoException`       | Ação não permitida com robô desligado                                   |
| `ColisaoException`             | Tentativa de ocupar espaço já ocupado                                   |
| `ForaDosLimitesException`      | Movimento fora dos limites do ambiente                                  |
| `MoverObstaculoException`      | Tentativa de mover um obstáculo                                         |
| `EnergiaInsuficienteException` | Falta de energia para movimentar ou executar tarefa                     |
| `RoboIncomunicavelException`   | Tentativa de comunicação com robô que não implementa `Comunicavel`      |
| `CargaExcessivaException`      | Tentativa de carregar peso acima do limite                              |
| `RoboTerrestreNaoVoaException` | Tentativa de robô terrestre voar                                        |

---

## 🔧 Robôs e Tarefas Específicas

| Robô               | Especialidade                            | Tarefa específica (executarTarefa)         |
|--------------------|-------------------------------------------|--------------------------------------------|
| `RoboTerrestre`    | Movimenta-se no plano XY                  | Patrulhar área do solo                      |
| `RoboAereo`        | Movimenta-se em 3D                        | Sobrevoar e mapear regiões elevadas        |
| `RoboFurtivo`      | Comunicação furtiva                       | Espionar e reportar                        |
| `RoboCargueiro`    | Carregamento de cargas                    | Transportar recursos entre regiões         |
| `BispoRobo`        | Movimento diagonal                        | Cobertura tática em X-Y com sensores       |
| `CavaloRobo`       | Movimento em L                            | Avanço estratégico em pontos complexos     |

---

## 🌍 Ambiente

- Representado por um cubo 3D (`TipoEntidade[][][]`)
- Entidades registradas em lista (`ArrayList<Entidade>`)
- Permite:
  - Adição/remoção de robôs
  - Visualização 2D (plano XY)
  - Detecção de colisões
  - Limite espacial

---

## 🧱 Obstáculos

Tipos definidos em `TipoObstaculo`:

- `PAREDE`, `ARVORE`, `PREDIO`, `BURACO`, `OUTRO`
- Cada um tem altura e bloqueio de passagem
- Representação no mapa: `#`, `@`, `&`, `O`, `?`

---

## 👁️ Sensores

Localizados em `sensores/`, destacam-se:

- `SensorMagnetico`, `SensorUltrassonico`
- `SensorMovimentoCavalo`, `SensorMovimentoDiagonal`
- `SensorStamina`, `SensorLimiteAmbiente`, `SensorAlcanceDiagonal`

Todos utilizam polimorfismo com a interface `Sensoreavel`.

---

## 💬 Comunicação

- Robôs que implementam `Comunicavel` podem enviar e receber mensagens.
- `CentralComunicacao` armazena histórico global de todas as interações.
- Exibição das mensagens é possível via menu.

---

## 🕹️ Menu Interativo

Disponível em `Main.java`, com as seguintes funcionalidades:

- Listar robôs por tipo e estado
- Controlar movimentação 3D
- Executar tarefas específicas
- Ativar/desligar robôs
- Ver ambiente 2D
- Monitorar sensores
- Realizar comunicação
- Ver mensagens trocadas

---

## 🧪 Testes Automatizados

`TesteAutomatizado.java` permite simulações com:
- Movimentações de robôs
- Testes de exceções
- Ações predefinidas

---

## 🧱 Diagrama UML

Veja o arquivo: [`diagrama_lab4.png`](./diagrama_lab4.png)

Inclui:
- Interfaces
- Relações de herança
- Exceções personalizadas

---

## ▶️ Compilação e Execução

### Usando terminal:

```bash
javac */*.java *.java
java Main
```

Ou para teste automatizado:

```bash
java TesteAutomatizado
```

---

## ✅ Requisitos Atendidos

- [x] Polimorfismo
- [x] Herança múltipla com interfaces
- [x] Exceções personalizadas
- [x] Tarefas específicas por robô
- [x] Sistema de sensores e comunicação
- [x] Menu interativo completo
- [x] Testes em classe separada
- [x] Documentação detalhada (este README)

---

Desenvolvido por: **Gabriel Mattias Antunes e Isaias Junio Jarcem**  
UNIVERSIDADE ESTADUAL DE CAMPINAS – Engenharia de Computação  
MC322 – Programação Orientada a Objetos

