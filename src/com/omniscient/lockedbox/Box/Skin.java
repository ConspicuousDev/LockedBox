package com.omniscient.lockedbox.Box;

public enum Skin {
    CYAN("&b", 6, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTUyOTcwZTEyNDE1M2FiZDI4YjVkNjQwMDNiOGZkODc5OTlkYmM3YTVjZGU4OTk5NjFlZTAxY2NkM2Q0YWI5OCJ9fX0="),
    PURPLE("&5", 5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTJhNDZiMDRlYmQzYmVmM2JmNzdhMmM2MjQ0MzQ5NDVkMzRhMGM5NDdhMmFiMThhNzg1ZTYzM2JkY2VlZDkwIn19fQ=="),
    GOLD("&6", 11, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJlZDI5ODg1ODk1MWI0M2QyNDQ4ZGI1YzY1NmU3MDA0ZWI5MWUzMDc2ZTVmYzg4ZDI0ZTUyOWU4ODQ5ZTJjMCJ9fX0="),
    RED("&c", 1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDhjMWUxYzYyZGM2OTVlYjkwZmExOTJkYTZhY2E0OWFiNGY5ZGZmYjZhZGI1ZDI2MjllYmZjOWIyNzg4ZmEyIn19fQ=="),
    GREEN("&2", 2, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhmODhiMTYxNzYzZjYyZTRjNTFmNWViMWQzOGZhZjNiODJjNDhhODM5YWMzMTcxMjI5NTU3YWRlNDI3NDM0In19fQ=="),
    BLUE("&9", 4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNjNzk5NTRkMzUwYTk4YzcyMTcyNTY4MzFmNjVjNjJhNDI4MDc0YjZlNGFlOWVlZGU3YTQ0ZjlkZTRhNyJ9fX0="),
    ;

    private final String color;
    private final byte dyeID;
    private final String texture;
    Skin(String color, int dyeID, String texture){
        this.color = color;
        this.dyeID = (byte) dyeID;
        this.texture = texture;
    }

    public String getColor() {
        return color;
    }
    public byte getDyeID() {
        return dyeID;
    }
    public String getTexture() {
        return texture;
    }
}
