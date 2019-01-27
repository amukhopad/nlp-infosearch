import os

from index import index
from search import query_cli

datadir = '/Users/enginebreaksdown/dev/data'
indexfile = 'index.json'


def cli():
    print('Welcome to my pathetic file indexer.')
    help()
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
            help()



def help():
    print("""
        - To list files type <list>
        - To initiate index type <index>
        - To execute query type <query>
        """)



if __name__ == "__main__":
    cli()
