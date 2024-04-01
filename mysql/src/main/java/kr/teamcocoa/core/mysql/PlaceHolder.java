package kr.teamcocoa.core.mysql;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter(AccessLevel.PROTECTED)
public class PlaceHolder {

    private List<Object> placeholders;

    public PlaceHolder(int amount) {
        this.placeholders = new ArrayList<>(amount);
    }

    public void addPlaceHolder(Object object) {
        placeholders.add(object);
    }

}
