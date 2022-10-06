package com.omniscient.lockedbox.Box;

public enum Tier {
    COMMON("&7", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQzY2ZjMjM5MDA2YjI1N2I4YjIwZjg1YTdiZjQyMDI2YzRhZGEwODRjMTQ0OGQwNGUwYzQwNmNlOGEyZWEzMSJ9fX0="),
    UNCOMMON("&a", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc2OTVmOTZkZGE2MjZmYWFhMDEwZjRhNWYyOGE1M2NkNjZmNzdkZTBjYzI4MGU3YzU4MjVhZDY1ZWVkYzcyZSJ9fX0="),
    ELITE("&9", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY4NjkwNDhmMDZkMzE4ZTUwNThiY2EwYTg3NmE1OTg2MDc5ZjQ1YTc2NGQxMmFiMzRhNDkzMWRiNmI4MGFkYyJ9fX0="),
    RARE("&c", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M0ZDdhM2JjM2RlODMzZDMwMzJlODVhMGJmNmYyYmVmNzY4Nzg2MmIzYzZiYzQwY2U3MzEwNjRmNjE1ZGQ5ZCJ9fX0="),
    LEGENDARY("&6", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTMzMzBmYmVkMzc3YzI0NGY0ODdlNGJjNTY4MmQxNWFmNDBkM2NlNGMzMmVlMDNmYzI0YTdmOTUyZTdkMjlhOSJ9fX0="),
    MYSTIC("&3", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTBmYjFmMTNlY2I3ZmJiMmZhNDljZDAzZDM1N2ZhN2UyNzg1MDJiNzg3MzA2MDJhYWExMDY1NWU0ZDk0OTBlMSJ9fX0="),
    ;

    private final String color;
    private final String texture;
    Tier(String color, String texture){
        this.color = color;
        this.texture = texture;
    }

    public String getColor() {
        return color;
    }
    public String getTexture() {
        return texture;
    }
}
