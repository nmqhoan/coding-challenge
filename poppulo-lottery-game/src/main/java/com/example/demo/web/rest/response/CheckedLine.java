package com.example.demo.web.rest.response;

import com.example.demo.domain.Line;

public class CheckedLine {
    private Line line;
    private int result;

    public CheckedLine(Line line){
        this.line = line;
        this.result = line.calculateResult();
    }

    public int getResult() {
        return result;
    }

    public Line getLine() {
        return line;
    }
}
