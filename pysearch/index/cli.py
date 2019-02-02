import json
import os

from index import index
from search import interpret, print_search_result

datadir = '/Users/enginebreaksdown/dev/data'
indexfile = 'index.json'


def cli():
    print('Welcome to my pathetic file indexer.')
    help_cli()
    while True:
        cmd = input('>> ')

        if cmd == 'list':
            print('\n'.join(map(str, os.listdir(datadir))))
        elif cmd == 'index':
            index(datadir, indexfile)
        elif cmd == 'query':
            if not os.path.isfile(indexfile):
                print("""
                Ooops, your files seem not to be indexed.
                To initiate index type <index>.
                """)
            else:
                query_cli(indexfile)
        else:
            help_cli()


def query_cli(filename: str):
    json_file = open(filename, 'r')

    index = json.load(json_file)

    while True:
        query = input('query> ')
        try:
            ans = interpret(query, index)
            print_search_result(ans, index)
        except (SyntaxError, ValueError) as e:
            print(f"Bad syntax. {e}")


def help_cli():
    print("""
        - To list files type <list>
        - To initiate index type <index>
        - To execute query type <query>
        """)


if __name__ == "__main__":
    cli()
