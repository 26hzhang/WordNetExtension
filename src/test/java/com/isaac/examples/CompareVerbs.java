package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CompareVerbs {
    public static void main (String[] args) throws IOException {
        String alexVerbPath = WordNetUtils.GLOBALPATH.concat("data/1000verbs-alex.txt");
        String coreVerbPath = WordNetUtils.GLOBALPATH.concat("data/upd_corewn_with_sense.txt");
        LinkedHashSet<String> alexVerbs = new LinkedHashSet<>();
        LinkedHashSet<String> coreVerbs = new LinkedHashSet<>();

        // read alex's verbs
        BufferedReader alexReader = new BufferedReader(new FileReader(alexVerbPath));
        String line;
        int index = 0;
        while ((line = alexReader.readLine()) != null) {
            String word = line.split("\t")[1].trim();
            alexVerbs.add(word);
            if (++index == 759) break;
        }
        alexReader.close();
        System.out.println("Alex Verb Size: " + alexVerbs.size());

        // read core verbs
        BufferedReader coreReader = new BufferedReader(new FileReader(coreVerbPath));
        while ((line = coreReader.readLine()) != null) {
            if (!line.split("\t")[0].equals("v")) continue;
            String word = line.split("\t")[2].split("%")[0].trim();
            coreVerbs.add(word);
            //List<String> words = Arrays.asList(line.split("\t")[8].split("----")[1]
            //        .replaceAll("\\{", "").replaceAll("}", "").split(", "));
            //words.stream().filter(word -> !word.contains("_")).forEach(coreVerbs::add);
        }
        coreReader.close();
        System.out.println("Core Verb Size: " + coreVerbs.size());

        // Compare
        List<String> alexUnique = alexVerbs.stream().filter(word -> !coreVerbs.contains(word)).collect(Collectors.toList());
        List<String> coreUnique = coreVerbs.stream().filter(word -> !alexVerbs.contains(word)).collect(Collectors.toList());
        List<String> common = alexVerbs.stream().filter(coreVerbs::contains).collect(Collectors.toList());
        System.out.println("Unique Size in Alex: " + alexUnique.size());
        System.out.println("Unique Size in Core: " + coreUnique.size());
        System.out.println("WordNetUtils Size: " + common.size());

        String outDirectory = "/Users/zhanghao/Desktop/";
        BufferedWriter commonWriter = new BufferedWriter(new FileWriter(outDirectory.concat("common.txt")));
        common.forEach(word -> {
            try {
                commonWriter.write(word.concat("\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        commonWriter.close();
        BufferedWriter alexWriter = new BufferedWriter(new FileWriter(outDirectory.concat("alex.txt")));
        alexUnique.forEach(word -> {
            try {
                alexWriter.write(word.concat("\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        alexWriter.close();
        BufferedWriter coreWriter = new BufferedWriter(new FileWriter(outDirectory.concat("core.txt")));
        coreUnique.forEach(word -> {
            try {
                coreWriter.write(word.concat("\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        coreWriter.close();
        System.out.println("done...");
    }
}
