package ru.foxseasha.tban.utils;


public enum AccessType
{
    BAN("BAN", 0), 
    TEMPBAN("TEMPBAN", 1), 
    BANIP("BANIP", 2), 
    TEMPBANIP("TEMPBANIP", 3), 
    MUTE("MUTE", 4), 
    TEMPMUTE("TEMPMUTE", 5), 
    KICK("KICK", 6);
    
    private AccessType(final String s, final int n) {
    }
}
