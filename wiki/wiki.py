# The script responsible for generating the wiki markdown from the JSON in-game wiki.

import json
import os


def make_filename_for_article(title):
    """
    Returns the correct filename for a given article
    :param title: the title of the article
    """
    return title + '.md'


def make_markdown_for_article(article):
    # Duplicate newlines because a newline that breaks a line on the in-game wiki will not do it in Markdown.
    # Although this generates Markdown with 4 newlines in some places, it is a good way to fix it.
    markdown = [article['content'].replace('\n', '\n\n')]
    see_also = article.get('seeAlso')
    if see_also is not None:
        markdown.append('\n\n')
        markdown.append('## See Also' + '\n\n' + '; '.join(see_also))
    return markdown


def should_be_deleted(filename):
    return not filename.startswith('_') and filename != 'Home.md'


def clean_wiki_clone(dungeon_root):
    """
    Deletes all files in the wiki clone to which should_be_deleted returns true.
    :param dungeon_root: the root of the dungeon repository
    """
    root = os.path.join(dungeon_root, 'dungeon.wiki')
    for filename in os.listdir(root):
        if os.path.isfile(os.path.join(root, filename)) and should_be_deleted(filename):
            os.remove(os.path.join(root, filename))


def main():
    dungeon_root = os.path.dirname(os.path.dirname(__file__))
    clean_wiki_clone(dungeon_root)
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


if __name__ == '__main__':
    main()
