from typing import List


def extension(filename: str) -> str:
    split = split_extension(filename)
    if len(split) == 1:
        return 'txt'

    return split[1]


def txt_name(filename: str) -> str:
    return split_extension(filename)[0] + '.txt'


def split_extension(filename: str) -> List[str]:
    return filename.rsplit('.', 1)
