package com.bear.brain;

public class OkAnswer extends AnswerListener<Object> {

    @Override
    public void ok(Object answer) {
        assert true;
    }

    @Override
    public void cancel() {
        assert false;
    }
}
