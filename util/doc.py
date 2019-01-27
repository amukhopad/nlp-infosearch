import sys

#from docx import opendocx, getdocumenttext

import util.file


def to_text(filename: str) -> str:
    out_filename = util.file.txt_name(filename)

    document = opendocx(filename)
    out_file = open(out_filename, 'w+')

    pages = getdocumenttext(document)

    for page in pages:
        out_file.write('\n\n'.join(page.encode("utf-8")))

    out_file.close()

    return out_filename
