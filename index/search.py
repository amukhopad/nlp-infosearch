import json
import re
from typing import List

from index import process


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
        if result & (1 << idx) > 0:
            print(files[idx])


def bit_not(val: int, max_bits: int):
    full_bits = int('1' * max_bits, 2)
    return full_bits ^ val


def search(query: str, index: dict) -> str:
    result = 0
    for token in process(query):

        result = result | index['index'].get(token, 0)
    return str(result)
