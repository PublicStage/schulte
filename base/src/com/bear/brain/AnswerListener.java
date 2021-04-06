package com.bear.brain;

public abstract class AnswerListener<T> {
    public abstract void ok(T answer);

    public abstract void cancel();

    public Class<T> getType() {
        return ReflectionUtils.getGenericParameterClass(this.getClass(), AnswerListener.class, 0);
    }
}
