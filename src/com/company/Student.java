package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;

public class Student {
    private int studentRecordBook;
    private String surname;
    private ArrayList<Pair<String, Integer>> rating;
    public static final int SIZE = 3;

    Student(String s) throws DataFormatException, NumberFormatException {
        rating = new ArrayList<>();
        StringTokenizer sb = new StringTokenizer(s, " ");
        if(sb.countTokens() % 2 != 0) throw new DataFormatException();
        studentRecordBook = Integer.parseInt(sb.nextToken());
        surname = sb.nextToken();
        while (sb.hasMoreTokens()){
            Pair<String, Integer> p = new Pair<>(sb.nextToken(), Integer.parseInt(sb.nextToken()));
            rating.add(p);
        }
    }

    @Override
    public boolean equals(Object obj) {
        Student student = (Student)obj;
        if(studentRecordBook == student.studentRecordBook && (surname.compareTo(student.surname)==0) && (rating.size() == student.rating.size())){
            for (Pair<String, Integer> p : rating){
                for (Pair<String, Integer> s : student.rating){
                    if(p.getKey().compareTo(s.getKey()) == 0){
                        if(p.getValue().compareTo(s.getValue()) != 0){
                            return false;
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return surname;
    }

    public boolean isHighAchiever(){
        for(Pair<String, Integer> p : rating){
            if(p.getValue() < 9){
                return false;
            }
        }
        return true;
    }

    public String getInformation() {
        StringBuilder str = new StringBuilder("N" + studentRecordBook + '\n' + "Surname: " + surname);
        for(Pair<String, Integer> p : rating){
            str.append('\n').append(p.getKey()).append(": ").append(p.getValue());
        }
        return str.toString();
    }
}
