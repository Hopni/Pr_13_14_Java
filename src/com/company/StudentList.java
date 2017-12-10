package com.company;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class StudentList extends DefaultListModel<Student> implements Iterable<Student> {
    @Override
    public void addElement(Student element) {
        if(!super.contains(element)) {
            super.addElement(element);
        }
    }

    public void sort(){
        ArrayList<Student> list = Collections.list(this.elements());
        list.sort(new StudentComparator());
        this.removeAllElements();
        for(Student s : list){
            this.addElement(s);
        }
    }

    public Iterator<Student> iterator(){
        return this.elements().asIterator();
    }
}
