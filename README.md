# jFileContentSearch
Small tool to look into all files of a directory and it's subdirectories and mark the files in which a given term appears.

```
CMD> java -jar jFCS.jar def -d src -l -e .py

Searching in src for ['def']

>> def
    cmd_parser.py
        [18, 41, 49]
    data\files.py
        [19, 24, 29, 34, 39, 44, 47]
    data\search_result.py
        [18, 23, 36, 49, 54, 59, 62]
    dir_walker.py
        [21, 32, 33, 37, 47, 64, 75, 90]
    file_content_search.py
        [19, 28, 39, 59, 76, 92, 111]
    file_inspector.py
        [19, 36, 47, 60, 74, 110, 141]
```

## Usage
**Using the commandline:**
```
java -jar jFCS.jar [term] -d [target-dir] -e [extensions] -l
```

## Parameter
The following commandline parameters are supported in the current version.

**Search for multiple terms at once:**
```
java -jar jFCS.jar "Egon Olsen" "Benny Frandsen" "Kjeld Jensen"
```
Separate terms by space and use double quotes to include whitespaces in the term.

**Choose the root directory for the search:**
```
java -jar jFCS.jar [term] -d C:\My\Path\To\Root\
java -jar jFCS.jar [term] --dir C:\My\Path\To\Root\
```
If no directory is given, the currently active directory is searched.

**Only look into files with given extensions:**
```
java -jar jFCS.jar [term] -e .py .txt
java -jar jFCS.jar [term] -extensions .py .txt
```
Only files with the given extensions are searched. Separate multiple extensions by space.

**Show in which line term appears:**
```
java -jar jFCS.jar [term] -l
java -jar jFCS.jar [term] --lines
```
Also show in which line of the file the term appears. Search will take longer.

**Activate case sensitive search:**
```
java -jar jFCS.jar [term] -cs
java -jar jFCS.jar [term] --case-sensitive
```
By default the search ignores upper- and lower case.

**Display full path to file:**
```
java -jar jFCS.jar [term] -long
```
The path to the file is shortened, display the full path with this option.

**Don't look into subdirectories:**
```
java -jar jFCS.jar [term] -ns
java -jar jFCS.jar [term] --no-subdirectories
```
By default also subdirectories of the root directory are searched. Only look into files in the root directory with this option.

## License

This project is licensed under the MIT license. See the [LICENSE](https://github.com/Ste-Tis/jFileContentSearch/blob/master/LICENSE) file for more info.