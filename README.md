<h1 align="center">
📊⚽<br>Match Saver API - Gerenciamento de Partidas 
</h1>

## 📚 O que é o Match Saver API?

A Match Saver API é uma interface desenvolvida para simplificar o gerenciamento de partidas esportivas. 
Esta API oferece uma variedade de funcionalidades essenciais para organizadores de eventos esportivos, 
clubes, e entusiastas que desejam manter um registro detalhado e preciso de suas partidas.

## 🐋 Executando a imagem MySQL com Docker Compose

Passo 1: Clonando o Repositório
Primeiro, clone o repositório do projeto para o seu computador local:

Passo 1: Clonando o Repositório
Primeiro, você precisa clonar o repositório do projeto para o seu computador local. Use o seguinte comando para clonar o repositório a partir do GitHub:

`git clone https://github.com/joaocruzmeli/match-saver-api.git`

Passo 2: Navegando para o Diretório do Projeto

Após a conclusão da clonagem, navegue para o diretório do projeto:

`cd matchsaver`

Passo 3: Construindo e Executando com Docker Compose
Certifique-se de estar no diretório raiz do stockz-docker, onde o arquivo docker-compose.yml está localizado. Use o seguinte comando para construir e executar os contêineres definidos no arquivo docker-compose.yml:

`docker-compose up -d`

## 🔧 Funcionalidades Principais:

📔 Gerenciamento de Partidas:

Crie, liste, atualize e exclua partidas de futebol.
Registre informações essenciais, como nome dos clubes, resultado da partida, data e hora, e nome do estádio.

🔄 Operações Básicas nas Partidas:

Realize operações básicas, como cadastrar uma nova partida, atualizar dados existentes e remover partidas do cadastro.

🔍 Buscas Específicas:

Realize buscas específicas, incluindo:


1. Partidas com goleadas (3 ou mais gols de diferença para um dos clubes).
2. Partidas sem gols para nenhum dos clubes.
3. Todas as partidas de um clube específico, podendo filtrar por atuação como mandante ou visitante.
4. Todas as partidas de um estádio específico.

📈 Retrospecto e Estatísticas:

1. Retrospecto Geral de um Clube:

- Receba um clube como entrada.
- Retorne a somatória de vitórias, empates, derrotas, gols pró e gols sofridos em todos os jogos.
- Filtre o retrospecto como mandante ou visitante.

2. Retrospecto Geral de um Confronto:

- Receba dois clubes como entrada.
- Retorne a somatória total dos resultados dos jogos entre esses dois clubes.
- Filtre o retrospecto total de um dos clubes como mandante.

3. Lista de Fregueses:

- Receba um clube X como entrada.
- Retorne os 5 clubes sobre os quais o clube X tem maior diferença positiva no confronto (vitórias sobre derrotas).
- Caso tenha menos de 5 clubes com saldo positivo, retorne a quantidade disponível.
- Ordene por diferença entre vitórias e derrotas, do maior para o menor.
- Inclua nome do clube freguês, quantidade total de partidas, vitórias e derrotas do clube X.


## 👨‍💻 Autor

Nome: João Cruz<br>Linkedin: https://www.linkedin.com/in/joaosilvacruz/

---

<h4 align=center>Made with 💚 by <a href="https://github.com/joaocruzmeli">João Cruz</a></h4>