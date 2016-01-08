# The script responsible to generating the wiki markdown from the JSON in-game wiki.

import json
import os


def make_filename_for_article(title):
    """
    Returns the correct filename for a given article
    """
    return title + '.md'


if __name__ == '__main__':
    dungeon_root = os.path.dirname(os.path.dirname(__file__))
    wiki_json_path = os.path.join(dungeon_root, 'src', 'main', 'resources', 'wiki.json')
    with open(wiki_json_path) as wiki_file:
        wiki_json = json.load(wiki_file)
        for article in wiki_json['articles']:
            article_title = article['title']
            markdown = ['# ' + article_title + '\n\n']
            markdown.extend(article['content'])
            file_path = os.path.join(dungeon_root, 'dungeon.wiki', make_filename_for_article(article_title))
            with open(file_path, 'w') as article_file:
                article_file.writelines(markdown)
                article_file.close()
