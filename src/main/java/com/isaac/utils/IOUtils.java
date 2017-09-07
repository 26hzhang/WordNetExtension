package com.isaac.utils;

import com.isaac.wordnet.WordNetUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    public static List<String> loadFile2List(String filename) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(WordNetUtils.GLOBALPATH.concat(filename)));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) continue;
            list.add(line);
        }
        reader.close();
        return list;
    }
}
