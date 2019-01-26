from setuptools import find_packages, setup
import util

setup(
    author='Alexander Mukhopad',
    author_email='alex.mukhopad@gmail.com',
    packages=find_packages(),
    include_package_data=True,
    cmdclass={
        "util": util
    }
)
