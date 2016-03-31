# id3-charset-converter
A Java command line application to covert charset of id3 tags to UTF-8.

## About
Some of the media players, for example iTunes, support character set UTF-8 only. Create this simple tool to convert the tags' character set to UTF-8.

> ID3 is a metadata container most often used in conjunction with the MP3 audio file format. It allows information such as the title, artist, album, track number, and other information about the file to be stored in the file itself.

> From Wikipedia.

## Download
**prerequisites: Java 8 or newer installed**

1. Download the zip or tar from [github](https://github.com/thcathy/id3-charset-converter/releases)
2. Extract to any folder

## Usage
**For Windows:**

  Execute ```bin\id-charset-converter.bat [-c <CHARSET>] [-h] [-t] source [target]```

**For Mac / Unix-like / Linux-Like:**

  Execute ```id3-charset-converter [-c <CHARSET>] [-h] [-t] source [target]```

  - __source (required):__ path of source file or folder
  - __target (optional):__ path of output file or folder
  - __-c or --charset:__ source CHARSET, default=BIG5
  - __-t or --test:__ test run without saving file(s)
  - __-h or --help:__ print help message
  
#### Examples ####
###### Convert single file from ISO-8859-1 to UTF-8 and save to new mp3 ######
```id3-charset-converter -c ISO-8859-1 ~/Downloads/input.mp3 /tmp/output.mp3```

###### Convert all files under folder music and save converted file under the same folder  ######
```id3-charset-converter music```

###### Test converting of file (do not save to new file) ######
```id3-charset-converter -t music/input.mp3```

## Note ##
This is a personal project by [Timmy Wong](https://github.com/thcathy). The project is adopted TDD with code coverage almost 100%.

Feel free to contact thcathy@gmail.com if you want any new feature or bug fixing.
