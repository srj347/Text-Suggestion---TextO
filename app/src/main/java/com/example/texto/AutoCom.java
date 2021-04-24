package com.example.texto;

public class AutoCom implements Comparable<AutoCom>{
    String str;
    int occ = 0;

    public AutoCom(String str, int occ){
        this.str = str;
        this.occ = occ;
    }

    @Override
    public int compareTo(AutoCom e) {
        return e.occ - this.occ;
    }

    @Override
    public String toString() {
        return str + " " + occ + " HERE";
    }
}