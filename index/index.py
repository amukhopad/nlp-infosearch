import re
import string
import os
import json

from typing import List
from nltk.stem.porter import PorterStemmer
from util.file import handle_formats


def index(input_dir: str, index_filename: str):
    mem_index = {}
    file_names = []

    print(f'Indexing files in {input_dir}')
    files = enumerate(os.listdir(input_dir))
    for fid, file in files:
        full_path = os.path.join(input_dir, file)
        if not file.startswith('.') and os.path.isfile(full_path):
            mem_index = update_index(fid, full_path, mem_index)
            file_names.append(full_path)

    with open(index_filename, 'w+') as index_file:
        data = {'files': file_names, 'index': mem_index}
        json.dump(data, index_file)

    print(f'\nDone indexing files')


def update_index(file_id: int, file_name: str, index: dict) -> dict:
    word_set = index_file(file_name)
    for word in word_set:
        usages = index.get(word, 0)
        index[word] = usages | (1 << file_id)
    return index


def index_file(input_name: str) -> List[str]:
    print(f'\nStart indexing {input_name}')
    filename = handle_formats(input_name)
    with open(filename, 'r') as file:
        data = file.read()
    procesed = process(data)
    unique = list(set(procesed))

    if filename != input_name:
        os.remove(filename)
        print(f'Removed {filename}')

    print(f'Finished indexing {input_name}')

    return unique


def process(text: str):
    result = re.sub(r'([a-z])([A-Z])', r'\1 \2', text).lower()
    result = re.split(f'[\s{string.punctuation}]+', result)
    return [PorterStemmer().stem(word) for word in result]
