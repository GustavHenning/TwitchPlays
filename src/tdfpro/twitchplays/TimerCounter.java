package tdfpro.twitchplays;

public class TimerCounter {
    private int millis;

    public TimerCounter(int millis) {
        this.millis = millis;
    }

    public boolean update(int delta) {
        return (millis =- delta) < 0;
    }

    public void reset(int millis) {
        this.millis = millis;
    }
}
