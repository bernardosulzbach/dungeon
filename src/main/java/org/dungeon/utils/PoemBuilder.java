package org.dungeon.utils;

public class PoemBuilder {
    private String title;
    private String author;
    private String content;

    public PoemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public PoemBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public PoemBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Checks that no fields are null.
     */
    public boolean isComplete() {
        return (title != null && author != null && content != null);
    }

    public Poem createPoem() {
        return new Poem(title, author, content);
    }
}