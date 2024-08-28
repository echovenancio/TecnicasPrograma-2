Use o setup.py para configurar o banco.
Rodando os comandos:
```
python3 setup.py setup
python3 setup.py executar
```

Uso do script:

```
usage: Setup do Banco de Dados [-h] {setup,novo,executar,rollback,status} ...

Script para configurar o banco de dados do Hackathon.

options:
  -h, --help            show this help message and exit

Sub-comandos:
  {setup,novo,executar,rollback,status}
    setup               Esse subcomandos não possui argumentos, quando executado criara todo ambiente para operar o banco de dados: sqlite3.
    novo                Possui um argumento posicional, que quando alimentado criara uma migração com o valor do argumento.
    executar            Esse subcomando não possui argumentos, quando executado executara todas as migrações pendentes.
    rollback            Possui um argumento posicional, que quando alimentado reverte todas as migrações até e incluindo a migração com o nome do valor do argumento.
    status              Esse subcomando não possui argumentos, quando executado retorna a quantidade de migrações pendentes.

Exemplos:
    setup.py setup # comando para criar o banco de dados
    setup.py novo exemplo # cria uma nova migração com o nome 'exemplo'
    setup.py executar # executa todas as migrações pendentes
    setup.py rollback exemplo # reverte todas as alterações até a migração com nome 'exemplo'
    setup.py status # verifica a quantidade de migrações pendentes.

```
