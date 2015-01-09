# A handy Python 3 script used to format poems according to the Dungeon code conventions.

import sys


def print_usage():
    """
    Prints instructions on how to use this program.
    :return: None
    """
    print('Usage: ' + sys.argv[0] + ' [filename.txt]')


def print_poem(filename):
    """
    Prints a poem read from a text file, after removing all trailing whitespace from its lines and adding backslashes
    at the same column.
    :type filename: str
    :return: None
    """
    with open(filename, 'r') as f:
        lines = f.readlines()
    max_width = 0
    for i in range(len(lines)):
        lines[i] = lines[i].rstrip()
        current_line_width = len(lines[i])
        if current_line_width > max_width:
            max_width = current_line_width
    for i in range(len(lines)):
        # If max_width is 10, as this is zero-indexed, the backslash should be at line[10].
        lines[i] = lines[i] + ' ' * (max_width - len(lines[i]))
        if i != len(lines) - 1:
            lines[i] = lines[i] + '\\'
    poem = '\n'.join(lines)
    print(poem)


if __name__ == '__main__':
    if len(sys.argv) == 1:
        print_usage()
    else:
        print_poem(sys.argv[1])
