import sys

from docx import Document

import util.file


def to_text(filename: str) -> str:
    out_filename = util.file.temp_name(filename)

    with open(out_filename, 'w+') as out_file:
        doc = Document(filename)
        for p in doc.paragraphs:
            text = paragapth_to_text(p)
            out_file.write(text)

    return out_filename


def paragapth_to_text(paragraph):
    rs = paragraph._element.xpath('.//w:t')
    return u" ".join([r.text for r in rs])
