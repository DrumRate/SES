package com.company;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MkbToJson {
    private String fileName;

    public MkbToJson(String fileName) {
        this.fileName = fileName;
        this.Read();
    }

    public boolean Read() {
        String text = "";
        try (FileReader reader = new FileReader(this.fileName)) {
            // читаем посимвольно
            int c;
            while ((c = reader.read()) != -1) {
                text = text.concat(String.valueOf((char) c));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        String[] arr = text.split("\r\n\r\n");
        ArrayList blocksList = new ArrayList();
        for (String item : arr) {
            blocksList.add(item);
        }
        String[] questions = arr[1].trim().split("\r\n");
        ArrayList questionsList = new ArrayList();
        for (String item : questions) {
            questionsList.add(item);
        }
        questionsList.remove(0); // готовый список вопросов
        String[] hypotises = arr[2].trim().split("\r\n");
        ArrayList hypotisesList = new ArrayList();
        for (String item : hypotises) {
            hypotisesList.add(item);
        }
        ArrayList<ArrayList> hypotisesesList = new ArrayList<ArrayList>();
        for (String item : hypotises) {
            ArrayList tempList = new ArrayList();
            String[] temp = item.split(",");
            for (String temp_item : temp) {
                tempList.add(temp_item);
            }
            hypotisesesList.add(tempList);
        }
        Gson gson = new Gson();
        String JSON_FILE = "";
        JSON_FILE = JSON_FILE.concat("{\"Questions\":");
        String questionsJson = gson.toJson(questionsList);
        JSON_FILE = JSON_FILE.concat(questionsJson + ",\n" +
                "\t\t\"Hypothesis\":");

        ArrayList H_ARR = new ArrayList<hypotise>();
        hypotise h = new hypotise();
        for (ArrayList list : hypotisesesList) {
            String name = list.get(0).toString();
            double start = Double.parseDouble(list.get(1).toString());
            probability[] hhArr = new probability[questionsList.size()];
            int counter = 0;
            for (int i = 2; i < list.size(); i = i + 3) {
                probability hh = new probability(Double.parseDouble(list.get(i + 1).toString()), Double.parseDouble(list.get(i + 2).toString()));
                hhArr[counter] = hh;
                counter++;
            }
            h = new hypotise(start, name, hhArr);
            H_ARR.add(h);
        }
        JSON_FILE = JSON_FILE.concat(gson.toJson(H_ARR) + "}");

        try (FileWriter writer = new FileWriter("KnowBase.json")) {
            writer.write(JSON_FILE);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }
}

class hypotise {
    double statrWith = 0;
    String name = "";
    probability[] hypothesiss = null;

    hypotise() {
    }

    hypotise(double statrWith, String name, probability[] arr) {
        this.statrWith = statrWith;
        this.name = name;
        this.hypothesiss = arr;
    }
}

class probability {
    double ifTrue;
    double ifFalse;

    probability(double d1, double d2) {
        this.ifTrue = d1;
        this.ifFalse = d2;
    }
}
