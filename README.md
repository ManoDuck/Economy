# Economy

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/41ceccfd3fa241f3a9741f6996f44ccd)](https://www.codacy.com/gh/ManoDuck/Economy/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ManoDuck/Economy&amp;utm_campaign=Badge_Grade)

Um sistema básico de economia para servidores de minecraft, com mensagens configuráveis e em breve com diversas opções configuráveis. As informações do plugin são salvas apenas em MySQL devido ao uso da tecnologia do HikariCP.

## Comandos
|Comando         |Descrição                      |Permissão                    |
|----------------|-------------------------------|-----------------------------|
|/money |Veja o seu money.|Nenhuma    |
|/money pay    |Envie uma quantia de money para outra pessoa.|Nenhuma    |
|/money [jogador]       |Veja o money de outro jogador.|Nenhuma         |
|/money add    |Adicione money a um jogador.|`economy.money.add`   |
|/money set  |Defina o money de um jogador.|`economy.money.set`   |                          |

## Download

Você pode encontrar o plugin pronto para baixar [**aqui**](https://github.com/ManoDuck/Economy/releases), ou se você quiser, pode optar por clonar o repositório e dar build no plugin com suas alterações.

## Configuração

O plugin conta com apenas 2 arquivos de configuração. (messages.yml e config.yml)

## Placeholders

### LegendChat ou nChat
-   "{tycoon}" ~ tag magnata.

## Dependências

### Obrigatórias
-   [Vault](https://github.com/MilkBowl/VaultAPI) - para suporte a todos os plugins que exigem uma economia (Spawners, Maquinas, Lojas, etc...).

## Informações de desenvolvimento

### Tecnologias usadas
-   [Lombok](https://projectlombok.org/) - Gera getters, setters e outros métodos útils durante a compilação por meio de anotações.
-   [HikariCP](https://github.com/brettwooldridge/HikariCP) - Tecnologia de pool de banco de dados.

### APIs e Frameworks
-   [configuration-injector](https://github.com/HenryFabio/configuration-injector) - Injetar valores de configurações automaticamente;
