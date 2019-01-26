import util.file
import util.pdf
import util.doc
import sys


def index(input: str):
    file_type = util.file.extension(input)
    filename = input

    if file_type == 'pdf':
        filename = util.pdf.to_text(filename)
    elif file_type == 'docx':
        filename = util.doc.to_text(filename)

    print("done")


index(sys.argv[1])

