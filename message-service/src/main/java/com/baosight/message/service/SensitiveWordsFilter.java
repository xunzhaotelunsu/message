package com.baosight.message.service;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by yang on 2018/6/27.
 */
@Component
public class SensitiveWordsFilter {

    @Autowired
    Trie trie;

    public String filterText(String content){
        String result = content;
        Collection<Emit> emits = trie.parseText(result);
        for(Emit emit: emits){
            int start = emit.getStart();
            int end = emit.getEnd();
            StringBuilder sb = new StringBuilder(result);
            sb.replace(emit.getStart(), emit.getEnd() + 1, generateReplacement(emit.getKeyword()));
            result = sb.toString();
        }
        return result;
    }

    private String generateReplacement(String keyWord){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i<keyWord.length(); i ++){
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }
}
