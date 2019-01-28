import re
from typing import List

from index import process
from util.bitwise import full_bits_int, bit_not


def interpret(query: str, index: dict) -> int:
    searched = search_operands(query, index)
    print(searched)
    inverted = search_not(searched, index)
    expression = ' '.join(inverted)
    return eval(expression)


def search_operands(query: str, index: dict) -> List[str]:
    operators = r'(&&|\|\||[!()])'
    split = re.split(f'\s*{operators}\s*', query)
    cmd = filter(lambda x: x != '', split)
    evaluated = []
    for param in cmd:
        if not bool(re.fullmatch(operators, param)):
            param = search(param, index)
        elif param == '&&':
            param = '&'
        elif param == '||':
            param = '|'
        evaluated.append(param)
    return evaluated


def search(query: str, index: dict) -> str:
    files = index['files']
    result = full_bits_int(len(files))
    for token in process(query):
        searched = index['index'].get(token, 0)
        result = result & searched
    return str(result)


def search_not(query: List[str], index: dict) -> List[str]:
    evaluated = []
    idx = 0
    max_bits = len(index['files'])
    while idx < len(query):
        param = query[idx]
        if param == '!':
            param = bit_not(int(query[idx + 1]), max_bits)
            idx += 1
        evaluated.append(str(param))
        idx += 1
    return evaluated


def print_search_result(result: int, index: dict):
    if result == 0:
        print("No results found :c")

    files = index['files']

    for idx in range(0, len(files)):
        if (result & (1 << idx)) > 0:
            print(files[idx])
