package com.baosight.message.config;

import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Trie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yang on 2018/6/27.
 */
@Configuration
@Slf4j
public class SensitiveWordsConfig {

    @Bean
    public Trie getTrie() throws IOException {
        Trie trie = Trie.builder().ignoreCase().addKeywords(getKeyWords()).build();
        return trie;
    }

    private Set<String> getKeyWords() throws IOException{
        log.info("...loading key words start...");
        File file = ResourceUtils.getFile("classpath:sensitive-words.txt");
        Set<String> set = new HashSet<>();
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("GBK"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String keyword;
        while((keyword = bufferedReader.readLine()) != null){
            set.add(keyword.trim());
        }
        inputStream.close();
        log.info("...loading key words finished...");
        return set;
    }

}
