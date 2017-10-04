# The script responsible for generating the wiki markdown from the JSON in-game wiki.

import json
import os
import xml.etree.ElementTree as ElementTree


def get_version_string():
    tree = ElementTree.parse('pom.xml')
    root = tree.getroot()
    for child in root:
        # Check the end of the tag to ignore the namespace Maven uses.
        if child.tag.endswith('version'):
            return child.text
    raise Exception('Failed to find the repository version!')


def get_wiki_root_url():
    return 'https://github.com/mafagafogigante/dungeon/wiki/'


def get_footer_filename():
    return '_Footer.md'


def make_filename_for_article(title):
    """
    Returns the correct filename for a given article
    :param title: the title of the article
    """
    return title + '.md'


def make_see_also_link(reference):
    """
    Makes a Markdown link for a See Also reference in an article.
    :param reference: the plain text reference to another article
    """
    return '[' + reference + '](' + get_wiki_root_url() + reference.replace(' ', '-') + '/)'


def make_markdown_for_article(article):
    # Duplicate newlines because a newline that breaks a line on the in-game wiki will not do it in Markdown.
    # Although this generates Markdown with 4 newlines in some places, it is a good way to fix it.
    markdown = [article['content'].replace('\n', '\n\n')]
    see_also = article.get('seeAlso')
    if see_also is not None:
        markdown.append('\n\n')
        markdown.append('## See Also' + '\n\n' + '; '.join([make_see_also_link(reference) for reference in see_also]))
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


def get_home_markdown():
    lines = ['# Welcome', '\n\n', 'Welcome to the Dungeon wiki!', '\n\n', '# About the wiki', '\n\n']
    wiki_json_file = 'https://github.com/mafagafogigante/dungeon/blob/master/src/main/resources/wiki.json'
    lines.append('This wiki is automatically generated from [this JSON file]({}).'.format(wiki_json_file))
    lines.append('\n\n')
    lines.append('If you want to edit an article, create a new issue or submit a pull request.')
    lines.append('\n\n')
    lines.append('Wiki generated for Dungeon {}.'.format(get_version_string()))
    return lines


def write_footer(path):
    footer_format = 'Dungeon {}. Licensed under the BSD 3-Clause license.'
    with open(os.path.join(path, get_footer_filename()), 'w') as footer_file:
        footer_file.writelines(footer_format.format(get_version_string()))


def main():
    dungeon_root = os.path.dirname(os.path.dirname(__file__))
    wiki_root = os.path.join(dungeon_root, 'dungeon.wiki')
    clean_wiki_clone(dungeon_root)
    get_version_string()
    wiki_json_path = os.path.join(dungeon_root, 'src', 'main', 'resources', 'wiki.json')
    with open(wiki_json_path) as wiki_file:
        write_footer(wiki_root)
        home_path = os.path.join(wiki_root, 'Home.md')
        with open(home_path, 'w') as home_file:
            home_file.writelines(get_home_markdown())
        wiki_json = json.load(wiki_file)
        for article in wiki_json['articles']:
            article_title = article['title']
            markdown = make_markdown_for_article(article)
            file_path = os.path.join(wiki_root, make_filename_for_article(article_title))
            with open(file_path, 'w') as article_file:
                article_file.writelines(markdown)


if __name__ == '__main__':
    main()
