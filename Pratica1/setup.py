import sqlite3
import argparse
import sys
import os
import time

migrations_dir = 'migrations'
migrations_hist = '.migrations_hist'

class Migration:
    def setup(self, args):
        with open(migrations_hist, 'w') as f:
            f.write('')
        if not os.path.exists(migrations_dir):
            os.mkdir(migrations_dir)
        try:
            conn = sqlite3.connect('database.db')
            conn.close()
        except Exception as e:
            sys.stderr.write(f'[ERR] criando Banco de Dados: {e}.\n')
            sys.exit(1)
        with open('.gitignore', 'r+') as f:
            lines = [line.strip() for line in f]
            f.seek(0, 2)
            if not migrations_hist in lines:
                f.write(migrations_hist + '\n')
            if not 'database.db' in lines:
                f.write('database.db' + '\n')
        sys.stdout.write('Banco configurado com sucesso.\n')

    def run(self, args):
        self._is_seted()
        hist = self._get_hist()
        conn = self._get_conn()
        self._run_migrations(conn, hist)

    def rollback(self, args):
        self._is_seted()
        hist = self._get_hist()
        conn = self._get_conn()
        self._rollback_migrations(args.nome, hist, conn)

    def new(self, args):
        self._is_seted()
        self._new_migration(args.nome)

    def status(self, args):
        self._is_seted()
        hist = set(self._get_hist())
        dirs = set(os.listdir(migrations_dir))
        diff = dirs - hist
        cnt = len(diff)
        if cnt == 0:
            sys.stdout.write('Nenhuma migração pendente.\n')
        else:
            sys.stdout.write(f'{cnt} migrações pendentes.\n')

    def _is_seted(self):
        err_list = []
        if not os.path.exists(migrations_dir):
            err_list.append(migrations_dir)
        if not os.path.exists(migrations_hist):
            err_list.append(migrations_hist)
        if len(err_list) == 0:
            return
        for e in err_list:
            sys.stderr.write(f'[ERR] {e} não foi encontrado.\n')
        sys.stderr.write('[ERR] Verifique se você está no diretório correto'
                         ' e já executou a função \'setup\'\n')
        sys.exit(1)
        return True

    def _get_hist(self):
        if not os.path.exists(migrations_hist):
            sys.stderr.write(f'[ERR] Historico de migrações não existe, tente criar: \'.migrations_hist\''
                              'ou execute a função de \'setup\'\n')
            sys.exit(1)
        hist = []
        with open('.migrations_hist', 'r') as f:
            for line in f:
                s = line.strip()
                if s == '':
                    pass
                hist.append(s)
        return hist

    def _save_to_hist(self, migration_name):
        with open(migrations_hist, 'a') as f:
            f.write(f'{migration_name}\n') 

    def _get_conn(self):
        conn = None
        try:
            conn = sqlite3.connect('database.db')
            conn.isolation_level = None
        except Exception as e:
            sys.stderr.write(f'[ERR] erro conectando ao banco: {e}\n',)
            sys.exit(1)
        return conn

    def _new_migration(self, name):
        dir_full_name = ''
        try:
            dir_name = str(int(round(time.time()))) + '_' + name
            dir_full_name = migrations_dir + os.sep + dir_name
            print(dir_full_name)
            os.mkdir(dir_full_name)
            with open(f'{dir_full_name}{os.sep}push.sql', 'w') as f:
                f.write('-- Arquivo push, insira as alterações que você deseja realizar no banco.\n')
            with open(f'{dir_full_name}{os.sep}pop.sql', 'w') as f:
                f.write('-- Arquivo pop, insira as operações para reverter as alterações aplicadas no push.sql.\n')
        except Exception as e:
            sys.stderr.write(f'[ERR] Criando nova migração: {e}.\n')
            sys.exit(1)
        sys.stdout.write(f'Nova migração criada em: {dir_full_name}.\n')

    def _rollback_migrations(self, migration_name, hist, conn):
        remove_list = []
        r_i = 0
        remove = False 
        for entry in hist:
            sep_index = entry.strip().find('_')
            name = entry[sep_index+1:]
            if name == migration_name.strip():
                remove = True
            if not remove:
                r_i += 1
                pass
            remove_list.append(entry)
        while len(remove_list) > r_i:
            to_remove = remove_list.pop(-1)
            try:
                pop_file = './' + migrations_dir + os.sep + to_remove + os.sep + 'pop.sql'
                with open(pop_file, 'r') as f:
                    cur = conn.cursor()
                    s = f.read()
                    cur.executescript(s)
                    conn.commit()
                    sys.stdout.write(f'Revertendo migração: {to_remove}.\n')
            except Exception as e:
                conn.rollback()
                conn.close()
                sys.stderr.write(f'[ERR] Revertendo migração: {to_remove}: {e}.\n')
                sys.exit(1)
        with open(migrations_hist, 'w') as f:
            for entry in hist[:r_i]:
                f.write(f'{entry}\n')

    def _run_migrations(self, conn, hist):
        dirs = os.listdir(migrations_dir)
        migration_list = []
        cnt = 0
        for directory in dirs:
            if directory in hist:
                continue
            cnt += 1
            sp = directory.find('_')
            migration_list.append((int(directory[:sp]), directory[sp+1:]))
        sorted_migration_list = sorted(migration_list, key=lambda x: x[0])
        for entry in sorted_migration_list:
            directory = str(entry[0]) + '_' + entry[1]
            path = migrations_dir + os.sep + directory
            path = migrations_dir + os.sep + directory
            push_file = os.sep + path + os.sep + 'push.sql'
            pop_file = os.sep + path + os.sep + 'pop.sql'
            cur = conn.cursor()
            try:
                with open('.' + push_file, 'r') as push, open('.' + pop_file, 'r') as pop:
                    sys.stdout.write(f'Executando migração: {directory}.\n')
                    pus = push.read()
                    pos = pop.read()
                    script = 'BEGIN;\n' + pus + '\n' + pos + '\n' + pus + '\nCOMMIT;'
                    cur.executescript(script)
            except Exception as e:
                conn.rollback()
                conn.close()
                sys.stderr.write(f'[ERR] Executando migração {e}.\n')
                sys.exit(1)
            self._save_to_hist(directory)
        conn.close()
        if cnt == 0:
            sys.stdout.write('Nenhuma migração pendente.\n')
        else:
            sys.stdout.write(f'{cnt} migrações executadas.\n')

