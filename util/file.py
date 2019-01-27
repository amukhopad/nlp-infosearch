from typing import List
import os

import util.pdf
import util.doc


def handle_formats(filename: str) -> str:
    file_type = extension(filename)

    if file_type == 'pdf':
        filename = util.pdf.to_text(filename)
    elif file_type == 'docx':
        filename = util.doc.to_text(filename)

    return filename


def extension(filename: str) -> str:
    split = split_extension(filename)
    if len(split) == 1:
        return 'txt'

    return split[1]


def txt_name(filename: str) -> str:
    return split_extension(filename)[0] + '_temp.txt'


def split_extension(filename: str) -> List[str]:
    return filename.rsplit('.', 1)
