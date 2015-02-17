# A Python 3 script that guarantees proper formatting for the wiki.
#
# $ python3 pretty_wiki.py <path_to_wiki.txt>
#
# This script first loads all the text into memory and then flushes the structure properly formatted into wiki.txt
# This is a WIP.

import sys

column_break = 120
line_break = '\\'
new_line = '\\\\'
article_first_key = 'ARTICLE:'


def print_usage():
    print("Usage:  $ python3 pretty_wiki.py [path_to_wiki_text]")


def endswith(s, seq):
    return len(s) >= len(seq) and s[-len(seq):] == seq


def add_to_list(l, s, concat_with_last):
    if concat_with_last:
        l[-1] += s
    else:
        l.append(s)


if __name__ == '__main__':
    if len(sys.argv) == 1:
        print_usage()
        sys.exit(0)
    wiki_txt = sys.argv[1]
    words = []
    last_ended_with_break = False
    with open(wiki_txt, 'r') as w:
        for line in w.readlines():
            line_words = line.split(' ')
            for word in line_words:
                words.append(word.replace('\n', '').replace(new_line, '\n').replace(line_break, ''))