if __name__ == '__main__':
    parser = argparse.ArgumentParser(
                prog='Setup do Banco de Dados',
                description='Script para configurar o banco de dados do Hackathon.',
                formatter_class=argparse.RawDescriptionHelpFormatter,
                epilog="""Exemplos:
    setup.py setup # comando para criar o banco de dados
    setup.py novo exemplo # cria uma nova migração com o nome 'exemplo'
    setup.py executar # executa todas as migrações pendentes
    setup.py rollback exemplo # reverte todas as alterações até a migração com nome 'exemplo'
    setup.py status # verifica a quantidade de migrações pendentes.
                """)
    subparsers = parser.add_subparsers(title='Sub-comandos')
    setup_subcomando = subparsers.add_parser('setup', 
                                             description='Sub-comando para'
                                             ' criar o banco de dados', 
                                             help='Esse subcomandos não possui argumentos,'
                                             ' quando executado criara todo ambiente para'
                                             ' operar o banco de dados: sqlite3.')
    novo_subcomando = subparsers.add_parser('novo', description='Sub-comando para criar'
                                            ' novas migrações', help='Possui um argumento'
                                            ' posicional, que quando alimentado criara uma'
                                            ' migração com o valor do argumento.')
    novo_subcomando.add_argument('nome', help='nome da migração')
    executar_subcomando = subparsers.add_parser('executar', description='Sub-comando para'
                                                ' executar migrações pendentes', 
                                                help='Esse subcomando não possui argumentos,'
                                                ' quando executado executara todas as migrações'
                                                ' pendentes.')
    rollback_subcomando = subparsers.add_parser('rollback', description='Sub-comando para'
                                                ' reverter migrações.', 
                                                help='Possui um argumento'
                                                ' posicional, que quando alimentado reverte todas'
                                                ' as migrações até e incluindo a migração com o'
                                                ' nome do valor do argumento.')
    rollback_subcomando.add_argument('nome', help='nome da migração')
    status_subcomando = subparsers.add_parser('status', description='Sub-comando para'
                                              ' verificar migrações pendentes.', 
                                              help='Esse subcomando não possui argumentos,'
                                              ' quando executado retorna a quantidade de migrações'
                                              ' pendentes.')
    mi = Migration()
    setup_subcomando.set_defaults(func=mi.setup)
    novo_subcomando.set_defaults(func=mi.new)
    executar_subcomando.set_defaults(func=mi.run)
    rollback_subcomando.set_defaults(func=mi.rollback)
    status_subcomando.set_defaults(func=mi.status)
    args = parser.parse_args()
    if not vars(args):
        parser.print_help()
        sys.exit()
    args.func(args)
