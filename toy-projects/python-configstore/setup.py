#!/usr/bin/env python

from setuptools import setup

setup(name='python-configstore',
      version='0.1',
      description='Configuration Storage',
      author='Peter Jakubčo',
      url='https://github.com/vbmacher/python-configstore',
      license='MIT',
      packages=['configstore'],
      test_suite='tests',
      install_requires=[],
      classifiers=[
          'Development Status :: 3 - Alpha',
          'Intended Audience :: Developers',
          'License :: OSI Approved :: MIT License',
          'Operating System :: OS Independent',
          'Programming Language :: Python :: 2.7',
          'Topic :: Software Development :: Libraries',
      ]
      )
