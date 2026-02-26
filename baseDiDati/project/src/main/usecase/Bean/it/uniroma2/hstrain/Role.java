package it.uniroma2.hstrain;

public enum Role {

    GESTORESERVIZIO(1),
    LAVORATORE(2),
    ACQUIRENTE(3),
    LOGIN(4);

    private final int id;
    public static Role fromInt(int id) {
        for (Role type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    private Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
