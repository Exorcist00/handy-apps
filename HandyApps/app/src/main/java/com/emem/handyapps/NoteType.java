package com.emem.handyapps;

public enum NoteType {
    SIMPLE_NOTE(1),
    LIST_NOTE(2),
    RECIPE_NOTE(3),
    INVENTORY_NOTE(4);

    int type;

    NoteType(int type) {
        this.type = type;
    }

    public static NoteType getNoteType(int type){
        for (NoteType noteType: NoteType.values()) {
            if (noteType.getType() == type)
                return noteType;
        }
        return null;
    }

    public int getType() {
        return type;
    }
}
