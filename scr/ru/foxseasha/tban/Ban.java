package ru.foxseasha.tban;

import java.util.concurrent.TimeUnit;
import ru.foxseasha.tban.utils.BanType;

public class Ban {
    private String player;
    private BanType type;
    private String owner;
    private long time;
    private long expire;
    private String reason;

    public Ban(String player, BanType type, String owner, long time, long expire, String reason) {
        this.player = player;
        this.type = type;
        this.owner = owner;
        this.time = time;
        this.expire = expire;
        this.reason = reason;
    }

    public String getPlayer() {
        return this.player;
    }

    public BanType getType() {
        return this.type;
    }

    public String getOwner() {
        return this.owner;
    }

    public long getTime() {
        return this.time;
    }

    public String getTimeLeft() {
        long left = this.expire - System.currentTimeMillis();
        if (left < 0) {
            left = 0;
        }
        String res = String.format("%d \u0434\u043d, %d \u0447, %d \u043c\u0438\u043d, %d \u0441\u0435\u043a", TimeUnit.MILLISECONDS.toDays(left), TimeUnit.MILLISECONDS.toHours(left) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(left)), TimeUnit.MILLISECONDS.toMinutes(left) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(left)), TimeUnit.MILLISECONDS.toSeconds(left) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(left)));
        return res;
    }

    public long getExpire() {
        return this.expire;
    }

    public String getReason() {
        return this.reason;
    }
}

