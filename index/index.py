import util.file
import util.pdf


def index(filename: str):
    type = util.file.extension(filename)

    if type == 'pdf':
        filename = util.pdf.to_text(filename)
    elif type.startswith('doc'):
        filename = util.doc.to_text(filename)

