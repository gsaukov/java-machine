package com.apps.trie;

import org.testng.annotations.Test;

public class TrieTest {

    @Test
    public void testTrie() {
        Trie trie = createExampleTrie();
        trie.insert("production");
        trie.insert("producer");
        trie.insert("project");
        trie.insert("lie");
        trie.find("al");
    }

    private Trie createExampleTrie() {
        Trie trie = new Trie();

        trie.insert("programming");
        trie.insert("is");
        trie.insert("a");
        trie.insert("way");
        trie.insert("of");
        trie.insert("life");

        return trie;
    }

}