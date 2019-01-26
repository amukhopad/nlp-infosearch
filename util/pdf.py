import PyPDF2
import util.file


def to_text(filename: str) -> str:
    out_filename = util.file.txt_name(filename)

    pdf_file = open(filename, 'rb')
    out_file = open(out_filename, 'w+')

    pdf_reader = PyPDF2.PdfFileReader(pdf_file)

    for i in range(0, pdf_reader.numPages):
        page = pdf_reader.getPage(i)
        out_file.write(page.extractText())

    out_file.close()
    return out_filename
