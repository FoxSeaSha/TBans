package ru.foxseasha.tban.utils;


public enum BanType
{
    BAN("BAN", 0), 
    BANIP("BANIP", 1), 
    MUTE("MUTE", 2);
    
    private BanType(final String s, final int n) {
    }
    
    public static BanType getByName(final String name) {
        try {
            return Enum.valueOf(BanType.class, name.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
}
