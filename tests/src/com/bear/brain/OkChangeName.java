package com.bear.brain;

import static org.junit.Assert.assertEquals;

public class OkChangeName extends AnswerListener<ChangeNameResult> {
    @Override
    public void ok(ChangeNameResult answer) {
        assertEquals(ChangeNameType.OK, answer.getType());
    }

    @Override
    public void cancel() {
        assert false;
    }
}
