import json
from typing import List

indexpath = 'index_rotation.json'


def index_rotation(index: dict):
    print('\nGenerating rotation index')
    words = index.keys()

    data = {}
    for word in words:
        for rotation in rotations(word):
            data[rotation] = word

    with open(indexpath, 'w+') as index_file:
        json.dump(data, index_file)

    print('\nFinished generating rotation index')


def rotations(word: str) -> List[str]:
    w = f'{word}$'
    result = [w]
    for i in range(0, len(w)):
        w = f'{w[1:]}{w[0]}'
        result.append(w)
    return result
