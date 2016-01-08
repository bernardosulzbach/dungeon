# Copyright (C) 2014-2016 Bernardo Sulzbach
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

# The script responsible for generating the wiki markdown from the JSON in-game wiki.

import json
import os


def make_filename_for_article(title):
    """
    Returns the correct filename for a given article
    """
    return title + '.md'


def make_markdown_for_article(article):
    title = article['title']
    markdown = ['# ' + title, '\n\n']
    markdown.extend(article['content'])
    see_also = article.get('seeAlso')
    if see_also is not None:
        markdown.append('\n\n')
        markdown.append('## See Also' + '\n\n' + '; '.join(see_also))
    return markdown


if __name__ == '__main__':
    dungeon_root = os.path.dirname(os.path.dirname(__file__))
    wiki_json_path = os.path.join(dungeon_root, 'src', 'main', 'resources', 'wiki.json')
    with open(wiki_json_path) as wiki_file:
        wiki_json = json.load(wiki_file)
        for article in wiki_json['articles']:
            article_title = article['title']
            markdown = make_markdown_for_article(article)
            file_path = os.path.join(dungeon_root, 'dungeon.wiki', make_filename_for_article(article_title))
            with open(file_path, 'w') as article_file:
                article_file.writelines(markdown)
                article_file.close()
