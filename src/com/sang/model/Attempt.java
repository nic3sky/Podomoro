package com.sang.model;

public class Attempt {
    private String mMessage;
    private int mRemainingSeconds;
    private AttemptKind mKind;

    public Attempt(String mMessage, AttemptKind mKind) {
        this.mMessage = mMessage;
        this.mKind = mKind;
        mRemainingSeconds = mKind.getmTotalSeconds();
    }


    public String getmMessage() {
        return mMessage;
    }

    public int getmRemainingSeconds() {
        return mRemainingSeconds;
    }

    public AttemptKind getmKind() {
        return mKind;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public void tick() {
        mRemainingSeconds--;
    }

    @Override
    public String toString() {
        return "Attempt{" +
                "mMessage='" + mMessage + '\'' +
                ", mRemainingSeconds=" + mRemainingSeconds +
                ", mKind=" + mKind +
                '}';
    }

    public void save() {
        System.out.println("Saving  " + this);
    }
}
