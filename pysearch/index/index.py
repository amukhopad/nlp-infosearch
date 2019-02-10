import re
import string
import os
import json

from typing import List
from nltk.stem.porter import PorterStemmer
from util.file import handle_formats, visible_files


def index(input_dir: str, index_filename: str):
    mem_index = {}
    file_names = []

    print(f'Indexing files in {input_dir}')
    for fid, file in enumerate(visible_files(input_dir)):
        full_path = os.path.join(input_dir, file)
        mem_index = update_index(fid, full_path, mem_index)
        file_names.append(full_path)

    with open(index_filename, 'w+') as index_file:
        data = {'files': file_names, 'index': mem_index}
        json.dump(data, index_file)

    print(f'\nDone indexing files')


def update_index(file_id: int, file_name: str, index: dict) -> dict:
    print(f'\nStart indexing {file_name} with id {file_id}')
    words = index_file(file_name)
    for pos, word in enumerate(words):
        docs = index.get(word, {})
        if docs == {}:
            index[word] = docs
        usages = docs.get(file_id, [])
        usages.append(pos)
        index[word][file_id] = usages
    return index


def index_file(input_name: str) -> List[str]:
    filename = handle_formats(input_name)
    with open(filename, 'r') as file:
        data = file.read()
    procesed = process(data)

    if filename != input_name:
        os.remove(filename)
        print(f'Removed {filename}')

    print(f'Finished indexing {input_name}')

    return procesed


def process(text: str):
    result = re.sub(r'([a-z])([A-Z])', r'\1 \2', text).lower()
    result = re.sub(r'[ \t]*\n[ \t]*', '', result)
    result = re.split(f'[\s{string.punctuation}]+', result)
    return [PorterStemmer().stem(word) for word in result]
