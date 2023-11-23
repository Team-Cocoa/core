package kr.teamcocoa.core.network.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class MojangProfile {

    private UUID uuid;
    private String headValue;
    private String userName;

}
