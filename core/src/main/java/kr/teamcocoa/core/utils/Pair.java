package kr.teamcocoa.core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Pair<K, V> {

    private K first;
    private V second;

}
