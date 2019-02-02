from setuptools import find_packages, setup
import util
import index
import docx

setup(
    author='Alexander Mukhopad',
    author_email='alex.mukhopad@gmail.com',
    packages=find_packages(),
    include_package_data=True,
    cmdclass={
        "util": util,
        "index": index,
        "docx": docx
    }
)
