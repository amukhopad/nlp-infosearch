import re
from typing import List

from index import process


def interpret(query: str, index: dict) -> List[str]:
    operators = r'(&&|\|\||!)'
    split = re.split(f'\s*{operators}\s*', query)
    cmd = list(filter(lambda x: x != '', split))

    if len(cmd) < 2:
        return list(search(query, index))

    eval_neg = eval_not(cmd, index)
    eval_and = eval(eval_neg, index, '&&', 0, lambda a, b: a & b)
    eval_or = eval(eval_and, index, '||', 0, lambda a, b: a | b)
    print(eval_or)
    return eval_or


def eval(query: List[str], index: dict, operator: str, i: int, func) -> List[str]:
    if i >= len(query):
        return query

    param = query[i]
    if param == operator:
        left = search(query[i - 1], index)
        right = search(query[i + 1], index)
        query[i-1:i+2] = func(left, right)
        return eval(query, index, operator, i-1, func)

    return eval(query, index, operator, i+1, func)


def eval_not(query: List[str], index: dict) -> List[str]:
    evaluated = []
    idx = 0
    full = set(map(lambda i: str(i), set(range(0, len(index['files'])))))

    while idx < len(query):
        param = query[idx]
        if param == '!':
            s = search(query[idx + 1], index)
            print(f'Search with ! {s}')
            param = full - s
            idx += 1
        evaluated.append(str(param))
        idx += 1
    return evaluated


def search(query: str, index: dict) -> set:
    split = query.rsplit(' ', 2)
    distance = 1
    if len(split) == 3 and split[1].startswith('-d'):
        distance = int(split[2])
        query = split[0]

    result = set(map(lambda i: str(i), set(range(0, len(index['files'])))))
    query = process(query)

    print(f'processed serach query  {query}')
    for word in query:
        searched = index['index'].get(word, {})
        files = searched.keys()
        print(f'Word {word} is contained in {files}')
        result &= set(files)

    return check_distance(query, result, index, distance)


def check_distance(query: List[str], files: List[str], index: dict, distance: int) -> set:
    if len(query) == 1:
        return set(files)

    result = []
    for file in files:
        if found_in_file(query, file, index, distance):
            result.append(file)

    return set(result)


def found_in_file(query: List[str], file: str, index: dict, distance: int) -> bool:
    first = set(index['index'].get(query[0], {}).get(file))
    for start in first:
        for w in range(1, len(query)):
            word = query[w]
            word_files = set(index['index'].get(word, {}).get(file))
            if word_not_in_distance(word_files, start, distance):
                break
        return True

    return False


def word_not_in_distance(word_files: set, start: int, distance: int) -> bool:
    for d in range(1, distance + 1):
        pos = start + d
        if pos in word_files:
            return False
    return True


def print_search_result(result: List[str], index: dict):
    if len(result) == 0:
        print("No results found :c")

    files = index['files']
    for r in result:
        print(files[int(r)])
