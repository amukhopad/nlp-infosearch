from typing import List
import os

import util.pdf
import util.doc


def visible_files(directory: str):
    return filter(lambda file:
                  not file.startswith('.')
                  and os.path.isfile(os.path.join(directory, file)),
                  os.listdir(directory))


def handle_formats(filename: str) -> str:
    file_type = extension(filename)
    doc = filename

    if file_type == 'pdf':
        doc = util.pdf.to_text(filename)
    elif file_type == 'docx':
        doc = util.doc.to_text(filename)

    if doc != filename:
        print(f'Created temporary file {doc}')

    return doc


def extension(filename: str) -> str:
    split = split_extension(filename)
    if len(split) == 1:
        return 'txt'

    return split[1]


def temp_name(filename: str) -> str:
    return split_extension(filename)[0] + '.temp'


def split_extension(filename: str) -> List[str]:
    return filename.rsplit('.', 1)
