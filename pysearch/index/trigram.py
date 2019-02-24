import json
from typing import List

indexpath = 'index_trigram.json'


def index_trigram(index: dict):
    print('\nGenerating trigram index')
    words = index.keys()

    data = {}
    for word in words:
        for tr in trigram(word):
            data[tr] = data.get(tr, [])
            data[tr].append(word)

    with open(indexpath, 'w+') as index_file:
        json.dump(data, index_file)

    print('\nFinished generating trigram index')


def trigram(word: str)-> List[str]:
    result = []
    w = f'$${word}$$'
    for i in range(0, len(w) - 2):
        result.append(w[i:i + 3])
    return result
